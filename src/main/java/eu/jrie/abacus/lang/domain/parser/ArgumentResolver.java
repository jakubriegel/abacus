package eu.jrie.abacus.lang.domain.parser;

import eu.jrie.abacus.core.domain.formula.ArgumentValueSupplier;

abstract class ArgumentResolver {
    abstract ArgumentValueSupplier resolve(String text);
}
