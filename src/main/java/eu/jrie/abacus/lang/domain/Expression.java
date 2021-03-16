package eu.jrie.abacus.lang.domain;

sealed public interface Expression permits Formula, Value {
    String calculateValue();
}
