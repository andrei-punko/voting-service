package by.andd3dfx.voting.services.impl;

import by.andd3dfx.voting.dto.request.VotingRequest;
import by.andd3dfx.voting.dto.response.CandidateItem;
import by.andd3dfx.voting.dto.response.CandidatesResponse;
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

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VotingService implements InitializingBean, IVotingService {

    private final ObjectMapper mapper;

    private List<CandidateItem> candidates;

    private Map<String, Set<VotingRequest>> votingMap = new HashMap<>();
    private Map<String, Long> votingResultsMap = new HashMap<>();
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
        return new VotingsResponse(Map.copyOf(votingResultsMap));
    }

    /**
     * Load candidates from JSON file during service start
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        candidates = mapper.readValue(this.getClass().getClassLoader().getResourceAsStream("data/candidates.json"),
                new TypeReference<>() {
                });

        candidates.forEach(item -> {
            votingResultsMap.put(item.getId(), 0L);
            candidateIds.add(item.getId());
        });
    }
}
