package eu.jrie.abacus.core.domain.expression;

public final record NumberValue(long value) implements Value {
    @Override
    public String calculateValue() {
        return String.valueOf(value);
    }
}
