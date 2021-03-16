package eu.jrie.abacus.lang.domain;

import eu.jrie.abacus.lang.Parser;

import java.util.List;
import java.util.function.Function;
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
