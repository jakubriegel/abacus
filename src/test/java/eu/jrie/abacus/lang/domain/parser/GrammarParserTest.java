package eu.jrie.abacus.lang.domain.parser;

import org.junit.jupiter.api.Test;

import java.util.LinkedList;

import static eu.jrie.abacus.lang.domain.parser.ParserTestHelper.CELL_REFERENCE_ARG_FUNCTION_MATCH;
import static eu.jrie.abacus.lang.domain.parser.ParserTestHelper.CELL_REFERENCE_ARG_FUNCTION_MATCH_WITH_SPACES;
import static eu.jrie.abacus.lang.domain.parser.ParserTestHelper.FORMULA_ARG_FORMULA_MATCH;
import static eu.jrie.abacus.lang.domain.parser.ParserTestHelper.FORMULA_ARG_FORMULA_MATCH_WITH_SPACES;
import static eu.jrie.abacus.lang.domain.parser.ParserTestHelper.FUNCTION_RULE;
import static eu.jrie.abacus.lang.domain.parser.ParserTestHelper.LOGIC_FALSE_ARG_FUNCTION_MATCH;
import static eu.jrie.abacus.lang.domain.parser.ParserTestHelper.LOGIC_FALSE_ARG_FUNCTION_MATCH_WITH_SPACES;
import static eu.jrie.abacus.lang.domain.parser.ParserTestHelper.LOGIC_TRUE_ARG_FUNCTION_MATCH;
import static eu.jrie.abacus.lang.domain.parser.ParserTestHelper.LOGIC_TRUE_ARG_FUNCTION_MATCH_WITH_SPACES;
import static eu.jrie.abacus.lang.domain.parser.ParserTestHelper.NO_ARG_FUNCTION_MATCH;
import static eu.jrie.abacus.lang.domain.parser.ParserTestHelper.NO_ARG_FUNCTION_MATCH_WITH_SPACES;
import static eu.jrie.abacus.lang.domain.parser.ParserTestHelper.NUMBER_ARG_FUNCTION_MATCH;
import static eu.jrie.abacus.lang.domain.parser.ParserTestHelper.NUMBER_ARG_FUNCTION_MATCH_WITH_SPACES;
import static eu.jrie.abacus.lang.domain.parser.ParserTestHelper.TEXT_ARG_FUNCTION_MATCH;
import static eu.jrie.abacus.lang.domain.parser.ParserTestHelper.TEXT_ARG_FUNCTION_MATCH_WITH_SPACES;
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
        assertIterableEquals(NO_ARG_FUNCTION_MATCH_WITH_SPACES, result);
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
        assertIterableEquals(NUMBER_ARG_FUNCTION_MATCH_WITH_SPACES, result);
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
        assertIterableEquals(TEXT_ARG_FUNCTION_MATCH_WITH_SPACES, result);
    }

    @Test
    void shouldMatchLogicTrueArgFormula() {
        // given
        var text = "action(true)";

        // when
        var result = parser.matchRule(FUNCTION_RULE, text, new LinkedList<>());

        // then
        assertIterableEquals(LOGIC_TRUE_ARG_FUNCTION_MATCH, result);
    }

    @Test
    void shouldMatchLogicTrueArgFormulaWithSpaces() {
        // given
        var text = "action ( true ) ";

        // when
        var result = parser.matchRule(FUNCTION_RULE, text, new LinkedList<>());

        // then
        assertIterableEquals(LOGIC_TRUE_ARG_FUNCTION_MATCH_WITH_SPACES, result);
    }

    @Test
    void shouldMatchLogicFalseArgFormula() {
        // given
        var text = "action(false)";

        // when
        var result = parser.matchRule(FUNCTION_RULE, text, new LinkedList<>());

        // then
        assertIterableEquals(LOGIC_FALSE_ARG_FUNCTION_MATCH, result);
    }

    @Test
    void shouldMatchLogicFalseArgFormulaWithSpaces() {
        // given
        var text = "action ( false ) ";

        // when
        var result = parser.matchRule(FUNCTION_RULE, text, new LinkedList<>());

        // then
        assertIterableEquals(LOGIC_FALSE_ARG_FUNCTION_MATCH_WITH_SPACES, result);
    }

    @Test
    void shouldMatchCellReferenceArgFormula() {
        // given
        var text = "action(C1)";

        // when
        var result = parser.matchRule(FUNCTION_RULE, text, new LinkedList<>());

        // then
        assertIterableEquals(CELL_REFERENCE_ARG_FUNCTION_MATCH, result);
    }

    @Test
    void shouldMatchCellReferenceArgFormulaWithSpaces() {
        // given
        var text = "action ( C1 ) ";

        // when
        var result = parser.matchRule(FUNCTION_RULE, text, new LinkedList<>());

        // then
        assertIterableEquals(CELL_REFERENCE_ARG_FUNCTION_MATCH_WITH_SPACES, result);
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
        assertIterableEquals(FORMULA_ARG_FORMULA_MATCH_WITH_SPACES, result);
    }
}