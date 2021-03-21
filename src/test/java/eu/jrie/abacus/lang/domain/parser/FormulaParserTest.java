package eu.jrie.abacus.lang.domain.parser;

import eu.jrie.abacus.core.domain.expression.Expression;
import eu.jrie.abacus.core.domain.expression.NumberValue;
import eu.jrie.abacus.core.domain.expression.TextValue;
import eu.jrie.abacus.core.domain.expression.Value;
import eu.jrie.abacus.core.domain.formula.ArgumentValueSupplier;
import eu.jrie.abacus.core.domain.formula.FormulaDefinition;
import eu.jrie.abacus.core.domain.workbench.WorkbenchContext;
import eu.jrie.abacus.lang.domain.exception.InvalidInputException;
import eu.jrie.abacus.lang.domain.grammar.Token;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.EnumSet;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;

import static eu.jrie.abacus.lang.domain.grammar.Token.CELL_REFERENCE;
import static eu.jrie.abacus.lang.domain.grammar.Token.NUMBER_VALUE;
import static eu.jrie.abacus.lang.domain.grammar.Token.TEXT_VALUE;
import static java.lang.String.format;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toUnmodifiableList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

class FormulaParserTest {

    private static final String FORMULA_NAME = "action";
    private static final String CELL_REFERENCE_ARG_TEXT = "C1";
    private static final String NUMBER_ARG_TEXT = "1";
    private static final String TEXT_ARG_TEXT = "'abc '";

    private static final List<FormulaDefinition> noArgDefinitions = singletonList(
            new FormulaDefinition(
                    FORMULA_NAME,
                    emptyList(),
                    (context, args) -> {
                        assertEquals(0, args.size());
                        return new TextValue("ok");
                    })
    );

    private static final List<FormulaDefinition> singleArgDefinitions = List.of(
            new FormulaDefinition(
                    FORMULA_NAME,
                    singletonList(NumberValue.class),
                    (context, args) -> {
                        assertEquals(1, args.size());
                        var arg = args.get(0).get(context);
                        assertTrue(arg instanceof NumberValue);
                        return arg;
                    }),
            new FormulaDefinition(
                    FORMULA_NAME,
                    singletonList(TextValue.class),
                    (context, args) -> {
                        assertEquals(1, args.size());
                        var arg = args.get(0).get(context);
                        assertTrue(arg instanceof TextValue);
                        return arg;
                    })
    );

    private static final List<FormulaDefinition> manyArgsDefinitions = List.of(
            new FormulaDefinition(
                    FORMULA_NAME,
                    List.of(NumberValue.class, NumberValue.class),
                    (context, args) -> {
                        assertEquals(2, args.size());
                        var a = args.get(0).get(context);
                        assertTrue(a instanceof NumberValue);
                        var b = args.get(1).get(context);
                        assertTrue(b instanceof NumberValue);
                        return new NumberValue(((NumberValue) a).value() + ((NumberValue) b).value());
                    }),
            new FormulaDefinition(
                    FORMULA_NAME,
                    List.of(TextValue.class, TextValue.class),
                    (context, args) -> {
                        assertEquals(2, args.size());
                        var a = args.get(0).get(context);
                        assertTrue(a instanceof TextValue);
                        var b = args.get(1).get(context);
                        assertTrue(b instanceof TextValue);
                        return new TextValue(a.getAsString() + b.getAsString());
                    }),
            new FormulaDefinition(
                    FORMULA_NAME,
                    List.of(NumberValue.class, TextValue.class),
                    (context, args) -> {
                        assertEquals(2, args.size());
                        var a = args.get(0).get(context);
                        assertTrue(a instanceof NumberValue);
                        var b = args.get(1).get(context);
                        assertTrue(b instanceof TextValue);
                        return new TextValue(a.getAsString() + b.getAsString());
                    }),
            new FormulaDefinition(
                    FORMULA_NAME,
                    List.of(TextValue.class, NumberValue.class),
                    (context, args) -> {
                        assertEquals(2, args.size());
                        var a = args.get(0).get(context);
                        assertTrue(a instanceof TextValue);
                        var b = args.get(1).get(context);
                        assertTrue(b instanceof NumberValue);
                        return new TextValue(a.getAsString() + b.getAsString());
                    })
    );

