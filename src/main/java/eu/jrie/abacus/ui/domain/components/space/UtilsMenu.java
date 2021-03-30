package eu.jrie.abacus.ui.domain.components.space;

import eu.jrie.abacus.ui.domain.components.factory.ComponentFactory;
import eu.jrie.abacus.ui.domain.components.factory.IconButtonFactory;

import javax.swing.*;
import java.awt.*;
import java.util.List;

import static eu.jrie.abacus.ui.domain.Colors.PRIMARY_COLOR;
import static eu.jrie.abacus.ui.infra.helper.ComponentHelper.setConstantSize;
import static java.awt.Color.white;
import static javax.swing.BorderFactory.createCompoundBorder;
import static javax.swing.BorderFactory.createEmptyBorder;
import static javax.swing.BorderFactory.createMatteBorder;
import static javax.swing.BoxLayout.Y_AXIS;

public class UtilsMenu extends JPanel {

    private static final int BORDER_SIZE = 1;
    private static final int BUTTON_SIZE = 35;
    private static final int BUTTON_ICON_SIZE = BUTTON_SIZE - 10;

    private final IconButtonFactory iconButtonFactory = new ComponentFactory();

    private final List<JComponent> buttons = List.of(
            button("Create new spreadsheet", "round_add_circle_black_48dp.png"),
            button("Open existing spreadsheet", "round_folder_open_black_48dp.png"),
            button("Save current spreadsheet", "round_save_alt_black_48dp.png"),
            button("Undo", "round_undo_black_48dp.png"),
            button("Redo", "round_redo_black_48dp.png"),
            button("Zoom in", "round_add_black_48dp.png"),
            button("Zoom out", "round_remove_black_48dp.png"),
            button("Search", "round_manage_search_black_48dp.png")
    );

    public UtilsMenu() {
        setBorder(createCompoundBorder(
                createMatteBorder(BORDER_SIZE, 0, 0, BORDER_SIZE, PRIMARY_COLOR),
                createEmptyBorder()
        ));
        setMinimumSize(new Dimension(BUTTON_SIZE + BORDER_SIZE,  BUTTON_SIZE * 3));
        setPreferredSize(new Dimension(BUTTON_SIZE + BORDER_SIZE, Integer.MAX_VALUE));
        setMaximumSize(new Dimension(BUTTON_SIZE + BORDER_SIZE, Integer.MAX_VALUE));
        setBackground(white);

        var container = new JPanel();
        container.setLayout(new BoxLayout(container, Y_AXIS));
        container.setAlignmentY(TOP_ALIGNMENT);
        setConstantSize(container, BUTTON_SIZE, BUTTON_SIZE * buttons.size());

        buttons.forEach(container::add);

        add(container);
    }

    private JComponent button(String text, String iconName) {
        return iconButtonFactory.buildButton(text, "/menu/" + iconName, BUTTON_ICON_SIZE);
    }
}