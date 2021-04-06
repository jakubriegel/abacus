package eu.jrie.abacus.ui.domain.components.toolbar.editor;

import eu.jrie.abacus.core.domain.cell.Cell;
import eu.jrie.abacus.core.domain.expression.LogicValue;
import eu.jrie.abacus.core.domain.expression.NumberValue;
import eu.jrie.abacus.core.domain.expression.TextValue;
import eu.jrie.abacus.core.infra.Alphabet;
import eu.jrie.abacus.ui.domain.workbench.WorkbenchAccessor;
import eu.jrie.abacus.ui.infra.ResourcesProvider;
import eu.jrie.abacus.ui.infra.event.Event;
import eu.jrie.abacus.ui.infra.event.EventAction;
import eu.jrie.abacus.ui.infra.event.EventBus;

import javax.swing.*;

import static eu.jrie.abacus.ui.domain.Colors.PRIMARY_COLOR;
import static eu.jrie.abacus.ui.infra.event.EventType.CELL_EDITOR_UPDATED;
import static eu.jrie.abacus.ui.infra.event.EventType.CELL_FOCUS;
import static eu.jrie.abacus.ui.infra.event.EventType.CELL_UPDATED;
import static eu.jrie.abacus.ui.infra.helper.ComponentHelper.setConstantSize;
import static eu.jrie.abacus.ui.infra.helper.ListenerHelper.keyReleasedListener;
import static java.awt.Color.white;
import static javax.swing.BorderFactory.createMatteBorder;
import static javax.swing.BoxLayout.X_AXIS;
import static javax.swing.BoxLayout.Y_AXIS;

public class CellEditor extends JTextArea {

    private static final int BORDER_SIZE = 2;
    private static final String PLACEHOLDER_ADDRESS = "--";
    private static final String PLACEHOLDER_TEXT = "Select cell to edit...";

    private final Symbol symbol;
    private final CellAddress address;
    private final CellEditorField cellEditorField;
    private final Alphabet alphabet = new Alphabet();

    private final ImageIcon formulaIcon;
    private final ImageIcon textIcon;
    private final ImageIcon numberIcon;
    private final ImageIcon logicIcon;

    private Cell cell = null;

    public CellEditor(
            ResourcesProvider resourcesProvider,
            Symbol symbol, CellAddress address, CellEditorField cellEditorField,
            EventBus bus,
            WorkbenchAccessor workbench
    ) {
        this.formulaIcon = resourcesProvider.getIcon("editor/round_functions_black_48dp.png");
        this.textIcon = resourcesProvider.getIcon("editor/round_text_fields_black_48dp.png");
        this.numberIcon = resourcesProvider.getIcon("editor/round_looks_one_black_48dp.png");
        this.logicIcon = resourcesProvider.getIcon("editor/outline_toll_black_48dp.png");
        this.symbol = symbol;
        this.address = address;
        this.cellEditorField = cellEditorField;

        EventAction updateEditor = event -> {
            var cell = workbench.getCell(event.position());
            setCellValue(cell, event.text());
        };
        bus.register("updateCellEditorOnFocus", CELL_FOCUS, updateEditor);
        bus.register("updateCellEditorOnUpdate", CELL_UPDATED, updateEditor);

        cellEditorField.addKeyListener(keyReleasedListener(event -> {
            if (cell != null) {
                var editedText = cellEditorField.getText();
                bus.accept(new Event(CELL_EDITOR_UPDATED, cell.getPosition(), editedText, null));
            }
        }));

        init();
    }

    private void init() {
        setBackground(white);
        setLayout(new BoxLayout(this, X_AXIS));

        var panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, Y_AXIS));
        setConstantSize(panel, 25, 50);
        panel.setBorder(createMatteBorder(0, BORDER_SIZE, 0, 0, PRIMARY_COLOR));
        panel.setBackground(white);
        panel.add(symbol);
        panel.add(address);

        add(panel);
        add(cellEditorField);

        setDefaultValue();
    }

    private void setCellValue(Cell cell, String newText) {
        this.cell = cell;
        setCellValue(newText);
    }

    private void setCellValue(String newText) {
        if (cell != null) {
            updateSymbol();
            updateAddress();
            updateEditor(newText);
        }
    }

    private void updateSymbol() {
        if (cell.hasFormula()) {
            symbol.set(formulaIcon);
        } else if (cell.getValue() instanceof NumberValue) {
            symbol.set(numberIcon);
        } else if (cell.getValue() instanceof TextValue) {
            symbol.set(textIcon);
        } else if (cell.getValue() instanceof LogicValue) {
            symbol.set(logicIcon);
        } else {
            symbol.set(null);
        }
    }

    private void updateAddress() {
        var column = alphabet.getLiteral(cell.getPosition().x());
        var row = cell.getPosition().y() + 1;
        address.set(column + row);
    }

    private void updateEditor(String newText) {
        cellEditorField.set(newText);
    }

    private void setDefaultValue() {
        symbol.set(null);
        address.set(PLACEHOLDER_ADDRESS);
        cellEditorField.set(PLACEHOLDER_TEXT);
    }

}
