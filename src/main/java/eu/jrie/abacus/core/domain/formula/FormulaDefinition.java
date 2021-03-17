package eu.jrie.abacus.core.domain.formula;

import eu.jrie.abacus.core.domain.argument.Argument;

import java.util.List;

public final record FormulaDefinition(
        String name,
        List<Class<? extends Argument>> argumentTypes,
        FormulaAction action
) {}
