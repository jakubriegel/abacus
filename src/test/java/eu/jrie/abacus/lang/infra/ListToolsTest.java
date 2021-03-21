package eu.jrie.abacus.lang.infra;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertIterableEquals;

class ListToolsTest {
    @Test
    void shouldAppendListWithSingleElement() {
        // given
        var list = List.of(1, 2, 3);
        var element = 4;

        // when
        var result = ListTools.appended(list, element);

        // then
        assertIterableEquals(List.of(1, 2, 3, 4), result);
    }

    @Test
    void shouldAppendListWithAnotherList() {
        // given
        var list = List.of(1, 2, 3);
        var another = List.of(4, 5, 6);

        // when
        var result = ListTools.appended(list, another);

        // then
        assertIterableEquals(List.of(1, 2, 3, 4, 5, 6), result);
    }
}
