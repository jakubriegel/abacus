package eu.jrie.abacus.lang.domain.parser;

import eu.jrie.abacus.core.domain.expression.NumberValue;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class NumberValueResolverTest {

    private static final ArgumentResolver resolver = new NumberValueResolver();

    @Test
    void shouldResolveNumberValue() {
        // when
        var result = resolver.resolve("1");

        // then
        assertEquals(new NumberValue(1), result.get(null));
    }
}
