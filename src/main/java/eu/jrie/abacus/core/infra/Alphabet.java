package eu.jrie.abacus.core.infra;

import java.util.List;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toUnmodifiableList;

public class Alphabet {

    private static final int ZERO_INDEX = 48;
    private static final int PRE_A_INDEX = 64;

    private static final List<Integer> alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".chars()
            .boxed()
            .collect(toUnmodifiableList());

    public String getLiteral(int number) {
        final var value = Integer.toString(number, 26);
        return value.toUpperCase()
                .chars()
                .map(digit -> {
                    if (digit >= '0' && digit <= '9') {
                        return alphabet.get(digit - ZERO_INDEX);
                    } else {
                        return alphabet.get(digit - PRE_A_INDEX + 9);
                    }
                })
                .mapToObj(l -> (char) l)
                .map(String::valueOf)
                .collect(joining());
    }
}
