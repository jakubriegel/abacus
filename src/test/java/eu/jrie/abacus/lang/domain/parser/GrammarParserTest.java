package eu.jrie.abacus.lang.domain.parser;

import eu.jrie.abacus.lang.domain.grammar.RuleMatch;
import eu.jrie.abacus.lang.domain.grammar.TokenMatch;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.List;

import static eu.jrie.abacus.lang.domain.grammar.Token.CELL_REFERENCE;
import static eu.jrie.abacus.lang.domain.grammar.Token.LOGIC_FALSE_VALUE;
import static eu.jrie.abacus.lang.domain.grammar.Token.LOGIC_TRUE_VALUE;
import static eu.jrie.abacus.lang.domain.grammar.Token.NUMBER_VALUE;
import static eu.jrie.abacus.lang.domain.grammar.Token.TEXT_VALUE;
import static eu.jrie.abacus.lang.domain.parser.ParserTestHelper.ARGS_START_MATCH;
import static eu.jrie.abacus.lang.domain.parser.ParserTestHelper.ARGS_START_WITH_SPACES_MATCH;
import static eu.jrie.abacus.lang.domain.parser.ParserTestHelper.ARGS_STOP_MATCH;
import static eu.jrie.abacus.lang.domain.parser.ParserTestHelper.ARGS_STOP_WITH_SPACES_MATCH;
import static eu.jrie.abacus.lang.domain.parser.ParserTestHelper.FORMULA_ARG_FORMULA_MATCH;
import static eu.jrie.abacus.lang.domain.parser.ParserTestHelper.FUNCTION_ARGS_RULE;
import static eu.jrie.abacus.lang.domain.parser.ParserTestHelper.FUNCTION_ARG_RULE;
import static eu.jrie.abacus.lang.domain.parser.ParserTestHelper.FUNCTION_NAME_MATCH;
import static eu.jrie.abacus.lang.domain.parser.ParserTestHelper.FUNCTION_NAME_WITH_SPACES_MATCH;
import static eu.jrie.abacus.lang.domain.parser.ParserTestHelper.FUNCTION_RULE;
import static eu.jrie.abacus.lang.domain.parser.ParserTestHelper.NO_ARG_FUNCTION_MATCH;
import static eu.jrie.abacus.lang.domain.parser.ParserTestHelper.NUMBER_ARG_FUNCTION_MATCH;
import static eu.jrie.abacus.lang.domain.parser.ParserTestHelper.TEXT_ARG_FUNCTION_MATCH;
import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;

class GrammarParserTest {

    private final GrammarParser parser = new GrammarParser();

    @Test
    void shouldMatchNoArgFormula() {
        // given
        var text = "action()";

        // when
        var result = parser.matchRule(FUNCTION_RULE, text, new LinkedList<>());

        // then
        assertIterableEquals(NO_ARG_FUNCTION_MATCH, result);
    }

    @Test
    void shouldMatchNoArgFormulaWithSpaces() {
        // given
        var text = "action ( ) ";

        // when
        var result = parser.matchRule(FUNCTION_RULE, text, new LinkedList<>());

        // then
        assertIterableEquals(
                singletonList(new RuleMatch(
                        FUNCTION_RULE,
                        List.of(FUNCTION_NAME_WITH_SPACES_MATCH, ARGS_START_WITH_SPACES_MATCH, ARGS_STOP_WITH_SPACES_MATCH))
                ),
                result
        );
    }

    @Test
    void shouldMatchNumberArgFormula() {
        // given
        var text = "action(1)";

        // when
        var result = parser.matchRule(FUNCTION_RULE, text, new LinkedList<>());

        // then
        assertIterableEquals(NUMBER_ARG_FUNCTION_MATCH, result);
    }

