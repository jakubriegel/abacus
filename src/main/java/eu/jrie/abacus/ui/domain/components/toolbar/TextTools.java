package eu.jrie.abacus.ui.domain.components.toolbar;

import eu.jrie.abacus.ui.domain.components.factory.ComponentFactory;
import eu.jrie.abacus.ui.domain.components.factory.IconButtonFactory;

import javax.swing.*;
import java.awt.*;
import java.util.List;

import static eu.jrie.abacus.ui.infra.ComponentHelper.setConstantSize;
import static java.awt.Color.black;
import static java.awt.Color.gray;
import static java.awt.Color.white;
import static javax.swing.BorderFactory.createCompoundBorder;
import static javax.swing.BorderFactory.createLineBorder;
import static javax.swing.BoxLayout.X_AXIS;
import static javax.swing.BoxLayout.Y_AXIS;

public class TextTools extends JPanel {

    private static final int BUTTONS_ROW_N = 10;
    private static final int BUTTON_SIZE = 23;
    private static final int BUTTON_ICON_SIZE = BUTTON_SIZE - 3;

    private final IconButtonFactory iconButtonFactory = new ComponentFactory();

    private final List<JComponent> topTools = List.of(
            button("Change font size", "round_format_size_black_48dp.png"),
            colorTile(gray),
            button("Choose font size", "../round_expand_more_black_48dp.png"),
            button("Align text left", "round_format_align_left_black_48dp.png"),
            button("Align text center", "round_format_align_center_black_48dp.png"),
            button("Align text right", "round_format_align_right_black_48dp.png"),
            button("Justify text", "round_format_align_justify_black_48dp.png"),
            button("Change text color", "round_format_color_text_black_48dp.png"),
            colorTile(black),
            button("Choose text color", "../round_expand_more_black_48dp.png")
    );

    private final List<JComponent> bottomTools = List.of(
            button("Bold text", "round_format_bold_black_48dp.png"),
            button("Italic text", "round_format_italic_black_48dp.png"),
            button("Underlined text", "round_format_underlined_black_48dp.png"),
            button("Align text bottom", "round_vertical_align_bottom_black_48dp.png"),
            button("Align text center", "round_vertical_align_center_black_48dp.png"),
            button("Align text top", "round_vertical_align_top_black_48dp.png"),
            button("Clear text formatting", "round_format_clear_black_48dp.png"),
            button("Change cell color", "round_format_color_fill_black_48dp.png"),
            colorTile(white),
            button("Choose cell color", "../round_expand_more_black_48dp.png")
    );

    public TextTools() {
        setConstantSize(this, BUTTON_SIZE * BUTTONS_ROW_N, BUTTON_SIZE * 2);
        setLayout(new BoxLayout(this, Y_AXIS));

        assert topTools.size() == BUTTONS_ROW_N;
        add(container(topTools));

        assert bottomTools.size() == BUTTONS_ROW_N;
        add(container(bottomTools));
    }

    private JPanel container(List<JComponent> buttons) {
        var container = new JPanel();
        container.setLayout(new BoxLayout(container, X_AXIS));
        setConstantSize(container, BUTTON_SIZE * BUTTONS_ROW_N, BUTTON_SIZE);
        buttons.forEach(container::add);
        return container;
    }

    private static JComponent colorTile(Color color) {
        var panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBorder(createCompoundBorder(
                createLineBorder(white, 4),
                createLineBorder(black, 2)
        ));
        panel.setBackground(color);
        setConstantSize(panel, BUTTON_SIZE, BUTTON_SIZE);
        return panel;
    }

    private JComponent button(String text, String iconName) {
        return iconButtonFactory.buildButton(text, "/toolbar/" + iconName, BUTTON_ICON_SIZE);
    }
}
