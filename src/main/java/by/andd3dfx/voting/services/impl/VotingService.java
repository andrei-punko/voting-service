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
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VotingService implements InitializingBean, IVotingService {

    private final ObjectMapper mapper;

    private static final String DATA_CANDIDATES_JSON = "data/candidates.json";

    private List<CandidateItem> candidates;
    private final Set<String> candidateIds = new HashSet<>();
    private final Map<String, Set<String>> votingResults = new ConcurrentHashMap<>();
    private final Set<String> votedPassportIds = ConcurrentHashMap.newKeySet();

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
        if (votedPassportIds.contains(passportId)) {
            throw new DoubleVoteException();
        }

        votingResults.get(candidateId).add(passportId);
        votedPassportIds.add(passportId);
    }

    @Override
    public VotingsResponse getVotingResults() {
        var map = votingResults.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue().size()));
        return new VotingsResponse(map);
    }

    @Override
    public VotingResponse getVotingResult(@NotNull String candidateId) {
        if (!candidateIds.contains(candidateId)) {
            throw new UnknownCandidateException(candidateId);
        }

        return new VotingResponse(candidateId, votingResults.get(candidateId).size());
    }

    @Override
    public void deleteVotingResults() {
        votingResults.values().forEach(Set::clear);
        votedPassportIds.clear();
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
            String id = candidate.getId();
            votingResults.put(id, new HashSet<>());
            candidateIds.add(id);
        }
    }
}
