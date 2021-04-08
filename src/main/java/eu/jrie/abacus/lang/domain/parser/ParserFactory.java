package eu.jrie.abacus.lang.domain.parser;

import eu.jrie.abacus.core.domain.workbench.WorkbenchContext;
import eu.jrie.abacus.lang.domain.parser.argument.ArgumentParser;
import eu.jrie.abacus.lang.domain.parser.argument.CellReferenceResolver;
import eu.jrie.abacus.lang.domain.parser.argument.LogicValueResolver;
import eu.jrie.abacus.lang.domain.parser.argument.NumberValueResolver;
import eu.jrie.abacus.lang.domain.parser.argument.TextValueResolver;

public abstract class ParserFactory {

    private ParserFactory() {}

    public static Parser buildParser(WorkbenchContext context) {
        var argumentsParser = buildArgumentParser();
        var grammarParser = new GrammarParser();
        var formulaParser = new FormulaParser(context, grammarParser, argumentsParser);
        var valueParser = new ValueParser();
        return new Parser(valueParser, formulaParser);
    }

    private static ArgumentParser buildArgumentParser() {
        var cellReferenceResolver = new CellReferenceResolver();
        var textValueResolver = new TextValueResolver();
        var numberValueResolver = new NumberValueResolver();
        var logicValueResolver = new LogicValueResolver();
        return new ArgumentParser(cellReferenceResolver, textValueResolver, numberValueResolver, logicValueResolver);
    }
}
