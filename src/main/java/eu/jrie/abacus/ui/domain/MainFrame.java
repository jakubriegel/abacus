package eu.jrie.abacus.ui.domain;

import eu.jrie.abacus.core.domain.workbench.Workbench;

import javax.swing.*;

public class MainFrame extends JFrame {

    private final Workbench  workbench = new Workbench();

    private final WorkbenchTable workbenchTable = new WorkbenchTable(workbench);
    private final WorkbenchScroll workbenchScroll = new WorkbenchScroll(workbenchTable);

    public void start() {
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        add(workbenchScroll);

        setVisible(true);
    }
}
