package by.andd3dfx.voting.controllers;

import by.andd3dfx.voting.dto.request.VotingRequest;
import by.andd3dfx.voting.dto.response.CandidatesResponse;
import by.andd3dfx.voting.dto.response.VotingResponse;
import by.andd3dfx.voting.dto.response.VotingsResponse;
import by.andd3dfx.voting.services.IVotingService;
import by.andd3dfx.voting.services.impl.VotingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@RestController
@RequiredArgsConstructor
public class VotingController {

    private final IVotingService votingService;

    @GetMapping("/candidates")
    public CandidatesResponse getCandidates() {
        return votingService.getCandidates();
    }

    @PostMapping("/votings/{candidateId}")
    @ResponseStatus(HttpStatus.CREATED)
    public void makeVote(@PathVariable @NotNull String candidateId, @RequestBody @Valid VotingRequest votingRequest) {
        votingService.makeVote(candidateId, votingRequest);
    }

    @GetMapping("/votings")
    public VotingsResponse getVotingResults() {
        return votingService.getVotingResults();
    }

    @GetMapping("/votings/{candidateId}")
    public VotingResponse getVotingResult(@PathVariable @NotNull String candidateId) {
        return votingService.getVotingResult(candidateId);
    }

    @DeleteMapping("/votings")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteVotingResults() {
        votingService.deleteVotingResults();
    }
}
