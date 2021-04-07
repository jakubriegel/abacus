package eu.jrie.abacus.lang.domain.exception.formula;

public class CouldNotMatchFormulaDefinitionException extends FormulaParsingException {
    public CouldNotMatchFormulaDefinitionException() {
        super("Formula unknown.", null);
    }
}
