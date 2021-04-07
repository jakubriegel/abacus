package eu.jrie.abacus.lang.domain.exception.formula;

import eu.jrie.abacus.lang.domain.exception.InvalidInputException;

public abstract class FormulaParsingException extends InvalidInputException {
    FormulaParsingException(String message, Throwable cause) {
        super(message, cause);
    }
}
