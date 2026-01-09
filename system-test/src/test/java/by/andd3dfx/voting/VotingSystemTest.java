package by.andd3dfx.voting;

import by.andd3dfx.voting.dto.exceptions.ExceptionMessage;
import by.andd3dfx.voting.dto.request.VotingRequest;
import by.andd3dfx.voting.dto.response.CandidateItem;
import by.andd3dfx.voting.dto.response.CandidatesResponse;
import by.andd3dfx.voting.dto.response.VotingResponse;
import by.andd3dfx.voting.dto.response.VotingsResponse;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Voting System Tests")
class VotingSystemTest extends TestConfiguration {

    @BeforeEach
    void setUp() {
        resetVotingResults();
    }

    @Test
    @DisplayName("Should get list of candidates")
    void testGetCandidates() {
        CandidatesResponse response = given()
                .contentType(ContentType.JSON)
                .when()
                .get("/candidates")
                .then()
                .statusCode(200)
                .extract()
                .as(CandidatesResponse.class);

        assertThat(response.getCandidates()).isNotNull();
        assertThat(response.getCandidates()).hasSize(4);

        List<CandidateItem> candidates = response.getCandidates();
        assertThat(candidates).extracting(CandidateItem::getId)
                .containsExactlyInAnyOrder("1", "2", "3", "4");

        assertThat(candidates).extracting(CandidateItem::getName)
                .containsExactlyInAnyOrder("Рыжков Алексей", "Тулегенов Арман", "Зайцев Дмитрий", "Добренко Павел");
    }

    @Test
    @DisplayName("Should create vote successfully")
    void testMakeVote() {
        String candidateId = "1";
        String passportId = "MP123456789";

        given()
                .contentType(ContentType.JSON)
                .body(new VotingRequest(passportId))
                .when()
                .post("/votings/{candidateId}", candidateId)
                .then()
                .statusCode(201);
    }

    @Test
    @DisplayName("Should return 400 when passportId is missing")
    void testMakeVoteWithoutPassportId() {
        String candidateId = "1";

        given()
                .contentType(ContentType.JSON)
                .body(new VotingRequest(null))
                .when()
                .post("/votings/{candidateId}", candidateId)
                .then()
                .statusCode(400);
    }

    @Test
    @DisplayName("Should return 400 when user tries to vote twice")
    void testMakeDoubleVote() {
        String candidateId = "1";
        String passportId = "MP987654321";

        // First vote
        given()
                .contentType(ContentType.JSON)
                .body(new VotingRequest(passportId))
                .when()
                .post("/votings/{candidateId}", candidateId)
                .then()
                .statusCode(201);

        // Second vote with same passportId
        String errorMessage = given()
                .contentType(ContentType.JSON)
                .body(new VotingRequest(passportId))
                .when()
                .post("/votings/{candidateId}", candidateId)
                .then()
                .statusCode(400)
                .extract()
                .jsonPath()
                .getString("error");

        assertThat(errorMessage).isEqualTo("User already voted!");
    }

    @Test
    @DisplayName("Should return 404 when voting for unknown candidate")
    void testMakeVoteForUnknownCandidate() {
        String candidateId = "999";
        String passportId = "MP111222333";

        String errorMessage = given()
                .contentType(ContentType.JSON)
                .body(new VotingRequest(passportId))
                .when()
                .post("/votings/{candidateId}", candidateId)
                .then()
                .statusCode(404)
                .extract()
                .jsonPath()
                .getString("error");

        assertThat(errorMessage).contains("Unknown candidate id=" + candidateId);
    }

