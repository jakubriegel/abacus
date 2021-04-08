package eu.jrie.abacus.lang.domain.grammar;

sealed public interface ElementMatch permits RuleMatch, TokenMatch {
    String raw();
}
