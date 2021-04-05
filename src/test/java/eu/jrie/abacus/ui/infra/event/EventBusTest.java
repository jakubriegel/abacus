package eu.jrie.abacus.ui.infra.event;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

import static eu.jrie.abacus.ui.infra.event.EventType.CELL_FOCUS;
import static eu.jrie.abacus.ui.infra.event.EventType.CELL_UPDATED;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class EventBusTest {
    @ParameterizedTest(name = "should register handler for {0}")
    @EnumSource(EventType.class)
    void shouldRegisterHandler(EventType type) {
        // given
        var handlers = new HashSet<EventHandler>();
        var bus = new EventBus(handlers);
        var name = "name-" + type;

        // when
        bus.register(name, type, null);

        // then
        assertIterableEquals(Set.of(new EventHandler(name, type, null)), handlers);
    }

    @Test
    void shouldTriggerHandler() {
        // given
        var bus = new EventBus();
        var matchingCalled = new AtomicBoolean(false);
        bus.register("matching", CELL_FOCUS, event -> matchingCalled.set(true));

        // and
        var invalidCalled = new AtomicBoolean(false);
        bus.register("invalid", CELL_UPDATED, event -> invalidCalled.set(true));

        // when
        bus.accept(new Event(CELL_FOCUS, null, null, null));

        // then
        assertTrue(matchingCalled.get());
        assertFalse(invalidCalled.get());
    }
}
