package org.bayaweaver.oce.domain.model;

import java.time.Year;

public class InMemoryCommunityElectionsRepository implements CommunityElectionsRepository {
    private final CommunityElections instance = new CommunityElections(Year.now());

    @Override
    public CommunityElections get() {
        return instance;
    }
}
