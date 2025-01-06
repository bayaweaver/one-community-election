package org.bayaweaver.oce.infrastructure;

import org.bayaweaver.oce.domain.model.elections.ElectionId;

public final class NumericElectionId implements ElectionId {
    private final int value;

    NumericElectionId(int value) {
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
        NumericElectionId that = (NumericElectionId) o;
        return value == that.value;
    }

    @Override
    public int hashCode() {
        return value;
    }
}
