package eu.jrie.abacus.lang.domain.grammar.rule;

import eu.jrie.abacus.lang.domain.grammar.GrammarElement;
import eu.jrie.abacus.lang.domain.grammar.GrammarRule;

import java.util.List;

import static eu.jrie.abacus.lang.domain.grammar.Token.FUNCTION_ARGS_SEPARATOR;

public final class FunctionArgs implements GrammarRule {
    @Override
    public List<List<GrammarElement>> getTokens() {
        return List.of(
                List.of(new FunctionArg(), FUNCTION_ARGS_SEPARATOR, new FunctionArgs()),
                List.of(new FunctionArg())
        );
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof FunctionArgs;
    }
}
