package eu.jrie.abacus.ui.domain.components.space.workbench;

import eu.jrie.abacus.core.domain.cell.Position;
import eu.jrie.abacus.core.infra.Alphabet;
import eu.jrie.abacus.ui.domain.workbench.WorkbenchAccessor;
import eu.jrie.abacus.ui.infra.event.Event;
import eu.jrie.abacus.ui.infra.event.EventBus;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.util.Set;
import java.util.Vector;
import java.util.logging.Logger;

import static eu.jrie.abacus.ui.domain.Colors.PRIMARY_COLOR;
import static eu.jrie.abacus.ui.infra.FontProvider.standardFont;
import static eu.jrie.abacus.ui.infra.event.EventType.CELL_EDITOR_UPDATED;
import static eu.jrie.abacus.ui.infra.event.EventType.CELL_FOCUS;
import static eu.jrie.abacus.ui.infra.event.EventType.CELL_STYLE_CHANGED;
import static eu.jrie.abacus.ui.infra.event.EventType.CELL_UPDATED;
import static eu.jrie.abacus.ui.infra.event.EventType.CELL_VALUE_CHANGED;
import static eu.jrie.abacus.ui.infra.helper.ListenerHelper.keyReleasedListener;
import static eu.jrie.abacus.ui.infra.helper.ListenerHelper.mouseClickedEvent;
import static eu.jrie.abacus.ui.infra.helper.ListenerHelper.propertyChangeListener;
import static java.util.logging.Logger.getLogger;
import static java.util.stream.Collectors.toList;
import static java.util.stream.IntStream.range;
import static javax.swing.SwingConstants.CENTER;

public class WorkbenchTable extends JTable {

    private static final Logger logger = getLogger(WorkbenchTable.class.getSimpleName());

    private static final Set<Integer> NAVIGATION_KEYS = Set.of(10, 37, 38, 39, 40);

    private final Alphabet alphabet = new Alphabet();

    private final WorkbenchAccessor workbench;
    private final WorkbenchTableModel model;
    private final EventBus bus;
    private final CellStyleRenderer renderer;

    public WorkbenchTable(WorkbenchAccessor workbench, WorkbenchTableModel model, EventBus bus, CellStyleRenderer renderer) {
        this.workbench = workbench;
        this.model = model;
        this.bus = bus;
        this.renderer = renderer;

        setFont(standardFont());
        setColumnWidths();
        setAutoResizeMode(AUTO_RESIZE_OFF);
        var header = getTableHeader();
        header.setReorderingAllowed(false);
        header.setResizingAllowed(true);

        setModel(model);
        setCellSelectionEnabled(true);
        setRowHeight(22);

        bus.register("workbenchTableCellValueUpdate", CELL_VALUE_CHANGED, event -> updateContent());
        bus.register("workbenchTableEditorUpdate", CELL_EDITOR_UPDATED, event -> {
            if (!isEditing()) {
                editCellAt(event.position());
            }
            var editor = getEditorField();
            editor.setText(event.text());
            sendUpdatedEvent(event.position(), event.text());
        });
        bus.register("drawCellStyle", CELL_STYLE_CHANGED, event -> repaint());

        addPropertyChangeListener(propertyChangeListener("tableCellEditor", event -> {
            var selected = getSelected();
            if (isEditing()) {
                var editor = getEditorField();
                var cellText = workbench.getText(selected);
                editor.setText(cellText);
                var editedText = editor.getText();
                sendUpdatedEvent(selected, editedText);
            } else {
                var editedText = getValue(selected);
                sendUpdatedEvent(selected, editedText);
            }
        }));
        addKeyListener(keyReleasedListener(event -> {
            var selected = getSelected();
            if (NAVIGATION_KEYS.contains(event.getKeyCode())) {
                sendFocusEvent(selected, workbench.getText(selected));
            } else {
                editCellAt(selected);
            }
        }));
        addMouseListener(mouseClickedEvent(event -> {
            var selected = getSelected();
            sendFocusEvent(selected, workbench.getText(selected));
        }));
    }

    private void updateContent() {
        repaint();
        for (int x = 1; x < getColumnCount(); x++) {
            for (int y = 0; y < getRowCount(); y++) {
                var position = new Position(x - 1, y);
                var value = workbench.getValueAsString(position);
                setValue(value, position);
            }
        }
    }

    private void editCellAt(Position position) {
        editCellAt(position.y(), position.x() + 1);
    }

    private JTextField getEditorField() {
        final var editor = (JTextField) getEditorComponent();
        editor.setBorder(BorderFactory.createLineBorder(PRIMARY_COLOR, 2));
        if (editor.getKeyListeners().length == 0) {
            editor.addKeyListener(keyReleasedListener(event -> {
                logger.info("ABC");
                var selected = getSelected();
                var editedText = ((JTextField) event.getSource()).getText();
                sendFocusEvent(selected, editedText);
            }));
        }
        return editor;
    }

    private void sendFocusEvent(Position position, String text) {
        var event = new Event(CELL_FOCUS, position, text, workbench.getCellStyle(position));
        bus.accept(event);
    }

    private void sendUpdatedEvent(Position position, String text) {
        var event = new Event(CELL_UPDATED, position, text, workbench.getCellStyle(position));
        bus.accept(event);
    }

    void newRow() {
        final int maxX = model.getColumnCount();
        final int y = model.getRowCount();

        var cellsValues = range(0, maxX)
                .mapToObj(x -> new Position(x, y))
                .map(workbench::getValueAsString)
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

    private void setColumnWidths() {
        if (columnModel.getColumnCount() > 0) {
            var rowAxisColumn = columnModel.getColumn(0);
            rowAxisColumn.setWidth(35);
            rowAxisColumn.setMinWidth(35);
            rowAxisColumn.setMaxWidth(35);
            var centerRenderer = new DefaultTableCellRenderer();
            centerRenderer.setHorizontalAlignment(CENTER);
            rowAxisColumn.setCellRenderer(centerRenderer);

            range(1, getColumnCount())
                    .mapToObj(i -> columnModel.getColumn(i))
                    .forEach(column -> {
                        column.setCellRenderer(renderer);
                        column.setMinWidth(15);
                        column.setWidth(85);
                    });
        }
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
