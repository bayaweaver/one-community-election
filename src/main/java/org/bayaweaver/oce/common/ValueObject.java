package org.bayaweaver.oce.common;

public interface ValueObject {

    @Override
    boolean equals(Object o);
    @Override
    int hashCode();
}
