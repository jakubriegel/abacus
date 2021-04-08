package eu.jrie.abacus.lang.domain.parser;

import eu.jrie.abacus.core.domain.expression.Expression;
import eu.jrie.abacus.core.domain.expression.NumberValue;
import eu.jrie.abacus.core.domain.expression.TextValue;
import eu.jrie.abacus.core.domain.expression.Value;
import eu.jrie.abacus.core.domain.formula.ArgumentValueSupplier;
import eu.jrie.abacus.core.domain.formula.FormulaImplementation;
import eu.jrie.abacus.core.domain.workbench.WorkbenchContext;
import eu.jrie.abacus.lang.domain.exception.InvalidInputException;
import eu.jrie.abacus.lang.domain.exception.formula.CouldNotMatchFormulaDefinitionException;
import eu.jrie.abacus.lang.domain.exception.formula.InvalidArgumentNumberException;
import eu.jrie.abacus.lang.domain.exception.formula.InvalidArgumentTypeException;
import eu.jrie.abacus.lang.domain.exception.formula.UnknownSyntaxException;
import eu.jrie.abacus.lang.domain.grammar.ElementMatch;
import eu.jrie.abacus.lang.domain.grammar.RuleMatch;
import eu.jrie.abacus.lang.domain.grammar.Token;
import eu.jrie.abacus.lang.domain.grammar.TokenMatch;
import eu.jrie.abacus.lang.domain.parser.argument.ArgumentParser;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatcher;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

