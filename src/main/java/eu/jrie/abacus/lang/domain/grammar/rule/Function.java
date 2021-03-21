package eu.jrie.abacus.lang.domain.grammar.rule;

import eu.jrie.abacus.lang.domain.grammar.GrammarElement;
import eu.jrie.abacus.lang.domain.grammar.GrammarRule;

import java.util.List;

import static eu.jrie.abacus.lang.domain.grammar.Token.FUNCTION_ARGS_START;
import static eu.jrie.abacus.lang.domain.grammar.Token.FUNCTION_ARGS_STOP;
import static eu.jrie.abacus.lang.domain.grammar.Token.FUNCTION_NAME;

public final class Function implements GrammarRule {

    @Override
    public List<List<GrammarElement>> getTokens() {
        return List.of(
                List.of(FUNCTION_NAME, FUNCTION_ARGS_START, new FunctionArgs(), FUNCTION_ARGS_STOP),
                List.of(FUNCTION_NAME, FUNCTION_ARGS_START, FUNCTION_ARGS_STOP)
        );
    }
}
