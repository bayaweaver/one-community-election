package org.bayaweaver.oce.domain.model;

import org.bayaweaver.oce.domain.model.common.DomainRuleViolationException;
import org.bayaweaver.oce.domain.model.common.Entity;
import org.bayaweaver.oce.domain.model.common.SingleAggregateRoot;

import java.time.Clock;
import java.time.Year;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class CommunityElections extends SingleAggregateRoot {
    private final Map<Year, Map<CongregationId, Election>> elections;
    private final Set<Member> members;

    public CommunityElections() {
        this.elections = new HashMap<>();
        this.members = new HashSet<>();
    }

    public Optional<Election> election(ElectionId id) {
        for (Map<CongregationId, Election> elections : this.elections.values()) {
            for (Election election : elections.values()) {
                if (election.id().equals(id)) {
                    return Optional.of(election);
                }
            }
        }
        return Optional.empty();
    }

    public void initiateElection(ElectionId id, CongregationId initiator, Clock clock)
            throws DomainRuleViolationException {

        Year currentYear = Year.now(clock);
        Map<CongregationId, Election> currentElections = this.elections.get(currentYear);
        if (currentElections != null) {
            if (currentElections.containsKey(initiator)) {
                throw new DomainRuleViolationException("Община ранее инициировавшая выборы,"
                        + " не может инициировать их повторно.");
            }
        } else {
            currentElections = new HashMap<>();
            this.elections.put(currentYear, currentElections);
        }
        Election e = new Election(id, initiator);
        currentElections.put(initiator, e);
    }

    public class Election extends Entity<ElectionId> {
        private final CongregationId initiator;
        private final OnlineVoting onlineVoting;

        private Election(ElectionId id, CongregationId initiator) {
            super(id);
            this.initiator = initiator;
            this.onlineVoting = new OnlineVoting(new OnlineVotingId(id));
        }

        public OnlineVoting onlineVoting() {
            return onlineVoting;
        }

        public class OnlineVoting extends Entity<OnlineVotingId> {
            private final Set<MemberId> votedMembers;

            private OnlineVoting(OnlineVotingId id) {
                super(id);
                this.votedMembers = new HashSet<>();
            }

            public void vote(MemberId voter, Set<? extends MemberId> votes) throws DomainRuleViolationException {
                if (votedMembers.contains(voter)) {
                    throw new DomainRuleViolationException("Член общины может голосовать только один раз.");
                }
                Collection<Member> congregationMembers = members.stream()
                        .filter(m -> m.homeCongregation.equals(initiator))
                        .toList();
                if (congregationMembers.stream().noneMatch(m -> m.id().equals(voter))) {
                    throw new DomainRuleViolationException("На выборах, инициированных определенной общиной,"
                            + " могут голосовать только члены этой общины.");
                }
                if (votes.size() != 5) {
                    throw new DomainRuleViolationException("Голосующий должен избрать 5 человек.");
                }
                for (MemberId vote : votes) {
                    Member member = congregationMembers.stream()
                            .filter(m -> m.id().equals(vote))
                            .findFirst()
                            .orElseThrow(() -> new DomainRuleViolationException("На выборах, инициированных"
                                    + " определенной общиной, могут быть избраны только члены этой общины."));
                    if (member.age < 21) {
                        throw new DomainRuleViolationException("Избираться могут только лица,"
                                + " достигшие возраста 21 года.");
                    }
                }
                votedMembers.add(voter);
            }
        }
    }

    class Member extends Entity<MemberId> {
        private int age;
        private CongregationId homeCongregation;

        private Member(MemberId id, int age, CongregationId homeCongregation) {
            super(id);
            this.age = age;
            this.homeCongregation = homeCongregation;
        }
    }
}
