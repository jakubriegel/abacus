package eu.jrie.abacus;

import static eu.jrie.abacus.Logging.configureLogging;
import static eu.jrie.abacus.ui.domain.AppFrameFactory.buildAppFrame;

public class Abacus {
    public static void main(String[] args) {
        configureLogging();
        final var frame = buildAppFrame();
        frame.start();
        frame.setVisible();
    }
}
