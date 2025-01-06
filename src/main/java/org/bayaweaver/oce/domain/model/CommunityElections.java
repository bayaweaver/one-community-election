package org.bayaweaver.oce.domain.model;

import org.bayaweaver.oce.domain.model.common.DomainRuleViolationException;
import org.bayaweaver.oce.domain.model.common.Entity;
import org.bayaweaver.oce.domain.model.common.SingleAggregateRoot;

import java.time.Year;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Deprecated
public class CommunityElections extends SingleAggregateRoot {
    private final Map<CongregationId, Election> elections;
    private final Set<Member> members;
    private final Map<CongregationId, Congregation> congregations;
    private Year currentYear;

    CommunityElections(Year currentYear) {
        this.currentYear = currentYear;
        this.elections = new HashMap<>();
        this.members = new HashSet<>();
        this.congregations = new HashMap<>();
    }
}
