package org.bayaweaver.oce.domain.model;

import org.bayaweaver.oce.domain.model.common.DomainRuleViolationException;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CommunityElectionsTest {

    @Test
    void generalTest() throws DomainRuleViolationException {
        var congregation = new NumericCongregationId(1);
        var member1 = new NumericMemberId(1);
        var member2 = new NumericMemberId(2);
        var member3 = new NumericMemberId(3);
        var member4 = new NumericMemberId(4);
        var member5 = new NumericMemberId(5);
        var id = new NumericElectionId(1);
        var root = new CommunityElections();
        root.registerMember(member1, 30, congregation);
        root.registerMember(member2, 30, congregation);
        root.registerMember(member3, 30, congregation);
        root.registerMember(member4, 30, congregation);
        root.registerMember(member5, 30, congregation);
        root.initiateElection(id, congregation, Clock.systemDefaultZone());
        var onlineVoting = root.election(id).get().onlineVoting();
        var votes = Set.of(member1, member2, member3, member4, member5);
        assertDoesNotThrow(() -> onlineVoting.vote(member1, votes));
    }

    @Test
    void votesBelongToCongregation() throws DomainRuleViolationException {
        var congregation = new NumericCongregationId(1);
        var member1 = new NumericMemberId(1);
        var member2 = new NumericMemberId(2);
        var member3 = new NumericMemberId(3);
        var member4 = new NumericMemberId(4);
        var member5 = new NumericMemberId(5);
        var id = new NumericElectionId(1);
        var root = new CommunityElections();
        root.registerMember(member1, 30, congregation);
        root.registerMember(member2, 30, congregation);
        root.registerMember(member3, 30, congregation);
        root.registerMember(member4, 30, congregation);
        root.registerMember(member5, 30, congregation);
        root.initiateElection(id, congregation, Clock.systemDefaultZone());
        var onlineVoting = root.election(id).get().onlineVoting();
        var votes = Set.of(member2, member3, member4, member5, new NumericMemberId(6));
        assertThrows(DomainRuleViolationException.class, () -> onlineVoting.vote(member1, votes));
    }

    @Test
    void candidateAge21Plus() throws DomainRuleViolationException {
        var congregation = new NumericCongregationId(1);
        var member1 = new NumericMemberId(1);
        var member2 = new NumericMemberId(2);
        var member3 = new NumericMemberId(3);
        var member4 = new NumericMemberId(4);
        var member5 = new NumericMemberId(5);
        var id = new NumericElectionId(1);
        var root = new CommunityElections();
        root.registerMember(member1, 30, congregation);
        root.registerMember(member2, 30, congregation);
        root.registerMember(member3, 20, congregation);
        root.registerMember(member4, 30, congregation);
        root.registerMember(member5, 30, congregation);
        root.initiateElection(id, congregation, Clock.systemDefaultZone());
        var onlineVoting = root.election(id).get().onlineVoting();
        var votes = Set.of(member1, member2, member3, member4, member5);
        assertThrows(DomainRuleViolationException.class, () -> onlineVoting.vote(member1, votes));
    }
}
