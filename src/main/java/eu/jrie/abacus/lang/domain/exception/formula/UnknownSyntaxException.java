package eu.jrie.abacus.lang.domain.exception.formula;

public class UnknownSyntaxException extends FormulaParsingException {
    public UnknownSyntaxException() {
        super("Invalid syntax.", null);
    }
}
