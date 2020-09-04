package com.example.votingservice.dto.response;

import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VotingsResponse {

    private Map<String, Long> votings;
}
