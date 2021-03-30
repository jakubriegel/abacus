package eu.jrie.abacus.ui.domain.components.space.workbench;

import javax.swing.table.DefaultTableModel;
import java.util.Vector;

import static java.util.Collections.singleton;

public class WorkbenchTableModel extends DefaultTableModel {
    public WorkbenchTableModel() {
        super(new Vector<>(singleton("")), 0);
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return column != 0;
    }


}
