package eu.jrie.abacus.core.domain.formula.impl.math.basic;

import eu.jrie.abacus.core.domain.formula.impl.math.infra.MathFormulaTest;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import java.util.List;
import java.util.stream.Stream;

class ModTest extends MathFormulaTest {
    @TestFactory
    Stream<DynamicTest> shouldCalculateMod() {
        return formulaTest(
                new Mod(),
                "should calculate remainder of ",
                new TestCase(List.of("1", "1"), "0"),
                new TestCase(List.of("10", "3"), "1"),
                new TestCase(List.of("3", "10"), "3"),
                new TestCase(List.of("10", "3.1"), "0.7"),
                new TestCase(List.of("1", "-1"), "0"),
                new TestCase(List.of("-10", "3"), "-1"),
                new TestCase(List.of("10", "-3"), "1"),
                new TestCase(List.of("10", "-3.1"), "0.7")
        );
    }
}