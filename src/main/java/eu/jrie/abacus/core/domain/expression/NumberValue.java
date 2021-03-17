package eu.jrie.abacus.core.domain.expression;

public final record NumberValue(long value) implements Value<Long> {

    public NumberValue(String value) {
        this(Long.parseLong(value));
    }

    @Override
    public Long get() {
        return value;
    }

    @Override
    public NumberValue calculateValue() {
        return this;
    }
}
