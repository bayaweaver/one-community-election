package org.bayaweaver.oce.domain.model.elections;

import org.bayaweaver.oce.domain.model.common.AggregateRoot;
import org.bayaweaver.oce.domain.model.common.Identifier;
import org.bayaweaver.oce.domain.model.common.SingleAggregateRoot;

public class Elections extends AggregateRoot<SingleAggregateRoot.Id> {

    public Elections(Identifier id) {
        super(SingleAggregateRoot.Id.SINGLE_VALUE);
    }
}
