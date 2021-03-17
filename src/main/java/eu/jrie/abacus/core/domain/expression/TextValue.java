package eu.jrie.abacus.core.domain.expression;

public final record TextValue(String value) implements Value<String> {

    @Override
    public String get() {
        return value;
    }

    @Override
    public TextValue calculateValue() {
        return this;
    }
}