    @Test
    void shouldMatchNumberArgFormulaWithSpaces() {
        // given
        var text = "action ( 1 ) ";

        // when
        var result = parser.matchRule(FUNCTION_RULE, text, new LinkedList<>());

        // then
        assertIterableEquals(singletonList(new RuleMatch(
                FUNCTION_RULE,
                List.of(
                        FUNCTION_NAME_WITH_SPACES_MATCH,
                        ARGS_START_WITH_SPACES_MATCH,
                        new RuleMatch(
                                FUNCTION_ARGS_RULE,
                                singletonList(new RuleMatch(
                                        FUNCTION_ARG_RULE,
                                        singletonList(new TokenMatch(NUMBER_VALUE, "1", "1 "))
                                ))
                        ),
                        ARGS_STOP_WITH_SPACES_MATCH
                )
        )), result);
    }

    @Test
    void shouldMatchTextArgFormula() {
        // given
        var text = "action('abc ')";

        // when
        var result = parser.matchRule(FUNCTION_RULE, text, new LinkedList<>());

        // then
        assertIterableEquals(TEXT_ARG_FUNCTION_MATCH, result);
    }

    @Test
    void shouldMatchTextArgFormulaWithSpaces() {
        // given
        var text = "action ( 'abc ' ) ";

        // when
        var result = parser.matchRule(FUNCTION_RULE, text, new LinkedList<>());

        // then
        assertIterableEquals(singletonList(new RuleMatch(
                FUNCTION_RULE,
                List.of(
                        FUNCTION_NAME_WITH_SPACES_MATCH,
                        ARGS_START_WITH_SPACES_MATCH,
                        new RuleMatch(
                                FUNCTION_ARGS_RULE,
                                singletonList(new RuleMatch(
                                        FUNCTION_ARG_RULE,
                                        singletonList(new TokenMatch(TEXT_VALUE, "'abc '", "'abc ' "))
                                ))
                        ),
                        ARGS_STOP_WITH_SPACES_MATCH
                )
        )), result);
    }

    @Test
    void shouldMatchLogicTrueArgFormula() {
        // given
        var text = "action(true)";

        // when
        var result = parser.matchRule(FUNCTION_RULE, text, new LinkedList<>());

        // then
        assertIterableEquals(singletonList(new RuleMatch(
                FUNCTION_RULE,
                List.of(
                        FUNCTION_NAME_MATCH,
                        ARGS_START_MATCH,
                        new RuleMatch(
                                FUNCTION_ARGS_RULE,
                                singletonList(new RuleMatch(
                                        FUNCTION_ARG_RULE,
                                        singletonList(new TokenMatch(LOGIC_TRUE_VALUE, "true", "true"))
                                ))
                        ),
                        ARGS_STOP_MATCH
                )
        )), result);
    }

    @Test
    void shouldMatchLogicTrueArgFormulaWithSpaces() {
        // given
        var text = "action ( true ) ";

        // when
        var result = parser.matchRule(FUNCTION_RULE, text, new LinkedList<>());

        // then
        assertIterableEquals(singletonList(new RuleMatch(
                FUNCTION_RULE,
                List.of(
                        FUNCTION_NAME_WITH_SPACES_MATCH,
                        ARGS_START_WITH_SPACES_MATCH,
                        new RuleMatch(
                                FUNCTION_ARGS_RULE,
                                singletonList(new RuleMatch(
                                        FUNCTION_ARG_RULE,
                                        singletonList(new TokenMatch(LOGIC_TRUE_VALUE, "true", "true "))
                                ))
                        ),
                        ARGS_STOP_WITH_SPACES_MATCH
                )
        )), result);
    }

    @Test
    void shouldMatchLogicFalseArgFormula() {
        // given
        var text = "action(false)";

        // when
        var result = parser.matchRule(FUNCTION_RULE, text, new LinkedList<>());

        // then
        assertIterableEquals(singletonList(new RuleMatch(
                FUNCTION_RULE,
                List.of(
                        FUNCTION_NAME_MATCH,
                        ARGS_START_MATCH,
                        new RuleMatch(
                                FUNCTION_ARGS_RULE,
                                singletonList(new RuleMatch(
                                        FUNCTION_ARG_RULE,
                                        singletonList(new TokenMatch(LOGIC_FALSE_VALUE, "false", "false"))
                                ))
                        ),
                        ARGS_STOP_MATCH
                )
        )), result);
    }

