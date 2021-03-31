package eu.jrie.abacus.core.domain.formula.impl.math.stats;

import eu.jrie.abacus.core.domain.expression.NumberValue;
import eu.jrie.abacus.core.domain.formula.ArgumentValueSupplier;
import eu.jrie.abacus.core.domain.formula.impl.math.MathFormula;
import eu.jrie.abacus.core.domain.workbench.WorkbenchContext;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static java.lang.Math.pow;
import static java.lang.Math.sqrt;
import static java.math.BigDecimal.ZERO;
import static java.util.stream.Collectors.toUnmodifiableList;

public final class Std extends MathFormula {
    @Override
    public NumberValue run(WorkbenchContext context, List<ArgumentValueSupplier> args) {
        return reduce(context, args, ZERO, values -> {
            var data = values.collect(toUnmodifiableList());
            if (data.size() == 0) {
                return Optional.empty();
            } else {
                return std(data);
            }
        });
    }

    private Optional<BigDecimal> std(List<BigDecimal> data) {
        var avg = data.stream()
                .mapToDouble(BigDecimal::doubleValue)
                .average()
                .orElse(.0);

        var avgDev = data.stream()
                .mapToDouble(BigDecimal::doubleValue)
                .map(n -> pow(n - avg, 2))
                .average()
                .orElse(.0);

        var std = sqrt(avgDev);
        return Optional.of(std)
                .map(BigDecimal::new);
    }
}
