package eu.jrie.abacus.core.domain.formula.impl.logic;

import eu.jrie.abacus.core.domain.expression.LogicValue;
import eu.jrie.abacus.core.domain.expression.NumberValue;
import eu.jrie.abacus.core.domain.expression.TextValue;
import eu.jrie.abacus.core.domain.formula.ArgumentValueSupplier;
import eu.jrie.abacus.core.domain.formula.FormulaImplementation;
import eu.jrie.abacus.core.domain.formula.impl.infra.FormulaImplTest;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;

import java.util.List;
import java.util.stream.Stream;

import static java.math.BigDecimal.ONE;
import static java.util.stream.Collectors.toUnmodifiableList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CompareTest extends FormulaImplTest {

    private static final FormulaImplementation formula = new Compare();

    @TestFactory
    Stream<DynamicTest> shouldCompareNumbers() {
        var cases = List.of(
                new TestCase(List.of("1", "1"), "true"),
                new TestCase(List.of("1", "2"), "false"),
                new TestCase(List.of("1", "-1"), "false"),
                new TestCase(List.of("1", "-2"), "false")
        );
        return test(formula, "should compare numbers -", cases, testCase -> {
            // given
            var args = testCase.given().stream()
                    .map(NumberValue::new)
                    .map(arg -> (ArgumentValueSupplier) context -> arg)
                    .collect(toUnmodifiableList());

            // when
            var result = formula.run(null, args);

            // then
            assertTrue(result instanceof LogicValue);
            var resultValue = ((LogicValue) result).value();
            assertEquals(testCase.expected(), String.valueOf(resultValue));
        });
    }

    @TestFactory
    Stream<DynamicTest> shouldCompareTexts() {
        var cases = List.of(
                new TestCase(List.of("abc", "abc"), "true"),
                new TestCase(List.of("abc", "def"), "false"),
                new TestCase(List.of("abc", "ABC"), "false"),
                new TestCase(List.of("", ""), "true")
        );
        return test(formula, "should compare texts -", cases, testCase -> {
            // given
            var args = testCase.given().stream()
                    .map(TextValue::new)
                    .map(arg -> (ArgumentValueSupplier) context -> arg)
                    .collect(toUnmodifiableList());

            // when
            var result = formula.run(null, args);

            // then
            assertTrue(result instanceof LogicValue);
            var resultValue = ((LogicValue) result).value();
            assertEquals(testCase.expected(), String.valueOf(resultValue));
        });
    }

    @Test
    void shouldReturnFalseForComparingDifferentTypes() {
        // given
        List<ArgumentValueSupplier> args = List.of(c -> new NumberValue(ONE), c -> new TextValue("1"));

        // when
        var result = formula.run(null, args);

        // then
        assertTrue(result instanceof LogicValue);
        var resultValue = ((LogicValue) result).value();
        assertFalse(resultValue);
    }
}
