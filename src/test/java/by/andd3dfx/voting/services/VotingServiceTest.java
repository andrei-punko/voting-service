package by.andd3dfx.voting.services;

import by.andd3dfx.voting.dto.request.VotingRequest;
import by.andd3dfx.voting.dto.response.CandidateItem;
import by.andd3dfx.voting.exceptions.DoubleVoteException;
import by.andd3dfx.voting.exceptions.UnknownCandidateException;
import by.andd3dfx.voting.services.impl.VotingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.fail;

class VotingServiceTest {

    private VotingService votingService;

    @BeforeEach
    public void setup() throws Exception {
        votingService = new VotingService();
        votingService.afterPropertiesSet();
    }

    @Test
    void getCandidatesAmount() {
        List<CandidateItem> result = votingService.getCandidates().getCandidates();

        assertThat("Wrong items amount", result.size(), is(3));
    }

    @Test
    void getCandidatesContent() {
        Set<CandidateItem> result = new HashSet<>(votingService.getCandidates().getCandidates());

        checkCandidateExistence(result, new CandidateItem("3434", "Test Candidate A"));
        checkCandidateExistence(result, new CandidateItem("54654", "Test Candidate B"));
        checkCandidateExistence(result, new CandidateItem("4565", "Test Candidate C"));
    }

    @Test
    void getCandidatesCheckByName() {
        List<CandidateItem> candidates = votingService.getCandidates().getCandidates();
        String[] expectedNames = {"Test Candidate A", "Test Candidate B", "Test Candidate C"};

        Set<String> names = candidates.stream().map(CandidateItem::getName).collect(Collectors.toSet());
        for (String name : expectedNames) {
            assertThat(String.format("Name %s is absent", name), names.contains(name), is(true));
        }
    }

    private void checkCandidateExistence(Set<CandidateItem> result, CandidateItem candidate) {
        assertThat(result.contains(candidate), is(true));
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
    public void makeVoteForUnknownCandidate() {
        try {
            votingService.makeVote("bla-bla", new VotingRequest("Vasya", "322982"));
            fail("UnknownCandidateException should be thrown");
        } catch (UnknownCandidateException uce) {
            assertThat(uce.getMessage(), is("Unknown candidate id!"));
        }
    }

    @Test
    public void tryDoubleVote() {
        votingService.makeVote("54654", new VotingRequest("Vasya", "322982"));
        try {
            votingService.makeVote("54654", new VotingRequest("Vasya", "322982"));
            fail("DoubleVoteException should be thrown");
        } catch (DoubleVoteException uce) {
            assertThat(uce.getMessage(), is("User already voted!"));
        }
    }

    @Test
    void getVotingResultsAfterVoting() {
        votingService.makeVote("54654", new VotingRequest("Vasya", "322982"));
        votingService.makeVote("4565", new VotingRequest("Sergei", "322893"));
        votingService.makeVote("4565", new VotingRequest("Andrei", "322894"));

        Map<String, Long> votingResults = votingService.getVotingResults().getVotings();
        assertThat("Wrong votingResults[0].candidate", votingResults.get("54654"), is(1L));
        assertThat("Wrong votingResults[1].candidate", votingResults.get("3434"), is(0L));
        assertThat("Wrong votingResults[2].candidate", votingResults.get("4565"), is(2L));
    }
}