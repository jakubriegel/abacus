package eu.jrie.abacus.lang;

import eu.jrie.abacus.core.domain.expression.Expression;
import eu.jrie.abacus.core.domain.expression.Formula;
import eu.jrie.abacus.core.domain.expression.NumberValue;
import eu.jrie.abacus.core.domain.expression.TextValue;
import eu.jrie.abacus.core.domain.formula.FormulaDefinition;
import eu.jrie.abacus.lang.domain.exception.InvalidInputException;
import eu.jrie.abacus.lang.domain.grammar.GrammarElement;
import eu.jrie.abacus.lang.domain.grammar.GrammarRule;
import eu.jrie.abacus.lang.domain.grammar.Token;
import eu.jrie.abacus.lang.domain.grammar.TokenMatch;
import eu.jrie.abacus.lang.domain.grammar.rule.Function;

import java.lang.reflect.InvocationTargetException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

import static eu.jrie.abacus.lang.domain.grammar.Token.FUNCTION_ARG;
import static eu.jrie.abacus.lang.domain.grammar.Token.FUNCTION_NAME;
import static java.lang.Integer.parseInt;
import static java.util.Collections.singletonList;
import static java.util.Collections.unmodifiableList;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

public class Parser {

    private static final String FORMULA_INIT = "=";

    private final Map<String, FormulaDefinition> formulas;

    public Parser(Map<String, FormulaDefinition> formulas) {
        this.formulas = formulas;
    }

    public Expression parse(String text) throws InvalidInputException {
        return parseTrimmed(text.trim());
    }

    private Expression parseTrimmed(String text) throws InvalidInputException {
        if (isFormula(text)) {
            return resolveFormula(text.substring(1));
        } else if (isNumberValue(text)) {
            return new NumberValue(parseInt(text));
        } else /* text value */ {
            return new TextValue(text);
        }
    }

    private static boolean isFormula(String text) {
        return text.startsWith(FORMULA_INIT);
    }

    private static boolean isNumberValue(String text) {
        Pattern pattern = Pattern.compile("[0-9]+");
        return pattern.matcher(text)
                .matches();
    }

    private Formula resolveFormula(String text) throws InvalidInputException {
        final var matches = matchRule(new Function(), text, new LinkedList<>());
        var name = matches.stream()
                .filter(it -> it.token() == FUNCTION_NAME)
                .findFirst()
                .map(TokenMatch::match)
                .get();

        var arguments = matches.stream()
                .filter(it -> it.token() == FUNCTION_ARG)
                .map(TokenMatch::match)
                .collect(toList());

        var definition = formulas.get("cool");

        if (definition.argumentTypes().size() != arguments.size()) {
            throw new IllegalStateException();
        }

        var readyArguments = IntStream.range(0, definition.argumentTypes().size())
                .mapToObj(i -> {
                    var arg = arguments.get(i);
                    var type = definition.argumentTypes().get(i);
                    try {
                        return type.getConstructor(String.class).newInstance(arg);
                    } catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
                        throw new IllegalStateException(e);
                    }
                })
                .collect(toList());

        return new Formula(name, readyArguments, () -> definition.action().run(readyArguments));
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
                var matcher = token.pattern.matcher(text);
                boolean found = matcher.find();
                if (found) {
                    if (matcher.start() == 0) {
                        var name = matcher.group();
                        var match = new TokenMatch(token, name);
                        return match(availableTokens, text.substring(matcher.end()), appended(results, match));
                    }
                }
                return null;
            } else if (element instanceof GrammarRule rule) {
                var matched = matchRule(rule, text, new LinkedList<>());
                if (matched == null) {
                    return null;
                } else {
                    var matchedText = matched.stream()
                            .map(TokenMatch::match)
                            .collect(joining());
                    return match(availableTokens, text.substring(matchedText.length()), appended(results, matched));
                }
            }
        }
        throw new IllegalStateException();
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
