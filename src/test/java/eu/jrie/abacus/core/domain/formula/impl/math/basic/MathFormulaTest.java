package eu.jrie.abacus.core.domain.formula.impl.math.basic;

import eu.jrie.abacus.core.domain.expression.NumberValue;
import eu.jrie.abacus.core.domain.formula.ArgumentValueSupplier;
import eu.jrie.abacus.core.domain.formula.FormulaImplementation;
import org.junit.jupiter.api.DynamicTest;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Stream;

import static java.text.MessageFormat.format;
import static java.util.Arrays.stream;
import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.toUnmodifiableList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

abstract class MathFormulaTest {
    protected static record TestCase (
            List<String> given,
            String expected
    ) {
        TestCase(String given, String expected) {
            this(singletonList(given), expected);
        }
    }

    protected static Stream<DynamicTest> formulaTest(FormulaImplementation formula, String namePrefix, TestCase...cases) {
        return stream(cases).map(testCase -> dynamicTest(
                format("{0} | {1} {2}", formula.getName(), namePrefix, testCase.given()),
                () -> {
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
                }));
    }
}
