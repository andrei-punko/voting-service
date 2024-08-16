package by.andd3dfx.voting.services;

import by.andd3dfx.voting.dto.request.VotingRequest;
import by.andd3dfx.voting.dto.response.CandidatesResponse;
import by.andd3dfx.voting.dto.response.VotingResponse;
import by.andd3dfx.voting.dto.response.VotingsResponse;

import javax.validation.constraints.NotNull;

public interface IVotingService {

    CandidatesResponse getCandidates();

    void makeVote(@NotNull String candidateId, VotingRequest votingRequest);

    VotingsResponse getVotingResults();

    VotingResponse getVotingResult(@NotNull String candidateId);
}
