package com.example.votingservice.controllers;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.example.votingservice.dto.response.CandidateItem;
import com.example.votingservice.dto.response.CandidatesResponse;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AsyncVotingControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void getCandidatesUsingFuture() {
        var result = restTemplate.getForObject("/async/candidates", CandidatesResponse.class);

        assertThat(result.getCandidates(), is(List.of(
                new CandidateItem("54654", "Test Candidate B"),
                new CandidateItem("3434", "Test Candidate A"),
                new CandidateItem("4565", "Test Candidate C")
        )));
    }

    @Test
    void getCandidatesUsingDeferredResult() {
        var result = restTemplate.getForObject("/async/candidates2", CandidatesResponse.class);

        assertThat(result.getCandidates(), is(List.of(
                new CandidateItem("54654", "Test Candidate B"),
                new CandidateItem("3434", "Test Candidate A"),
                new CandidateItem("4565", "Test Candidate C")
        )));
    }
}
