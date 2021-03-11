package eu.jrie.abacus.core.infra;

import java.util.Iterator;
import java.util.List;

import static java.util.stream.Collectors.toUnmodifiableList;

public class Alphabet implements Iterator<String> {

    private final List<String> alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".chars()
            .mapToObj(l -> (char) l)
            .map(String::valueOf)
            .collect(toUnmodifiableList());

    private Iterator<String> currentIterator = alphabet.iterator();

    @Override
    public boolean hasNext() {
        return true;
    }

    @Override
    public String next() {
        if (!currentIterator.hasNext()) {
            currentIterator = alphabet.iterator();
        }
        return currentIterator.next();
    }
}
