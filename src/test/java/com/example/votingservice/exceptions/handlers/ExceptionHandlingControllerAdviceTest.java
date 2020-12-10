package com.example.votingservice.exceptions.handlers;

import com.example.votingservice.exceptions.DoubleVoteException;
import com.example.votingservice.exceptions.UnknownCandidateException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

class ExceptionHandlingControllerAdviceTest {

    private ExceptionHandlingControllerAdvice exceptionHandlingControllerAdvice;

    @BeforeEach
    public void setup() {
        exceptionHandlingControllerAdvice = new ExceptionHandlingControllerAdvice();
    }

    @Test
    void doubleVoteException() {
        DoubleVoteException doubleVoteException = new DoubleVoteException();

        ResponseEntity<String> result = exceptionHandlingControllerAdvice.doubleVoteException(doubleVoteException);

        assertEquals(BAD_REQUEST, result.getStatusCode());
        assertEquals("{\"error\":\"" + doubleVoteException.getMessage() + "\"}", result.getBody());
    }

    @Test
    void unknownCandidateException() {
        UnknownCandidateException unknownCandidateException = new UnknownCandidateException();

        ResponseEntity<String> result = exceptionHandlingControllerAdvice.unknownCandidateException(unknownCandidateException);

        assertEquals(NOT_FOUND, result.getStatusCode());
        assertEquals("{\"error\":\"" + unknownCandidateException.getMessage() + "\"}", result.getBody());
    }
}
