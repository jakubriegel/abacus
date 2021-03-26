package eu.jrie.abacus.ui.domain.components.space;

import eu.jrie.abacus.ui.domain.components.space.workbench.WorkbenchScroll;

import javax.swing.*;

import static java.awt.Color.white;
import static javax.swing.BoxLayout.X_AXIS;

public class Space extends JPanel {

    public Space(UtilsMenu utilsMenu, WorkbenchScroll workbenchScroll) {
        setBackground(white);
        setLayout(new BoxLayout(this, X_AXIS));

        add(utilsMenu);
        add(workbenchScroll);
    }
}
