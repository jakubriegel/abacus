package eu.jrie.abacus.core.domain.expression;

sealed public interface Value extends Expression permits LogicValue, NumberValue, TextValue {
    String getAsString();
}
