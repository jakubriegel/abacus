package eu.jrie.abacus.lang.domain;

public final record TextValue(String value) implements Value {
    @Override
    public String calculateValue() {
        return value;
    }
}
