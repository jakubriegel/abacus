package eu.jrie.abacus.lang.domain.parser;

import eu.jrie.abacus.core.domain.workbench.WorkbenchContext;

public abstract class ParserFactory {

    private ParserFactory() {}

    public static Parser buildParser(WorkbenchContext context) {
        var cellReferenceResolver = new CellReferenceResolver();
        var textValueResolver = new TextValueResolver();
        var numberValueResolver = new NumberValueResolver();
        var formulaParser = new FormulaParser(context, cellReferenceResolver, textValueResolver, numberValueResolver);
        var valueParser = new ValueParser();
        return new Parser(valueParser, formulaParser);
    }
}
