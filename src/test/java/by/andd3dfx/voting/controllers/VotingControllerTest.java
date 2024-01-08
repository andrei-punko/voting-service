package by.andd3dfx.voting.controllers;

import by.andd3dfx.voting.dto.request.VotingRequest;
import by.andd3dfx.voting.dto.response.CandidateItem;
import by.andd3dfx.voting.dto.response.CandidatesResponse;
import by.andd3dfx.voting.dto.response.VotingsResponse;
import by.andd3dfx.voting.services.impl.VotingService;
import by.andd3dfx.voting.util.TestUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.HashMap;

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
                .andExpect(content().json(TestUtil.asJsonString(response)));

        verify(votingService).getCandidates();
    }

    private VotingsResponse buildVotingResponse() {
        return new VotingsResponse(
                new HashMap<String, Long>() {{
                    put("Candidate 54", 3L);
                }}
        );
    }

    @Test
    void makeVote() throws Exception {
        final String candidateId = "Candidate 45";
        final VotingRequest votingRequest = new VotingRequest("Andrei", "P12345WE789");

        mockMvc.perform(post("/votings/" + candidateId)
                .content(TestUtil.asJsonString(votingRequest))
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
                .andExpect(content().json(TestUtil.asJsonString(response)));

        verify(votingService).getVotingResults();
    }

    static CandidatesResponse buildCandidatesResponse() {
        return new CandidatesResponse(Arrays.asList(new CandidateItem("123qwe", "Andrei")));
    }
}