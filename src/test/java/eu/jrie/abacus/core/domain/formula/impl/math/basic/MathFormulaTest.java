package eu.jrie.abacus.core.domain.formula.impl.math.basic;

import java.util.List;

import static java.util.Collections.singletonList;

abstract class MathFormulaTest {
    protected static record TestCase (
            List<String> given,
            String expected
    ) {
        TestCase(String given, String expected) {
            this(singletonList(given), expected);
        }

        String singleGiven() {
            assert given.size() == 1;
            return given.get(0);
        }
    }
}
