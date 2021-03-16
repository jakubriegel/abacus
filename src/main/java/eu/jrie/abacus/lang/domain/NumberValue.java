package eu.jrie.abacus.lang.domain;

public final record NumberValue(long value) implements Value {
    @Override
    public String calculateValue() {
        return String.valueOf(value);
    }
}
