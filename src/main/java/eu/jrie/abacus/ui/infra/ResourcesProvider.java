package eu.jrie.abacus.ui.infra;

import javax.swing.*;

public class ResourcesProvider {

    private static final String ICONS_PATH = "/graphics/icons/";

    public ImageIcon getIcon(String name) {
        var url = getClass().getResource(ICONS_PATH + name);
        return new ImageIcon(url);
    }
}
