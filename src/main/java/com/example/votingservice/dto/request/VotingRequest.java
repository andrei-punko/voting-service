package com.example.votingservice.dto.request;

import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VotingRequest {

    @NotNull(message = "Name should be populated")
    private String name;

    @NotNull(message = "PassportId should be populated")
    private String passportId;
}
