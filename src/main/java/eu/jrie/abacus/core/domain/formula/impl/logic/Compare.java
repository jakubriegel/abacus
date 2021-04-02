package eu.jrie.abacus.core.domain.formula.impl.logic;

import eu.jrie.abacus.core.domain.expression.Expression;
import eu.jrie.abacus.core.domain.expression.LogicValue;
import eu.jrie.abacus.core.domain.expression.Value;
import eu.jrie.abacus.core.domain.formula.ArgumentValueSupplier;
import eu.jrie.abacus.core.domain.formula.FormulaImplementation;
import eu.jrie.abacus.core.domain.workbench.WorkbenchContext;

import java.util.List;

import static java.util.stream.Collectors.toUnmodifiableSet;

public class Compare extends FormulaImplementation {

    @Override
    public boolean isVararg() {
        return true;
    }

    @Override
    public Class<? extends Expression> getVarargType() {
        return Value.class;
    }

    @Override
    public Value run(WorkbenchContext context, List<ArgumentValueSupplier> args) {
        if (notAllSameType(context, args)) {
            return new LogicValue(false);
        } else {
            return new LogicValue(allSameValue(context, args));
        }
    }

    private static boolean notAllSameType(WorkbenchContext context, List<ArgumentValueSupplier> args) {
        return args.stream()
                .map(arg -> arg.get(context))
                .map(Value::getClass)
                .collect(toUnmodifiableSet())
                .size() != 1;
    }

    private static boolean allSameValue(WorkbenchContext context, List<ArgumentValueSupplier> args) {
        return args.stream()
                .map(arg -> arg.get(context))
                .map(Value::getAsString)
                .collect(toUnmodifiableSet())
                .size() == 1;
    }
}
