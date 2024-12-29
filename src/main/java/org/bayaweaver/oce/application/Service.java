package org.bayaweaver.oce.application;

import org.bayaweaver.oce.domain.model.CongregationId;
import org.bayaweaver.oce.domain.model.ElectionId;
import org.bayaweaver.oce.domain.model.MemberId;
import org.bayaweaver.oce.domain.model.common.DomainRuleViolationException;

import java.util.Set;

public class Service {

    public ElectionId initiateElection(CongregationId initiator)
            throws DomainRuleViolationException { /* TODO */ return null; }
    public void registerMember(MemberId id, int age, CongregationId homeCongregation) { /* TODO */ }
    public void establishCongregation(CongregationId id) { /* TODO */ }
    public void dissolveCongregation(CongregationId id) { /* TODO */ }
    public void completeElection(ElectionId electionId, Set<MemberId> potentialCouncilMembers)
            throws DomainRuleViolationException { /* TODO */ }
    public void voteIn(ElectionId electionId, MemberId voterId, Set<MemberId> voteIds)
            throws DomainRuleViolationException { /* TODO */ }
}
