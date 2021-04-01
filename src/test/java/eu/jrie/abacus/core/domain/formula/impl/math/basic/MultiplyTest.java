package eu.jrie.abacus.core.domain.formula.impl.math.basic;

import eu.jrie.abacus.core.domain.formula.impl.infra.FormulaImplTest;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import java.util.List;
import java.util.stream.Stream;

public class MultiplyTest extends FormulaImplTest {
    @TestFactory
    Stream<DynamicTest> shouldCalculateProduct() {
        return testNumberToNumber(
                new Multiply(),
                "should calculate product of ",
                new TestCase(List.of("1", "1"), "1"),
                new TestCase(List.of("2", "3"), "6"),
                new TestCase(List.of("2", "3.33"), "6.66"),
                new TestCase(List.of("1", "-1"), "-1"),
                new TestCase(List.of("2", "-3"), "-6"),
                new TestCase(List.of("2", "-3.33"), "-6.66"),
                new TestCase(List.of("-1", "-1"), "1"),
                new TestCase(List.of("-2", "-3"), "6"),
                new TestCase(List.of("-2", "-3.33"), "6.66")
        );
    }
}
