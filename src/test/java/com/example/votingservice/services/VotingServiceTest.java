package com.example.votingservice.services;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.example.votingservice.dto.response.CandidateItem;
import com.example.votingservice.dto.response.VotingResultItem;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class VotingServiceTest {

    private VotingService votingService;

    @BeforeEach
    public void setup() throws Exception {
        votingService = new VotingService();
        votingService.afterPropertiesSet();
    }

    @Test
    void getCandidatesAmount() {
        List<CandidateItem> result = votingService.getCandidates();

        assertThat("Wrong items amount", result.size(), is(3));
    }

    @Test
    void getCandidatesContent() {
        Set<CandidateItem> result = new HashSet<>(votingService.getCandidates());

        checkCandidateExistence(result, new CandidateItem("3434", "Candidate A"));
        checkCandidateExistence(result, new CandidateItem("54654", "Candidate B"));
        checkCandidateExistence(result, new CandidateItem("4565", "Candidate C"));
    }

    @Test
    void getCandidatesCheckByName() {
        List<CandidateItem> candidates = votingService.getCandidates();
        String[] expectedNames = {"Candidate A", "Candidate B", "Candidate C"};

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
    }

    @Test
    void makeVote() {
    }
}