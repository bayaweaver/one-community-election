package org.bayaweaver.oce.application;

import org.bayaweaver.oce.domain.model.CommunityElections;
import org.bayaweaver.oce.domain.model.CongregationId;
import org.bayaweaver.oce.domain.model.ElectionId;
import org.bayaweaver.oce.domain.model.ElectionIdPool;
import org.bayaweaver.oce.domain.model.MemberId;
import org.bayaweaver.oce.domain.model.common.DomainRuleViolationException;

import javax.transaction.Transactional;
import java.time.Clock;
import java.util.Collections;
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

    @Transactional
    public ElectionId initiateElection(CongregationId initiator)
            throws DomainRuleViolationException {

        if (initiator == null) {
            throw new ApplicationException("Идентификатор общины не указан.");
        }
        ElectionId id = electionIdPool.nextId();
        communityElections.initiateElection(id, initiator, clock);
        return id;
    }

    @Transactional
    public void registerMember(MemberId id, int age, CongregationId homeCongregation) {
        if (id == null) {
            throw new ApplicationException("Идентификатор члена общины не указан.");
        }
        if (homeCongregation == null) {
            throw new ApplicationException("Идентификатор общины не указан.");
        }
        communityElections.registerMember(id, age, homeCongregation);
    }

    @Transactional
    public void establishCongregation(CongregationId id) {
        if (id == null) {
            throw new ApplicationException("Идентификатор общины не указан.");
        }
        communityElections.establishCongregation(id);
    }

    @Transactional
    public void dissolveCongregation(CongregationId id) {
        if (id == null) {
            throw new ApplicationException("Идентификатор общины не указан.");
        }
        communityElections.dissolveCongregation(id);
    }

    @Transactional
    public void completeElection(ElectionId id, Set<MemberId> potentialCouncilMembers)
            throws DomainRuleViolationException {

        if (potentialCouncilMembers == null) {
            potentialCouncilMembers = Collections.emptySet();
        }
        CommunityElections.Election election = findElection(id);
        election.complete(potentialCouncilMembers);
    }

    @Transactional
    public void voteIn(ElectionId electionId, MemberId voterId, Set<MemberId> votes)
            throws DomainRuleViolationException {

        if (voterId == null) {
            throw new ApplicationException("Идентификатор голосующего не указан.");
        }
        if (votes == null) {
            votes = Collections.emptySet();
        }
        CommunityElections.Election election = findElection(electionId);
        election.onlineVoting().vote(voterId, votes);
    }

    private CommunityElections.Election findElection(ElectionId id) {
        if (id == null) {
            throw new ApplicationException("Идентификатор выборов не указан.");
        }
        return communityElections
                .election(id)
                .orElseThrow(() -> new ApplicationException("Election '" + id + "' was not found."));
    }
}
