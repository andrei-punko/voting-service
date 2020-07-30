package com.example.votingservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VotingResultItem {

    private CandidateItem candidate;
    private int usersAmount;
}
