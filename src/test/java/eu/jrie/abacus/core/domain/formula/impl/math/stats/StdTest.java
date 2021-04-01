package eu.jrie.abacus.core.domain.formula.impl.math.stats;

import eu.jrie.abacus.core.domain.formula.impl.infra.FormulaImplTest;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import java.util.List;
import java.util.stream.Stream;

class StdTest extends FormulaImplTest {
    @TestFactory
    Stream<DynamicTest> shouldCalculateStd() {
        return testNumberToNumber(
                new Std(),
                "should calculate std of ",
                new TestCase(List.of("1", "1", "1"), "0"),
                new TestCase(List.of("0", "4"), "2"),
                new TestCase(List.of("0", "-4"), "2")
        );
    }
}
