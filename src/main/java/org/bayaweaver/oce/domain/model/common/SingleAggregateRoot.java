package org.bayaweaver.oce.domain.model.common;

public abstract class SingleAggregateRoot extends Entity<SingleAggregateRoot.Id> {

    public SingleAggregateRoot() {
        super(Id.SINGLE_VALUE);
    }

    public static final class Id implements Identifier {
        public static final Id SINGLE_VALUE = new Id();

        private Id() {}
    }
}
