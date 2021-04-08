package eu.jrie.abacus.lang.domain.parser;

import eu.jrie.abacus.lang.domain.grammar.ElementMatch;
import eu.jrie.abacus.lang.domain.grammar.GrammarRule;
import eu.jrie.abacus.lang.domain.grammar.RuleMatch;
import eu.jrie.abacus.lang.domain.grammar.TokenMatch;
import eu.jrie.abacus.lang.domain.grammar.rule.Function;
import eu.jrie.abacus.lang.domain.grammar.rule.FunctionArg;
import eu.jrie.abacus.lang.domain.grammar.rule.FunctionArgs;

import java.util.List;

import static eu.jrie.abacus.lang.domain.grammar.Token.FUNCTION_ARGS_START;
import static eu.jrie.abacus.lang.domain.grammar.Token.FUNCTION_ARGS_STOP;
import static eu.jrie.abacus.lang.domain.grammar.Token.FUNCTION_NAME;
import static eu.jrie.abacus.lang.domain.grammar.Token.NUMBER_VALUE;
import static eu.jrie.abacus.lang.domain.grammar.Token.TEXT_VALUE;
import static java.util.Collections.singletonList;

abstract class ParserTestHelper {

    static final GrammarRule FUNCTION_RULE = new Function();
    static final GrammarRule FUNCTION_ARG_RULE = new FunctionArg();
    static final GrammarRule FUNCTION_ARGS_RULE = new FunctionArgs();

    static final TokenMatch FUNCTION_NAME_MATCH = new TokenMatch(FUNCTION_NAME, "action", "action");
    static final TokenMatch ARGS_START_MATCH = new TokenMatch(FUNCTION_ARGS_START, "(", "(");
    static final TokenMatch ARGS_STOP_MATCH = new TokenMatch(FUNCTION_ARGS_STOP, ")", ")");

    static final TokenMatch FUNCTION_NAME_WITH_SPACES_MATCH = new TokenMatch(FUNCTION_NAME, "action", "action ");
    static final TokenMatch ARGS_START_WITH_SPACES_MATCH = new TokenMatch(FUNCTION_ARGS_START, "(", "( ");
    static final TokenMatch ARGS_STOP_WITH_SPACES_MATCH = new TokenMatch(FUNCTION_ARGS_STOP, ")", ") ");

    static final List<ElementMatch> NO_ARG_FUNCTION_MATCH = singletonList(new RuleMatch(
            FUNCTION_RULE,
            List.of(FUNCTION_NAME_MATCH, ARGS_START_MATCH, ARGS_STOP_MATCH))
    );

    static final List<ElementMatch> NUMBER_ARG_FUNCTION_MATCH = singletonList(new RuleMatch(
            FUNCTION_RULE,
            List.of(
                    FUNCTION_NAME_MATCH,
                    ARGS_START_MATCH,
                    new RuleMatch(
                            FUNCTION_ARGS_RULE,
                            singletonList(new RuleMatch(
                                    FUNCTION_ARG_RULE,
                                    singletonList(new TokenMatch(NUMBER_VALUE, "1", "1"))
                            ))
                    ),
                    ARGS_STOP_MATCH
            )
    ));

    static final List<ElementMatch> TEXT_ARG_FUNCTION_MATCH = singletonList(new RuleMatch(
            FUNCTION_RULE,
            List.of(
                    FUNCTION_NAME_MATCH,
                    ARGS_START_MATCH,
                    new RuleMatch(
                            FUNCTION_ARGS_RULE,
                            singletonList(new RuleMatch(
                                    FUNCTION_ARG_RULE,
                                    singletonList(new TokenMatch(TEXT_VALUE, "'abc '", "'abc '"))
                            ))
                    ),
                    ARGS_STOP_MATCH
            )
    ));

    static final List<ElementMatch> FORMULA_ARG_FORMULA_MATCH = singletonList(new RuleMatch(
            FUNCTION_RULE,
            List.of(
                    FUNCTION_NAME_MATCH,
                    ARGS_START_MATCH,
                    new RuleMatch(
                            FUNCTION_ARGS_RULE,
                            singletonList(new RuleMatch(
                                    FUNCTION_ARG_RULE,
                                    singletonList(new RuleMatch(
                                            FUNCTION_RULE,
                                            List.of(
                                                    FUNCTION_NAME_MATCH,
                                                    ARGS_START_MATCH,
                                                    new RuleMatch(
                                                            FUNCTION_ARGS_RULE,
                                                            singletonList(new RuleMatch(
                                                                    FUNCTION_ARG_RULE,
                                                                    singletonList(new TokenMatch(NUMBER_VALUE, "1", "1"))
                                                            ))
                                                    ),
                                                    ARGS_STOP_MATCH
                                            )
                                    ))
                            ))
                    ),
                    ARGS_STOP_MATCH
            )
    ));

    private ParserTestHelper() {}


}