    private final WorkbenchContext context = mock(WorkbenchContext.class);
    private final CellReferenceResolver cellReferenceResolver = mock(CellReferenceResolver.class);
    private final TextValueResolver textValueResolver = mock(TextValueResolver.class);
    private final NumberValueResolver numberValueResolver = mock(NumberValueResolver.class);

    private final FormulaParser parser = new FormulaParser(
            context, cellReferenceResolver, textValueResolver, numberValueResolver
    );

    @ParameterizedTest(name = "should match no arg formula - \"{0}\"")
    @ValueSource(strings = {"action()", "action ( ) "})
    void shouldMatchNoArgFormula(String formulaText) throws InvalidInputException {
        // given
        when(context.findFormulasDefinition(FORMULA_NAME)).thenReturn(noArgDefinitions);

        // when
        var result = parser.parse(formulaText);

        // then
        verify(context).findFormulasDefinition(FORMULA_NAME);
        verifyNoInteractions(cellReferenceResolver);
        verifyNoInteractions(textValueResolver);
        verifyNoInteractions(numberValueResolver);

        // and
        assertEquals(FORMULA_NAME, result.functionName());
        assertIterableEquals(emptyList(), result.arguments());
        assertEquals("ok", result.action().get().getAsString());
    }

    @ParameterizedTest(name = "should match single number arg formula - \"{0}\"")
    @ValueSource(strings = {"action(1)", "action ( 1 ) "})
    void shouldMatchSingleNumberArgFormula(String formulaText) throws InvalidInputException {
        // given
        var expectedArg = new NumberValue(1);

        // and
        when(context.findFormulasDefinition(FORMULA_NAME)).thenReturn(singleArgDefinitions);
        when(numberValueResolver.resolve(NUMBER_ARG_TEXT)).thenReturn(c -> expectedArg);

        // when
        var result = parser.parse(formulaText);

        // then
        verify(context).findFormulasDefinition(FORMULA_NAME);
        verifyNoInteractions(cellReferenceResolver);
        verifyNoInteractions(textValueResolver);
        verify(numberValueResolver).resolve(NUMBER_ARG_TEXT);

        // and
        assertEquals(FORMULA_NAME, result.functionName());
        assertArgumentsEquals(singletonList(expectedArg), result.arguments());
        assertEquals(expectedArg, result.action().get());
    }

    @ParameterizedTest(name = "should match single text arg formula - \"{0}\"")
    @ValueSource(strings = {"action('abc ')", "action ( 'abc ' ) "})
    void shouldMatchSingleTextArgFormula(String formulaText) throws InvalidInputException {
        // given
        var expectedArg = new TextValue("abc ");

        // and
        when(context.findFormulasDefinition(FORMULA_NAME)).thenReturn(singleArgDefinitions);
        when(textValueResolver.resolve(TEXT_ARG_TEXT)).thenReturn(c -> expectedArg);

        // when
        var result = parser.parse(formulaText);

        // then
        verify(context, atLeast(1)).findFormulasDefinition(FORMULA_NAME);
        verifyNoInteractions(cellReferenceResolver);
        verify(textValueResolver).resolve(TEXT_ARG_TEXT);
        verifyNoInteractions(numberValueResolver);

        // and
        assertEquals(FORMULA_NAME, result.functionName());
        assertArgumentsEquals(singletonList(expectedArg), result.arguments());
        assertEquals(expectedArg, result.action().get());
    }

    @ParameterizedTest(name = "should match single cell reference arg formula - \"{0}\"")
    @ValueSource(strings = {"action(C1)", "action ( C1 ) "})
    void shouldMatchSingleCellReferenceArgFormula(String formulaText) throws InvalidInputException {
        // given
        var expectedArg = new NumberValue(1);

        // and
        when(context.findFormulasDefinition(FORMULA_NAME)).thenReturn(singleArgDefinitions);
        when(cellReferenceResolver.resolve(CELL_REFERENCE_ARG_TEXT)).thenReturn(c -> expectedArg);

        // when
        var result = parser.parse(formulaText);

        // then
        verify(context).findFormulasDefinition(FORMULA_NAME);
        verify(cellReferenceResolver).resolve(CELL_REFERENCE_ARG_TEXT);
        verifyNoInteractions(textValueResolver);
        verifyNoInteractions(numberValueResolver);

        // and
        assertEquals(FORMULA_NAME, result.functionName());
        assertArgumentsEquals(singletonList(expectedArg), result.arguments());
        assertEquals(expectedArg, result.action().get());
    }

