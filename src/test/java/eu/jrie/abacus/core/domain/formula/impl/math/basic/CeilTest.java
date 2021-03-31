package eu.jrie.abacus.core.domain.formula.impl.math.basic;

import eu.jrie.abacus.core.domain.formula.impl.math.infra.MathFormulaTest;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import java.util.stream.Stream;

class CeilTest extends MathFormulaTest {
    @TestFactory
    Stream<DynamicTest> shouldCalculateCeiling() {
        return formulaTest(
                new Ceil(),
                "should calculate sum of ",
                new TestCase("1", "1"),
                new TestCase("1.01", "2"),
                new TestCase("-1", "-1"),
                new TestCase("-1.01", "-2")
        );
    }
}
