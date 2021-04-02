package eu.jrie.abacus.lang.domain.parser;

import eu.jrie.abacus.core.domain.expression.Formula;
import eu.jrie.abacus.core.domain.workbench.WorkbenchContext;
import eu.jrie.abacus.lang.domain.exception.CouldNotMatchFormulaDefinitionException;
import eu.jrie.abacus.lang.domain.exception.InvalidInputException;
import eu.jrie.abacus.lang.domain.exception.UnknownSyntaxException;
import eu.jrie.abacus.lang.domain.grammar.GrammarElement;
import eu.jrie.abacus.lang.domain.grammar.GrammarRule;
import eu.jrie.abacus.lang.domain.grammar.Token;
import eu.jrie.abacus.lang.domain.grammar.TokenMatch;
import eu.jrie.abacus.lang.domain.grammar.rule.Function;
import eu.jrie.abacus.lang.domain.parser.argument.ArgumentParser;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import static eu.jrie.abacus.lang.domain.grammar.Token.CELL_REFERENCE;
import static eu.jrie.abacus.lang.domain.grammar.Token.FUNCTION_NAME;
import static eu.jrie.abacus.lang.domain.grammar.Token.LOGIC_FALSE_VALUE;
import static eu.jrie.abacus.lang.domain.grammar.Token.LOGIC_TRUE_VALUE;
import static eu.jrie.abacus.lang.domain.grammar.Token.NUMBER_VALUE;
import static eu.jrie.abacus.lang.domain.grammar.Token.TEXT_VALUE;
import static eu.jrie.abacus.lang.infra.ListTools.appended;
import static java.util.Collections.unmodifiableSet;
import static java.util.EnumSet.of;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

class FormulaParser {

    private static final Set<Token> ARGUMENT_TOKENS = unmodifiableSet(of(
            TEXT_VALUE, NUMBER_VALUE, CELL_REFERENCE, LOGIC_TRUE_VALUE, LOGIC_FALSE_VALUE
    ));

    private final WorkbenchContext context;
    private final ArgumentParser argumentParser;

    FormulaParser(WorkbenchContext context, ArgumentParser argumentParser) {
        this.context = context;
        this.argumentParser = argumentParser;
    }

    Formula parse(String text) throws InvalidInputException {
        final var matches = matchRule(new Function(), text.trim(), new LinkedList<>());
        if (matches == null) {
            throw new UnknownSyntaxException();
        } else {
            return resolveFormula(matches);
        }
    }

    private Formula resolveFormula(List<TokenMatch> matches) throws InvalidInputException {
        final var name = matches.stream()
                .filter(it -> it.token() == FUNCTION_NAME)
                .findFirst()
                .map(TokenMatch::match)
                .orElseThrow(CouldNotMatchFormulaDefinitionException::new);

        final var givenArguments = matches.stream()
                .filter(it -> ARGUMENT_TOKENS.contains(it.token()))
                .collect(toList());

        return Optional.ofNullable(context.findFormulasDefinition(name))
                .stream()
                .flatMap(Collection::stream)
                .map(definition -> {
                    try {
                        var parsedArguments = argumentParser.parseArgs(definition, givenArguments);
                        return new Formula(name, parsedArguments, () -> definition.run(context, parsedArguments));
                    } catch (InvalidInputException e) {
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .findFirst()
                .orElseThrow(CouldNotMatchFormulaDefinitionException::new);
    }

    private List<TokenMatch> matchRule(GrammarRule rule, String text, List<TokenMatch> results) {
        for (List<GrammarElement> elements : new LinkedList<>(rule.getTokens())) {
            final var matches = match(new LinkedList<>(elements), text, new LinkedList<>());
            if (matches != null && !matches.isEmpty()) {
                return appended(results, matches);
            }
        }
        return null;
    }

    private List<TokenMatch> match(List<GrammarElement> availableTokens, String text, List<TokenMatch> results) {
        if (availableTokens.isEmpty()) {
            return results;
        } else {
            final var element = availableTokens.remove(0);
            if (element instanceof Token token) {
                final var matcher = token.pattern.matcher(text);
                if (!matcher.find() || matcher.start() != 0) {
                    return null;
                } else {
                    var matched = matcher.group();
                    var match = new TokenMatch(token, matched.trim(), matched);
                    return match(availableTokens, text.substring(matcher.end()), appended(results, match));
                }
            } else if (element instanceof GrammarRule rule) {
                final var matched = matchRule(rule, text, new LinkedList<>());
                if (matched == null) {
                    return null;
                } else {
                    var matchedText = matched.stream()
                            .map(TokenMatch::raw)
                            .collect(joining());
                    return match(availableTokens, text.substring(matchedText.length()), appended(results, matched));
                }
            } else {
                throw new IllegalStateException();
            }
        }
    }
}
