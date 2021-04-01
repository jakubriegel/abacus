package eu.jrie.abacus.lang.domain.parser.argument;

import eu.jrie.abacus.core.domain.expression.LogicValue;
import eu.jrie.abacus.core.domain.formula.ArgumentValueSupplier;

public class LogicValueResolver extends ArgumentResolver {
    @Override
    ArgumentValueSupplier resolve(String text) {
        return context -> new LogicValue(text);
    }
}
