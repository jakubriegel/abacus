package eu.jrie.abacus.lang.domain.grammar;

import java.util.List;

import static java.util.stream.Collectors.joining;

public record RuleMatch(
        List<ElementMatch> tokens,
        GrammarRule rule
) implements ElementMatch {
    @Override
    public String raw() {
        return tokens.stream()
                .map(ElementMatch::raw)
                .collect(joining());
    }
}
