package com.example.votingservice.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class DoubleVoteException extends RuntimeException {

    public DoubleVoteException() {
        super("User already voted!");
    }
}
