package com.example.votingservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VotingResultItem {

    private CandidateItem candidateId;
    private int usersAmount;
}
