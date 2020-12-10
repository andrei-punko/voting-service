package com.example.votingservice.services;

import com.example.votingservice.dto.request.VotingRequest;
import com.example.votingservice.dto.response.CandidateItem;
import com.example.votingservice.dto.response.CandidatesResponse;
import com.example.votingservice.dto.response.VotingsResponse;
import com.example.votingservice.exceptions.DoubleVoteException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

@Service
public class VotingService implements InitializingBean, IVotingService {

    private List<CandidateItem> candidates;

    private Map<String, Set<VotingRequest>> votingMap = new HashMap<>();
    private Map<String, Long> votingResultsMap = new HashMap<>();

    @Override
    public CandidatesResponse getCandidates() {
        return new CandidatesResponse(candidates);
    }

    @Override
    public void makeVote(String candidateId, VotingRequest votingRequest) {
        Set<VotingRequest> votingRequestSet = votingMap.get(candidateId);
        if (votingRequestSet == null) {
            votingRequestSet = new HashSet<>();
            votingMap.put(candidateId, votingRequestSet);
        }

        if (!votingRequestSet.add(votingRequest)) {
            throw new DoubleVoteException();
        }

        votingResultsMap.merge(candidateId, 0L, (oldValue, newValue) -> oldValue + 1);
    }

    @Override
    public VotingsResponse getVotingResults() {
        return new VotingsResponse(votingResultsMap);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        candidates = mapper.readValue(this.getClass().getClassLoader().getResourceAsStream("data/candidates.json"),
            new TypeReference<List<CandidateItem>>() {
            });

        candidates.forEach(item -> {
            votingResultsMap.put(item.getId(), 0L);
        });
    }
}
