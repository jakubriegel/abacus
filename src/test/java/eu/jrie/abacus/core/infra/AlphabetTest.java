package eu.jrie.abacus.core.infra;

import org.junit.jupiter.api.DynamicNode;
import org.junit.jupiter.api.TestFactory;

import java.util.stream.Stream;

import static java.lang.String.format;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

class AlphabetTest {

    private static final Alphabet alphabet = new Alphabet();

    private static record TestCase(int number, String literal) {}

    private final Stream<TestCase> cases = Stream.of(
            new TestCase(0, "A"),
            new TestCase(1,  "B"),
            new TestCase(26, "BA"),
            new TestCase(53, "CB"),
            new TestCase(102, "DY")
    );

    @TestFactory
    Stream<DynamicNode> shouldParseNumberToLiteral() {
        return cases.map(testCase ->
                dynamicTest(
                        format("should parse %s to %s", testCase.number, testCase.literal),
                        () -> {
                            // when
                            var result = alphabet.getLiteral(testCase.number);

                            // then
                            assertEquals(testCase.literal, result);
                        })
        );
    }

    @TestFactory
    Stream<DynamicNode> shouldParseLiteralToNumber() {
        return cases.map(testCase ->
                dynamicTest(
                        format("should parse %s to %s", testCase.literal, testCase.number),
                        () -> {
                            // when
                            var result = alphabet.getNumber(testCase.literal);

                            // then
                            assertEquals(testCase.number, result);
                        })
        );
    }
}
