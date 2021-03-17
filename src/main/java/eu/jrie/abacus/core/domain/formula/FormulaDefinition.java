package eu.jrie.abacus.core.domain.formula;

import eu.jrie.abacus.core.domain.expression.Expression;
import eu.jrie.abacus.core.domain.expression.Value;

import java.util.List;

public final record FormulaDefinition(
        String name,
        List<Class<? extends Expression>> argumentTypes,
        FormulaAction<? extends Value> action
) {}
