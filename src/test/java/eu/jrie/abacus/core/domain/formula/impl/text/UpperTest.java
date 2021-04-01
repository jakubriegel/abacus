package eu.jrie.abacus.core.domain.formula.impl.text;

import eu.jrie.abacus.core.domain.formula.impl.infra.FormulaImplTest;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import java.util.stream.Stream;

class UpperTest extends FormulaImplTest {
    @TestFactory
    Stream<DynamicTest> shouldMapStringToUpper() {
        return testTextToText(
                new Upper(),
                "should map string to upper -",
                new TestCase("abc", "ABC"),
                new TestCase("ABC", "ABC"),
                new TestCase("123", "123"),
                new TestCase("", "")
        );
    }
}
