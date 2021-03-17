package eu.jrie.abacus.lang.domain.parser;

import eu.jrie.abacus.core.domain.expression.NumberValue;
import eu.jrie.abacus.core.domain.formula.FormulaDefinition;
import eu.jrie.abacus.lang.domain.exception.InvalidInputException;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FormulaParserTest {

    private static final Map<String, List<FormulaDefinition>> formulas = Map.of(
            "add", List.of(
                    new FormulaDefinition("add", List.of(NumberValue.class, NumberValue.class), args -> {
                        var a = (NumberValue) args.get(0);
                        var b = (NumberValue) args.get(1);
                        return new NumberValue(a.value() + b.value());
                    })
            )
    );

    private final FormulaParser parser = new FormulaParser(formulas);

    @Test
    void shouldMatchFormula() throws InvalidInputException {
        // given
        var formulaText = "add(1,2)";

        // when
        var result = parser.parse(formulaText);

        // then
        assertEquals("add", result.functionName());
        assertEquals(List.of(new NumberValue(1), new NumberValue(2)), result.arguments());
        assertEquals("3", result.action().get().getAsString());
    }

    @Test
    void shouldMatchFormulaWithSpaces() throws InvalidInputException {
        // given
        var formulaText = " add  ( 1 , 2 ) ";

        // when
        var result = parser.parse(formulaText);

        // then
        assertEquals("add", result.functionName());
        assertEquals(List.of(new NumberValue(1), new NumberValue(2)), result.arguments());
        assertEquals("3", result.action().get().getAsString());
    }
}
