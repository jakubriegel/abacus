package eu.jrie.abacus.core.domain.cell;

import eu.jrie.abacus.core.domain.expression.Value;

import java.util.Optional;

public class Cell {

    private final Position position;
    private String text;
    private Value value;

    public Cell(Position position) {
        this.position = position;
        this.value = null;
    }

    public Cell(Position position, String text, Value value) {
        this.position = position;
        this.text = text;
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cell cell = (Cell) o;
        return position.equals(cell.position);
    }

    @Override
    public int hashCode() {
        return position.hashCode();
    }

    public String getValueAsString() {
        return Optional.ofNullable(value)
                .map(Value::getAsString)
                .orElse("");
    }

    public Value getValue() {
        return value;
    }

    public void setValue(Value value) {
        this.value = value;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
