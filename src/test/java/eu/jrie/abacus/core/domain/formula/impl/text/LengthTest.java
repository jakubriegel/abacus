package eu.jrie.abacus.core.domain.formula.impl.text;

import eu.jrie.abacus.core.domain.formula.impl.infra.FormulaImplTest;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import java.util.stream.Stream;

class LengthTest extends FormulaImplTest {
    @TestFactory
    Stream<DynamicTest> shouldCalculateStringLength() {
        return testTextToNumber(
                new Length(),
                "should calculate length of ",
                new TestCase("abc", "3"),
                new TestCase("", "0")
        );
    }
}
