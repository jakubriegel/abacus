package eu.jrie.abacus.core.domain.formula;

import eu.jrie.abacus.core.domain.formula.impl.math.Abs;
import eu.jrie.abacus.core.domain.formula.impl.math.Add;
import eu.jrie.abacus.core.domain.formula.impl.math.Max;
import eu.jrie.abacus.core.domain.formula.impl.math.Multiply;
import eu.jrie.abacus.core.domain.formula.impl.math.Subtract;

import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Stream;

import static java.util.Arrays.asList;
import static java.util.function.Predicate.isEqual;
import static java.util.stream.Collectors.toMap;
import static java.util.stream.Collectors.toSet;

public class FormulaManager {

    private static final Map<String, List<FormulaImplementation>> formulas = buildFormulas();

    private static Map<String, List<FormulaImplementation>> buildFormulas() {
        return Stream.of(
                buildFormula(new Add()),
                buildFormula(new Subtract()),
                buildFormula(new Abs()),
                buildFormula(new Max()),
                buildFormula(new Multiply())
        ).collect(toMap(Entry::getKey, Entry::getValue));
    }

    private static Entry<String, List<FormulaImplementation>> buildFormula(FormulaImplementation...impl) {
        return buildFormula(asList(impl));
    }

    private static Entry<String, List<FormulaImplementation>> buildFormula(List<FormulaImplementation> impls) {
        final var name = impls.get(0).getName();

        assert impls.stream()
                .map(FormulaImplementation::getName)
                .allMatch(isEqual(name));

        assert impls.size() == impls.stream()
                .map(FormulaImplementation::getArgumentTypes)
                .collect(toSet())
                .size();

        return new SimpleImmutableEntry<>(name, impls);
    }

    public List<FormulaImplementation> findDefinitions(String name) {
        return formulas.get(name);
    }

}
