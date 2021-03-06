package eu.jrie.abacus.ui.domain.components.toolbar;

import eu.jrie.abacus.core.domain.cell.Position;
import eu.jrie.abacus.core.domain.cell.style.CellStyle;
import eu.jrie.abacus.core.domain.cell.style.CellTextAlignment;
import eu.jrie.abacus.core.domain.cell.style.CellTextPosition;
import eu.jrie.abacus.ui.domain.components.factory.IconButtonFactory;
import eu.jrie.abacus.ui.infra.event.Event;
import eu.jrie.abacus.ui.infra.event.EventBus;

import javax.swing.*;
import java.awt.*;
import java.util.Optional;

import static eu.jrie.abacus.core.domain.cell.style.CellStyle.Builder.from;
import static eu.jrie.abacus.core.domain.cell.style.CellTextAlignment.CENTER;
import static eu.jrie.abacus.core.domain.cell.style.CellTextAlignment.JUSTIFY;
import static eu.jrie.abacus.core.domain.cell.style.CellTextAlignment.LEFT;
import static eu.jrie.abacus.core.domain.cell.style.CellTextAlignment.RIGHT;
import static eu.jrie.abacus.core.domain.cell.style.CellTextPosition.BOTTOM;
import static eu.jrie.abacus.core.domain.cell.style.CellTextPosition.MIDDLE;
import static eu.jrie.abacus.core.domain.cell.style.CellTextPosition.TOP;
import static eu.jrie.abacus.ui.domain.Colors.LIGHT_COLOR;
import static eu.jrie.abacus.ui.infra.FontProvider.DEFAULT_FONT_SIZE;
import static eu.jrie.abacus.ui.infra.event.EventType.CELL_FOCUS;
import static eu.jrie.abacus.ui.infra.event.EventType.CELL_STYLE_UPDATED;
import static eu.jrie.abacus.ui.infra.event.EventType.CELL_UPDATED;
import static eu.jrie.abacus.ui.infra.helper.ComponentHelper.setConstantSize;
import static java.awt.Color.black;
import static java.awt.Color.white;
import static java.util.Objects.requireNonNull;
import static javax.swing.BorderFactory.createCompoundBorder;
import static javax.swing.BorderFactory.createEmptyBorder;
import static javax.swing.BorderFactory.createLineBorder;
import static javax.swing.BoxLayout.X_AXIS;
import static javax.swing.BoxLayout.Y_AXIS;
import static javax.swing.JColorChooser.showDialog;

public class TextTools extends JPanel {

    private static final int BUTTONS_ROW_N = 10;
    private static final int BUTTON_SIZE = 23;
    private static final int BUTTON_ICON_SIZE = BUTTON_SIZE - 3;

    private final EventBus bus;
    private final IconButtonFactory iconButtonFactory;

    private Position cellPosition = null;
    private CellStyle cellStyle = null;

    // top tools
    private final JComboBox<Integer> fontSizePicker;
    private final JButton alignLeftButton;
    private final JButton alignCenterButton;
    private final JButton alignRightButton;
    private final JButton justifyButton;
    private final JPanel fontColorTile;

    // bottom tools
    private final JButton boldButton;
    private final JButton italicButton;
    private final JButton underlinedButton;
    private final JButton alignBottomButton;
    private final JButton alignMiddleButton;
    private final JButton alignTopButton;
    private final JPanel backgroundColorTile;

