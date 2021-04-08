package eu.jrie.abacus.lang.domain.parser;

import eu.jrie.abacus.core.domain.expression.Formula;
import eu.jrie.abacus.core.domain.formula.ArgumentValueSupplier;
import eu.jrie.abacus.core.domain.formula.FormulaImplementation;
import eu.jrie.abacus.core.domain.workbench.WorkbenchContext;
import eu.jrie.abacus.lang.domain.exception.InvalidInputException;
import eu.jrie.abacus.lang.domain.exception.formula.CouldNotMatchFormulaDefinitionException;
import eu.jrie.abacus.lang.domain.exception.formula.FormulaParsingException;
import eu.jrie.abacus.lang.domain.exception.formula.InvalidArgumentNumberException;
import eu.jrie.abacus.lang.domain.exception.formula.InvalidArgumentTypeException;
import eu.jrie.abacus.lang.domain.exception.formula.UnknownSyntaxException;
import eu.jrie.abacus.lang.domain.grammar.ElementMatch;
import eu.jrie.abacus.lang.domain.grammar.GrammarElement;
import eu.jrie.abacus.lang.domain.grammar.GrammarRule;
import eu.jrie.abacus.lang.domain.grammar.RuleMatch;
import eu.jrie.abacus.lang.domain.grammar.Token;
import eu.jrie.abacus.lang.domain.grammar.TokenMatch;
import eu.jrie.abacus.lang.domain.grammar.rule.Function;
import eu.jrie.abacus.lang.domain.grammar.rule.FunctionArgs;
import eu.jrie.abacus.lang.domain.parser.argument.ArgumentParser;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

import static eu.jrie.abacus.lang.domain.grammar.Token.FUNCTION_NAME;
import static eu.jrie.abacus.lang.infra.ListTools.appended;
import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toUnmodifiableList;

class FormulaParser {

    private final WorkbenchContext context;
    private final ArgumentParser argumentParser;

    FormulaParser(WorkbenchContext context, ArgumentParser argumentParser) {
        this.context = context;
        this.argumentParser = argumentParser;
    }

    Formula parse(String text) throws InvalidInputException {
        final var matches = matchRule(new Function(), text.trim(), new LinkedList<>());
        if (matches == null || matches.size() != 1) {
            throw new UnknownSyntaxException();
        } else {
            var match = (RuleMatch) matches.get(0);
            return resolveFormula(match);
        }
    }

    private Formula resolveFormula(RuleMatch match) throws InvalidInputException {
        var name = resolveName(match);
        if (match.tokens().size() == 3) {
            return resolveFormula(name, emptyList());
        } else if (match.tokens().size() == 4) {
            var argsMatch = (RuleMatch) match.tokens().get(2);
            var args = flattenArgs(argsMatch)
                    .map(RuleMatch::tokens)
                    .map(matches -> matches.get(0))
                    .collect(toUnmodifiableList());
            return resolveFormula(name, args);
        } else {
            throw new UnknownSyntaxException();
        }
    }

    private static String resolveName(RuleMatch match) throws CouldNotMatchFormulaDefinitionException {
        return match.tokens()
                .stream()
                .filter(it -> it instanceof TokenMatch)
                .map(it -> (TokenMatch) it)
                .filter(it -> it.token() == FUNCTION_NAME)
                .findFirst()
                .map(TokenMatch::match)
                .orElseThrow(CouldNotMatchFormulaDefinitionException::new);
    }

    private static Stream<RuleMatch> flattenArgs(RuleMatch ruleMatch) {
        return ruleMatch.tokens()
                .stream()
                .filter(match -> !(match instanceof TokenMatch))
                .map(match -> (RuleMatch) match)
                .flatMap(match -> {
                    if (match.rule() instanceof FunctionArgs) {
                        return flattenArgs(match);
                    } else {
                        return Stream.of(match);
                    }
                });
    }

    private Formula resolveFormula(String name, List<ElementMatch> givenArguments) throws InvalidInputException {
        return Optional.ofNullable(context.findFormulasDefinition(name))
                .stream()
                .flatMap(Collection::stream)
                .map(definition -> {
                    try {
                        var parsedArguments = parseArgs(definition, givenArguments);
                        return new Formula(name, parsedArguments, () -> definition.run(context, parsedArguments));
                    } catch (FormulaParsingException e) {
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .findFirst()
                .orElseThrow(CouldNotMatchFormulaDefinitionException::new);
    }

    private List<ArgumentValueSupplier> parseArgs(
            FormulaImplementation definition, List<ElementMatch> givenArguments
    ) throws InvalidArgumentNumberException, InvalidArgumentTypeException {
        return argumentParser.parseArgs(definition, givenArguments, this::resolveFormula);
    }

    private static List<ElementMatch> matchRule(GrammarRule rule, String text, List<ElementMatch> results) {
        for (List<GrammarElement> elements : new LinkedList<>(rule.getTokens())) {
            final var matches = match(new LinkedList<>(elements), text, new LinkedList<>());
            if (matches != null && !matches.isEmpty()) {
                return appended(results, new RuleMatch(matches, rule));
            }
        }
        return null;
    }

    private static List<ElementMatch> match(List<GrammarElement> availableTokens, String text, List<ElementMatch> results) {
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
                            .map(ElementMatch::raw)
                            .collect(joining());
                    return match(availableTokens, text.substring(matchedText.length()), appended(results, matched));
                }
            } else {
                throw new IllegalStateException();
            }
        }
    }
}
