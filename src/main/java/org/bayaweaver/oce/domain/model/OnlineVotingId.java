package org.bayaweaver.oce.domain.model;

import org.bayaweaver.oce.domain.model.common.Identifier;

public final class OnlineVotingId implements Identifier {
    private final ElectionId value;

    OnlineVotingId(ElectionId value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        OnlineVotingId that = (OnlineVotingId) o;
        return value.equals(that.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
