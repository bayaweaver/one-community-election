package org.bayaweaver.oce.domain.model.common;

public class AggregateRoot<T extends Identifier> extends Entity<T> {

    public AggregateRoot(T id) {
        super(id);
    }
}
