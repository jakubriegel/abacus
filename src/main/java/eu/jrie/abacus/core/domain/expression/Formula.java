package eu.jrie.abacus.core.domain.expression;

import java.util.List;
import java.util.function.Supplier;

public record Formula(
        String functionName,
        List<? extends Expression> arguments,
        Supplier<Value> action
) implements Expression {
    @Override
    public Value calculateValue() {
        return action.get();
    }
}
