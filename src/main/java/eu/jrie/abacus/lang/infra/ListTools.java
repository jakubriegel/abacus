package eu.jrie.abacus.lang.infra;

import java.util.LinkedList;
import java.util.List;

import static java.util.Collections.singletonList;
import static java.util.Collections.unmodifiableList;

public class ListTools {
    public static <E> List<E> appended(final List<E> list, final E element) {
        return appended(list, singletonList(element));
    }

    public static <E> List<E> appended(final List<E> list, final List<E> elements) {
        final var result = new LinkedList<>(list);
        result.addAll(elements);
        return unmodifiableList(result);
    }
}
