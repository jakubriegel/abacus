package eu.jrie.abacus.core.domain.formula.impl.logic;

import eu.jrie.abacus.core.domain.expression.Expression;
import eu.jrie.abacus.core.domain.expression.LogicValue;
import eu.jrie.abacus.core.domain.expression.Value;
import eu.jrie.abacus.core.domain.formula.ArgumentValueSupplier;
import eu.jrie.abacus.core.domain.formula.FormulaImplementation;
import eu.jrie.abacus.core.domain.workbench.WorkbenchContext;

import java.util.List;

public class If extends FormulaImplementation {

    @Override
    public List<Class<? extends Expression>> getArgumentTypes() {
        return List.of(LogicValue.class, Value.class, Value.class);
    }

    @Override
    public Value run(WorkbenchContext context, List<ArgumentValueSupplier> args) {
        var condition = (LogicValue) args.get(0).get(context);
        if (condition.value()) {
            return args.get(1).get(context);
        } else {
            return args.get(2).get(context);
        }
    }
}
