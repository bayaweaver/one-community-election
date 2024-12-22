package org.bayaweaver.oce.domain.model.common;

public interface ValueObject {

    @Override
    boolean equals(Object o);
    @Override
    int hashCode();
}
