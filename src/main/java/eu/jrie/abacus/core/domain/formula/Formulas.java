package eu.jrie.abacus.core.domain.formula;

import eu.jrie.abacus.core.domain.formula.impl.math.basic.Abs;
import eu.jrie.abacus.core.domain.formula.impl.math.basic.Add;
import eu.jrie.abacus.core.domain.formula.impl.math.basic.Ceil;
import eu.jrie.abacus.core.domain.formula.impl.math.basic.Div;
import eu.jrie.abacus.core.domain.formula.impl.math.basic.Divide;
import eu.jrie.abacus.core.domain.formula.impl.math.basic.Floor;
import eu.jrie.abacus.core.domain.formula.impl.math.basic.Mod;
import eu.jrie.abacus.core.domain.formula.impl.math.basic.Multiply;
import eu.jrie.abacus.core.domain.formula.impl.math.basic.Subtract;
import eu.jrie.abacus.core.domain.formula.impl.math.stats.Max;

import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Stream;

import static java.util.Arrays.asList;
import static java.util.function.Predicate.isEqual;
import static java.util.stream.Collectors.toSet;
import static java.util.stream.Collectors.toUnmodifiableMap;

public abstract class Formulas {

    private Formulas() {}

    public static Map<String, List<FormulaImplementation>> buildFormulas() {
        return Stream.of(
                buildFormula(new Add()),
                buildFormula(new Subtract()),
                buildFormula(new Abs()),
                buildFormula(new Multiply()),
                buildFormula(new Divide()),
                buildFormula(new Mod()),
                buildFormula(new Div()),
                buildFormula(new Floor()),
                buildFormula(new Ceil()),
                buildFormula(new Max())
        ).collect(toUnmodifiableMap(Entry::getKey, Entry::getValue));
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
}
