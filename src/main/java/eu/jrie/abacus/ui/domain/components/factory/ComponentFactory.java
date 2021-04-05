package eu.jrie.abacus.ui.domain.components.factory;

import eu.jrie.abacus.ui.domain.components.IconButton;
import eu.jrie.abacus.ui.infra.ResourcesProvider;

import javax.swing.*;
import java.awt.*;

import static java.awt.Image.SCALE_SMOOTH;
import static javax.swing.BorderFactory.createCompoundBorder;
import static javax.swing.BorderFactory.createEmptyBorder;

public class ComponentFactory implements IconButtonFactory {

    private final ResourcesProvider resourcesProvider = new ResourcesProvider();

    @Override
    public JComponent buildButton(String text, String iconPath, int iconSize, Runnable onClick) {
        var panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBorder(createCompoundBorder(createEmptyBorder(), createEmptyBorder()));

        var button = new IconButton(text, buildIcon(iconPath, iconSize));
        button.addActionListener(e -> onClick.run());

        panel.add(button);
        return panel;
    }

    private ImageIcon buildIcon(String path, int size) {
        var img = resourcesProvider.getIcon(path)
                .getImage()
                .getScaledInstance(size, size, SCALE_SMOOTH);
        return new ImageIcon(img);
    }
}
