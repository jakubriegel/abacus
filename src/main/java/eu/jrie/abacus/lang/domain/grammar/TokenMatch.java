package eu.jrie.abacus.lang.domain.grammar;

public record TokenMatch (
        Token token,
        String match,
        String raw
) {}
