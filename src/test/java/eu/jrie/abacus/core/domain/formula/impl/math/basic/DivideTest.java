package eu.jrie.abacus.core.domain.formula.impl.math.basic;

import eu.jrie.abacus.core.domain.formula.impl.infra.FormulaImplTest;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import java.util.List;
import java.util.stream.Stream;

public class DivideTest extends FormulaImplTest {
    @TestFactory
    Stream<DynamicTest> shouldCalculateDiv() {
        return testNumberToNumber(
                new Divide(),
                "should calculate division of ",
                new FormulaImplTest.TestCase(List.of("1", "1"), "1"),
                new FormulaImplTest.TestCase(List.of("10", "4"), "2.5"),
                new FormulaImplTest.TestCase(List.of("5", "10"), "0.5"),
                new FormulaImplTest.TestCase(List.of("10", "3.1"), "3.225806451612903"),
                new FormulaImplTest.TestCase(List.of("1", "-1"), "-1"),
                new FormulaImplTest.TestCase(List.of("10", "-4"), "-2.5"),
                new FormulaImplTest.TestCase(List.of("5", "-10"), "-0.5"),
                new FormulaImplTest.TestCase(List.of("10", "-3.1"), "-3.225806451612903")
        );
    }
}
