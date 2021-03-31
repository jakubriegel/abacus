package eu.jrie.abacus.lang.domain.parser;

import eu.jrie.abacus.core.domain.workbench.WorkbenchContext;
import eu.jrie.abacus.lang.domain.parser.argument.ArgumentParser;
import eu.jrie.abacus.lang.domain.parser.argument.CellReferenceResolver;
import eu.jrie.abacus.lang.domain.parser.argument.NumberValueResolver;
import eu.jrie.abacus.lang.domain.parser.argument.TextValueResolver;

public abstract class ParserFactory {

    private ParserFactory() {}

    public static Parser buildParser(WorkbenchContext context) {
        var argumentsParser = buildArgumentParser();
        var formulaParser = new FormulaParser(context, argumentsParser);
        var valueParser = new ValueParser();
        return new Parser(valueParser, formulaParser);
    }

    private static ArgumentParser buildArgumentParser() {
        var cellReferenceResolver = new CellReferenceResolver();
        var textValueResolver = new TextValueResolver();
        var numberValueResolver = new NumberValueResolver();
        return new ArgumentParser(cellReferenceResolver, textValueResolver, numberValueResolver);
    }
}
