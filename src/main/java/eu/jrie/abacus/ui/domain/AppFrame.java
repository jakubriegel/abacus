package eu.jrie.abacus.ui.domain;

import eu.jrie.abacus.ui.domain.components.space.Space;
import eu.jrie.abacus.ui.domain.components.toolbar.Toolbar;
import eu.jrie.abacus.ui.infra.ResourcesProvider;

import javax.swing.*;
import java.awt.*;

import static javax.swing.BoxLayout.Y_AXIS;

public class AppFrame extends JFrame {

    private final ResourcesProvider resourcesProvider;

    private final Toolbar toolbar;
    private final Space space;

    AppFrame(ResourcesProvider resourcesProvider, Toolbar toolbar, Space space) {
        this.resourcesProvider = resourcesProvider;
        this.toolbar = toolbar;
        this.space = space;
    }

    public void start() {
        setSize(1200, 800);
        setMinimumSize(new Dimension(600, 120));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BoxLayout(getContentPane(), Y_AXIS));

        setTitle("untitled.abcsv - Abacus");
        setIconImage(resourcesProvider.getIcon("abacus.png").getImage());

        add(toolbar);
        add(space);
    }

    public void setVisible() {
        setVisible(true);
    }
}
