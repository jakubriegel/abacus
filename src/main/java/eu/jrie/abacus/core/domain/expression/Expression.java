package eu.jrie.abacus.core.domain.expression;

sealed public interface Expression permits Formula, Value {
    String calculateValue();
}