    @ParameterizedTest(name = "should match many number args formula - \"{0}\"")
    @ValueSource(strings = {"action(1,2)", "action ( 1 , 2 ) "})
    void shouldMatchManyNumberArgsFormula(String formulaText) throws InvalidInputException {
        // given
        var expectedArg1 = new NumberValue(1);
        var expectedArg2 = new NumberValue(2);
        var expectedResult = new NumberValue(3);

        // and
        when(context.findFormulasDefinition(FORMULA_NAME)).thenReturn(manyArgsDefinitions);
        when(numberValueResolver.resolve(NUMBER_ARG_TEXT)).thenReturn(c -> expectedArg1);
        when(numberValueResolver.resolve("2")).thenReturn(c -> expectedArg2);

        // when
        var result = parser.parse(formulaText);

        // then
        verify(context).findFormulasDefinition(FORMULA_NAME);
        verifyNoInteractions(cellReferenceResolver);
        verifyNoInteractions(textValueResolver);
        verify(numberValueResolver).resolve(NUMBER_ARG_TEXT);
        verify(numberValueResolver).resolve("2");

        // and
        assertEquals(FORMULA_NAME, result.functionName());
        assertArgumentsEquals(List.of(expectedArg1, expectedArg2), result.arguments());
        assertEquals(expectedResult, result.action().get());
    }

    @ParameterizedTest(name = "should match many text args formula - \"{0}\"")
    @ValueSource(strings = {"action('abc ','xyz')", "action ( 'abc ' , 'xyz' ) "})
    void shouldMatchManyTextArgsFormula(String formulaText) throws InvalidInputException {
        // given
        var expectedArg1 = new TextValue("abc ");
        var expectedArg2 = new TextValue("xyz");
        var expectedResult = new TextValue("abc xyz");

        // and
        when(context.findFormulasDefinition(FORMULA_NAME)).thenReturn(manyArgsDefinitions);
        when(textValueResolver.resolve(TEXT_ARG_TEXT)).thenReturn(c -> expectedArg1);
        when(textValueResolver.resolve("'xyz'")).thenReturn(c -> expectedArg2);

        // when
        var result = parser.parse(formulaText);

        // then
        verify(context).findFormulasDefinition(FORMULA_NAME);
        verifyNoInteractions(cellReferenceResolver);
        verify(textValueResolver).resolve(TEXT_ARG_TEXT);
        verify(textValueResolver).resolve("'xyz'");
        verifyNoInteractions(numberValueResolver);

        // and
        assertEquals(FORMULA_NAME, result.functionName());
        assertArgumentsEquals(List.of(expectedArg1, expectedArg2), result.arguments());
        assertEquals(expectedResult, result.action().get());
    }

    @ParameterizedTest(name = "should match many cell reference args formula - \"{0}\"")
    @ValueSource(strings = {"action(C1,D2)", "action ( C1 , D2 ) "})
    void shouldMatchManyCellReferenceArgsFormula(String formulaText) throws InvalidInputException {
        // given
        var expectedArg1 = new NumberValue(1);
        var expectedArg2 = new NumberValue(2);
        var expectedResult = new NumberValue(3);

        // and
        when(context.findFormulasDefinition(FORMULA_NAME)).thenReturn(manyArgsDefinitions);
        when(cellReferenceResolver.resolve(CELL_REFERENCE_ARG_TEXT)).thenReturn(c -> expectedArg1);
        when(cellReferenceResolver.resolve("D2")).thenReturn(c -> expectedArg2);

        // when
        var result = parser.parse(formulaText);

        // then
        verify(context).findFormulasDefinition(FORMULA_NAME);
        verify(cellReferenceResolver).resolve(CELL_REFERENCE_ARG_TEXT);
        verifyNoInteractions(textValueResolver);
        verifyNoInteractions(numberValueResolver);

        // and
        assertEquals(FORMULA_NAME, result.functionName());
        assertArgumentsEquals(List.of(expectedArg1, expectedArg2), result.arguments());
        assertEquals(expectedResult, result.action().get());
    }

