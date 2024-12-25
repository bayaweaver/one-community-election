package org.bayaweaver.oce.domain.model;

final class NumericMemberId implements MemberId {
    private final int value;

    NumericMemberId(int value) {
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
        NumericMemberId that = (NumericMemberId) o;
        return value == that.value;
    }

    @Override
    public int hashCode() {
        return value;
    }
}
