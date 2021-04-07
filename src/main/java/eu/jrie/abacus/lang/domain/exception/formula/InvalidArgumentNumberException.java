package eu.jrie.abacus.lang.domain.exception.formula;

public class InvalidArgumentNumberException  extends FormulaParsingException {
    public InvalidArgumentNumberException() {
        super("Provided arguments do not match formula definition.", null);
    }
}
