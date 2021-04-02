package eu.jrie.abacus.lang.domain.parser.argument;

import eu.jrie.abacus.core.domain.expression.NumberValue;
import org.junit.jupiter.api.Test;

import static java.math.BigDecimal.ONE;
import static org.junit.jupiter.api.Assertions.assertEquals;

class NumberValueResolverTest {

    private static final ArgumentResolver resolver = new NumberValueResolver();

    @Test
    void shouldResolveNumberValue() {
        // when
        var result = resolver.resolve("1");

        // then
        assertEquals(new NumberValue(ONE), result.get(null));
    }
}
