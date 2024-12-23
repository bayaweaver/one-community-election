package org.bayaweaver.oce.domain.model;

import org.bayaweaver.oce.domain.model.common.Identifier;

public final class CommunityElectionsId implements Identifier {
    static final CommunityElectionsId SINGLE_VALUE = new CommunityElectionsId();

    private CommunityElectionsId() {}

    @Override
    public boolean equals(Object o) {
        return o == this;
    }

    @Override
    public int hashCode() {
        return 0;
    }
}
