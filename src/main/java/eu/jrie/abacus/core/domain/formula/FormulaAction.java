package eu.jrie.abacus.core.domain.formula;

import eu.jrie.abacus.core.domain.expression.Value;
import eu.jrie.abacus.core.domain.workbench.WorkbenchContext;

import java.util.List;

@FunctionalInterface
public interface FormulaAction<T extends Value> {
    T run(WorkbenchContext context, List<ArgumentValueSupplier> args);
}
