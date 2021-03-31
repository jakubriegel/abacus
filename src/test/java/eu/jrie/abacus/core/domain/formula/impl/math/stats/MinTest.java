package eu.jrie.abacus.core.domain.formula.impl.math.stats;

import eu.jrie.abacus.core.domain.formula.impl.math.infra.MathFormulaTest;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import java.util.List;
import java.util.stream.Stream;

class MinTest extends MathFormulaTest {
    @TestFactory
    Stream<DynamicTest> shouldCalculateMin() {
        return formulaTest(
                new Min(),
                "should calculate min of ",
                new TestCase(List.of("1", "1", "1"), "1"),
                new TestCase(List.of("1", "2", "3"), "1"),
                new TestCase(List.of("-1", "-3", "-3"), "-3"),
                new TestCase(List.of("-1", "2", "-3"), "-3")
        );
    }
}
