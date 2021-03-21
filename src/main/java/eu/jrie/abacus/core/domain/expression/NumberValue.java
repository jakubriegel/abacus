package eu.jrie.abacus.core.domain.expression;

public final record NumberValue(long value) implements Value {

    public NumberValue(String value) {
        this(Long.parseLong(value));
    }

    @Override
    public NumberValue calculateValue() {
        return this;
    }

    @Override
    public String getAsString() {
        return String.valueOf(value);
    }
}
