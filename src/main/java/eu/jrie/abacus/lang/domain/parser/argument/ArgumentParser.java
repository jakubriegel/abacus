package eu.jrie.abacus.lang.domain.parser.argument;

import eu.jrie.abacus.core.domain.expression.Expression;
import eu.jrie.abacus.core.domain.expression.LogicValue;
import eu.jrie.abacus.core.domain.expression.NumberValue;
import eu.jrie.abacus.core.domain.expression.TextValue;
import eu.jrie.abacus.core.domain.formula.ArgumentValueSupplier;
import eu.jrie.abacus.core.domain.formula.FormulaImplementation;
import eu.jrie.abacus.lang.domain.exception.InvalidArgumentNumberException;
import eu.jrie.abacus.lang.domain.exception.InvalidArgumentTypeException;
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

    public List<ArgumentValueSupplier> parseArgs(FormulaImplementation impl, List<TokenMatch> givenArguments) throws InvalidArgumentTypeException, InvalidArgumentNumberException {
        if (impl.isVararg()) {
            return parseVarargs(impl.getVarargType(), givenArguments);
        } else {
            return parseListArgs(impl, givenArguments);
        }
    }

    private List<ArgumentValueSupplier> parseVarargs(Class<? extends Expression> type, List<TokenMatch> givenArguments) throws InvalidArgumentTypeException {
        if (areAllArgumentsMatching(givenArguments, type)) {
            return givenArguments.stream()
                    .map(this::parseArg)
                    .collect(toUnmodifiableList());
        } else {
            throw new InvalidArgumentTypeException();
        }
    }

    private List<ArgumentValueSupplier> parseListArgs(FormulaImplementation impl, List<TokenMatch> givenArguments) throws InvalidArgumentNumberException, InvalidArgumentTypeException {
        final int definedArgsSize = impl.getArgumentTypes().size();
        if (definedArgsSize != givenArguments.size()) {
            throw new InvalidArgumentNumberException();
        }

        final var parsedArguments = new ArrayList<ArgumentValueSupplier>(givenArguments.size());
        for (int i = 0; i < definedArgsSize; i++) {
            var arg = givenArguments.get(i);
            var type = impl.getArgumentTypes().get(i);

            if (!areArgumentsMatching(arg.token(), type)) {
                throw new InvalidArgumentTypeException();
            }

            parsedArguments.add(parseArg(arg));
        }
        return unmodifiableList(parsedArguments);
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

    private static boolean areAllArgumentsMatching(List<TokenMatch> givenArguments, Class<? extends Expression> argType) {
        return givenArguments.stream()
                .allMatch(arg -> areArgumentsMatching(arg.token(), argType));
    }

    private static boolean areArgumentsMatching(Token argToken, Class<? extends Expression> argType) {
        return switch (argToken) {
            case CELL_REFERENCE -> true;
            case TEXT_VALUE -> argType == TextValue.class;
            case NUMBER_VALUE -> argType == NumberValue.class;
            case LOGIC_TRUE_VALUE, LOGIC_FALSE_VALUE -> argType == LogicValue.class;
            default -> throw new IllegalStateException("Unexpected token: " + argToken);
        };
    }
}
