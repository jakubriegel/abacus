package eu.jrie.abacus.core;

import java.util.HashMap;
import java.util.Map;

public class Workbench {
    private final Map<Position, Cell> cells = new HashMap<>();

    public Workbench() {
        cells.put(new Position(3, 5), new Cell(new Position(3, 5), "add(a, b)", "test"));
    }

    public String getTextAt(Position position) {
        return getCellAt(position).getText();
    }

    public void setTextAt(Position position, String text) {
        var cell = getCellAt(position);
        cell.setText(text);
        updateCell(cell);
    }

    private void updateCell(Cell cell) {
        cell.setValue(cell.getText().toUpperCase());
    }

    public String getValueAt(Position position) {
        return getCellAt(position).getValue();
    }

    private Cell getCellAt(Position position) {
        return cells.computeIfAbsent(position, Cell::new);
    }

}
