package eu.jrie.abacus.lang.domain.parser;

import eu.jrie.abacus.core.domain.expression.NumberValue;
import eu.jrie.abacus.core.domain.expression.TextValue;
import eu.jrie.abacus.core.domain.expression.Value;

import static eu.jrie.abacus.lang.domain.grammar.Token.NUMBER_VALUE;
import static java.lang.Integer.parseInt;

class ValueParser {

    Value parse(String text) {
        if (isNumberValue(text)) {
            return new NumberValue(parseInt(text));
        } else /* text value */ {
            return new TextValue(text);
        }
    }

    private static boolean isNumberValue(String text) {
        return NUMBER_VALUE.pattern.matcher(text)
                .matches();
    }
}
