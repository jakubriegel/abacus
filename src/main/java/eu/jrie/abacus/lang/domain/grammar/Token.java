package eu.jrie.abacus.lang.domain.grammar;

import java.util.regex.Pattern;

import static java.lang.String.format;

public enum Token implements GrammarElement {

    FUNCTION_NAME("[a-z]+"),
    FUNCTION_ARGS_START("\\("),

    CELL_REFERENCE("[A-Z]+[0-9]+"),
    TEXT_VALUE("'[^']*'"),
    NUMBER_VALUE("-?[0-9]+"),

    FUNCTION_ARGS_SEPARATOR(","),
    FUNCTION_ARGS_STOP("\\)");

    public final Pattern pattern;

    Token(String regex) {
        var wrapped = format("\\s*%s\\s*", regex);
        this.pattern = Pattern.compile(wrapped);
    }
}
