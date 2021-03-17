package eu.jrie.abacus.core.domain.expression;

public final record TextValue(String value) implements Value {
    @Override
    public String calculateValue() {
        return value;
    }
}
