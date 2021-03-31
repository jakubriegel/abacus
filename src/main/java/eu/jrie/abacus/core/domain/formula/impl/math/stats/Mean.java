package eu.jrie.abacus.core.domain.formula.impl.math.stats;

import eu.jrie.abacus.core.domain.expression.NumberValue;
import eu.jrie.abacus.core.domain.formula.ArgumentValueSupplier;
import eu.jrie.abacus.core.domain.formula.impl.math.MathFormula;
import eu.jrie.abacus.core.domain.workbench.WorkbenchContext;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static java.math.BigDecimal.ZERO;

public final class Mean extends MathFormula {
    @Override
    public NumberValue run(WorkbenchContext context, List<ArgumentValueSupplier> args) {
        return reduce(context, args, ZERO, values -> {
            var avg = values.mapToDouble(BigDecimal::doubleValue)
                    .average()
                    .orElse(.0);
            return Optional.of(avg)
                    .map(BigDecimal::new);
        });
    }
}
