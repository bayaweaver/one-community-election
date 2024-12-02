package org.bayaweaver.oce.administration.domain.model;

public interface ValueObject {

    @Override
    boolean equals(Object o);
    @Override
    int hashCode();
}
