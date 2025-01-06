package org.bayaweaver.oce.application;

import org.bayaweaver.oce.domain.model.CommunityElections;
import org.bayaweaver.oce.domain.model.CommunityElectionsRepository;
import org.bayaweaver.oce.domain.model.CongregationId;
import org.bayaweaver.oce.domain.model.ElectionId;
import org.bayaweaver.oce.domain.model.ElectionIdPool;
import org.bayaweaver.oce.domain.model.MemberId;
import org.bayaweaver.oce.domain.model.common.DomainRuleViolationException;

import javax.transaction.Transactional;
import java.time.Year;
import java.util.Collections;
import java.util.Set;

public class Service {
    private final ElectionIdPool electionIdPool;
    private final CommunityElectionsRepository repository;

    public Service(ElectionIdPool electionIdPool, CommunityElectionsRepository repository) {
        this.electionIdPool = electionIdPool;
        this.repository = repository;
    }

    @Transactional
    public ElectionId initiateElection(CongregationId initiator)
            throws DomainRuleViolationException {

        if (initiator == null) {
            throw new ApplicationException("Идентификатор общины не указан.");
        }
        ElectionId id = electionIdPool.nextId();
        CommunityElections communityElections = repository.get();
        communityElections.initiateElection(id, initiator);
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
        CommunityElections communityElections = repository.get();
        communityElections.registerMember(id, age, homeCongregation);
    }

    @Transactional
    public void establishCongregation(CongregationId id) {
        if (id == null) {
            throw new ApplicationException("Идентификатор общины не указан.");
        }
        CommunityElections communityElections = repository.get();
        communityElections.establishCongregation(id);
    }

    @Transactional
    public void dissolveCongregation(CongregationId id) {
        if (id == null) {
            throw new ApplicationException("Идентификатор общины не указан.");
        }
        CommunityElections communityElections = repository.get();
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

    @Transactional
    public void beginNewElectionYear(Year year) throws DomainRuleViolationException {
        CommunityElections communityElections = repository.get();
        communityElections.beginNewElectionYear(year);
    }

    private CommunityElections.Election findElection(ElectionId id) {
        if (id == null) {
            throw new ApplicationException("Идентификатор выборов не указан.");
        }
        CommunityElections communityElections = repository.get();
        try {
            return communityElections.election(id);
        } catch (IllegalArgumentException e) {
            throw new ApplicationException(e.getMessage());
        }
    }
}
