package eu.jrie.abacus.core.domain.expression;

sealed public interface Value<T> extends Expression permits NumberValue, TextValue {
    T get();
    default String getAsString() {
        return String.valueOf(get());
    }
}
