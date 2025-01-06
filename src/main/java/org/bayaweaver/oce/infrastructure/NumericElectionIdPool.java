package org.bayaweaver.oce.infrastructure;

import org.bayaweaver.oce.domain.model.elections.ElectionId;
import org.bayaweaver.oce.domain.model.elections.ElectionIdPool;

import java.util.concurrent.atomic.AtomicInteger;

public class NumericElectionIdPool implements ElectionIdPool {
    private static final AtomicInteger i = new AtomicInteger(0);

    @Override
    public ElectionId nextId() {
        return new NumericElectionId(i.getAndIncrement());
    }
}
