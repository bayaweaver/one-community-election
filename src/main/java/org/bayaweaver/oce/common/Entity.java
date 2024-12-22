package org.bayaweaver.oce.common;

public abstract class Entity {
    private final int id;

    public Entity(int id) {
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
        Entity other = (Entity) o;
        return id == other.id;
    }

    @Override
    public final int hashCode() {
        return id;
    }
}
