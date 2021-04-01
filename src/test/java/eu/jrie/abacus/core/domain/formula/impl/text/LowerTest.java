package eu.jrie.abacus.core.domain.formula.impl.text;

import eu.jrie.abacus.core.domain.formula.impl.infra.FormulaImplTest;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import java.util.stream.Stream;

class LowerTest extends FormulaImplTest {
    @TestFactory
    Stream<DynamicTest> shouldMapStringToLower() {
        return testTextToText(
                new Lower(),
                "should map string to lower -",
                new TestCase("abc", "abc"),
                new TestCase("ABC", "abc"),
                new TestCase("123", "123"),
                new TestCase("", "")
        );
    }
}
