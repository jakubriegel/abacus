package eu.jrie.abacus.core.domain;

public class Cell {

    private final Position position;
    private String text;
    private String value;

    public Cell(Position position) {
        this.position = position;
        this.value = null;
    }

    public Cell(Position position, String text, String value) {
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

    public String getValue() {
        return value;
    }

    void setValue(String value) {
        this.value = value;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
