package eu.jrie.abacus.lang.domain.parser;

import eu.jrie.abacus.core.domain.expression.LogicValue;
import eu.jrie.abacus.core.domain.expression.NumberValue;
import eu.jrie.abacus.core.domain.expression.TextValue;
import eu.jrie.abacus.core.domain.expression.Value;

import static eu.jrie.abacus.lang.domain.grammar.Token.LOGIC_FALSE_VALUE;
import static eu.jrie.abacus.lang.domain.grammar.Token.LOGIC_TRUE_VALUE;
import static eu.jrie.abacus.lang.domain.grammar.Token.NUMBER_VALUE;

class ValueParser {

    Value parse(String text) {
        if (isNumberValue(text)) {
            return new NumberValue(text);
        } else if (isLogicValue(text)) {
            return new LogicValue(text);
        } else /* text value */ {
            return new TextValue(text);
        }
    }

    private static boolean isNumberValue(String text) {
        return NUMBER_VALUE.pattern.matcher(text)
                .matches();
    }

    private static boolean isLogicValue(String text) {
        return LOGIC_TRUE_VALUE.pattern.matcher(text).matches()
                || LOGIC_FALSE_VALUE.pattern.matcher(text).matches();
    }
}
