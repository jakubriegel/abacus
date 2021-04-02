package eu.jrie.abacus.core.domain.formula;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;

class FormulaManagerTest {

    private static final String FORMULA_NAME = "action";
    private final FormulaImplementation formulaImplementation = mock(FormulaImplementation.class);

    private final Map<String, List<FormulaImplementation>> formulas = Map.of(
            FORMULA_NAME, singletonList(formulaImplementation),
            "another", emptyList()
    );

    private final FormulaManager manager = new FormulaManager(formulas);

    @Test
    void shouldFindImplementationsOfFormula() {
        // when
        var result = manager.findImplementations(FORMULA_NAME);

        // then
        assertIterableEquals(singletonList(formulaImplementation), result);
    }

    @Test
    void shouldReturnNullWhenFindingNotExistingFormula() {
        // when
        var result = manager.findImplementations("add");

        // then
        assertNull(result);
    }
}
