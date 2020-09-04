package com.example.votingservice.services;

import com.example.votingservice.dto.response.CandidateItem;
import com.example.votingservice.dto.request.VotingRequest;
import com.example.votingservice.dto.response.CandidatesResponse;
import com.example.votingservice.dto.response.VotingResultItem;
import com.example.votingservice.dto.response.VotingsResponse;
import com.example.votingservice.exceptions.DoubleVoteException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

@Service
public class VotingService implements InitializingBean {

    private List<CandidateItem> candidates;
    private Map<String, CandidateItem> candidateIdToCandidateMap;

    private Map<String, Set<VotingRequest>> votingMap = new HashMap<>();

    public CandidatesResponse getCandidates() {
        return new CandidatesResponse(candidates);
    }

    public void makeVote(String candidateId, VotingRequest votingRequest) {
        Set<VotingRequest> votingRequestSet = votingMap.get(candidateId);
        if (votingRequestSet == null) {
            votingRequestSet = new HashSet<>();
            votingMap.put(candidateId, votingRequestSet);
        }

        if (!votingRequestSet.add(votingRequest)) {
            throw new DoubleVoteException();
        }
    }

    public VotingsResponse getVotingResults() {
        List<VotingResultItem> result = new ArrayList<>();

        for (String candidateId : candidateIdToCandidateMap.keySet()) {
            result.add(new VotingResultItem(candidateIdToCandidateMap.get(candidateId), extractSize(candidateId)));
        }
        return new VotingsResponse(result);
    }

    private int extractSize(String candidateId) {
        if (!votingMap.containsKey(candidateId)) {
            return 0;
        }
        return votingMap.get(candidateId).size();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        candidates = mapper.readValue(this.getClass().getClassLoader().getResourceAsStream("data/candidates.json"),
            new TypeReference<List<CandidateItem>>() {
            });

        candidateIdToCandidateMap = new LinkedHashMap<>();
        candidates.forEach(item -> {
            candidateIdToCandidateMap.put(item.getId(), item);
        });
    }
}
