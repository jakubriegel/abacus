package eu.jrie.abacus.ui.infra.event;

record EventHandler(
        String name,
        EventType eventType,
        EventAction action
) {
    public void accept(Event event) {
        action.accept(event);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        var that = (EventHandler) o;
        return name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
