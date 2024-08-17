package by.andd3dfx.voting.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VotingResponse {

    private String candidateId;
    private Set<String> passportIds;
}
