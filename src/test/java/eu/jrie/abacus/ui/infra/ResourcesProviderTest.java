package eu.jrie.abacus.ui.infra;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class ResourcesProviderTest {

    private final ResourcesProvider provider = new ResourcesProvider();

    @Test
    void shouldGetIcon() {
        // when
        var result = provider.getIcon("round_expand_more_black_48dp.png");

        // then
        assertNotNull(result);
    }
}