    @TestFactory
    Stream<DynamicTest> shouldMatchMultiArgFormula() {
        var argTypes = EnumSet.of(TEXT_VALUE, NUMBER_VALUE, CELL_REFERENCE);
        var permutations = new LinkedList<List<Token>>();
        for (var a : argTypes) {
            for (var b : argTypes) {
                for (var c : argTypes) {
                    permutations.add(List.of(a, b, c));
                }
            }
        }
        return permutations.stream()
                .map(permutation -> {
                    var formulaText = permutation.stream()
                            .map(FormulaParserTest::argText)
                            .collect(joining(", ", "action(", ")"));

                    return dynamicTest(
                            format("should match multi arg formula - '%s'", formulaText),
                            () -> {
                                // when
                                var formulaDefinition = formulaDefinition(permutation);
                                var expectedArg1 = expectedArg(permutation.get(0));
                                var expectedArg2 = expectedArg(permutation.get(1));
                                var expectedArg3 = expectedArg(permutation.get(2));
                                var expectedResult = Stream.of(expectedArg1, expectedArg2, expectedArg3)
                                        .map(Value::getAsString)
                                        .collect(joining(" "));

                                // and
                                when(context.findFormulasDefinition(FORMULA_NAME)).thenReturn(formulaDefinition);
                                when(cellReferenceResolver.resolve(CELL_REFERENCE_ARG_TEXT)).thenReturn(c -> new NumberValue(1));
                                when(textValueResolver.resolve(TEXT_ARG_TEXT)).thenReturn(c -> new TextValue("abc "));
                                when(numberValueResolver.resolve(NUMBER_ARG_TEXT)).thenReturn(c -> new NumberValue(1));

                                // when
                                var result = parser.parse(formulaText);

                                // then
                                verify(context).findFormulasDefinition(FORMULA_NAME);
                                if (permutation.contains(CELL_REFERENCE)) {
                                    verify(cellReferenceResolver, atLeast(1)).resolve(CELL_REFERENCE_ARG_TEXT);
                                }
                                if (permutation.contains(TEXT_VALUE)) {
                                    verify(textValueResolver, atLeast(1)).resolve(TEXT_ARG_TEXT);
                                }
                                if (permutation.contains(NUMBER_VALUE)) {
                                    verify(numberValueResolver, atLeast(1)).resolve(NUMBER_ARG_TEXT);
                                }

                                // and
                                assertEquals(FORMULA_NAME, result.functionName());
                                assertArgumentsEquals(List.of(expectedArg1, expectedArg2, expectedArg3), result.arguments());
                                assertEquals(expectedResult, result.action().get().getAsString());

                                // cleanup
                                reset(context, cellReferenceResolver, textValueResolver, numberValueResolver);
                            }
                    );
                });
    }

    private void assertArgumentsEquals(List<Value> expectedArgs, List<ArgumentValueSupplier> result) {
        final var resultArgs = result.stream()
                .map(it -> it.get(context))
                .collect(toUnmodifiableList());
        assertIterableEquals(expectedArgs, resultArgs);
    }

    private static String argText(Token token) {
        return switch (token) {
            case CELL_REFERENCE -> CELL_REFERENCE_ARG_TEXT;
            case NUMBER_VALUE -> NUMBER_ARG_TEXT;
            case TEXT_VALUE -> TEXT_ARG_TEXT;
            default -> throw new IllegalStateException();
        };
    }

    private static Value expectedArg(Token token) {
        return switch (token) {
            case CELL_REFERENCE, NUMBER_VALUE -> new NumberValue(1);
            case TEXT_VALUE -> new TextValue("abc ");
            default -> throw new IllegalStateException();
        };
    }

    private static List<FormulaDefinition> formulaDefinition(List<Token> argument) {
        List<Class<? extends Expression>> argTypes = argument.stream()
                .map(arg -> switch (arg) {
                    case CELL_REFERENCE, NUMBER_VALUE -> NumberValue.class;
                    case TEXT_VALUE -> TextValue.class;
                    default -> throw new IllegalStateException();
                })
                .collect(toUnmodifiableList());

        return singletonList(
                new FormulaDefinition(
                        FORMULA_NAME,
                        argTypes,
                        (context, args) -> {
                            assertEquals(3, args.size());
                            var text = args.stream()
                                    .map(arg -> arg.get(context))
                                    .map(Value::getAsString)
                                    .collect(joining(" "));
                            return new TextValue(text);
                        })
        );
    }
}
