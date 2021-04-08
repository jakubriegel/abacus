package eu.jrie.abacus.lang.domain.parser;

import eu.jrie.abacus.lang.domain.grammar.ElementMatch;
import eu.jrie.abacus.lang.domain.grammar.GrammarElement;
import eu.jrie.abacus.lang.domain.grammar.GrammarRule;
import eu.jrie.abacus.lang.domain.grammar.RuleMatch;
import eu.jrie.abacus.lang.domain.grammar.Token;
import eu.jrie.abacus.lang.domain.grammar.TokenMatch;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;

import static eu.jrie.abacus.lang.infra.ListTools.appended;
import static java.util.Objects.isNull;

class GrammarParser {
    List<ElementMatch> matchRule(GrammarRule rule, String text, List<ElementMatch> results) {
        for (List<GrammarElement> elements : new LinkedList<>(rule.getTokens())) {
            var matches = match(new LinkedList<>(elements), text, new LinkedList<>());
            if (elementFound(matches)) {
                return appended(results, new RuleMatch(rule, matches));
            }
        }
        return null;
    }

    private static boolean elementFound(List<ElementMatch> matches) {
        return matches != null && !matches.isEmpty();
    }

    private List<ElementMatch> match(List<GrammarElement> availableTokens, String text, List<ElementMatch> results) {
        if (availableTokens.isEmpty()) {
            return results;
        } else {
            var element = availableTokens.remove(0);
            if (element instanceof Token token) {
                var matcher = token.pattern.matcher(text);
                if (tokenNotFound(matcher)) {
                    return null;
                } else {
                    var matched = matcher.group();
                    var match = new TokenMatch(token, matched.trim(), matched);
                    return match(availableTokens, text.substring(matcher.end()), appended(results, match));
                }
            } else if (element instanceof GrammarRule rule) {
                var matched = matchRule(rule, text, new LinkedList<>());
                if (ruleNotFound(matched)) {
                    return null;
                } else {
                    int length = matchedTextLength(matched);
                    return match(availableTokens, text.substring(length), appended(results, matched));
                }
            } else {
                throw new IllegalStateException();
            }
        }
    }

    private static boolean tokenNotFound(Matcher tokenMatcher) {
        return !tokenMatcher.find() || tokenMatcher.start() != 0;
    }

    private static boolean ruleNotFound(List<ElementMatch> matchedRule) {
        return isNull(matchedRule);
    }

    private static int matchedTextLength(List<ElementMatch> matchedRule) {
        return matchedRule.stream()
                .map(ElementMatch::raw)
                .mapToInt(String::length)
                .sum();
    }
}
