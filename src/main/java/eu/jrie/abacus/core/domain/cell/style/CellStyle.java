package eu.jrie.abacus.core.domain.cell.style;

import java.awt.*;

public record CellStyle(
        float fontSize,
        boolean isBold,
        boolean isItalic,
        boolean isUnderlined,
        Color fontColor,
        Color backgroundColor,
        CellTextAlignment textAlignment,
        CellTextPosition textPosition
) {
    public static class Builder {

        private float fontSize;
        private boolean isBold;
        private boolean isItalic;
        private boolean isUnderlined;
        private Color fontColor;
        private Color backgroundColor;
        private CellTextAlignment textAlignment;
        private CellTextPosition textPosition;

        public Builder(
                float fontSize,
                boolean isBold,
                boolean isItalic,
                boolean isUnderlined,
                Color fontColor,
                Color backgroundColor,
                CellTextAlignment textAlignment,
                CellTextPosition textPosition
        ) {
            this.fontSize = fontSize;
            this.isBold = isBold;
            this.isItalic = isItalic;
            this.isUnderlined = isUnderlined;
            this.fontColor = fontColor;
            this.backgroundColor = backgroundColor;
            this.textAlignment = textAlignment;
            this.textPosition = textPosition;
        }

        public static Builder from(CellStyle style) {
            return new Builder(
                    style.fontSize(),
                    style.isBold(),
                    style.isItalic(),
                    style.isUnderlined(),
                    style.fontColor(),
                    style.backgroundColor(),
                    style.textAlignment(),
                    style.textPosition()
            );
        }

        public Builder  withFontSize(float fontSize) {
            this.fontSize = fontSize;
            return this;
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

        public Builder withBFontColor(Color color) {
            this.fontColor = color;
            return this;
        }

        public Builder withBackgroundColor(Color color) {
            this.backgroundColor = color;
            return this;
        }

        public Builder withTextAlignment(CellTextAlignment textAlignment) {
            this.textAlignment = textAlignment;
            return this;
        }

        public Builder withTextPosition(CellTextPosition position) {
            this.textPosition = position;
            return this;
        }

        public CellStyle build() {
            return new CellStyle(
                    fontSize,
                    isBold, isItalic, isUnderlined,
                    fontColor, backgroundColor,
                    textAlignment, textPosition
            );
        }
    }
}
