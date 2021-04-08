package eu.jrie.abacus.lang.domain.parser.argument;

import eu.jrie.abacus.core.domain.expression.Expression;
import eu.jrie.abacus.core.domain.expression.LogicValue;
import eu.jrie.abacus.core.domain.expression.NumberValue;
import eu.jrie.abacus.core.domain.expression.TextValue;
import eu.jrie.abacus.core.domain.expression.Value;
import eu.jrie.abacus.core.domain.formula.ArgumentValueSupplier;
import eu.jrie.abacus.core.domain.formula.FormulaImplementation;
import eu.jrie.abacus.lang.domain.exception.InvalidInputException;
import eu.jrie.abacus.lang.domain.exception.formula.InvalidArgumentNumberException;
import eu.jrie.abacus.lang.domain.exception.formula.InvalidArgumentTypeException;
import eu.jrie.abacus.lang.domain.grammar.ElementMatch;
import eu.jrie.abacus.lang.domain.grammar.RuleMatch;
import eu.jrie.abacus.lang.domain.grammar.Token;
import eu.jrie.abacus.lang.domain.grammar.TokenMatch;

import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.unmodifiableList;
import static java.util.stream.Collectors.toUnmodifiableList;

public class ArgumentParser {

    private final CellReferenceResolver cellReferenceResolver;
    private final TextValueResolver textValueResolver;
    private final NumberValueResolver numberValueResolver;
    private final LogicValueResolver logicValueResolver;

    public ArgumentParser(CellReferenceResolver cellReferenceResolver, TextValueResolver textValueResolver, NumberValueResolver numberValueResolver, LogicValueResolver logicValueResolver) {
        this.cellReferenceResolver = cellReferenceResolver;
        this.textValueResolver = textValueResolver;
        this.numberValueResolver = numberValueResolver;
        this.logicValueResolver = logicValueResolver;
    }

    public List<ArgumentValueSupplier> parseArgs(
            FormulaImplementation impl, List<ElementMatch> givenArguments, FormulaResolver formulaResolver
    ) throws InvalidArgumentTypeException, InvalidArgumentNumberException {
        if (impl.isVararg()) {
            return parseVarargs(impl.getVarargType(), givenArguments, formulaResolver);
        } else {
            return parseListArgs(impl, givenArguments, formulaResolver);
        }
    }

    private List<ArgumentValueSupplier> parseVarargs(
            Class<? extends Expression> type,
            List<ElementMatch> givenArguments,
            FormulaResolver formulaResolver
    ) throws InvalidArgumentTypeException {
        if (areAllArgumentsMatching(givenArguments, type)) {
            return givenArguments.stream()
                    .map(arg -> parseArg(arg, formulaResolver))
                    .collect(toUnmodifiableList());
        } else {
            throw new InvalidArgumentTypeException();
        }
    }

    private List<ArgumentValueSupplier> parseListArgs(
            FormulaImplementation impl, List<ElementMatch> givenArguments, FormulaResolver formulaResolver
    ) throws InvalidArgumentNumberException, InvalidArgumentTypeException {
        final int definedArgsSize = impl.getArgumentTypes().size();
        if (definedArgsSize != givenArguments.size()) {
            throw new InvalidArgumentNumberException();
        }

        final var parsedArguments = new ArrayList<ArgumentValueSupplier>(givenArguments.size());
        for (int i = 0; i < definedArgsSize; i++) {
            var arg = givenArguments.get(i);
            var type = impl.getArgumentTypes().get(i);

            if (!areArgumentsMatching(arg, type)) {
                throw new InvalidArgumentTypeException();
            }

            var parsed = parseArg(arg, formulaResolver);
            parsedArguments.add(parsed);
        }
        return unmodifiableList(parsedArguments);
    }

    private ArgumentValueSupplier parseArg(ElementMatch arg, FormulaResolver formulaResolver) {
        if (arg instanceof RuleMatch ruleMatch) {
            return parseArg(ruleMatch, formulaResolver);
        } else if (arg instanceof TokenMatch tokenMatch){
            return parseArg(tokenMatch);
        } else {
            throw new IllegalStateException();
        }
    }

    private ArgumentValueSupplier parseArg(RuleMatch arg, FormulaResolver formulaResolver) {
        try {
            var formula = formulaResolver.resolve(arg);
            return context -> formula.calculateValue();
        } catch (InvalidInputException e) {
            throw new IllegalStateException(e);
        }
    }

    private ArgumentValueSupplier parseArg(TokenMatch arg) {
        return switch (arg.token()) {
            case CELL_REFERENCE -> cellReferenceResolver.resolve(arg.match());
            case TEXT_VALUE -> textValueResolver.resolve(arg.match());
            case NUMBER_VALUE -> numberValueResolver.resolve(arg.match());
            case LOGIC_TRUE_VALUE, LOGIC_FALSE_VALUE -> logicValueResolver.resolve(arg.match());
            default -> throw new IllegalStateException("Unexpected token: " + arg.token());
        };
    }

    private static boolean areAllArgumentsMatching(List<ElementMatch> givenArguments, Class<? extends Expression> argType) {
        return givenArguments.stream()
                .filter(arg -> arg instanceof TokenMatch)
                .map(arg -> (TokenMatch) arg)
                .allMatch(arg -> areArgumentsMatching(arg.token(), argType));
    }

    private static boolean areArgumentsMatching(ElementMatch match, Class<? extends Expression> argType) {
        if (match instanceof TokenMatch tokenMatch) {
            return areArgumentsMatching(tokenMatch.token(), argType);
        } else {
            return true;
        }
    }

    private static boolean areArgumentsMatching(Token argToken, Class<? extends Expression> argType) {
        return argType == Value.class || switch (argToken) {
            case CELL_REFERENCE -> true;
            case TEXT_VALUE -> argType == TextValue.class;
            case NUMBER_VALUE -> argType == NumberValue.class;
            case LOGIC_TRUE_VALUE, LOGIC_FALSE_VALUE -> argType == LogicValue.class;
            default -> throw new IllegalStateException("Unexpected token: " + argToken);
        };
    }
}
