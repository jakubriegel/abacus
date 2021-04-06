package eu.jrie.abacus.core.domain.cell;

import eu.jrie.abacus.core.domain.expression.Formula;
import eu.jrie.abacus.core.domain.expression.Value;

import java.util.Optional;

public class Cell {

    private final Position position;
    private Formula formula;
    private String text;
    private Value value;

    public Cell(Position position) {
        this.position = position;
        this.value = null;
    }

    public Cell(Position position, Formula formula, String text, Value value) {
        this.position = position;
        this.formula = formula;
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

    public Position getPosition() {
        return position;
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

    public Formula getFormula() {
        return formula;
    }

    public boolean hasFormula() {
        return formula != null;
    }

    public void setFormula(Formula formula) {
        this.formula = formula;
    }

    @Override
    public String toString() {
        return "Cell{" +
                "position=" + position +
                ", formula=" + formula +
                ", text='" + text + '\'' +
                ", value=" + value +
                '}';
    }
}
