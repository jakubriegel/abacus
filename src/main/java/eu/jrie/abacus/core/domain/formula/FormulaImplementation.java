package eu.jrie.abacus.core.domain.formula;

import eu.jrie.abacus.core.domain.expression.Expression;
import eu.jrie.abacus.core.domain.expression.Value;
import eu.jrie.abacus.core.domain.workbench.WorkbenchContext;

import java.util.List;

import static java.util.Collections.emptyList;

public abstract class FormulaImplementation {
    public String getName() {
        return getClass().getSimpleName()
                .toLowerCase();
    }

    public boolean isVararg() {
        return false;
    }

    public Class<? extends Expression> getVarargType() {
        return null;
    }

    public List<Class<? extends Expression>> getArgumentTypes() {
        return emptyList();
    }

    public abstract Value run(WorkbenchContext context, List<ArgumentValueSupplier> args);
}
