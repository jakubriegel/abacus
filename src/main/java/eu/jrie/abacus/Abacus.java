package eu.jrie.abacus;

import static eu.jrie.abacus.ui.domain.AppFrameFactory.buildAppFrame;

public class Abacus {
    public static void main(String[] args) {
        final var frame = buildAppFrame();
        frame.start();
        frame.setVisible();
    }
}
