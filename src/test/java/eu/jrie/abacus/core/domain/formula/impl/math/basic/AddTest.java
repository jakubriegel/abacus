package eu.jrie.abacus.core.domain.formula.impl.math.basic;

import eu.jrie.abacus.core.domain.formula.impl.infra.FormulaImplTest;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import java.util.List;
import java.util.stream.Stream;

class AddTest extends FormulaImplTest {
    @TestFactory
    Stream<DynamicTest> shouldCalculateSum() {
        return testNumberToNumber(
                new Add(),
                "should calculate sum of ",
                new TestCase(List.of("1", "2"), "3"),
                new TestCase(List.of("1", "2.34"), "3.34"),
                new TestCase(List.of("1", "-2"), "-1"),
                new TestCase(List.of("1", "0.12"), "1.12"),
                new TestCase(List.of("1", "-2.34"), "-1.34")
        );
    }
}
