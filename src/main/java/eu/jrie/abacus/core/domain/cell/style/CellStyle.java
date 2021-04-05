package eu.jrie.abacus.core.domain.cell.style;

import java.awt.*;

public record CellStyle(
        boolean isBold,
        boolean isItalic,
        boolean isUnderlined,
//            Color fontColor,
        Color backgroundColor,
//            CellTextAlign textAlign,
        CellTextPosition textPosition
//            int fontSize
) {
    public static class Builder {

        private boolean isBold;
        private boolean isItalic;
        private boolean isUnderlined;
        private Color backgroundColor;
        private CellTextPosition textPosition;

        private Builder(boolean isBold, boolean isItalic, boolean isUnderlined, Color backgroundColor, CellTextPosition cellTextPosition) {
            this.isBold = isBold;
            this.isItalic = isItalic;
            this.isUnderlined = isUnderlined;
            this.backgroundColor = backgroundColor;
            this.textPosition = cellTextPosition;
        }

        public static Builder from(CellStyle style) {
            return new Builder(
                    style.isBold(),
                    style.isItalic(),
                    style.isUnderlined(),
                    style.backgroundColor(),
                    style.textPosition()
            );
        }

        public Builder withBold(boolean isBold) {
            this.isBold = isBold;
            return this;
        }

        public Builder withItalic(boolean isItalic) {
            this.isItalic = isItalic;
            return this;
        }

        public Builder withUnderlined(boolean isUnderlined) {
            this.isUnderlined = isUnderlined;
            return this;
        }

        public Builder withTextPosition(CellTextPosition position) {
            this.textPosition = position;
            return this;
        }

        public CellStyle build() {
            return new CellStyle(isBold, isItalic, isUnderlined, backgroundColor, textPosition);
        }
    }
}
