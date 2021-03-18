package eu.jrie.abacus.core.infra;

import org.junit.jupiter.api.DynamicNode;
import org.junit.jupiter.api.TestFactory;

import java.util.stream.Stream;

import static java.lang.String.format;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

class AlphabetTest {

    private static final Alphabet alphabet = new Alphabet();

    private record TestCase(int given, String expected) {}

    @TestFactory
    Stream<DynamicNode> shouldParseNumberToLiteral() {
        return Stream.of(
                new TestCase(0, "A"),
                new TestCase(1,  "B"),
                new TestCase(26, "BA"),
                new TestCase(102, "DV")
        ).map(testCase -> dynamicTest(format("should parse %s to %s", testCase.given, testCase.expected), () -> {
            // when
            var result = alphabet.getLiteral(testCase.given);

            // then
            assertEquals(testCase.expected, result);
        }));
    }
}
