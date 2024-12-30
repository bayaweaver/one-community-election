package org.bayaweaver.oce.application;

import org.bayaweaver.oce.domain.model.CommunityElections;
import org.bayaweaver.oce.domain.model.CongregationId;
import org.bayaweaver.oce.domain.model.ElectionId;
import org.bayaweaver.oce.domain.model.ElectionIdPool;
import org.bayaweaver.oce.domain.model.MemberId;
import org.bayaweaver.oce.domain.model.common.DomainRuleViolationException;

import java.time.Clock;
import java.util.Set;

public class Service {
    private final Clock clock;
    private final ElectionIdPool electionIdPool;
    private final CommunityElections communityElections;

    public Service(Clock clock, ElectionIdPool electionIdPool) {
        this.clock = clock;
        this.electionIdPool = electionIdPool;
        this.communityElections = new CommunityElections();
    }

    public ElectionId initiateElection(CongregationId initiator)
            throws DomainRuleViolationException {

        ElectionId id = electionIdPool.nextId();
        communityElections.initiateElection(id, initiator, clock);
        return id;
    }

    public void registerMember(MemberId id, int age, CongregationId homeCongregation) {
        communityElections.registerMember(id, age, homeCongregation);
    }

    public void establishCongregation(CongregationId id) {
        communityElections.establishCongregation(id);
    }

    public void dissolveCongregation(CongregationId id) {
        communityElections.dissolveCongregation(id);
    }

    public void completeElection(ElectionId id, Set<MemberId> potentialCouncilMembers)
            throws DomainRuleViolationException {

        CommunityElections.Election election = findElection(id);
        election.complete(potentialCouncilMembers);
    }

    public void voteIn(ElectionId electionId, MemberId voterId, Set<MemberId> votes)
            throws DomainRuleViolationException {

        CommunityElections.Election election = findElection(electionId);
        election.onlineVoting().vote(voterId, votes);
    }

    private CommunityElections.Election findElection(ElectionId id) {
        return communityElections
                .election(id)
                .orElseThrow(() -> new ApplicationException("Election '" + id + "' was not found."));
    }
}
