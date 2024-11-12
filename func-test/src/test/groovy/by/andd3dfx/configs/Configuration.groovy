package by.andd3dfx.configs

import by.andd3dfx.model.Candidate
import groovyx.net.http.RESTClient

class Configuration {

    public static final String votingServiceHost = "localhost"
    public static final String votingServiceUrl = "http://$votingServiceHost:8090"
    public static final RESTClient votingServiceRestClient = new RESTClient(votingServiceUrl)

    public static final expectedCandidates = [
            new Candidate('54654', "Candidate B"),
            new Candidate('3434', "Candidate A"),
            new Candidate('4565', "Candidate C"),
            new Candidate('787878', "Candidate D")
    ]
}
