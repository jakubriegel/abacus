package eu.jrie.abacus.core.domain.formula.impl.math.stats;

import eu.jrie.abacus.core.domain.formula.impl.infra.FormulaImplTest;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import java.util.List;
import java.util.stream.Stream;

class MeanTest extends FormulaImplTest {
    @TestFactory
    Stream<DynamicTest> shouldCalculateMean() {
        return testNumberToNumber(
                new Mean(),
                "should calculate mean of ",
                new TestCase(List.of("1", "1", "1"), "1"),
                new TestCase(List.of("1", "2", "3"), "2"),
                new TestCase(List.of("-1", "-2", "-3"), "-2"),
                new TestCase(List.of("-2", "2", "-3"), "-1")
        );
    }
}