    public TextTools(EventBus bus, IconButtonFactory iconButtonFactory) {
        this.bus = bus;
        this.iconButtonFactory = iconButtonFactory;

        setConstantSize(this, BUTTON_SIZE * BUTTONS_ROW_N, BUTTON_SIZE * 2);
        setLayout(new BoxLayout(this, Y_AXIS));

        var top = container();
        var bottom = container();

        button(top, "Change font size", "round_format_size_black_48dp.png");
        fontSizePicker = fontSizePicker(top);
        alignLeftButton = button(top, "Align text left", "round_format_align_left_black_48dp.png", () -> switchTextAlignment(LEFT));
        alignCenterButton = button(top, "Align text center", "round_format_align_center_black_48dp.png", () -> switchTextAlignment(CENTER));
        alignRightButton = button(top, "Align text right", "round_format_align_right_black_48dp.png", () -> switchTextAlignment(RIGHT));
        justifyButton = button(top, "Justify text", "round_format_align_justify_black_48dp.png", () -> switchTextAlignment(JUSTIFY));
        button(top, "Change text color", "round_format_color_text_black_48dp.png");
        fontColorTile = colorTile(top, black);
        button(top, "Choose text color", "../round_expand_more_black_48dp.png", this::switchFontColor);

        boldButton = button(bottom, "Bold text", "round_format_bold_black_48dp.png", this::switchBold);
        italicButton = button(bottom, "Italic text", "round_format_italic_black_48dp.png", this::switchItalic);
        underlinedButton = button(bottom, "Underlined text", "round_format_underlined_black_48dp.png", this::switchUnderlined);
        alignBottomButton = button(bottom, "Align text bottom", "round_vertical_align_bottom_black_48dp.png", () -> switchTextPosition(BOTTOM));
        alignMiddleButton = button(bottom, "Align text center", "round_vertical_align_center_black_48dp.png", () -> switchTextPosition(MIDDLE));
        alignTopButton = button(bottom, "Align text top", "round_vertical_align_top_black_48dp.png", () -> switchTextPosition(TOP));
        button(bottom, "Clear text formatting", "round_format_clear_black_48dp.png", this::clearFormatting);
        button(bottom, "Change cell color", "round_format_color_fill_black_48dp.png");
        backgroundColorTile = colorTile(bottom, white);
        button(bottom, "Choose cell color", "../round_expand_more_black_48dp.png", this::switchBackgroundColor);

        add(top);
        add(bottom);

        bus.register("updateTextEditorStateOnUpdate", CELL_UPDATED, event -> update(event.position(), event.cellStyle()));
        bus.register("updateTextEditorStateOnFocus", CELL_FOCUS, event -> update(event.position(), event.cellStyle()));
    }

    private void switchFontSize(float size) {
        var updatedStyle = from(cellStyle)
                .withFontSize(size)
                .build();
        updateStyle(updatedStyle);
    }

    private void updateFontSize() {
        fontSizePicker.setSelectedItem((int) cellStyle.fontSize());
    }

    private void switchFontColor() {
        if (cellStyle != null) {
            var color = showDialog(null, "Choose text color", cellStyle.fontColor(), true);
            var updatedStyle = from(cellStyle)
                    .withBFontColor(color)
                    .build();
            updateStyle(updatedStyle);
        }
    }

    private void updateFontColor() {
        fontColorTile.setBackground(cellStyle.fontColor());
    }

    private void switchTextAlignment(CellTextAlignment textAlignment) {
        var updatedStyle = from(cellStyle)
                .withTextAlignment(textAlignment)
                .build();
        updateStyle(updatedStyle);
    }

    private void updateTextAlignment() {
        switch (cellStyle.textAlignment()) {
            case LEFT -> {
                switchButton(alignLeftButton, true);
                switchButton(alignCenterButton, false);
                switchButton(alignRightButton, false);
                switchButton(justifyButton, false);
            }
            case CENTER -> {
                switchButton(alignLeftButton, false);
                switchButton(alignCenterButton, true);
                switchButton(alignRightButton, false);
                switchButton(justifyButton, false);
            }
            case RIGHT -> {
                switchButton(alignLeftButton, false);
                switchButton(alignCenterButton, false);
                switchButton(alignRightButton, true);
                switchButton(justifyButton, false);
            }
            case JUSTIFY -> {
                switchButton(alignLeftButton, false);
                switchButton(alignCenterButton, false);
                switchButton(alignRightButton, false);
                switchButton(justifyButton, true);
            }
        }
    }

    private void switchBold() {
        var updatedStyle = from(cellStyle)
                .withBold(!cellStyle.isBold())
                .build();
        updateStyle(updatedStyle);
    }

    private void updateBold() {
        switchButton(boldButton, cellStyle.isBold());
    }

    private void switchItalic() {
        var updatedStyle = from(cellStyle)
                .withItalic(!cellStyle.isItalic())
                .build();
        updateStyle(updatedStyle);
    }

    private void updateItalic() {
        switchButton(italicButton, cellStyle.isItalic());
    }

