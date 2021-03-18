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
                var cellText = workbench.getTextAt(getSelected());
                editor.setText(cellText);
            } else {
                var selected = getSelected();
                var editedText = getValue(selected);
                workbench.setTextAt(selected, editedText);
                var updatedValue = workbench.getValueAt(selected);
                setValue(updatedValue, selected);
            }
        }));
        addPropertyChangeListener(new WorkbenchTablePropertyChangeListener(propertyChangeActions));
    }

    void newRow() {
        final int maxX = model.getColumnCount();
        final int y = model.getRowCount();

        var cellsValues = range(0, maxX)
                .mapToObj(x -> new Position(x, y))
                .map(workbench::getValueAt)
                .collect(toList());

        var allCells = new Vector<>(cellsValues.size()+1);
        allCells.add(String.valueOf(y+1));
        allCells.addAll(cellsValues);
        model.addRow(allCells);
    }

    void newColumn() {
        final int x = model.getColumnCount() - 1;
        model.addColumn(alphabet.getLiteral(x));
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
        final int x = getSelectedColumn() - 1;
        return new Position(x, y);
    }

    private String getValue(Position position) {
        return (String) model.getValueAt(position.y(), position.x()+1);
    }

    private void setValue(String value, Position position) {
        model.setValueAt(value, position.y(), position.x()+1);
    }
}
