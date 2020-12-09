package by.andd3dfx.configs

import groovyx.net.http.RESTClient

class Configuration {
    public static final String votingServiceHost = "localhost"

    public static final String votingServiceUrl = "http://$votingServiceHost:8090"

    public static final RESTClient votingServiceRestClient = new RESTClient(votingServiceUrl)
}
