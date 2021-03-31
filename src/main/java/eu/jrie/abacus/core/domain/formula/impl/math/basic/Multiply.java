package eu.jrie.abacus.core.domain.formula.impl.math.basic;

import eu.jrie.abacus.core.domain.expression.Value;
import eu.jrie.abacus.core.domain.formula.ArgumentValueSupplier;
import eu.jrie.abacus.core.domain.formula.impl.math.MathFormula;
import eu.jrie.abacus.core.domain.workbench.WorkbenchContext;

import java.math.BigDecimal;
import java.util.List;

import static java.math.BigDecimal.ZERO;

public class Multiply extends MathFormula {
    @Override
    public String getName() {
        return "multiply";
    }

    @Override
    public Value run(WorkbenchContext context, List<ArgumentValueSupplier> args) {
        return reduce(context, args, ZERO, values -> values.reduce(BigDecimal::multiply));
    }
}
