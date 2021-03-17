package eu.jrie.abacus.core.domain.formula;

import eu.jrie.abacus.core.domain.argument.Argument;

import java.util.List;

@FunctionalInterface
public interface FormulaAction {
    String run(List<? extends Argument> args);
}
