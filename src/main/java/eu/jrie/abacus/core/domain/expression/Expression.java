package eu.jrie.abacus.core.domain.expression;

sealed public interface Expression permits CellReference, Formula, Value {
    Value calculateValue();
}
