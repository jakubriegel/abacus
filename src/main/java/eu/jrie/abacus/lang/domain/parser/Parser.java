package eu.jrie.abacus.lang.domain.parser;

import eu.jrie.abacus.core.domain.expression.Expression;
import eu.jrie.abacus.lang.domain.exception.InvalidInputException;

public class Parser {

    private static final String FORMULA_PREFIX = "=";

    private final FormulaParser formulaParser;
    private final ValueParser valueParser;

    public Parser(ValueParser valueParser, FormulaParser formulaParser) {
        this.valueParser = valueParser;
        this.formulaParser = formulaParser;
    }

    public Expression parse(String text) throws InvalidInputException {
        return parseTrimmed(text.trim());
    }

    private Expression parseTrimmed(String text) throws InvalidInputException {
        if (hasFormulaPrefix(text)) {
            return formulaParser.parse(text.substring(1));
        } else {
            return valueParser.parse(text);
        }
    }

    private static boolean hasFormulaPrefix(String text) {
        return text.startsWith(FORMULA_PREFIX);
    }
}