    @Test
    @DisplayName("Should get all voting results")
    void testGetVotingResults() {
        // Create some votes
        makeVote("1", "MP111111111");
        makeVote("2", "MP222222222");
        makeVote("3", "MP333333333");
        makeVote("3", "MP444444444");
        makeVote("4", "MP555555555");
        makeVote("4", "MP666666666");
        makeVote("4", "MP777777777");

        VotingsResponse response = given()
                .contentType(ContentType.JSON)
                .when()
                .get("/votings")
                .then()
                .statusCode(200)
                .extract()
                .as(VotingsResponse.class);

        assertThat(response.getVotings()).isNotNull();
        assertThat(response.getVotings()).hasSize(4);
        assertThat(response.getVotings().get("1")).isEqualTo(1);
        assertThat(response.getVotings().get("2")).isEqualTo(1);
        assertThat(response.getVotings().get("3")).isEqualTo(2);
        assertThat(response.getVotings().get("4")).isEqualTo(3);
    }

    @Test
    @DisplayName("Should get voting result for specific candidate")
    void testGetVotingResultForCandidate() {
        String candidateId = "2";
        makeVote(candidateId, "MP888888888");
        makeVote(candidateId, "MP999999999");

        VotingResponse response = given()
                .contentType(ContentType.JSON)
                .when()
                .get("/votings/{candidateId}", candidateId)
                .then()
                .statusCode(200)
                .extract()
                .as(VotingResponse.class);

        assertThat(response.getCandidateId()).isEqualTo(candidateId);
        assertThat(response.getVotes()).isEqualTo(2);
    }

    @Test
    @DisplayName("Should return 404 when getting result for unknown candidate")
    void testGetVotingResultForUnknownCandidate() {
        String candidateId = "999";

        String errorMessage = given()
                .contentType(ContentType.JSON)
                .when()
                .get("/votings/{candidateId}", candidateId)
                .then()
                .statusCode(404)
                .extract()
                .jsonPath()
                .getString("error");

        assertThat(errorMessage).contains("Unknown candidate id=" + candidateId);
    }

    @Test
    @DisplayName("Should delete all voting results")
    void testDeleteVotingResults() {
        // Create some votes
        makeVote("1", "MP111111111");
        makeVote("2", "MP222222222");

        // Verify votes exist
        VotingsResponse beforeDelete = given()
                .contentType(ContentType.JSON)
                .when()
                .get("/votings")
                .then()
                .statusCode(200)
                .extract()
                .as(VotingsResponse.class);
        assertThat(beforeDelete.getVotings().get("1")).isEqualTo(1);
        assertThat(beforeDelete.getVotings().get("2")).isEqualTo(1);

        // Delete votes
        given()
                .contentType(ContentType.JSON)
                .when()
                .delete("/votings")
                .then()
                .statusCode(204);

        // Verify votes are deleted (all votes should be 0)
        VotingsResponse afterDelete = given()
                .contentType(ContentType.JSON)
                .when()
                .get("/votings")
                .then()
                .statusCode(200)
                .extract()
                .as(VotingsResponse.class);
        assertThat(afterDelete.getVotings().get("1")).isEqualTo(0);
        assertThat(afterDelete.getVotings().get("2")).isEqualTo(0);
        assertThat(afterDelete.getVotings().get("3")).isEqualTo(0);
        assertThat(afterDelete.getVotings().get("4")).isEqualTo(0);
    }

    @Test
    @DisplayName("Should handle multiple votes for different candidates")
    void testMultipleVotesForDifferentCandidates() {
        makeVote("1", "MP111111111");
        makeVote("2", "MP222222222");
        makeVote("3", "MP333333333");
        makeVote("4", "MP444444444");

        VotingsResponse response = given()
                .contentType(ContentType.JSON)
                .when()
                .get("/votings")
                .then()
                .statusCode(200)
                .extract()
                .as(VotingsResponse.class);

        Map<String, Integer> votings = response.getVotings();
        assertThat(votings.get("1")).isEqualTo(1);
        assertThat(votings.get("2")).isEqualTo(1);
        assertThat(votings.get("3")).isEqualTo(1);
        assertThat(votings.get("4")).isEqualTo(1);
    }

    private void makeVote(String candidateId, String passportId) {
        given()
                .contentType(ContentType.JSON)
                .body(new VotingRequest(passportId))
                .when()
                .post("/votings/{candidateId}", candidateId)
                .then()
                .statusCode(201);
    }
}
