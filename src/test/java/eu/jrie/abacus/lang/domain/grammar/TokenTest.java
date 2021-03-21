package eu.jrie.abacus.lang.domain.grammar;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.junit.jupiter.api.Assertions.assertTrue;

class TokenTest {

    private static final String WHITESPACE_REGEX = "\\s*";

    @ParameterizedTest(name = "Token {0} matches white characters before and after")
    @EnumSource(Token.class)
    void  shouldMatchWhiteCharactersBeforeAndAfterToken(Token token) {
        // when
        final var regex = token.pattern.pattern();

        // then
        assertTrue(regex.startsWith(WHITESPACE_REGEX));
        assertTrue(regex.endsWith(WHITESPACE_REGEX));
        assertTrue(regex.length() > 6);
    }
}