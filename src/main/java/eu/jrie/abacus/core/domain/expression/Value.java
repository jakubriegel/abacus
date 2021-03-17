package eu.jrie.abacus.core.domain.expression;

sealed public interface Value extends Expression permits NumberValue, TextValue {
    String getAsString();
}
