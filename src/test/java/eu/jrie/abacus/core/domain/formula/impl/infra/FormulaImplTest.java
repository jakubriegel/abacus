package eu.jrie.abacus.core.domain.formula.impl.infra;

import eu.jrie.abacus.core.domain.expression.NumberValue;
import eu.jrie.abacus.core.domain.expression.TextValue;
import eu.jrie.abacus.core.domain.formula.ArgumentValueSupplier;
import eu.jrie.abacus.core.domain.formula.FormulaImplementation;
import org.junit.jupiter.api.DynamicTest;

import java.math.BigDecimal;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Stream;

import static java.text.MessageFormat.format;
import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.toUnmodifiableList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

public abstract class FormulaImplTest {

    public static record TestCase (
            List<String> given,
            String expected
    ) {
        public TestCase(String given, String expected) {
            this(singletonList(given), expected);
        }
    }

    protected static Stream<DynamicTest> testNumberToNumber(FormulaImplementation formula, String namePrefix, TestCase...cases) {
        return test(formula, namePrefix, asList(cases), testCase -> {
            // given
            var args = testCase.given().stream()
                    .map(BigDecimal::new)
                    .map(NumberValue::new)
                    .map(arg -> (ArgumentValueSupplier) context -> arg)
                    .collect(toUnmodifiableList());

            // when
            var result = formula.run(null, args);

            // then
            assertTrue(result instanceof NumberValue);
            var resultValue = ((NumberValue) result).value();
            assertEquals(testCase.expected(), resultValue.stripTrailingZeros().toString());
        });
    }

    protected static Stream<DynamicTest> testTextToNumber(FormulaImplementation formula, String namePrefix, TestCase...cases) {
        return test(formula, namePrefix, asList(cases), testCase -> {
            // given
            var args = testCase.given().stream()
                    .map(TextValue::new)
                    .map(arg -> (ArgumentValueSupplier) context -> arg)
                    .collect(toUnmodifiableList());

            // when
            var result = formula.run(null, args);

            // then
            assertTrue(result instanceof NumberValue);
            var resultValue = ((NumberValue) result).value();
            assertEquals(testCase.expected(), resultValue.stripTrailingZeros().toString());
        });
    }

    protected static Stream<DynamicTest> testTextToText(FormulaImplementation formula, String namePrefix, TestCase...cases) {
        return test(formula, namePrefix, asList(cases), testCase -> {
            // given
            var args = testCase.given().stream()
                    .map(TextValue::new)
                    .map(arg -> (ArgumentValueSupplier) context -> arg)
                    .collect(toUnmodifiableList());

            // when
            var result = formula.run(null, args);

            // then
            assertTrue(result instanceof TextValue);
            assertEquals(testCase.expected(), result.getAsString());
        });
    }

    protected static Stream<DynamicTest> test(FormulaImplementation formula, String namePrefix, List<TestCase> cases, Consumer<TestCase> test) {
        return cases.stream()
                .map(testCase -> dynamicTest(
                        name(formula, namePrefix, testCase),
                        () -> test.accept(testCase)
                ));
    }

    private static String name(FormulaImplementation formula, String prefix, TestCase testCase) {
        return format("{0} | {1} {2}", formula.getName(), prefix, testCase.given());
    }
}
