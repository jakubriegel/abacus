package eu.jrie.abacus.ui.infra;

import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.awt.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ComponentHelperTest {

    @Test
    void shouldSetConstantSize() {
        // given
        var component = new JPanel();
        int width = 5;
        int height = 6;
        var expectedSize = new Dimension(width, height);

        // when
        ComponentHelper.setConstantSize(component, width, height);

        // then
        assertEquals(expectedSize, component.getMinimumSize());
        assertEquals(expectedSize, component.getPreferredSize());
        assertEquals(expectedSize, component.getMaximumSize());
    }

    @Test
    void shouldSetHorizontallyFlexibleSize() {
        // given
        var component = new JPanel();
        int width = 5;
        int height = 6;

        // when
        ComponentHelper.setHorizontallyFlexibleSize(component, width, height);

        // then
        assertEquals(new Dimension(0, height), component.getMinimumSize());
        assertEquals(new Dimension(width, height), component.getPreferredSize());
        assertEquals(new Dimension(Integer.MAX_VALUE, height), component.getMaximumSize());
    }
}
