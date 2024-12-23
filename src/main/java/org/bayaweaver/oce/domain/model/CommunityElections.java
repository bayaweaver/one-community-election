package org.bayaweaver.oce.domain.model;

import org.bayaweaver.oce.domain.model.common.DomainRuleViolationException;
import org.bayaweaver.oce.domain.model.common.Entity;
import org.bayaweaver.oce.domain.model.common.SingleAggregateRoot;

import java.time.Clock;
import java.time.Year;
import java.util.HashMap;
import java.util.Map;

public class CommunityElections extends SingleAggregateRoot {
    private final Map<Year, Map<CongregationId, Election>> elections;

    public CommunityElections() {
        this.elections = new HashMap<>();
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
        Election e = new Election(id);
        currentElections.put(initiator, e);
    }

    public class Election extends Entity<ElectionId> {

        private Election(ElectionId id) {
            super(id);
            //... = new OnlineVoting(new OnlineVotingId(id));
        }

        public class OnlineVoting extends Entity<OnlineVotingId> {

            private OnlineVoting(OnlineVotingId id) {
                super(id);
            }
        }
    }
}
