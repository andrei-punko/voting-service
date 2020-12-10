package com.example.votingservice.exceptions;

public class UnknownCandidateException extends RuntimeException {

    public UnknownCandidateException() {
        super("Unknown candidate id!");
    }
}
