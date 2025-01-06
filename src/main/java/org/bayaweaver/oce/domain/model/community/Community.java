package org.bayaweaver.oce.domain.model.community;

import org.bayaweaver.oce.domain.model.common.AggregateRoot;
import org.bayaweaver.oce.domain.model.common.DomainRuleViolationException;
import org.bayaweaver.oce.domain.model.common.Entity;
import org.bayaweaver.oce.domain.model.common.SingleAggregateRoot;
import org.bayaweaver.oce.domain.model.elections.ElectionId;
import org.bayaweaver.oce.domain.model.elections.OnlineVotingId;

import java.util.HashSet;
import java.util.Set;

public class Community extends AggregateRoot<SingleAggregateRoot.Id> {

    public Community() {
        super(SingleAggregateRoot.Id.SINGLE_VALUE);
    }

    public void registerMember(MemberId id, int age, CongregationId homeCongregation) {
        Member m = new Member(id, age);
        if (members.contains(m)) {
            throw new IllegalArgumentException("Член общины '" + id + "' уже зарегистрирован.");
        }
        Congregation c = congregations.get(homeCongregation);
        if (c == null) {
            throw new IllegalArgumentException("Общины '" + id + "' не существует.");
        }
        members.add(m);
        c.members.add(m);
    }

    public void establishCongregation(CongregationId id) {
        Congregation c = new Congregation(id);
        if (congregations.containsKey(id)) {
            throw new IllegalArgumentException("Община '" + id + "' уже зарегистрирована.");
        }
        congregations.put(id, c);
    }

    public void dissolveCongregation(CongregationId id) {
        Congregation c = congregations.remove(id);
        if (c == null) {
            return;
        }
        Election e = elections.get(id);
        if (e != null && e.status == Election.Status.OPEN) {
            e.cancel();
        }
    }

    public class Election extends Entity<ElectionId> {
        private final Congregation initiator;
        private final OnlineVoting onlineVoting;
        private Status status;
        private final int numberOfVacancies = 5;

        private Election(ElectionId id, Congregation initiator) {
            super(id);
            this.initiator = initiator;
            this.onlineVoting = new OnlineVoting(new OnlineVotingId(id));
            this.status = Status.OPEN;
        }

        public OnlineVoting onlineVoting() throws DomainRuleViolationException {
            if (status != Status.OPEN) {
                throw new DomainRuleViolationException("Онлайн-голосование доступно только если выборы открыты.");
            }
            return onlineVoting;
        }

        public void complete(Set<MemberId> potentialCouncilMembers) throws DomainRuleViolationException {
            if (!onlineVoting.votes.containsAll(potentialCouncilMembers)) {
                throw new DomainRuleViolationException("Потенциальными членами совета общины могут быть только лица,"
                        + " за которых отдан хотя бы один голос.");
            }
            if (potentialCouncilMembers.size() != numberOfVacancies) {
                throw new DomainRuleViolationException("В качестве потенциальных членов совета"
                        + " должно быть выбрано 5 человек.");
            }
            status = Status.COMPLETED;
        }

        void cancel() {
            status = Status.CANCELLED;
        }

        public class OnlineVoting extends Entity<OnlineVotingId> {
            private final Set<MemberId> votedMembers;
            private final Set<MemberId> votes;

            private OnlineVoting(OnlineVotingId id) {
                super(id);
                this.votedMembers = new HashSet<>();
                this.votes = new HashSet<>();
            }

            public void vote(MemberId voter, Set<MemberId> votes) throws DomainRuleViolationException {
                if (this.votedMembers.contains(voter)) {
                    throw new DomainRuleViolationException("Член общины может голосовать только один раз.");
                }
                if (Election.this.initiator.members.stream().noneMatch(m -> m.id().equals(voter))) {
                    throw new DomainRuleViolationException("На выборах, инициированных определенной общиной,"
                            + " могут голосовать только члены этой общины.");
                }
                if (votes.size() != numberOfVacancies) {
                    throw new DomainRuleViolationException("Голосующий должен избрать 5 человек.");
                }
                for (MemberId vote : votes) {
                    Member member = Election.this.initiator.members.stream()
                            .filter(m -> m.id().equals(vote))
                            .findFirst()
                            .orElseThrow(() -> new DomainRuleViolationException("На выборах, инициированных"
                                    + " определенной общиной, могут быть избраны только члены этой общины."));
                    if (member.age < 21) {
                        throw new DomainRuleViolationException("Избираться могут только лица,"
                                + " достигшие возраста 21 года.");
                    }
                }
                this.votedMembers.add(voter);
                this.votes.addAll(votes);
            }
        }

        private enum Status {
            OPEN, COMPLETED, CANCELLED
        }
    }

    class Member extends Entity<MemberId> {
        private int age;

        private Member(MemberId id, int age) {
            super(id);
            this.age = age;
        }
    }

    class Congregation extends Entity<CongregationId> {
        private final Set<Member> members;

        public Congregation(CongregationId id) {
            super(id);
            this.members = new HashSet<>();
        }
    }
}
