package eu.jrie.abacus.ui.infra;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Set;

public class WorkbenchTablePropertyChangeListener implements PropertyChangeListener {

    private final Set<PropertyChangeAction> actions;

    public WorkbenchTablePropertyChangeListener(Set<PropertyChangeAction> actions) {
        this.actions = actions;
    }

    @Override
    public void propertyChange(PropertyChangeEvent event) {
        System.out.println("propertyChange start name=" + event.getPropertyName());
        actions.stream()
                .filter(action -> action.propertyName.equals(event.getPropertyName()))
                .forEach(PropertyChangeAction::start);
    }
}
