package eu.jrie.abacus.lang.domain.parser;

import eu.jrie.abacus.core.domain.expression.TextValue;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TextValueResolverTest {

    private static final ArgumentResolver resolver = new TextValueResolver();

    @Test
    void shouldResolveNumberValue() {
        // when
        var result = resolver.resolve("'abc'");

        // then
        assertEquals(new TextValue("abc"), result.get(null));
    }
}
