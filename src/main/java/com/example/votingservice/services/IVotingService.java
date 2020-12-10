package com.example.votingservice.services;

import com.example.votingservice.dto.request.VotingRequest;
import com.example.votingservice.dto.response.CandidatesResponse;
import com.example.votingservice.dto.response.VotingsResponse;

public interface IVotingService {

    CandidatesResponse getCandidates();

    void makeVote(String candidateId, VotingRequest votingRequest);

    VotingsResponse getVotingResults();
}
