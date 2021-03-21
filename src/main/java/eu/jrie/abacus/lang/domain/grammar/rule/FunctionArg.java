package eu.jrie.abacus.lang.domain.grammar.rule;

import eu.jrie.abacus.lang.domain.grammar.GrammarElement;
import eu.jrie.abacus.lang.domain.grammar.GrammarRule;

import java.util.List;

import static eu.jrie.abacus.lang.domain.grammar.Token.CELL_REFERENCE;
import static eu.jrie.abacus.lang.domain.grammar.Token.NUMBER_VALUE;
import static eu.jrie.abacus.lang.domain.grammar.Token.TEXT_VALUE;
import static java.util.Collections.singletonList;

public class FunctionArg implements GrammarRule {
    @Override
    public List<List<GrammarElement>> getTokens() {
        return List.of(
                singletonList(TEXT_VALUE),
                singletonList(NUMBER_VALUE),
                singletonList(CELL_REFERENCE)
        );
    }
}