    @Test
    void shouldMatchLogicFalseArgFormulaWithSpaces() {
        // given
        var text = "action ( false ) ";

        // when
        var result = parser.matchRule(FUNCTION_RULE, text, new LinkedList<>());

        // then
        assertIterableEquals(singletonList(new RuleMatch(
                FUNCTION_RULE,
                List.of(
                        FUNCTION_NAME_WITH_SPACES_MATCH,
                        ARGS_START_WITH_SPACES_MATCH,
                        new RuleMatch(
                                FUNCTION_ARGS_RULE,
                                singletonList(new RuleMatch(
                                        FUNCTION_ARG_RULE,
                                        singletonList(new TokenMatch(LOGIC_FALSE_VALUE, "false", "false "))
                                ))
                        ),
                        ARGS_STOP_WITH_SPACES_MATCH
                )
        )), result);
    }

    @Test
    void shouldMatchCellReferenceArgFormula() {
        // given
        var text = "action(C1)";

        // when
        var result = parser.matchRule(FUNCTION_RULE, text, new LinkedList<>());

        // then
        assertIterableEquals(singletonList(new RuleMatch(
                FUNCTION_RULE,
                List.of(
                        FUNCTION_NAME_MATCH,
                        ARGS_START_MATCH,
                        new RuleMatch(
                                FUNCTION_ARGS_RULE,
                                singletonList(new RuleMatch(
                                        FUNCTION_ARG_RULE,
                                        singletonList(new TokenMatch(CELL_REFERENCE, "C1", "C1"))
                                ))
                        ),
                        ARGS_STOP_MATCH
                )
        )), result);
    }

    @Test
    void shouldMatchCellReferenceArgFormulaWithSpaces() {
        // given
        var text = "action ( C1 ) ";

        // when
        var result = parser.matchRule(FUNCTION_RULE, text, new LinkedList<>());

        // then
        assertIterableEquals(singletonList(new RuleMatch(
                FUNCTION_RULE,
                List.of(
                        FUNCTION_NAME_WITH_SPACES_MATCH,
                        ARGS_START_WITH_SPACES_MATCH,
                        new RuleMatch(
                                FUNCTION_ARGS_RULE,
                                singletonList(new RuleMatch(
                                        FUNCTION_ARG_RULE,
                                        singletonList(new TokenMatch(CELL_REFERENCE, "C1", "C1 "))
                                ))
                        ),
                        ARGS_STOP_WITH_SPACES_MATCH
                )
        )), result);
    }

    @Test
    void shouldMatchFormulaArgFormula() {
        // given
        var text = "action(action(1))";

        // when
        var result = parser.matchRule(FUNCTION_RULE, text, new LinkedList<>());

        // then
        assertIterableEquals(FORMULA_ARG_FORMULA_MATCH, result);
    }

    @Test
    void shouldMatchFormulaArgFormulaWithSpaces() {
        // given
        var text = "action ( action ( 1 ) ) ";

        // when
        var result = parser.matchRule(FUNCTION_RULE, text, new LinkedList<>());

        // then
        assertIterableEquals(singletonList(new RuleMatch(
                FUNCTION_RULE,
                List.of(
                        FUNCTION_NAME_WITH_SPACES_MATCH,
                        ARGS_START_WITH_SPACES_MATCH,
                        new RuleMatch(
                                FUNCTION_ARGS_RULE,
                                singletonList(new RuleMatch(
                                        FUNCTION_ARG_RULE,
                                        singletonList(new RuleMatch(
                                                FUNCTION_RULE,
                                                List.of(
                                                        FUNCTION_NAME_WITH_SPACES_MATCH,
                                                        ARGS_START_WITH_SPACES_MATCH,
                                                        new RuleMatch(
                                                                FUNCTION_ARGS_RULE,
                                                                singletonList(new RuleMatch(
                                                                        FUNCTION_ARG_RULE,
                                                                        singletonList(new TokenMatch(NUMBER_VALUE, "1", "1 "))
                                                                ))
                                                        ),
                                                        ARGS_STOP_WITH_SPACES_MATCH
                                                )
                                        ))
                                ))
                        ),
                        ARGS_STOP_WITH_SPACES_MATCH
                )
        )), result);
    }
}