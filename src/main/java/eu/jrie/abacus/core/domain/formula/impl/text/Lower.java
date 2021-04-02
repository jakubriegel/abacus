package eu.jrie.abacus.core.domain.formula.impl.text;

import eu.jrie.abacus.core.domain.expression.Expression;
import eu.jrie.abacus.core.domain.expression.TextValue;
import eu.jrie.abacus.core.domain.expression.Value;
import eu.jrie.abacus.core.domain.formula.ArgumentValueSupplier;
import eu.jrie.abacus.core.domain.formula.FormulaImplementation;
import eu.jrie.abacus.core.domain.workbench.WorkbenchContext;

import java.util.List;

import static java.util.Collections.singletonList;

public class Lower extends FormulaImplementation {

    @Override
    public List<Class<? extends Expression>> getArgumentTypes() {
        return singletonList(TextValue.class);
    }

    @Override
    public Value run(WorkbenchContext context, List<ArgumentValueSupplier> args) {
        var upper = args.get(0)
                .get(context)
                .getAsString()
                .toLowerCase();
        return new TextValue(upper);
    }
}
