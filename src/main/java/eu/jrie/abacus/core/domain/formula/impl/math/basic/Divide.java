package eu.jrie.abacus.core.domain.formula.impl.math.basic;

import eu.jrie.abacus.core.domain.expression.Value;
import eu.jrie.abacus.core.domain.formula.ArgumentValueSupplier;
import eu.jrie.abacus.core.domain.formula.impl.math.MathFormula;
import eu.jrie.abacus.core.domain.workbench.WorkbenchContext;

import java.util.List;

import static java.math.BigDecimal.ZERO;
import static java.math.RoundingMode.HALF_UP;

public class Divide extends MathFormula {
    @Override
    public Value run(WorkbenchContext context, List<ArgumentValueSupplier> args) {
        return reduce(context, args, ZERO, values -> values.reduce((a, b) -> a.divide(b, 15, HALF_UP)));
    }
}

