package eu.jrie.abacus.core.domain.expression;

public final record TextValue(String value) implements Value {

    @Override
    public TextValue calculateValue() {
        return this;
    }

    @Override
    public String getAsString() {
        return value;
    }
}
