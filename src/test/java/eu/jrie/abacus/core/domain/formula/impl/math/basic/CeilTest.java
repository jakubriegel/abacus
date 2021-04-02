package eu.jrie.abacus.core.domain.formula.impl.math.basic;

import eu.jrie.abacus.core.domain.formula.impl.infra.FormulaImplTest;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import java.util.stream.Stream;

class CeilTest extends FormulaImplTest {
    @TestFactory
    Stream<DynamicTest> shouldCalculateCeiling() {
        return testNumberToNumber(
                new Ceil(),
                "should calculate sum of ",
                new TestCase("1", "1"),
                new TestCase("1.01", "2"),
                new TestCase("-1", "-1"),
                new TestCase("-1.01", "-2")
        );
    }
}
