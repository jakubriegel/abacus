package eu.jrie.abacus.core.domain.expression;

import eu.jrie.abacus.core.domain.formula.ArgumentValueSupplier;

import java.util.List;
import java.util.function.Supplier;

public record Formula(
        String functionName,
        List<ArgumentValueSupplier> arguments,
        Supplier<Value> action
) implements Expression {
    @Override
    public Value calculateValue() {
        return action.get();
    }
}
