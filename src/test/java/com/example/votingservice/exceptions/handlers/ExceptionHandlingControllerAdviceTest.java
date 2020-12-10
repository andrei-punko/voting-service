package com.example.votingservice.exceptions.handlers;

import com.example.votingservice.exceptions.DoubleVoteException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

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
}
