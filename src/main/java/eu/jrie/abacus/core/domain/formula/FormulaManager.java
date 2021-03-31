package eu.jrie.abacus.core.domain.formula;

import java.util.List;
import java.util.Map;

public class FormulaManager {

    private final Map<String, List<FormulaImplementation>> formulas;

    public FormulaManager(Map<String, List<FormulaImplementation>> formulas) {
        this.formulas = formulas;
    }

    public List<FormulaImplementation> findImplementations(String name) {
        return formulas.get(name);
    }

}
