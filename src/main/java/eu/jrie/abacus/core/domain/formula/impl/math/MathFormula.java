package eu.jrie.abacus.core.domain.formula.impl.math;

import eu.jrie.abacus.core.domain.expression.Expression;
import eu.jrie.abacus.core.domain.expression.NumberValue;
import eu.jrie.abacus.core.domain.formula.ArgumentValueSupplier;
import eu.jrie.abacus.core.domain.formula.FormulaImplementation;
import eu.jrie.abacus.core.domain.workbench.WorkbenchContext;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

public abstract class MathFormula extends FormulaImplementation {

    @Override
    public boolean isVararg() {
        return true;
    }

    @Override
    public Class<? extends Expression> getVarargType() {
        return NumberValue.class;
    }

    protected static NumberValue reduce(
            WorkbenchContext context,
            List<ArgumentValueSupplier> args,
            BigDecimal defaultValue,
            Function<Stream<BigDecimal>, Optional<BigDecimal>> action
    ) {
        var argValues = args.stream()
                .map(arg -> arg.get(context))
                .map(arg -> (NumberValue) arg)
                .map(NumberValue::value);
        return action.apply(argValues)
                .or(() -> Optional.of(defaultValue))
                .map(NumberValue::new)
                .get();
    }
}
