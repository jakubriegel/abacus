package eu.jrie.abacus.core.domain.formula.impl.text;

import eu.jrie.abacus.core.domain.expression.NumberValue;
import eu.jrie.abacus.core.domain.expression.TextValue;
import eu.jrie.abacus.core.domain.formula.ArgumentValueSupplier;
import eu.jrie.abacus.core.domain.formula.impl.infra.FormulaImplTest;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SubTest extends FormulaImplTest {
    @TestFactory
    Stream<DynamicTest> shouldGetSubString() {
        var formula = new Sub();
        var cases = List.of(
                new TestCase(List.of("abcdef", "0", "3"), "abc"),
                new TestCase(List.of("abcdef", "1", "4"), "bcd"),
                new TestCase(List.of("", "0", "0"), "")
        );
        return test(formula, "should get sub string of", cases, testCase -> {
            // given
            ArgumentValueSupplier text = c -> new TextValue(testCase.given().get(0));
            ArgumentValueSupplier a = c -> new NumberValue(testCase.given().get(1));
            ArgumentValueSupplier b = c -> new NumberValue(testCase.given().get(2));

            // when
            var result = formula.run(null, List.of(text, a, b));

            // then
            assertTrue(result instanceof TextValue);
            assertEquals(testCase.expected(), result.getAsString());
        });
    }
}
