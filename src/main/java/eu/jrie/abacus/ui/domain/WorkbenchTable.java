package eu.jrie.abacus.ui.domain;

import eu.jrie.abacus.core.domain.Workbench;
import eu.jrie.abacus.core.domain.cell.Position;
import eu.jrie.abacus.core.infra.Alphabet;
import eu.jrie.abacus.ui.infra.PropertyChangeAction;
import eu.jrie.abacus.ui.infra.WorkbenchTablePropertyChangeListener;

import javax.swing.*;
import java.util.HashSet;
import java.util.Vector;

import static java.util.stream.Collectors.toList;
import static java.util.stream.IntStream.range;

class WorkbenchTable extends JTable {

    private final Alphabet alphabet = new Alphabet();
    private final WorkbenchTableModel model = new WorkbenchTableModel();

    public final Workbench workbench;

    WorkbenchTable(Workbench workbench) {
        super();
        this.workbench = workbench;

        setModel(model);
        setBounds(0, 0, 300, 400);
        setColumnWidths();
        setAutoResizeMode(AUTO_RESIZE_OFF);
        getTableHeader().setReorderingAllowed(false);

        setCellSelectionEnabled(true);
        var propertyChangeActions = new HashSet<PropertyChangeAction>();
        propertyChangeActions.add(new PropertyChangeAction("tableCellEditor", () -> {
            if (isEditing()) {
                var editor = (JTextField) getEditorComponent();
                editor.setText(workbench.getTextAt(getSelected()));
            } else {
                var selected = getSelected();
                var editedText = (String) model.getValueAt(selected.y(), selected.x());
                workbench.setTextAt(selected, editedText);
                var t = workbench.getValueAt(selected);
                model.setValueAt(t, selected.y(), selected.x());
            }
        }));
        addPropertyChangeListener(new WorkbenchTablePropertyChangeListener(propertyChangeActions));
    }

    void newRow() {
        final int maxX = model.getColumnCount();
        final int y = model.getRowCount();

        var cellsValues = range(1, maxX)
                .mapToObj(x -> new Position(x, y))
                .map(workbench::getValueAt)
                .collect(toList());

        var allCells = new Vector<>(cellsValues.size()+1);
        allCells.add(String.valueOf(y+1));
        allCells.addAll(cellsValues);
        model.addRow(allCells);
    }

    void newColumn() {
        model.addColumn(alphabet.next());
        setColumnWidths();
    }

    void setColumnWidths() {
        columnModel.getColumn(0).setMaxWidth(35);
        range(1, getColumnCount())
                .mapToObj(i -> columnModel.getColumn(i))
                .forEach(column -> {
                    column.setMinWidth(75);
                    column.setMaxWidth(75);
                });
    }

    private Position getSelected() {
        final int y = getSelectedRow();
        final int x = getSelectedColumn();
        return new Position(x, y);
    }
}
