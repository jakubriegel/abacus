package eu.jrie.abacus.lang.domain;

sealed public interface Value extends Expression permits NumberValue, TextValue {
}
