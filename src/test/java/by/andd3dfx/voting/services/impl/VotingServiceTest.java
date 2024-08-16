package by.andd3dfx.voting.services.impl;

import by.andd3dfx.voting.dto.request.VotingRequest;
import by.andd3dfx.voting.dto.response.CandidateItem;
import by.andd3dfx.voting.dto.response.VotingResponse;
import by.andd3dfx.voting.exceptions.DoubleVoteException;
import by.andd3dfx.voting.exceptions.UnknownCandidateException;
import by.andd3dfx.voting.util.TestUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Set;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
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

        var candidates = Set.copyOf(response.getCandidates());
        assertThat("Wrong items amount", candidates.size(), is(3));
        assertThat(candidates, hasItems(
                new CandidateItem("3434", "Test Candidate A"),
                new CandidateItem("54654", "Test Candidate B"),
                new CandidateItem("4565", "Test Candidate C")
        ));
    }

    @Test
    void getVotingResults() {
        Map<String, Long> votingResults = votingService.getVotingResults().getVotings();

        assertThat("Wrong votingResults amount", votingResults.size(), is(3));
        assertThat("Wrong votingResults[0].candidate", votingResults.get("54654"), is(0L));
        assertThat("Wrong votingResults[1].candidate", votingResults.get("3434"), is(0L));
        assertThat("Wrong votingResults[2].candidate", votingResults.get("4565"), is(0L));
    }

    @Test
    void getVotingResult() {
        votingService.makeVote("54654", new VotingRequest("Vasya", "322982"));
        votingService.makeVote("4565", new VotingRequest("Sergei", "322893"));
        votingService.makeVote("4565", new VotingRequest("Andrei", "322894"));

        assertThat(votingService.getVotingResult("54654"), is(new VotingResponse("54654", 1L)));
        assertThat(votingService.getVotingResult("3434"), is(new VotingResponse("3434", 0L)));
        assertThat(votingService.getVotingResult("4565"), is(new VotingResponse("4565", 2L)));
    }

    @Test
    public void makeVoteForUnknownCandidate() {
        try {
            votingService.makeVote("bla-bla", new VotingRequest("Vasya", "322982"));
            fail("UnknownCandidateException should be thrown");
        } catch (UnknownCandidateException uce) {
            assertThat(uce.getMessage(), is("Unknown candidate id!"));
        }
    }

    @Test
    public void makeVoteTryingDoubleVote() {
        votingService.makeVote("54654", new VotingRequest("Vasya", "322982"));
        try {
            votingService.makeVote("54654", new VotingRequest("Vasya", "322982"));
            fail("DoubleVoteException should be thrown");
        } catch (DoubleVoteException uce) {
            assertThat(uce.getMessage(), is("User already voted!"));
        }
    }

    @Test
    void makeVoteAndGetVotingResults() {
        votingService.makeVote("54654", new VotingRequest("Vasya", "322982"));
        votingService.makeVote("4565", new VotingRequest("Sergei", "322893"));
        votingService.makeVote("4565", new VotingRequest("Andrei", "322894"));

        Map<String, Long> votingResults = votingService.getVotingResults().getVotings();
        assertThat("Wrong votingResults[0].candidate", votingResults.get("54654"), is(1L));
        assertThat("Wrong votingResults[1].candidate", votingResults.get("3434"), is(0L));
        assertThat("Wrong votingResults[2].candidate", votingResults.get("4565"), is(2L));
    }
}
