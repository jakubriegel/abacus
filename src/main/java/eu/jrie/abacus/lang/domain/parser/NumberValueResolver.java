package eu.jrie.abacus.lang.domain.parser;

import eu.jrie.abacus.core.domain.expression.NumberValue;
import eu.jrie.abacus.core.domain.formula.ArgumentValueSupplier;

class NumberValueResolver extends ArgumentResolver {
    @Override
    ArgumentValueSupplier resolve(String text) {
        return context -> new NumberValue(text);
    }
}
