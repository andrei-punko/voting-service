package by.andd3dfx.specs

import groovyx.net.http.HttpResponseException
import org.apache.commons.io.IOUtils
import spock.lang.Specification

import static by.andd3dfx.configs.Configuration.expectedCandidates
import static by.andd3dfx.configs.Configuration.votingServiceRestClient

class VotingSpec extends Specification {

    def 'Get candidates'() {
        when: 'Get candidates list'
        def response = votingServiceRestClient.get(
                path: '/candidates'
        )

        then: 'Response status should be 200'
        response.status == 200

        and: 'Candidates list should contain 4 records'
        def candidates = response.responseData.candidates
        candidates.size() == 4

        and: 'Candidates should be from predefined list'
        for (int i = 0; i < candidates.size(); i++) {
            candidates[i].id == expectedCandidates[i].id
            candidates[i].name == expectedCandidates[i].name
        }
    }

    def 'Make vote'() {
        when: 'Make vote'
        def voteResponse = votingServiceRestClient.post(
                path: '/votings/' + candidateId,
                body: [name      : name,
                       passportId: passportId
                ],
                requestContentType: 'application/json'
        )

        then: 'Response status should be 201'
        voteResponse.status == 201

        where:
        candidateId | name       | passportId
        54654       | 'Andrei'   | '12345MW12'
        3434        | 'Olga'     | '12346MW32'
        4565        | 'Aleksey'  | '12347MW14'
        4565        | 'Ilya'     | '12348MW52'
        787878      | 'Nina'     | '32345MW16'
        787878      | 'Vladimir' | '72345MW72'
        787878      | 'Vitaly'   | '71345MW72'
    }

    def 'Get votings'() {
        when: 'Get votings'
        def response = votingServiceRestClient.get(
                path: '/votings'
        )

        then: 'Response status should be 200'
        response.status == 200

        and: 'Voting map should have 4 records'
        def votings = response.responseData.votings
        votings.size() == 4

        and: 'Voting map should have expected set of values'
        votings['54654'] == 1
        votings['3434'] == 1
        votings['4565'] == 2
        votings['787878'] == 3
    }

    def 'Make double vote'() {
        when: 'Try to make double vote'
        votingServiceRestClient.post(
                path: '/votings/' + candidateId,
                body: [name      : name,
                       passportId: passportId
                ],
                requestContentType: 'application/json'
        )

        then: 'Response status should be 400'
        def error = thrown(HttpResponseException)
        error.statusCode == 400

        and: 'Message about double vote should be in response'
        IOUtils.toString(error.response.responseData) == '{"error":"User already voted!"}'

        where:
        candidateId | name     | passportId
        54654       | 'Andrei' | '12345MW12'
    }
}
