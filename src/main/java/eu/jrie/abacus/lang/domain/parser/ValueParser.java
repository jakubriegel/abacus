package eu.jrie.abacus.lang.domain.parser;

import eu.jrie.abacus.core.domain.expression.NumberValue;
import eu.jrie.abacus.core.domain.expression.TextValue;
import eu.jrie.abacus.core.domain.expression.Value;

import java.util.regex.Pattern;

import static java.lang.Integer.parseInt;

class ValueParser {

    private static final Pattern numberPattern = Pattern.compile("-?[0-9]+");

    Value parse(String text) {
        if (isNumberValue(text)) {
            return new NumberValue(parseInt(text));
        } else /* text value */ {
            return new TextValue(text);
        }
    }

    private static boolean isNumberValue(String text) {
        return numberPattern.matcher(text)
                .matches();
    }
}
