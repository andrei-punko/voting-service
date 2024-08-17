package by.andd3dfx.voting.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VotingsResponse {

    private Map<String, Set<String>> votings;
}
