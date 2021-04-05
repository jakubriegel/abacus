package eu.jrie.abacus.ui.domain.components.space.workbench;

import eu.jrie.abacus.core.domain.cell.Position;
import eu.jrie.abacus.core.domain.cell.style.CellStyle;
import eu.jrie.abacus.core.domain.cell.style.CellStyleProvider;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.font.TextAttribute;
import java.util.Map;

import static java.awt.Font.BOLD;
import static java.awt.Font.ITALIC;
import static java.awt.Font.PLAIN;
import static java.awt.font.TextAttribute.UNDERLINE;
import static java.awt.font.TextAttribute.UNDERLINE_ON;

public class CellStyleRenderer extends DefaultTableCellRenderer {

    private final CellStyleProvider styleProvider;

    public CellStyleRenderer(CellStyleProvider styleProvider) {
        this.styleProvider = styleProvider;
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        var cell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        applyStyle(cell, row, column);
        return cell;
    }

    private CellStyle getStyle(int row, int column) {
        var position = new Position(column - 1, row);
        return styleProvider.getStyle(position);
    }

    private void applyStyle(Component cell, int row, int column) {
        var style = getStyle(row, column);
        applyStyle(cell, style);

    }

    private static void applyStyle(Component cell, CellStyle style) {
        applyBoldAndItalic(cell, style);
        applyUnderline(cell, style);
        applyBackground(cell, style);
        applyPosition(cell, style);
        applyAlign(cell, style);
        applyFontSize(cell, style);
        applyFontColor(cell, style);
    }

    private static void applyBoldAndItalic(Component cell, CellStyle style) {
        var fontStyle = PLAIN + (style.isBold() ? BOLD : 0) + (style.isItalic() ? ITALIC : 0);
        cell.setFont(cell.getFont().deriveFont(fontStyle));
    }

    private static void applyUnderline(Component cell, CellStyle style) {
        var font = cell.getFont();
        @SuppressWarnings("unchecked")
        var attributes = (Map<TextAttribute, Object>) font.getAttributes();
        if (style.isUnderlined()) {
            attributes.put(UNDERLINE, UNDERLINE_ON);
        } else {
            attributes.remove(UNDERLINE);
        }
        cell.setFont(font.deriveFont(attributes));
    }

    private static void applyBackground(Component cell, CellStyle style) {
        cell.setBackground(style.backgroundColor());
    }

    private static void applyPosition(Component cell, CellStyle style) {
        var position = switch (style.textPosition()) {
            case TOP -> DefaultTableCellRenderer.TOP;
            case MIDDLE -> DefaultTableCellRenderer.CENTER;
            case BOTTOM -> DefaultTableCellRenderer.BOTTOM;
        };
        ((DefaultTableCellRenderer) cell).setVerticalAlignment(position);
    }

    private static void applyAlign(Component cell, CellStyle style) {
        var align = switch (style.textAlignment()) {
            case LEFT -> DefaultTableCellRenderer.LEFT;
            case CENTER -> DefaultTableCellRenderer.CENTER;
            case RIGHT -> DefaultTableCellRenderer.RIGHT;
            case JUSTIFY -> DefaultTableCellRenderer.LEADING;
        };
        ((DefaultTableCellRenderer) cell).setHorizontalAlignment(align);
    }

    private static void applyFontSize(Component cell, CellStyle style) {
        var font = cell.getFont();
        cell.setFont(font.deriveFont(style.fontSize()));
    }

    private static void applyFontColor(Component cell, CellStyle style) {
        cell.setForeground(style.fontColor());
    }
}
