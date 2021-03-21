package eu.jrie.abacus.lang.domain.grammar;

import java.util.List;

public non-sealed interface GrammarRule extends GrammarElement {
    List<List<GrammarElement>> getTokens();
}
