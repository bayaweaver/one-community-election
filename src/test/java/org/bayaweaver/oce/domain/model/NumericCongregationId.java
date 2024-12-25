package org.bayaweaver.oce.domain.model;

final class NumericCongregationId implements CongregationId {
    private final int value;

    NumericCongregationId(int value) {
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
        NumericCongregationId that = (NumericCongregationId) o;
        return value == that.value;
    }

    @Override
    public int hashCode() {
        return value;
    }
}
