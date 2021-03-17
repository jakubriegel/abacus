package eu.jrie.abacus.lang.domain.parser;

import eu.jrie.abacus.core.domain.expression.Expression;
import eu.jrie.abacus.core.domain.expression.Formula;
import eu.jrie.abacus.core.domain.formula.FormulaDefinition;
import eu.jrie.abacus.lang.domain.exception.CouldNotMatchFormulaDefinitionException;
import eu.jrie.abacus.lang.domain.exception.InvalidArgumentNumberException;
import eu.jrie.abacus.lang.domain.exception.InvalidArgumentTypeException;
import eu.jrie.abacus.lang.domain.exception.InvalidInputException;
import eu.jrie.abacus.lang.domain.exception.UnknownSyntaxException;
import eu.jrie.abacus.lang.domain.grammar.GrammarElement;
import eu.jrie.abacus.lang.domain.grammar.GrammarRule;
import eu.jrie.abacus.lang.domain.grammar.Token;
import eu.jrie.abacus.lang.domain.grammar.TokenMatch;
import eu.jrie.abacus.lang.domain.grammar.rule.Function;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import static eu.jrie.abacus.lang.domain.grammar.Token.FUNCTION_ARG;
import static eu.jrie.abacus.lang.domain.grammar.Token.FUNCTION_NAME;
import static java.util.Collections.singletonList;
import static java.util.Collections.unmodifiableList;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

class FormulaParser {

    private final Map<String, List<FormulaDefinition>> formulas;

    FormulaParser(Map<String, List<FormulaDefinition>> formulas) {
        this.formulas = formulas;
    }

    Formula parse(String text)  throws InvalidInputException {
        final var matches = matchRule(new Function(), text, new LinkedList<>());
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
                .filter(it -> it.token() == FUNCTION_ARG)
                .map(TokenMatch::match)
                .collect(toList());

        return Optional.ofNullable(formulas.get(name))
                .stream()
                .flatMap(Collection::stream)
                .map(definition -> {
                    try {
                        var parsedArguments = tryDefine(definition, givenArguments);
                        return new Formula(name, parsedArguments, () -> definition.action().run(parsedArguments));
                    } catch (InvalidInputException e) {
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .findFirst()
                .orElseThrow(CouldNotMatchFormulaDefinitionException::new);
    }

    private List<Expression> tryDefine(FormulaDefinition definition, List<String> givenArguments) throws InvalidInputException {

        final int argsSizeInDefinition = definition.argumentTypes().size();
        if (argsSizeInDefinition != givenArguments.size()) {
            throw new InvalidArgumentNumberException();
        }

        var parsedArguments = new ArrayList<Expression>(argsSizeInDefinition);
        for (int i = 0; i < argsSizeInDefinition; i++) {
            var arg = givenArguments.get(i);
            var type = definition.argumentTypes().get(i);
            try {
                var parsedArg = type.getConstructor(String.class).newInstance(arg);
                parsedArguments.add(parsedArg);
            } catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
                throw new InvalidArgumentTypeException(e);
            }
        }

        return parsedArguments;
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
                    var name = matcher.group();
                    var match = new TokenMatch(token, name);
                    return match(availableTokens, text.substring(matcher.end()), appended(results, match));
                }
            } else if (element instanceof GrammarRule rule) {
                final var matched = matchRule(rule, text, new LinkedList<>());
                if (matched == null) {
                    return null;
                } else {
                    var matchedText = matched.stream()
                            .map(TokenMatch::match)
                            .collect(joining());
                    return match(availableTokens, text.substring(matchedText.length()), appended(results, matched));
                }
            } else {
                throw new IllegalStateException();
            }
        }
    }

    private static <E> List<E> appended(final List<E> list, final E element) {
        return appended(list, singletonList(element));
    }

    private static <E> List<E> appended(final List<E> list, final List<E> elements) {
        final var result = new LinkedList<>(list);
        result.addAll(elements);
        return unmodifiableList(result);
    }
}