import static eu.jrie.abacus.lang.domain.grammar.Token.NUMBER_VALUE;
import static eu.jrie.abacus.lang.domain.parser.ParserTestHelper.FORMULA_ARG_FORMULA_MATCH;
import static eu.jrie.abacus.lang.domain.parser.ParserTestHelper.FUNCTION_RULE;
import static eu.jrie.abacus.lang.domain.parser.ParserTestHelper.NO_ARG_FUNCTION_MATCH;
import static eu.jrie.abacus.lang.domain.parser.ParserTestHelper.NUMBER_ARG_FUNCTION_MATCH;
import static java.math.BigDecimal.ONE;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.toUnmodifiableList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class FormulaParserTest {

    private static final String FORMULA_TEXT = "text";
    private static final String FORMULA_NAME = "action";
    private static final String NUMBER_ARG_TEXT = "1";

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
    private static final FormulaImplementation SINGLE_ARG_FORMULA = formula(singletonList(NumberValue.class), args -> {
        assertEquals(1, args.size());
        var arg = args.get(0).get(null);
        assertTrue(arg instanceof NumberValue);
        return arg;
    });
    private static final List<FormulaImplementation> SINGLE_ARG_FORMULAS = singletonList(SINGLE_ARG_FORMULA);

    private final WorkbenchContext context = mock(WorkbenchContext.class);
    private final GrammarParser grammarParser = mock(GrammarParser.class);
    private final ArgumentParser argumentParser = mock(ArgumentParser.class);

    private final FormulaParser parser = new FormulaParser(context, grammarParser, argumentParser);

    @Test
    void shouldMatchNoArgFormula() throws InvalidInputException {
        // given
        mockGrammarParserFor(NO_ARG_FUNCTION_MATCH);
        when(context.findFormulasDefinition(FORMULA_NAME)).thenReturn(singletonList(NO_ARG_FORMULA));
        when(argumentParser.parseArgs(eq(NO_ARG_FORMULA), eq(emptyList()), any())).thenReturn(emptyList());

        // when
        var result = parser.parse(FORMULA_TEXT);

        // then
        verify(context).findFormulasDefinition(FORMULA_NAME);
        verify(argumentParser).parseArgs(eq(NO_ARG_FORMULA), eq(emptyList()), any());

        // and
        assertEquals(FORMULA_NAME, result.functionName());
        assertIterableEquals(emptyList(), result.arguments());
        assertEquals("ok", result.action().get().getAsString());
    }

    @Test
    void shouldThrowExceptionWhenParserDidNotFindAnyMatches() {
        // given
        mockGrammarParserFor(null);

        // when
        assertThrows(UnknownSyntaxException.class, () -> parser.parse(FORMULA_TEXT));
    }

    @Test
    void shouldThrowExceptionWhenParserReturnedEmptyMatches() {
        // given
        mockGrammarParserFor(emptyList());

        // when
        assertThrows(UnknownSyntaxException.class, () -> parser.parse(FORMULA_TEXT));
    }

    @Test
    void shouldMatchSingleArgFormula() throws InvalidInputException {
        // given
        mockGrammarParserFor(NUMBER_ARG_FUNCTION_MATCH);
        var matchedArg = new TokenMatchMatcher(NUMBER_VALUE, NUMBER_ARG_TEXT);
        var expectedArg = new NumberValue(ONE);

        // and
        when(context.findFormulasDefinition(FORMULA_NAME)).thenReturn(SINGLE_ARG_FORMULAS);
        when(argumentParser.parseArgs(eq(SINGLE_ARG_FORMULA), matches(matchedArg), any())).thenReturn(singletonList(c -> expectedArg));

        // when
        var result = parser.parse(FORMULA_TEXT);

        // then
        verify(context).findFormulasDefinition(FORMULA_NAME);
        verify(argumentParser).parseArgs(eq(SINGLE_ARG_FORMULA), matches(matchedArg), any());

        // and
        assertEquals(FORMULA_NAME, result.functionName());
        assertArgumentsEquals(singletonList(expectedArg), result.arguments());
        assertEquals(expectedArg, result.action().get());
    }

    @Test
    void shouldMatchSingleFormulaArgFormula() throws InvalidInputException {
        // given
        mockGrammarParserFor(FORMULA_ARG_FORMULA_MATCH);
        var expectedArg = new NumberValue(ONE);

        // and
        when(context.findFormulasDefinition(FORMULA_NAME)).thenReturn(SINGLE_ARG_FORMULAS);
        when(argumentParser.parseArgs(eq(SINGLE_ARG_FORMULA), matchesFormula(), any())).thenReturn(singletonList(c -> expectedArg));

        // when
        var result = parser.parse(FORMULA_TEXT);

        // then
        verify(context).findFormulasDefinition(FORMULA_NAME);
        verify(argumentParser).parseArgs(eq(SINGLE_ARG_FORMULA), matchesFormula(), any());

        // and
        assertEquals(FORMULA_NAME, result.functionName());
        assertArgumentsEquals(singletonList(expectedArg), result.arguments());
        assertEquals(expectedArg, result.action().get());
    }

    @Test
    void shouldThrowExceptionWhenGivenFormulaDoesNotExist() {
        // given
        mockGrammarParserFor(NUMBER_ARG_FUNCTION_MATCH);
        when(context.findFormulasDefinition(FORMULA_NAME)).thenReturn(null);

        // when
        assertThrows(CouldNotMatchFormulaDefinitionException.class, () -> parser.parse(FORMULA_TEXT));
    }

    @Test
    void shouldThrowExceptionWhenNoArgsWereMatched() throws InvalidArgumentNumberException, InvalidArgumentTypeException {
        // given
        mockGrammarParserFor(NUMBER_ARG_FUNCTION_MATCH);
        when(context.findFormulasDefinition(FORMULA_NAME)).thenReturn(SINGLE_ARG_FORMULAS);
        when(argumentParser.parseArgs(any(), any(), any())).thenThrow(new InvalidArgumentNumberException());

        // when
        assertThrows(CouldNotMatchFormulaDefinitionException.class, () -> parser.parse(FORMULA_TEXT));
    }

    private void mockGrammarParserFor(List<ElementMatch> match) {
        when(grammarParser.matchRule(FUNCTION_RULE, FORMULA_TEXT, new LinkedList<>())).thenReturn(match);
    }

    private void assertArgumentsEquals(List<Value> expectedArgs, List<ArgumentValueSupplier> result) {
        final var resultArgs = result.stream()
                .map(it -> it.get(context))
                .collect(toUnmodifiableList());
        assertIterableEquals(expectedArgs, resultArgs);
    }

    private static record TokenMatchMatcher (
            Token token,
            String match
    ) {}

    private static record TokenMatchListMatcher(
            TokenMatchMatcher expected
    ) implements ArgumentMatcher<List<ElementMatch>> {
        @Override
        public boolean matches(List<ElementMatch> other) {
            return other.size() == 1 && other.stream()
                    .map(match -> (TokenMatch) match)
                    .allMatch(match -> Objects.equals(expected.token(), match.token())
                            && Objects.equals(expected.match, match.match()));
        }
    }

    private static List<ElementMatch> matches(TokenMatchMatcher matcher) {
        return argThat(new TokenMatchListMatcher(matcher));
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