    private void switchUnderlined() {
        var updatedStyle = from(cellStyle)
                .withUnderlined(!cellStyle.isUnderlined())
                .build();
        updateStyle(updatedStyle);
    }

    private void updateUnderlined() {
        switchButton(underlinedButton, cellStyle.isUnderlined());
    }

    private void switchTextPosition(CellTextPosition textPosition) {
        var updatedStyle = from(cellStyle)
                .withTextPosition(textPosition)
                .build();
        updateStyle(updatedStyle);
    }

    private void updateTextPosition() {
        switch (cellStyle.textPosition()) {
            case TOP -> {
                switchButton(alignTopButton, true);
                switchButton(alignMiddleButton, false);
                switchButton(alignBottomButton, false);
            }
            case MIDDLE -> {
                switchButton(alignTopButton, false);
                switchButton(alignMiddleButton, true);
                switchButton(alignBottomButton, false);
            }
            case BOTTOM -> {
                switchButton(alignTopButton, false);
                switchButton(alignMiddleButton, false);
                switchButton(alignBottomButton, true);
            }
        }
    }

    private void clearFormatting() {
        bus.accept(new Event(CELL_STYLE_UPDATED, cellPosition, null, null));
    }

    private void switchBackgroundColor() {
        if (cellStyle != null) {
            var color = showDialog(null, "Choose cell color", cellStyle.backgroundColor(), true);
            var updatedStyle = from(cellStyle)
                    .withBackgroundColor(color)
                    .build();
            updateStyle(updatedStyle);
        }
    }

    private void updateBackgroundColor() {
        backgroundColorTile.setBackground(cellStyle.backgroundColor());
    }

    private void update(Position cellPosition, CellStyle cellStyle) {
        this.cellPosition = requireNonNull(cellPosition);
        this.cellStyle = requireNonNull(cellStyle);
        updateStyle(this.cellStyle);
    }

    private void updateStyle(CellStyle updatedStyle) {
        this.cellStyle = updatedStyle;
        updateFontSize();
        updateBold();
        updateItalic();
        updateUnderlined();
        updateFontColor();
        updateBackgroundColor();
        updateTextAlignment();
        updateTextPosition();
        bus.accept(new Event(CELL_STYLE_UPDATED, cellPosition, null, updatedStyle));
    }

    private static void switchButton(JButton button, boolean trigger) {
        if (trigger) {
            button.setBackground(LIGHT_COLOR);
        } else {
            button.setBackground(white);
        }
    }

    private static JPanel container() {
        var container = new JPanel();
        container.setLayout(new BoxLayout(container, X_AXIS));
        setConstantSize(container, BUTTON_SIZE * BUTTONS_ROW_N, BUTTON_SIZE);
        return container;
    }

    private static JPanel colorTile(JPanel container, Color color) {
        var panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBorder(createCompoundBorder(
                createLineBorder(white, 4),
                createLineBorder(black, 2)
        ));
        panel.setBackground(color);
        setConstantSize(panel, BUTTON_SIZE, BUTTON_SIZE);
        container.add(panel);
        return panel;
    }

    private JComboBox<Integer> fontSizePicker(JPanel container) {
        var panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBorder(createCompoundBorder(createEmptyBorder(), createEmptyBorder()));

        var picker = new JComboBox<>(new Integer[]{6, 7, 8, 9, 10, 11, 12, 14, 18, 24, 36});
        picker.setToolTipText("Choose font size");
        picker.setBackground(white);
        picker.addActionListener(e -> {
            var newSize = Optional.ofNullable(picker.getSelectedItem())
                    .map(size -> (Integer) size)
                    .map(Integer::floatValue)
                    .orElse(DEFAULT_FONT_SIZE);
            switchFontSize(newSize);
        });

        panel.add(picker);
        container.add(panel);
        return picker;
    }

    private JButton button(JPanel panel, String text, String iconName) {
        return button(panel, text, iconName, () -> {});
    }

    private JButton button(JPanel panel, String text, String iconName, Runnable onClick) {
        var container = iconButtonFactory.buildButton(text, "/toolbar/" + iconName, BUTTON_ICON_SIZE, onClick);
        panel.add(container);
        return (JButton) container.getComponent(0);
    }
}
