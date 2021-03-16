package eu.jrie.abacus.core.formula;

import eu.jrie.abacus.lang.domain.NumberArgument;
import eu.jrie.abacus.lang.domain.TextArgument;

import java.util.List;
import java.util.Map;

import static java.lang.String.format;

public class FormulaManager {
    final Map<String, FormulaDefinition> formulas = Map.of(
            "cool", new FormulaDefinition("cool", List.of(TextArgument.class, NumberArgument.class), args -> {
                System.out.println("in cool " + args);
                return format("%s - %s", args.get(0), args.get(1));
            })
    );

    public Map<String, FormulaDefinition> getFormulas() {
        return formulas;
    }
}
