package eu.jrie.abacus.core.domain.formula.impl.math.basic;

import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import java.util.stream.Stream;

class FloorTest extends MathFormulaTest {
    @TestFactory
    Stream<DynamicTest> shouldCalculateFloor() {
        return formulaTest(
                new Floor(),
                "should calculate floor of ",
                new TestCase("1", "1"),
                new TestCase("1.01", "1"),
                new TestCase("-1", "-1"),
                new TestCase("-1.01", "-1")
        );
    }
}
