package org.bayaweaver.oce.domain.model.common;

public abstract class Entity<T extends Identifier> {
    protected final T id;

    public Entity(T id) {
        if (id == null) {
            throw new IllegalArgumentException("Identifier must be provided.");
        }
        this.id = id;
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Entity<?> other = (Entity<?>) o;
        return id.equals(other.id);
    }

    @Override
    public final int hashCode() {
        return id.hashCode();
    }
}
