package eu.jrie.abacus.core.domain.formula.impl.math.basic;

import eu.jrie.abacus.core.domain.expression.NumberValue;
import eu.jrie.abacus.core.domain.formula.ArgumentValueSupplier;
import eu.jrie.abacus.core.domain.formula.FormulaImplementation;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toUnmodifiableList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

class AddTest extends MathFormulaTest {

    private final FormulaImplementation formula = new Add();

    @TestFactory
    Stream<DynamicTest> shouldCalculateAbsoluteValue() {
        return Stream.of(
                new TestCase(List.of("1", "2"), "3"),
                new TestCase(List.of("1", "2.34"), "3.34"),
                new TestCase(List.of("1", "-2"), "-1"),
                new TestCase(List.of("1", "0.12"), "1.12"),
                new TestCase(List.of("1", "-2.34"), "-1.34")
        ).map(testCase -> dynamicTest("should calculate sum of " + testCase.given(), () -> {
            // given
            var args = testCase.given().stream()
                    .map(BigDecimal::new)
                    .map(NumberValue::new)
                    .map(arg -> (ArgumentValueSupplier) context -> arg)
                    .collect(toUnmodifiableList());

            // when
            var result = formula.run(null, args);

            // then
            assertEquals(new NumberValue(testCase.expected()), result);
        }));
    }
}
