package eu.jrie.abacus.core.formula;

import eu.jrie.abacus.lang.Parser;
import eu.jrie.abacus.lang.domain.Argument;

import java.util.List;

public final record FormulaDefinition(
        String name,
        List<Class<? extends Argument>> argumentTypes,
        FormulaAction action
) {}
