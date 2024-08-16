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
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class VotingService implements InitializingBean, IVotingService {

    private final ObjectMapper mapper;

    private static final String DATA_CANDIDATES_JSON = "data/candidates.json";

    private List<CandidateItem> candidates;
    private Map<String, Set<VotingRequest>> votingMap = new HashMap<>();
    private Map<String, Long> votingResults = new HashMap<>();
    private Set<String> candidateIds = new HashSet<>();

    @Override
    public CandidatesResponse getCandidates() {
        return new CandidatesResponse(List.copyOf(candidates));
    }

    @Override
    public void makeVote(String candidateId, VotingRequest votingRequest) {
        if (!candidateIds.contains(candidateId)) {
            throw new UnknownCandidateException();
        }

        Set<VotingRequest> votingRequestsSet = votingMap.computeIfAbsent(candidateId, k -> new HashSet<>());

        if (!votingRequestsSet.add(votingRequest)) {
            throw new DoubleVoteException();
        }

        votingResults.merge(candidateId, 0L, (oldValue, newValue) -> oldValue + 1);
    }

    @Override
    public VotingsResponse getVotingResults() {
        return new VotingsResponse(Map.copyOf(votingResults));
    }

    @Override
    public VotingResponse getVotingResult(@NotNull String candidateId) {
        return new VotingResponse(candidateId, votingResults.get(candidateId));
    }

    /**
     * Load candidates from json file during service start
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        candidates = mapper.readValue(this.getClass().getClassLoader().getResourceAsStream(DATA_CANDIDATES_JSON),
                new TypeReference<>() {
                });

        candidates.stream()
                .map(CandidateItem::getId)
                .forEach(id -> {
                    votingResults.put(id, 0L);
                    candidateIds.add(id);
                });
    }
}
