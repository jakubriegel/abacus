package eu.jrie.abacus.lang.domain.grammar.rule;

import eu.jrie.abacus.lang.domain.grammar.GrammarElement;
import eu.jrie.abacus.lang.domain.grammar.GrammarRule;

import java.util.List;

import static eu.jrie.abacus.lang.domain.grammar.Token.FUNCTION_ARG;
import static eu.jrie.abacus.lang.domain.grammar.Token.FUNCTION_ARGS_SEPARATOR;

public final class FunctionArgs implements GrammarRule {

    @Override
    public List<List<GrammarElement>> getTokens() {
        return List.of(
                List.of(FUNCTION_ARG, FUNCTION_ARGS_SEPARATOR, new FunctionArgs()),
                List.of(FUNCTION_ARG)
        );
    }
}
