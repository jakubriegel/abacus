package eu.jrie.abacus.ui.infra.helper;

import org.junit.jupiter.api.Test;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ListenerHelperTest {
    @Test
    void shouldCreatePropertyChangeListener() {
        // given
        var propertyName = "property";
        var actionCalled = new AtomicBoolean(false);
        Consumer<PropertyChangeEvent> action = event -> actionCalled.set(true);

        // when
        var listener = ListenerHelper.propertyChangeListener(propertyName, action);

        // and
        var invalidEvent = new PropertyChangeEvent("", "someName", null, null);
        listener.propertyChange(invalidEvent);

        // then
        assertFalse(actionCalled.get());

        // when
        var matchingEvent = new PropertyChangeEvent("", propertyName, null, null);
        listener.propertyChange(matchingEvent);

        // then
        assertTrue(actionCalled.get());
    }

    @Test
    void shouldCreateKeyReleasedListener() {
        // given
        var actionCalled = new AtomicBoolean(false);
        Consumer<KeyEvent> action = event -> actionCalled.set(true);

        // when
        var listener = ListenerHelper.keyReleasedListener(action);

        // and
        listener.keyPressed(null);

        // then
        assertFalse(actionCalled.get());

        // when
        listener.keyTyped(null);

        // then
        assertFalse(actionCalled.get());

        // when
        listener.keyReleased(null);

        // then
        assertTrue(actionCalled.get());
    }

    @Test
    void shouldCreateMouseClickedListener() {
        // given
        var actionCalled = new AtomicBoolean(false);
        Consumer<MouseEvent> action = event -> actionCalled.set(true);

        // when
        var listener = ListenerHelper.mouseClickedEvent(action);

        // and
        listener.mousePressed(null);

        // then
        assertFalse(actionCalled.get());

        // when
        listener.mouseReleased(null);

        // then
        assertFalse(actionCalled.get());

        // when
        listener.mouseEntered(null);

        // then
        assertFalse(actionCalled.get());

        // when
        listener.mouseExited(null);

        // then
        assertFalse(actionCalled.get());

        // when
        listener.mouseClicked(null);

        // then
        assertTrue(actionCalled.get());
    }
}
