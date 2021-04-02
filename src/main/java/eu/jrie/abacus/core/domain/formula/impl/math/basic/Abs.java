package eu.jrie.abacus.core.domain.formula.impl.math.basic;

import eu.jrie.abacus.core.domain.expression.Expression;
import eu.jrie.abacus.core.domain.expression.NumberValue;
import eu.jrie.abacus.core.domain.expression.Value;
import eu.jrie.abacus.core.domain.formula.ArgumentValueSupplier;
import eu.jrie.abacus.core.domain.formula.FormulaImplementation;
import eu.jrie.abacus.core.domain.workbench.WorkbenchContext;

import java.util.List;

import static java.util.Collections.singletonList;

public final class Abs extends FormulaImplementation {
    @Override
    public List<Class<? extends Expression>> getArgumentTypes() {
        return singletonList(NumberValue.class);
    }

    @Override
    public Value run(WorkbenchContext context, List<ArgumentValueSupplier> args) {
        var n = (NumberValue) args.get(0).get(context);
        var abs = n.value().abs();
        return new NumberValue(abs);
    }
}
