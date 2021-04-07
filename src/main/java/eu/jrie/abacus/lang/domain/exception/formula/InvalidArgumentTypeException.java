package eu.jrie.abacus.lang.domain.exception.formula;

public class InvalidArgumentTypeException extends FormulaParsingException {
    public InvalidArgumentTypeException() {
        super("Provided argument has invalid type.", null);
    }
}
