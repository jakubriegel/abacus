package eu.jrie.abacus.core.formula;

import eu.jrie.abacus.lang.domain.Argument;

import java.util.List;

@FunctionalInterface
public interface FormulaAction {
    String run(List<? extends Argument> args);
}
