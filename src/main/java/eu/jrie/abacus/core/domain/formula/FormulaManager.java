package eu.jrie.abacus.core.domain.formula;

import eu.jrie.abacus.core.domain.expression.NumberValue;
import eu.jrie.abacus.core.domain.expression.TextValue;

import java.util.List;
import java.util.Map;

import static java.lang.String.format;

public class FormulaManager {
    final Map<String, FormulaDefinition> formulas = Map.of(
            "cool", new FormulaDefinition("cool", List.of(TextValue.class, NumberValue.class), args -> {
                System.out.println("in cool " + args);
                return new TextValue(format("%s - %s", args.get(0), args.get(1)));
            })
    );

    public Map<String, FormulaDefinition> getFormulas() {
        return formulas;
    }
}
