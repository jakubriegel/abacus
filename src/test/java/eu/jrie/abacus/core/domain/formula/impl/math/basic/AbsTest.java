package eu.jrie.abacus.core.domain.formula.impl.math.basic;

import eu.jrie.abacus.core.domain.expression.NumberValue;
import eu.jrie.abacus.core.domain.formula.FormulaImplementation;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import java.math.BigDecimal;
import java.util.stream.Stream;

import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

class AbsTest extends MathFormulaTest {

    private final FormulaImplementation formula = new Abs();

    @TestFactory
    Stream<DynamicTest> shouldCalculateAbsoluteValue() {
        return Stream.of(
                new TestCase("1", "1"),
                new TestCase("1.23", "1.23"),
                new TestCase("-1", "1"),
                new TestCase("-1.23", "1.23")
        ).map(testCase -> dynamicTest("should calculate absolute value of " + testCase.singleGiven(), () -> {
            // given
            var arg = new BigDecimal(testCase.singleGiven());

            // when
            var result = formula.run(null, singletonList(c -> new NumberValue(arg)));

            // then
            assertEquals(new NumberValue(testCase.expected()), result);
        }));
    }
}
