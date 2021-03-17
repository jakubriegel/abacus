package eu.jrie.abacus.core.domain.expression;

import eu.jrie.abacus.core.domain.argument.Argument;

import java.util.List;
import java.util.function.Supplier;

public record Formula(
        String functionName,
        List<? extends Argument> arguments,
        Supplier<String> action
) implements Expression {
    @Override
    public String calculateValue() {
        return action.get();
    }
}
