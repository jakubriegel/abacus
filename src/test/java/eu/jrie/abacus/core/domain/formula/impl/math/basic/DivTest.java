package eu.jrie.abacus.core.domain.formula.impl.math.basic;

import eu.jrie.abacus.core.domain.formula.impl.infra.FormulaImplTest;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import java.util.List;
import java.util.stream.Stream;

class DivTest extends FormulaImplTest {
    @TestFactory
    Stream<DynamicTest> shouldCalculateDiv() {
        return testNumberToNumber(
                new Div(),
                "should calculate div of ",
                new TestCase(List.of("1", "1"), "1"),
                new TestCase(List.of("10", "3"), "3"),
                new TestCase(List.of("3", "10"), "0"),
                new TestCase(List.of("10", "3.1"), "3"),
                new TestCase(List.of("1", "-1"), "-1"),
                new TestCase(List.of("-10", "3"), "-3"),
                new TestCase(List.of("10", "-3"), "-3"),
                new TestCase(List.of("10", "-3.1"), "-3")
        );
    }
}
