package eu.jrie.abacus.ui.infra;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FontProviderTest {
    @Test
    void shouldProvideStandardFont() {
        // when
        var font = FontProvider.standardFont();

        // then
        assertEquals(12f, font.getSize());
        assertEquals("JetBrains Mono Regular", font.getName());
    }
}