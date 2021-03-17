package eu.jrie.abacus.core.domain.formula;

import eu.jrie.abacus.core.domain.expression.Expression;
import eu.jrie.abacus.core.domain.expression.Value;

import java.util.List;

@FunctionalInterface
public interface FormulaAction<T extends Value<?>> {
    T run(List<? extends Expression> args);
}
