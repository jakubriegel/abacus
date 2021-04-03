package eu.jrie.abacus.core.domain.cell.style;

import java.awt.*;

public record CellStyle(
        boolean isBold,
        boolean isItalic,
        boolean isUnderlined,
//            Color fontColor,
        Color backgroundColor
//            CellTextAlign textAlign,
//            CellTextPosition textPosition,
//            int fontSize
) {
}
