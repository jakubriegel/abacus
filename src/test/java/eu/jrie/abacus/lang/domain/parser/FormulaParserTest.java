package eu.jrie.abacus.lang.domain.parser;

import eu.jrie.abacus.core.domain.expression.Expression;
import eu.jrie.abacus.core.domain.expression.LogicValue;
import eu.jrie.abacus.core.domain.expression.NumberValue;
import eu.jrie.abacus.core.domain.expression.TextValue;
import eu.jrie.abacus.core.domain.expression.Value;
import eu.jrie.abacus.core.domain.formula.ArgumentValueSupplier;
import eu.jrie.abacus.core.domain.formula.FormulaImplementation;
import eu.jrie.abacus.core.domain.workbench.WorkbenchContext;
import eu.jrie.abacus.lang.domain.exception.InvalidInputException;
import eu.jrie.abacus.lang.domain.exception.formula.InvalidArgumentTypeException;
import eu.jrie.abacus.lang.domain.grammar.ElementMatch;
import eu.jrie.abacus.lang.domain.grammar.RuleMatch;
import eu.jrie.abacus.lang.domain.grammar.Token;
import eu.jrie.abacus.lang.domain.grammar.TokenMatch;
import eu.jrie.abacus.lang.domain.parser.argument.ArgumentParser;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentMatcher;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Stream;

