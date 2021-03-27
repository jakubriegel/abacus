package eu.jrie.abacus.ui.infra.helper;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Objects;
import java.util.function.Consumer;

public abstract class ListenerHelper {

    private ListenerHelper() {}

    public static PropertyChangeListener propertyChangeListener(String propertyName, Consumer<PropertyChangeEvent> action) {
        return evt -> {
            if (Objects.equals(evt.getPropertyName(), propertyName)) {
                action.accept(evt);
            }
        };
    }

    public static KeyListener keyReleasedListener(Consumer<KeyEvent> action) {
        return new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {}

            @Override
            public void keyPressed(KeyEvent e) {}

            @Override
            public void keyReleased(KeyEvent e) {
                action.accept(e);
            }
        };
    }

    public static MouseListener mouseClickedEvent(Consumer<MouseEvent> action) {
        return new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                action.accept(e);
            }

            @Override
            public void mousePressed(MouseEvent e) {}

            @Override
            public void mouseReleased(MouseEvent e) {}

            @Override
            public void mouseEntered(MouseEvent e) {}

            @Override
            public void mouseExited(MouseEvent e) {}
        };
    }
}
