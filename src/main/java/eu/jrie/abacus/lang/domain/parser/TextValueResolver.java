package eu.jrie.abacus.lang.domain.parser;

import eu.jrie.abacus.core.domain.expression.TextValue;
import eu.jrie.abacus.core.domain.formula.ArgumentValueSupplier;

class TextValueResolver extends ArgumentResolver {
    @Override
    ArgumentValueSupplier resolve(String text) {
        final var value = text.substring(1, text.length()-1);
        return context -> new TextValue(value);
    }
}
