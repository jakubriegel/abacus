package eu.jrie.abacus.lang.domain.parser.argument;

import eu.jrie.abacus.core.domain.expression.Formula;
import eu.jrie.abacus.lang.domain.exception.InvalidInputException;
import eu.jrie.abacus.lang.domain.grammar.RuleMatch;

@FunctionalInterface
public interface FormulaResolver {
    Formula resolve(RuleMatch match) throws InvalidInputException;
}
