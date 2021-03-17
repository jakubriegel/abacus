package eu.jrie.abacus.lang.domain.grammar;

import java.util.regex.Pattern;

public enum Token implements GrammarElement {
    FUNCTION_NAME("[a-z]+"),
    FUNCTION_ARGS_START("\\("),
    FUNCTION_ARG("[0-9a-zA-Z]+"),

    CELL_REFERENCE("[A-Z]+[0-9]+"),
    TEXT_VALUE("'[^']*'"),
    NUMBER_VALUE("[0-9]+"),

    FUNCTION_ARGS_SEPARATOR(","),
    FUNCTION_ARGS_STOP("\\)");

    public final Pattern pattern;

    Token(String pattern) {
        this.pattern = Pattern.compile(pattern);
    }
}