import static eu.jrie.abacus.lang.domain.grammar.Token.CELL_REFERENCE;
import static eu.jrie.abacus.lang.domain.grammar.Token.LOGIC_TRUE_VALUE;
import static eu.jrie.abacus.lang.domain.grammar.Token.NUMBER_VALUE;
import static eu.jrie.abacus.lang.domain.grammar.Token.TEXT_VALUE;
import static java.lang.String.format;
import static java.math.BigDecimal.ONE;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toUnmodifiableList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class FormulaParserTest {

    private static final String FORMULA_NAME = "action";
    private static final String CELL_REFERENCE_ARG_TEXT = "C1";
    private static final String NUMBER_ARG_TEXT = "1";
    private static final String TEXT_ARG_TEXT = "'abc '";
    private static final String LOGIC_TRUE_ARG_TEXT = "true";
    private static final String LOGIC_FALSE_ARG_TEXT = "false";

    private static FormulaImplementation formula(List<Class<? extends Expression>> argTypes, Function<List<ArgumentValueSupplier>, Value> action) {
        return new FormulaImplementation() {
            @Override
            public String getName() { return FORMULA_NAME; }
            @Override
            public List<Class<? extends Expression>> getArgumentTypes() { return argTypes; }
            @Override
            public Value run(WorkbenchContext context, List<ArgumentValueSupplier> args) { return action.apply(args); }
        };
    }

    private static final FormulaImplementation NO_ARG_FORMULA = formula(emptyList(), args -> {
        assertEquals(0, args.size());
        return new TextValue("ok");
    });

    private static final FormulaImplementation SINGLE_NUMBER_ARG_FORMULA = formula(singletonList(NumberValue.class), args -> {
        assertEquals(1, args.size());
        var arg = args.get(0).get(null);
        assertTrue(arg instanceof NumberValue);
        return arg;
    });

    private static final FormulaImplementation SINGLE_TEXT_ARG_FORMULA = formula(singletonList(TextValue.class), args -> {
        assertEquals(1, args.size());
        var arg = args.get(0).get(null);
        assertTrue(arg instanceof TextValue);
        return arg;
    });

    private static final FormulaImplementation SINGLE_LOGIC_ARG_FORMULA = formula(singletonList(LogicValue.class), args -> {
        assertEquals(1, args.size());
        var arg = args.get(0).get(null);
        assertTrue(arg instanceof LogicValue);
        return arg;
    });

    private static final List<FormulaImplementation> singleArgFormulas = List.of(
            SINGLE_NUMBER_ARG_FORMULA, SINGLE_TEXT_ARG_FORMULA, SINGLE_LOGIC_ARG_FORMULA
    );

    private final WorkbenchContext context = mock(WorkbenchContext.class);
    private final ArgumentParser argumentParser = mock(ArgumentParser.class);

    private final FormulaParser parser = new FormulaParser(context, argumentParser);

    @ParameterizedTest(name = "should match no arg formula - \"{0}\"")
    @ValueSource(strings = {"action()", "action ( ) "})
    void shouldMatchNoArgFormula(String formulaText) throws InvalidInputException {
        // given
        when(context.findFormulasDefinition(FORMULA_NAME)).thenReturn(singletonList(NO_ARG_FORMULA));
        when(argumentParser.parseArgs(eq(NO_ARG_FORMULA), eq(emptyList()), any())).thenReturn(emptyList());

        // when
        var result = parser.parse(formulaText);

        // then
        verify(context).findFormulasDefinition(FORMULA_NAME);
        verify(argumentParser).parseArgs(eq(NO_ARG_FORMULA), eq(emptyList()), any());

        // and
        assertEquals(FORMULA_NAME, result.functionName());
        assertIterableEquals(emptyList(), result.arguments());
        assertEquals("ok", result.action().get().getAsString());
    }

    @ParameterizedTest(name = "should match single number arg formula - \"{0}\"")
    @ValueSource(strings = {"action(1)", "action ( 1 ) "})
    void shouldMatchSingleNumberArgFormula(String formulaText) throws InvalidInputException {
        // given
        var matchedArg = new TokenMatchMatcher(NUMBER_VALUE, NUMBER_ARG_TEXT);
        var expectedArg = new NumberValue(ONE);

        // and
        when(context.findFormulasDefinition(FORMULA_NAME)).thenReturn(singleArgFormulas);
        when(argumentParser.parseArgs(eq(SINGLE_NUMBER_ARG_FORMULA), matches(matchedArg), any())).thenReturn(singletonList(c -> expectedArg));

        // when
        var result = parser.parse(formulaText);

        // then
        verify(context).findFormulasDefinition(FORMULA_NAME);
        verify(argumentParser).parseArgs(eq(SINGLE_NUMBER_ARG_FORMULA), matches(matchedArg), any());

        // and
        assertEquals(FORMULA_NAME, result.functionName());
        assertArgumentsEquals(singletonList(expectedArg), result.arguments());
        assertEquals(expectedArg, result.action().get());
    }

    @ParameterizedTest(name = "should match single text arg formula - \"{0}\"")
    @ValueSource(strings = {"action('abc ')", "action ( 'abc ' ) "})
    void shouldMatchSingleTextArgFormula(String formulaText) throws InvalidInputException {
        // given
        var matchedArg = new TokenMatchMatcher(TEXT_VALUE, TEXT_ARG_TEXT);
        var expectedArg = new TextValue("abc ");

        // and
        when(context.findFormulasDefinition(FORMULA_NAME)).thenReturn(singleArgFormulas);
        when(argumentParser.parseArgs(eq(SINGLE_NUMBER_ARG_FORMULA), any(), any())).thenThrow(new InvalidArgumentTypeException());
        when(argumentParser.parseArgs(eq(SINGLE_TEXT_ARG_FORMULA), matches(matchedArg), any())).thenReturn(singletonList(c -> expectedArg));

        // when
        var result = parser.parse(formulaText);

        // then
        verify(context, atLeast(1)).findFormulasDefinition(FORMULA_NAME);
        verify(argumentParser).parseArgs(eq(SINGLE_TEXT_ARG_FORMULA), matches(matchedArg), any());

        // and
        assertEquals(FORMULA_NAME, result.functionName());
        assertArgumentsEquals(singletonList(expectedArg), result.arguments());
        assertEquals(expectedArg, result.action().get());
    }

    @ParameterizedTest(name = "should match single logic arg formula with true value- \"{0}\"")
    @ValueSource(strings = {"action(true)", "action ( true ) "})
    void shouldMatchSingleLogicArgFormulaWithTrueValue(String formulaText) throws InvalidInputException {
        // given
        var matchedArg = new TokenMatchMatcher(LOGIC_TRUE_VALUE, LOGIC_TRUE_ARG_TEXT);
        var expectedArg = new LogicValue(true);

        // and
        when(context.findFormulasDefinition(FORMULA_NAME)).thenReturn(singleArgFormulas);
        when(argumentParser.parseArgs(eq(SINGLE_NUMBER_ARG_FORMULA), any(), any())).thenThrow(new InvalidArgumentTypeException());
        when(argumentParser.parseArgs(eq(SINGLE_TEXT_ARG_FORMULA), any(), any())).thenThrow(new InvalidArgumentTypeException());
        when(argumentParser.parseArgs(eq(SINGLE_LOGIC_ARG_FORMULA), matches(matchedArg), any())).thenReturn(singletonList(c -> expectedArg));

        // when
        var result = parser.parse(formulaText);

        // then
        verify(context, atLeast(1)).findFormulasDefinition(FORMULA_NAME);
        verify(argumentParser).parseArgs(eq(SINGLE_LOGIC_ARG_FORMULA), matches(matchedArg), any());

        // and
        assertEquals(FORMULA_NAME, result.functionName());
        assertArgumentsEquals(singletonList(expectedArg), result.arguments());
        assertEquals(expectedArg, result.action().get());
    }

    @ParameterizedTest(name = "should match single cell reference arg formula - \"{0}\"")
    @ValueSource(strings = {"action(C1)", "action ( C1 ) "})
    void shouldMatchSingleCellReferenceArgFormula(String formulaText) throws InvalidInputException {
        // given
        var matchedArg = new TokenMatchMatcher(CELL_REFERENCE, CELL_REFERENCE_ARG_TEXT);
        var expectedArg = new NumberValue(ONE);

        // and
        when(context.findFormulasDefinition(FORMULA_NAME)).thenReturn(singleArgFormulas);
        when(argumentParser.parseArgs(eq(SINGLE_NUMBER_ARG_FORMULA), matches(matchedArg), any())).thenReturn(singletonList(c -> expectedArg));

        // when
        var result = parser.parse(formulaText);

        // then
        verify(context).findFormulasDefinition(FORMULA_NAME);
        verify(argumentParser).parseArgs(eq(SINGLE_NUMBER_ARG_FORMULA), matches(matchedArg), any());

        // and
        assertEquals(FORMULA_NAME, result.functionName());
        assertArgumentsEquals(singletonList(expectedArg), result.arguments());
        assertEquals(expectedArg, result.action().get());
    }

    @ParameterizedTest(name = "should match formula arg formula - \"{0}\"")
    @ValueSource(strings = {"action(action(1))", "action ( action ( 1 ) ) "})
    void shouldMatchSingleFormulaArgFormula(String formulaText) throws InvalidInputException {
        // given
        var expectedArg = new NumberValue(ONE);

        // and
        when(context.findFormulasDefinition(FORMULA_NAME)).thenReturn(singleArgFormulas);
        when(argumentParser.parseArgs(eq(SINGLE_NUMBER_ARG_FORMULA), matchesFormula(), any())).thenReturn(singletonList(c -> expectedArg));

        // when
        var result = parser.parse(formulaText);

        // then
        verify(context).findFormulasDefinition(FORMULA_NAME);
        verify(argumentParser).parseArgs(eq(SINGLE_NUMBER_ARG_FORMULA), matchesFormula(), any());

        // and
        assertEquals(FORMULA_NAME, result.functionName());
        assertArgumentsEquals(singletonList(expectedArg), result.arguments());
        assertEquals(expectedArg, result.action().get());
    }

    @TestFactory
    Stream<DynamicTest> shouldMatchMultiArgFormula() {
        var argTypes = List.of(TEXT_VALUE, NUMBER_VALUE, CELL_REFERENCE, LOGIC_TRUE_VALUE);
        var permutations = new LinkedList<List<Token>>();
        for (var a : argTypes) {
            for (var b : argTypes) {
                for (var c : argTypes) {
                    for (var d : argTypes) {
                        permutations.add(List.of(a, b, c, d));
                    }
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
                                var impl = formulaImplementation(permutation);
                                var expectedArgs = List.of(
                                        expectedArg(permutation.get(0)),
                                        expectedArg(permutation.get(1)),
                                        expectedArg(permutation.get(2)),
                                        expectedArg(permutation.get(3))
                                );
                                var expectedResult = expectedArgs.stream()
                                        .map(Value::getAsString)
                                        .collect(joining(" "));

                                var matchedArgs = permutation.stream()
                                        .map(token -> {
                                            var text = argText(token);
                                            return new TokenMatchMatcher(token, text);
                                        })
                                        .collect(toUnmodifiableList());

                                var parsedArgs = permutation.stream()
                                        .map(token -> {
                                            var expectedArg = expectedArg(token);
                                            @SuppressWarnings("UnnecessaryLocalVariable")
                                            ArgumentValueSupplier supplier = c -> expectedArg;
                                            return supplier;
                                        })
                                        .collect(toUnmodifiableList());


                                // and
                                when(context.findFormulasDefinition(FORMULA_NAME)).thenReturn(singletonList(impl));
                                when(argumentParser.parseArgs(eq(impl), matches(matchedArgs), any())).thenReturn(parsedArgs);

                                // when
                                var result = parser.parse(formulaText);

                                // then
                                verify(context).findFormulasDefinition(FORMULA_NAME);
                                verify(argumentParser).parseArgs(eq(impl), matches(matchedArgs), any());

                                // and
                                assertEquals(FORMULA_NAME, result.functionName());
                                assertArgumentsEquals(expectedArgs, result.arguments());
                                assertEquals(expectedResult, result.action().get().getAsString());

                                // cleanup
                                reset(context, argumentParser);
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
            case LOGIC_TRUE_VALUE -> LOGIC_TRUE_ARG_TEXT;
            case LOGIC_FALSE_VALUE -> LOGIC_FALSE_ARG_TEXT;
            default -> throw new IllegalStateException();
        };
    }

    private static Value expectedArg(Token token) {
        return switch (token) {
            case CELL_REFERENCE, NUMBER_VALUE -> new NumberValue(ONE);
            case TEXT_VALUE -> new TextValue("abc ");
            case LOGIC_TRUE_VALUE -> new LogicValue(true);
            case LOGIC_FALSE_VALUE -> new LogicValue(false);
            default -> throw new IllegalStateException();
        };
    }

    private static FormulaImplementation formulaImplementation(List<Token> argument) {
        List<Class<? extends Expression>> argTypes = argument.stream()
                .map(arg -> switch (arg) {
                    case CELL_REFERENCE, NUMBER_VALUE -> NumberValue.class;
                    case TEXT_VALUE -> TextValue.class;
                    case LOGIC_TRUE_VALUE, LOGIC_FALSE_VALUE -> LogicValue.class;
                    default -> throw new IllegalStateException();
                })
                .collect(toUnmodifiableList());

        return formula(argTypes, args -> {
            assertEquals(4, args.size());
            var text = args.stream()
                    .map(arg -> arg.get(null))
                    .map(Value::getAsString)
                    .collect(joining(" "));
            return new TextValue(text);
        });
    }

    private static record TokenMatchMatcher (
            Token token,
            String match
    ) {}

    private static record TokenMatchListMatcher(
            List<TokenMatchMatcher> matchers
    ) implements ArgumentMatcher<List<ElementMatch>> {
        @Override
        public boolean matches(List<ElementMatch> other) {
            var tokenMatches = other.stream()
                    .map(match -> (TokenMatch) match)
                    .collect(toUnmodifiableList());
            return matchesTokens(tokenMatches);
        }

        private boolean matchesTokens(List<TokenMatch> other) {
            if (other.size() != matchers.size()) {
                return false;
            } else {
                for (int i = 0; i < matchers.size(); i++) {
                    var expected = matchers.get(i);
                    var match = other.get(i);
                    var equal = Objects.equals(expected.token(), match.token())
                            && Objects.equals(expected.match, match.match());
                    if (!equal) return false;
                }
            }
            return true;
        }
    }

    private static List<ElementMatch> matches(TokenMatchMatcher matcher) {
        return matches(singletonList(matcher));
    }

    private static List<ElementMatch> matches(List<TokenMatchMatcher> matchers) {
        return argThat(new TokenMatchListMatcher(matchers));
    }

    private static class FormulaMatchListMatcher implements ArgumentMatcher<List<ElementMatch>> {
        @Override
        public boolean matches(List<ElementMatch> other) {
            return other.size() == 1 && other.stream()
                    .map(match -> (RuleMatch) match)
                    .map(RuleMatch::rule)
                    .allMatch(rule -> rule instanceof eu.jrie.abacus.lang.domain.grammar.rule.Function);
        }
    }

    private static List<ElementMatch> matchesFormula() {
        return argThat(new FormulaMatchListMatcher());
    }
}
