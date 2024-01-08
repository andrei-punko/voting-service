package by.andd3dfx.voting.services;

import by.andd3dfx.voting.dto.request.VotingRequest;
import by.andd3dfx.voting.dto.response.CandidatesResponse;
import by.andd3dfx.voting.dto.response.VotingsResponse;

public interface IVotingService {

    CandidatesResponse getCandidates();

    void makeVote(String candidateId, VotingRequest votingRequest);

    VotingsResponse getVotingResults();
}
