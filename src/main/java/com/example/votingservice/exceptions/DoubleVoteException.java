package com.example.votingservice.exceptions;

public class DoubleVoteException extends RuntimeException {

    public DoubleVoteException() {
        super("User already voted!");
    }
}
