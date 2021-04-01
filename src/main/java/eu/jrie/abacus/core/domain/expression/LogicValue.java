package eu.jrie.abacus.core.domain.expression;

import static java.lang.Boolean.parseBoolean;

public final record LogicValue(boolean value) implements Value {

    public LogicValue(String value) {
        this(parseBoolean(value));
    }

    @Override
    public LogicValue calculateValue() {
        return this;
    }

    @Override
    public String getAsString() {
        return String.valueOf(value);
    }
}
