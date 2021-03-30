package eu.jrie.abacus.ui.infra.event;

import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

import static java.util.logging.Logger.getLogger;

public class EventBus {

    private static final Logger logger = getLogger(EventBus.class.getSimpleName());

    private final Set<EventHandler> handlers;

    public EventBus() {
        this(new HashSet<>());
    }

    EventBus(Set<EventHandler> handlers) {
        this.handlers = handlers;
    }

    public void accept(Event event) {
        logger.info("Event: " + event);
        handlers.stream()
                .filter(handler -> handler.eventType().equals(event.type()))
                .forEach(handler -> handler.accept(event));
    }

    public void register(String handlerName, EventType eventType, EventAction action) {
        var handler = new EventHandler(handlerName, eventType, action);
        register(handler);
    }

    private void register(EventHandler handler) {
        handlers.add(handler);
    }
}
