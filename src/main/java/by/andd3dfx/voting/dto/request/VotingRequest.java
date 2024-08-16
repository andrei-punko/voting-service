package by.andd3dfx.voting.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VotingRequest {

    @NotNull(message = "Name should be populated")
    private String name;

    @NotNull(message = "PassportId should be populated")
    private String passportId;
}
