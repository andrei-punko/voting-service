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

        assertThat(candidates).isEqualTo(List.of(
                new CandidateItem("54654", "Test Candidate B"),
                new CandidateItem("3434", "Test Candidate A"),
                new CandidateItem("4565", "Test Candidate C")
        ));
    }

    @Test
    public void makeVoteForUnknownCandidate() {
        final String WRONG_CANDIDATE_ID = "568978";
        try {
            votingService.makeVote(WRONG_CANDIDATE_ID, new VotingRequest("322982"));
            fail("UnknownCandidateException should be thrown");
        } catch (UnknownCandidateException uce) {
            assertThat(uce.getMessage()).isEqualTo("Unknown candidate id=" + WRONG_CANDIDATE_ID);
        }
    }

    @Test
    public void tryDoubleVoteSamePersonVotesForSameCandidate() {
        votingService.makeVote("54654", new VotingRequest("322982"));
        try {
            votingService.makeVote("54654", new VotingRequest("322982"));
            fail("DoubleVoteException should be thrown");
        } catch (DoubleVoteException uce) {
            assertThat(uce.getMessage()).isEqualTo("User already voted!");
        }
    }

    @Test
    public void tryDoubleVoteSamePersonVotesForDifferentCandidates() {
        votingService.makeVote("54654", new VotingRequest("322982"));
        try {
            votingService.makeVote("4565", new VotingRequest("322982"));
            fail("DoubleVoteException should be thrown");
        } catch (DoubleVoteException uce) {
            assertThat(uce.getMessage()).isEqualTo("User already voted!");
        }
    }

    @Test
    void getVotingResultsAfterVoting() {
        votingService.makeVote("54654", new VotingRequest("322982"));
        votingService.makeVote("4565", new VotingRequest("322893"));
        votingService.makeVote("4565", new VotingRequest("322894"));

        var response = votingService.getVotingResults();

        assertThat(response.getVotings()).isEqualTo(Map.of(
                "54654", 1,
                "3434", 0,
                "4565", 2
        ));
    }

    @Test
    void getVotingResult() {
        votingService.makeVote("54654", new VotingRequest("322982"));
        votingService.makeVote("4565", new VotingRequest("322893"));
        votingService.makeVote("4565", new VotingRequest("322894"));

        checkVotesAmount("54654", 1);
        checkVotesAmount("4565", 2);
        checkVotesAmount("3434", 0);
    }

    @Test
    void getVotingResultForUnknownCandidate() {
        final String WRONG_CANDIDATE_ID = "568978";
        try {
            votingService.getVotingResult(WRONG_CANDIDATE_ID);
            fail("UnknownCandidateException should be thrown");
        } catch (UnknownCandidateException uce) {
            assertThat(uce.getMessage()).isEqualTo("Unknown candidate id=" + WRONG_CANDIDATE_ID);
        }
    }

    @Test
    void deleteVotingResults() {
        votingService.makeVote("54654", new VotingRequest("322982"));
        votingService.makeVote("4565", new VotingRequest("322893"));
        votingService.makeVote("4565", new VotingRequest("322894"));
        checkVotesAmount("54654", 1);
        checkVotesAmount("4565", 2);
        checkVotesAmount("3434", 0);

        votingService.deleteVotingResults();

        checkVotesAmount("54654", 0);
        checkVotesAmount("4565", 0);
        checkVotesAmount("3434", 0);
    }

    private void checkVotesAmount(String candidateId, int expectedVotesAmount) {
        assertThat(votingService.getVotingResult(candidateId))
                .isEqualTo(new VotingResponse(candidateId, expectedVotesAmount));
    }
}
