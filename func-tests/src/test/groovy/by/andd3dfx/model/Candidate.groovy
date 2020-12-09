package by.andd3dfx.model

import groovy.transform.EqualsAndHashCode

@EqualsAndHashCode
class Candidate {
    String id
    String name

    Candidate(String id, String name) {
        this.id = id
        this.name = name
    }
}
