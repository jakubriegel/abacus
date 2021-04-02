package eu.jrie.abacus.core.domain.formula.impl.logic;

import eu.jrie.abacus.core.domain.expression.LogicValue;
import eu.jrie.abacus.core.domain.expression.NumberValue;
import eu.jrie.abacus.core.domain.formula.ArgumentValueSupplier;
import eu.jrie.abacus.core.domain.formula.FormulaImplementation;
import eu.jrie.abacus.core.domain.formula.impl.infra.FormulaImplTest;
import org.junit.jupiter.api.Test;

import java.util.List;

import static java.math.BigDecimal.ONE;
import static java.math.BigDecimal.TEN;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class IfTest extends FormulaImplTest {

    private static final FormulaImplementation formula = new If();

    @Test
    void shouldReturnFirstValueIfTrue() {
        // given
        var args = argsWith(true);

        // when
        var result = formula.run(null, args);

        // then
        assertTrue(result instanceof NumberValue);
        var resultValue = ((NumberValue) result).value();
        assertEquals(ONE, resultValue);
    }

    @Test
    void shouldReturnSecondValueIfFalse() {
        // given
        var args = argsWith(false);

        // when
        var result = formula.run(null, args);

        // then
        assertTrue(result instanceof NumberValue);
        var resultValue = ((NumberValue) result).value();
        assertEquals(TEN, resultValue);
    }

    private static List<ArgumentValueSupplier> argsWith(boolean conditionValue) {
        return List.of(c -> new LogicValue(conditionValue), c -> new NumberValue(ONE), c -> new NumberValue(TEN));
    }
}
