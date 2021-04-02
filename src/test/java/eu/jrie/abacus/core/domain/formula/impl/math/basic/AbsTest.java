package eu.jrie.abacus.core.domain.formula.impl.math.basic;

import eu.jrie.abacus.core.domain.formula.impl.infra.FormulaImplTest;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import java.util.stream.Stream;

class AbsTest extends FormulaImplTest {
    @TestFactory
    Stream<DynamicTest> shouldCalculateAbsoluteValue() {
        return testNumberToNumber(
                new Abs(),
                "should calculate absolute value of ",
                new TestCase("1", "1"),
                new TestCase("1.23", "1.23"),
                new TestCase("-1", "1"),
                new TestCase("-1.23", "1.23")
        );
    }
}
