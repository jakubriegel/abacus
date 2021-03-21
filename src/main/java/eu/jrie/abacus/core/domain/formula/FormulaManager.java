package eu.jrie.abacus.core.domain.formula;

import eu.jrie.abacus.core.domain.expression.NumberValue;
import eu.jrie.abacus.core.domain.expression.TextValue;

import java.util.List;
import java.util.Map;

import static java.lang.String.format;

public class FormulaManager {
    final Map<String, List<FormulaDefinition>> formulas = Map.of(
            "cool", List.of(
                    new FormulaDefinition(
                            "cool",
                            List.of(TextValue.class, NumberValue.class),
                            (context, args) -> {
                                var text = (TextValue) args.get(0).get(context);
                                var number = (NumberValue) args.get(1).get(context);
                                System.out.println("in cool " + args);
                                return new TextValue(format("%s - %s", text.value(), number.value()));
                            })
            ));

    public List<FormulaDefinition> findDefinitions(String name) {
        return formulas.get(name);
    }
}
