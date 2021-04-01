package eu.jrie.abacus.core.domain.formula.impl.text;

import eu.jrie.abacus.core.domain.expression.Expression;
import eu.jrie.abacus.core.domain.expression.NumberValue;
import eu.jrie.abacus.core.domain.expression.TextValue;
import eu.jrie.abacus.core.domain.expression.Value;
import eu.jrie.abacus.core.domain.formula.ArgumentValueSupplier;
import eu.jrie.abacus.core.domain.formula.FormulaImplementation;
import eu.jrie.abacus.core.domain.workbench.WorkbenchContext;

import java.math.BigDecimal;
import java.util.List;

import static java.util.Collections.singletonList;

public class Length extends FormulaImplementation {

    @Override
    public List<Class<? extends Expression>> getArgumentTypes() {
        return singletonList(TextValue.class);
    }

    @Override
    public Value run(WorkbenchContext context, List<ArgumentValueSupplier> args) {
        var length = args.get(0)
                .get(context)
                .getAsString()
                .length();
        return new NumberValue(new BigDecimal(length));
    }
}
