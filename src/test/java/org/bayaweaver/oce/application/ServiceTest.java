package org.bayaweaver.oce.application;

import org.bayaweaver.oce.domain.model.InMemoryCommunityElectionsRepository;
import org.bayaweaver.oce.domain.model.MemberId;
import org.bayaweaver.oce.domain.model.common.DomainRuleViolationException;
import org.bayaweaver.oce.infrastructure.NumericElectionIdPool;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Year;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ServiceTest {
    private Service service;
    
    @BeforeEach
    void initialize() {
        var repository = new InMemoryCommunityElectionsRepository();
        this.service = new Service(new NumericElectionIdPool(), repository);
    }

    @Test
    void vote() throws DomainRuleViolationException {
        var congregation = new NumericCongregationId(1);
        service.establishCongregation(congregation);
        var member1 = new NumericMemberId(1);
        var member2 = new NumericMemberId(2);
        var member3 = new NumericMemberId(3);
        var member4 = new NumericMemberId(4);
        var member5 = new NumericMemberId(5);
        final var age = 30;
        service.registerMember(member1, age, congregation);
        service.registerMember(member2, age, congregation);
        service.registerMember(member3, age, congregation);
        service.registerMember(member4, age, congregation);
        service.registerMember(member5, age, congregation);
        var id = service.initiateElection(congregation);
        var votes = Set.<MemberId> of(member1, member2, member3, member4, member5);
        assertDoesNotThrow(() -> service.voteIn(id, member1, votes));
    }

    @Test
    void votesBelongToCongregation() throws DomainRuleViolationException {
        var congregation = new NumericCongregationId(1);
        service.establishCongregation(congregation);
        var member1 = new NumericMemberId(1);
        var member2 = new NumericMemberId(2);
        var member3 = new NumericMemberId(3);
        var member4 = new NumericMemberId(4);
        var member5 = new NumericMemberId(5);
        final var age = 30;
        service.registerMember(member1, age, congregation);
        service.registerMember(member2, age, congregation);
        service.registerMember(member3, age, congregation);
        service.registerMember(member4, age, congregation);
        service.registerMember(member5, age, congregation);
        var id = service.initiateElection(congregation);
        var votes = Set.<MemberId> of(member2, member3, member4, member5, new NumericMemberId(6));
        assertThrows(DomainRuleViolationException.class, () -> service.voteIn(id, member1, votes));
    }

    @Test
    void candidateAge21Plus() throws DomainRuleViolationException {
        
        var congregation = new NumericCongregationId(1);
        service.establishCongregation(congregation);
        var member1 = new NumericMemberId(1);
        var member2 = new NumericMemberId(2);
        var member3 = new NumericMemberId(3);
        var member4 = new NumericMemberId(4);
        var member5 = new NumericMemberId(5);
        final var age1 = 30;
        final var age2 = 20;
        service.registerMember(member1, age1, congregation);
        service.registerMember(member2, age1, congregation);
        service.registerMember(member3, age2, congregation);
        service.registerMember(member4, age1, congregation);
        service.registerMember(member5, age1, congregation);
        var id = service.initiateElection(congregation);
        var votes = Set.<MemberId> of(member1, member2, member3, member4, member5);
        assertThrows(DomainRuleViolationException.class, () -> service.voteIn(id, member1, votes));
    }

    @Test
    void complete() throws DomainRuleViolationException {
        var congregation = new NumericCongregationId(1);
        service.establishCongregation(congregation);
        var member1 = new NumericMemberId(1);
        var member2 = new NumericMemberId(2);
        var member3 = new NumericMemberId(3);
        var member4 = new NumericMemberId(4);
        var member5 = new NumericMemberId(5);
        final var age = 30;
        service.registerMember(member1, age, congregation);
        service.registerMember(member2, age, congregation);
        service.registerMember(member3, age, congregation);
        service.registerMember(member4, age, congregation);
        service.registerMember(member5, age, congregation);
        var id = service.initiateElection(congregation);
        var votes = Set.<MemberId> of(member1, member2, member3, member4, member5);
        service.voteIn(id, member1, votes);
        assertDoesNotThrow(() -> service.completeElection(id, votes));
    }
    @Test
    void completeWhenOtherThan5Persons() throws DomainRuleViolationException {
        var congregation = new NumericCongregationId(1);
        service.establishCongregation(congregation);
        var member1 = new NumericMemberId(1);
        var member2 = new NumericMemberId(2);
        var member3 = new NumericMemberId(3);
        var member4 = new NumericMemberId(4);
        var member5 = new NumericMemberId(5);
        final var age = 30;
        service.registerMember(member1, age, congregation);
        service.registerMember(member2, age, congregation);
        service.registerMember(member3, age, congregation);
        service.registerMember(member4, age, congregation);
        service.registerMember(member5, age, congregation);
        var id = service.initiateElection(congregation);
        var votes = Set.<MemberId> of(member1, member2, member3, member4, member5);
        service.voteIn(id, member1, votes);
        var fourMembers = Set.<MemberId> of(member1, member2, member3, member4);
        assertThrows(DomainRuleViolationException.class, () -> service.completeElection(id, fourMembers));
        var sixMembers = Set.<MemberId> of(member1, member2, member3, member4, member5, new NumericMemberId(6));
        assertThrows(DomainRuleViolationException.class, () -> service.completeElection(id, sixMembers));
    }
    @Test
    void completeWhenOneWithoutAnyVote() throws DomainRuleViolationException {
        var congregation = new NumericCongregationId(1);
        service.establishCongregation(congregation);
        var member1 = new NumericMemberId(1);
        var member2 = new NumericMemberId(2);
        var member3 = new NumericMemberId(3);
        var member4 = new NumericMemberId(4);
        var member5 = new NumericMemberId(5);
        final var age = 30;
        service.registerMember(member1, age, congregation);
        service.registerMember(member2, age, congregation);
        service.registerMember(member3, age, congregation);
        service.registerMember(member4, age, congregation);
        service.registerMember(member5, age, congregation);
        var id = service.initiateElection(congregation);
        var votes = Set.<MemberId> of(member1, member2, member3, member4, member5);
        service.voteIn(id, member1, votes);
        var members = Set.<MemberId> of(member2, member3, member4, member5, new NumericMemberId(6));
        assertThrows(DomainRuleViolationException.class, () -> service.completeElection(id, members));
    }

    @Test
    void congregationNotExist() {
        var congregation = new NumericCongregationId(1);
        var member1 = new NumericMemberId(1);
        assertThrows(IllegalArgumentException.class, () -> service.registerMember(member1, 30, congregation));
    }

    @Test
    void cancelElectionsWhenCongregationDissolved() throws DomainRuleViolationException {
        var congregation = new NumericCongregationId(1);
        service.establishCongregation(congregation);
        var id = service.initiateElection(congregation);
        var members = List.of(
                new NumericMemberId(1),
                new NumericMemberId(2),
                new NumericMemberId(3),
                new NumericMemberId(4),
                new NumericMemberId(5));
        final var age = 30;
        for (var member : members) {
            service.registerMember(member, age, congregation);
        }
        assertDoesNotThrow(() -> service.voteIn(id, members.get(0), new HashSet<>(members)));
        service.dissolveCongregation(congregation);
        var exception = assertThrows(
                DomainRuleViolationException.class,
                () -> service.voteIn(id, members.get(1), new HashSet<>(members)));
        assertTrue(exception.getMessage().contains("Онлайн-голосование") && exception.getMessage().contains("открыт"));
    }

    @Test
    void allElectionsAreCompletedBeforeNewYear() throws DomainRuleViolationException {
        var congregation = new NumericCongregationId(1);
        service.establishCongregation(congregation);
        var member1 = new NumericMemberId(1);
        var member2 = new NumericMemberId(2);
        var member3 = new NumericMemberId(3);
        var member4 = new NumericMemberId(4);
        var member5 = new NumericMemberId(5);
        final var age = 30;
        service.registerMember(member1, age, congregation);
        service.registerMember(member2, age, congregation);
        service.registerMember(member3, age, congregation);
        service.registerMember(member4, age, congregation);
        service.registerMember(member5, age, congregation);
        var id = service.initiateElection(congregation);
        var votes = Set.<MemberId> of(member1, member2, member3, member4, member5);
        assertDoesNotThrow(() -> service.voteIn(id, member1, votes));
        final var nextYear = Year.now().plusYears(1);
        assertThrows(DomainRuleViolationException.class, () -> service.beginNewElectionYear(nextYear));
        service.completeElection(id, votes);
        assertDoesNotThrow(() -> service.beginNewElectionYear(nextYear));
        final var prevYear = Year.now().minusYears(1);
        assertThrows(DomainRuleViolationException.class, () -> service.beginNewElectionYear(prevYear));
    }
}
