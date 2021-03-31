package eu.jrie.abacus.core.domain.formula.impl.math.basic;

import eu.jrie.abacus.core.domain.expression.Expression;
import eu.jrie.abacus.core.domain.expression.NumberValue;
import eu.jrie.abacus.core.domain.expression.Value;
import eu.jrie.abacus.core.domain.formula.ArgumentValueSupplier;
import eu.jrie.abacus.core.domain.formula.FormulaImplementation;
import eu.jrie.abacus.core.domain.workbench.WorkbenchContext;

import java.util.List;

public class Mod extends FormulaImplementation {
    @Override
    public List<Class<? extends Expression>> getArgumentTypes() {
        return List.of(NumberValue.class, NumberValue.class);
    }

    @Override
    public Value run(WorkbenchContext context, List<ArgumentValueSupplier> args) {
        var a = (NumberValue) args.get(0).get(context);
        var b = (NumberValue) args.get(1).get(context);
        var mod = a.value().remainder(b.value());
        return new NumberValue(mod);
    }
}
