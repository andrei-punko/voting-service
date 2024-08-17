package by.andd3dfx.voting.services.impl;

import by.andd3dfx.voting.dto.request.VotingRequest;
import by.andd3dfx.voting.dto.response.CandidateItem;
import by.andd3dfx.voting.dto.response.VotingResponse;
import by.andd3dfx.voting.exceptions.DoubleVoteException;
import by.andd3dfx.voting.exceptions.UnknownCandidateException;
import by.andd3dfx.voting.util.TestUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;

class VotingServiceTest {

    private VotingService votingService;

    @BeforeEach
    public void setup() throws Exception {
        votingService = new VotingService(TestUtil.getMapper());
        votingService.afterPropertiesSet();
    }

    @Test
    void getCandidates() {
        var response = votingService.getCandidates();

        var candidates = response.getCandidates();
        assertThat(candidates.size()).isEqualTo(3);
        assertThat(candidates).isEqualTo(List.of(
                new CandidateItem("54654", "Test Candidate B"),
                new CandidateItem("3434", "Test Candidate A"),
                new CandidateItem("4565", "Test Candidate C")
        ));
    }

    @Test
    void getVotingResults() {
        var response = votingService.getVotingResults();

        assertThat(response.getVotings()).isEqualTo(buildEmptyVotingResult());
    }

    private static Map<String, Set<Object>> buildEmptyVotingResult() {
        return Map.of(
                "54654", Set.of(),
                "3434", Set.of(),
                "4565", Set.of()
        );
    }

    @Test
    void makeVoteAndGetVotingResults() {
        checkVotingResult("54654", Set.of());
        checkVotingResult("3434", Set.of());
        checkVotingResult("4565", Set.of());
        assertThat(votingService.getVotingResults().getVotings()).isEqualTo(buildEmptyVotingResult());

        votingService.makeVote("54654", new VotingRequest("ABC1"));
        votingService.makeVote("4565", new VotingRequest("ABC2"));
        votingService.makeVote("4565", new VotingRequest("ABC3"));

        checkVotingResult("54654", Set.of("ABC1"));
        checkVotingResult("3434", Set.of());
        checkVotingResult("4565", Set.of("ABC2", "ABC3"));
        assertThat(votingService.getVotingResults().getVotings()).isEqualTo(Map.of(
                "54654", Set.of("ABC1"),
                "3434", Set.of(),
                "4565", Set.of("ABC2", "ABC3")
        ));
    }

    private void checkVotingResult(String candidateId, Set<String> passportIds) {
        assertThat(votingService.getVotingResult(candidateId))
                .isEqualTo(new VotingResponse(candidateId, passportIds));
    }

    @Test
    void getVotingResultForUnknownCandidate() {
        final String WRONG_CANDIDATE_ID = "7856";
        try {
            votingService.getVotingResult(WRONG_CANDIDATE_ID);
            fail("UnknownCandidateException should be thrown!");
        } catch (UnknownCandidateException uce) {
            assertThat(uce.getMessage()).isEqualTo("Candidate id=%s is unknown".formatted(WRONG_CANDIDATE_ID));
        }
    }

    @Test
    public void makeVoteForUnknownCandidate() {
        final String WRONG_CANDIDATE_ID = "7856";
        try {
            votingService.makeVote(WRONG_CANDIDATE_ID, new VotingRequest("322982"));
            fail("UnknownCandidateException should be thrown!");
        } catch (UnknownCandidateException uce) {
            assertThat(uce.getMessage()).isEqualTo("Candidate id=%s is unknown".formatted(WRONG_CANDIDATE_ID));
        }
    }

    @Test
    public void makeVoteTryingDoubleVoteToOneCandidate() {
        votingService.makeVote("54654", new VotingRequest("322982"));
        try {
            votingService.makeVote("54654", new VotingRequest("322982"));
            fail("DoubleVoteException should be thrown");
        } catch (DoubleVoteException uce) {
            assertThat(uce.getMessage()).isEqualTo("User already voted!");
        }
    }

    @Test
    public void makeVoteTryingDoubleVoteToDifferentCandidates() {
        votingService.makeVote("54654", new VotingRequest("322982"));
        try {
            votingService.makeVote("3434", new VotingRequest("322982"));
            fail("DoubleVoteException should be thrown");
        } catch (DoubleVoteException uce) {
            assertThat(uce.getMessage()).isEqualTo("User already voted!");
        }
    }
}
