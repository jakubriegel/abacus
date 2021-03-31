package eu.jrie.abacus.core.domain.formula.impl.math.basic;

import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import java.util.List;
import java.util.stream.Stream;

public class SubtractTest extends MathFormulaTest {
    @TestFactory
    Stream<DynamicTest> shouldCalculateDifference() {
        return formulaTest(
                new Subtract(),
                "should calculate difference of ",
                new TestCase(List.of("2", "1"), "1"),
                new TestCase(List.of("1", "-2"), "3"),
                new TestCase(List.of("1", "2"), "-1"),
                new TestCase(List.of("2.34", "1"), "1.34"),
                new TestCase(List.of("1", "0.12"), "0.88"),
                new TestCase(List.of("1", "-0.12"), "1.12"),
                new TestCase(List.of("1", "2.34"), "-1.34")
        );
    }
}
