package eu.jrie.abacus.core.domain.formula.impl.math.basic;

import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import java.util.stream.Stream;

class AbsTest extends MathFormulaTest {
    @TestFactory
    Stream<DynamicTest> shouldCalculateAbsoluteValue() {
        return formulaTest(
                new Abs(),
                "should calculate absolute value of ",
                new TestCase("1", "1"),
                new TestCase("1.23", "1.23"),
                new TestCase("-1", "1"),
                new TestCase("-1.23", "1.23")
        );
    }
}
