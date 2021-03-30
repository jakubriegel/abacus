package eu.jrie.abacus.ui.infra.event;

import java.util.function.Consumer;

@FunctionalInterface
public interface EventAction extends Consumer<Event> {
    @Override
    void accept(Event event);
}
