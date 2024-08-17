package by.andd3dfx.voting.services.impl;

import by.andd3dfx.voting.dto.request.VotingRequest;
import by.andd3dfx.voting.dto.response.CandidateItem;
import by.andd3dfx.voting.dto.response.CandidatesResponse;
import by.andd3dfx.voting.dto.response.VotingResponse;
import by.andd3dfx.voting.dto.response.VotingsResponse;
import by.andd3dfx.voting.exceptions.DoubleVoteException;
import by.andd3dfx.voting.exceptions.UnknownCandidateException;
import by.andd3dfx.voting.services.IVotingService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VotingService implements InitializingBean, IVotingService {

    private static final String DATA_CANDIDATES_JSON = "data/candidates.json";

    private final ObjectMapper mapper;

    private List<CandidateItem> candidates;
    private final Set<String> candidateIds = new HashSet<>();
    private final Map<String, Set<String>> votingResults = new ConcurrentHashMap<>();
    private final Set<String> passportIds = ConcurrentHashMap.newKeySet();

    @Override
    public CandidatesResponse getCandidates() {
        return new CandidatesResponse(List.copyOf(candidates));
    }

    @Override
    public void makeVote(String candidateId, VotingRequest votingRequest) {
        if (!candidateIds.contains(candidateId)) {
            throw new UnknownCandidateException(candidateId);
        }

        var passportId = votingRequest.getPassportId();
        if (passportIds.contains(passportId)) {
            throw new DoubleVoteException();
        }

        votingResults.get(candidateId).add(passportId);
        passportIds.add(passportId);
    }

    @Override
    public VotingsResponse getVotingResults() {
        var map = votingResults.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey, entry -> entry.getValue().size()
                ));
        return new VotingsResponse(map);
    }

    @Override
    public VotingResponse getVotingResult(String candidateId) {
        if (!candidateIds.contains(candidateId)) {
            throw new UnknownCandidateException(candidateId);
        }

        return new VotingResponse(candidateId, votingResults.get(candidateId).size());
    }

    @Override
    public void deleteVotingResults() {
        votingResults.values().forEach(Set::clear);
        passportIds.clear();
    }

    /**
     * Load candidates from JSON file during service start
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        candidates = mapper.readValue(this.getClass().getClassLoader().getResourceAsStream(DATA_CANDIDATES_JSON),
                new TypeReference<>() {
                });

        for (var candidate : candidates) {
            var id = candidate.getId();
            votingResults.put(id, new HashSet<>());
            candidateIds.add(id);
        }
    }
}
