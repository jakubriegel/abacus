package eu.jrie.abacus.core.domain.expression;

import java.math.BigDecimal;

public final record NumberValue(BigDecimal value) implements Value {

    public NumberValue(String value) {
        this(new BigDecimal(value));
    }

    @Override
    public NumberValue calculateValue() {
        return this;
    }

    @Override
    public String getAsString() {
        return value.stripTrailingZeros()
                .toString();
    }
}
