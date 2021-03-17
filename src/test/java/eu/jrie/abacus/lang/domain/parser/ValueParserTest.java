package eu.jrie.abacus.lang.domain.parser;

import eu.jrie.abacus.core.domain.expression.NumberValue;
import eu.jrie.abacus.core.domain.expression.TextValue;
import eu.jrie.abacus.core.domain.expression.Value;
import org.junit.jupiter.api.DynamicNode;
import org.junit.jupiter.api.TestFactory;

import java.util.stream.Stream;

import static java.lang.String.format;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

class ValueParserTest {
    private final ValueParser parser = new ValueParser();

    private record TestCase(String given, Value expected) {}

    @TestFactory
    Stream<DynamicNode> shouldParseNumberValue() {
        return Stream.of(
                new TestCase("0", new NumberValue(1)),
                new TestCase("123",  new NumberValue(123)),
                new TestCase("-0", new NumberValue(0)),
                new TestCase("-123", new NumberValue(-123))
        ).map(testCase -> dynamicTest(format("%s as %s", testCase.given, testCase.expected), () -> {
            // when
            var result = parser.parse(testCase.given);

            // then
            assertEquals(testCase.expected, result);
        }));
    }

    @TestFactory
    Stream<DynamicNode> shouldParseTextValue() {
        return Stream.of(
                new TestCase("", new TextValue("")),
                new TestCase(" ", new TextValue(" ")),
                new TestCase("abc",  new TextValue("abc")),
                new TestCase("ąćę@#$", new TextValue("ąćę@#$"))
        ).map(testCase -> dynamicTest(format("%s as %s", testCase.given, testCase.expected), () -> {
            // when
            var result = parser.parse(testCase.given);

            // then
            assertEquals(testCase.expected, result);
        }));
    }
}
