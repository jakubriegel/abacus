package eu.jrie.abacus.core.domain.argument;

import static java.lang.Long.parseLong;

public final record NumberArgument(long value) implements Argument {
    public NumberArgument(String valueAsString) {
        this(parseLong(valueAsString));
    }
}
