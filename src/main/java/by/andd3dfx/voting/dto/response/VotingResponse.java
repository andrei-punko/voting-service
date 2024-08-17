package by.andd3dfx.voting.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class VotingResponse {

    private String candidateId;
    private Integer votesAmount;
}
