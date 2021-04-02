package eu.jrie.abacus.core.domain.formula.impl.math.stats;

import eu.jrie.abacus.core.domain.formula.impl.infra.FormulaImplTest;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import java.util.List;
import java.util.stream.Stream;

class MaxTest extends FormulaImplTest {
    @TestFactory
    Stream<DynamicTest> shouldCalculateMax() {
        return testNumberToNumber(
                new Max(),
                "should calculate max of ",
                new TestCase(List.of("1", "1", "1"), "1"),
                new TestCase(List.of("1", "2", "3"), "3"),
                new TestCase(List.of("-1", "-3", "-3"), "-1"),
                new TestCase(List.of("-1", "2", "-3"), "2")
        );
    }
}
