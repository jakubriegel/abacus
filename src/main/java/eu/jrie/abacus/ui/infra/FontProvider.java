package eu.jrie.abacus.ui.infra;

import java.awt.*;
import java.io.File;
import java.io.IOException;

import static java.awt.Font.TRUETYPE_FONT;
import static java.awt.GraphicsEnvironment.getLocalGraphicsEnvironment;

public class FontProvider {

    private static final File FONT_FILE = new File(FontProvider.class.getResource("/JetBrainsMono-Regular.ttf").getFile());
    private static Font font = null;

    private FontProvider() {}

    public static Font standardFont() {
        if (font == null) {
            font = createFont().deriveFont(12f);
            getLocalGraphicsEnvironment().registerFont(font);
        }
        return font;
    }

    private static Font createFont() {
        try {
            return Font.createFont(TRUETYPE_FONT, FONT_FILE);
        } catch (IOException | FontFormatException e) {
            throw new IllegalStateException(e);
        }
    }
}
