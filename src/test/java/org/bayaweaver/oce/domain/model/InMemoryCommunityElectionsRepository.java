package org.bayaweaver.oce.domain.model;

public class InMemoryCommunityElectionsRepository implements CommunityElectionsRepository {
    private final CommunityElections instance = new CommunityElections();

    @Override
    public CommunityElections get() {
        return instance;
    }
}
