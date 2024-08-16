package by.andd3dfx.voting.controllers;

import by.andd3dfx.voting.dto.request.VotingRequest;
import by.andd3dfx.voting.dto.response.CandidateItem;
import by.andd3dfx.voting.dto.response.CandidatesResponse;
import by.andd3dfx.voting.dto.response.VotingResponse;
import by.andd3dfx.voting.dto.response.VotingsResponse;
import by.andd3dfx.voting.services.impl.VotingService;
import by.andd3dfx.voting.util.TestUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.List;

import static by.andd3dfx.voting.util.TestUtil.asJson;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(VotingController.class)
class VotingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private VotingService votingService;

    @Test
    void getCandidates() throws Exception {
        final CandidatesResponse response = buildCandidatesResponse();
        given(votingService.getCandidates()).willReturn(response);

        mockMvc.perform(get("/candidates"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(asJson(response)));

        verify(votingService).getCandidates();
    }

    private CandidatesResponse buildCandidatesResponse() {
        return new CandidatesResponse(
                List.of(new CandidateItem("123qwe", "Andrei"))
        );
    }

    @Test
    void makeVote() throws Exception {
        final String candidateId = "Candidate 45";
        final VotingRequest votingRequest = new VotingRequest("Andrei", "P12345WE789");

        mockMvc.perform(post("/votings/{candidateId}", candidateId)
                .content(asJson(votingRequest))
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8"))
                .andDo(print())
                .andExpect(status().isCreated());

        verify(votingService).makeVote(candidateId, votingRequest);
    }

    @Test
    void getVotingResults() throws Exception {
        final VotingsResponse response = buildVotingResponse();
        given(votingService.getVotingResults()).willReturn(response);

        mockMvc.perform(get("/votings"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(asJson(response)));

        verify(votingService).getVotingResults();
    }

    @Test
    void getVotingResult() throws Exception {
        final String candidateId = "Candidate 45";
        VotingResponse response = new VotingResponse(candidateId, 45L);
        given(votingService.getVotingResult(candidateId)).willReturn(response);

        mockMvc.perform(get("/votings/{candidateId}", candidateId))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(asJson(response)));

        verify(votingService).getVotingResult(candidateId);
    }

    private VotingsResponse buildVotingResponse() {
        return new VotingsResponse(
                new HashMap<>() {{
                    put("Candidate 54", 3L);
                }}
        );
    }
}