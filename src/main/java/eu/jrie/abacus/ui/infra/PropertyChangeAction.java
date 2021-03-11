package eu.jrie.abacus.ui.infra;

public class PropertyChangeAction {
    final String propertyName;
    private final Runnable action;

    public PropertyChangeAction(String propertyName, Runnable action) {
        this.propertyName = propertyName;
        this.action = action;
    }

    public void start() {
        action.run();
    }
}
