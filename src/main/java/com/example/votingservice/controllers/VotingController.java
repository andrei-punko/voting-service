package com.example.votingservice.controllers;

import com.example.votingservice.dto.request.VotingRequest;
import com.example.votingservice.dto.response.CandidatesResponse;
import com.example.votingservice.dto.response.VotingsResponse;
import com.example.votingservice.services.VotingService;
import javax.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequiredArgsConstructor
public class VotingController {

    private final VotingService votingService;

    @GetMapping("/candidates")
    public CandidatesResponse getCandidates() {
        return votingService.getCandidates();
    }

    @PostMapping("/votings/{candidateId}")
    @ResponseStatus(HttpStatus.CREATED)
    public void makeVote(@PathVariable @NotNull String candidateId, @RequestBody VotingRequest votingRequest) {
        votingService.makeVote(candidateId, votingRequest);
    }

    @GetMapping("/votings")
    public VotingsResponse getVotingResults() {
        return votingService.getVotingResults();
    }
}
