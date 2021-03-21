package eu.jrie.abacus.core.domain.formula;

import eu.jrie.abacus.core.domain.expression.Value;
import eu.jrie.abacus.core.domain.workbench.WorkbenchContext;

@FunctionalInterface
public  interface ArgumentValueSupplier {
    Value get(WorkbenchContext context);
}
