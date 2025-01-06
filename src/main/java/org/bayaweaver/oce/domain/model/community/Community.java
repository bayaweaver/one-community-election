package org.bayaweaver.oce.domain.model.community;

import org.bayaweaver.oce.domain.model.common.AggregateRoot;
import org.bayaweaver.oce.domain.model.common.Identifier;
import org.bayaweaver.oce.domain.model.common.SingleAggregateRoot;

public class Community extends AggregateRoot<SingleAggregateRoot.Id> {

    public Community(Identifier id) {
        super(SingleAggregateRoot.Id.SINGLE_VALUE);
    }
}
