package eu.jrie.abacus.lang.domain;

import static java.lang.Long.parseLong;

public final record NumberArgument(long value) implements Argument {
    public NumberArgument(String valueAsString) {
        this(parseLong(valueAsString));
    }
}
