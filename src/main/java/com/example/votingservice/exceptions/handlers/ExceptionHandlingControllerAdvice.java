package com.example.votingservice.exceptions.handlers;

import com.example.votingservice.exceptions.DoubleVoteException;
import com.example.votingservice.dto.exceptions.ExceptionMessage;
import com.example.votingservice.exceptions.UnknownCandidateException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionHandlingControllerAdvice {

    private ObjectMapper objectMapper = new ObjectMapper();

    @ExceptionHandler(DoubleVoteException.class)
    public ResponseEntity<String> doubleVoteException(DoubleVoteException ex) {
        return new ResponseEntity<>(toJson(new ExceptionMessage(ex.getMessage())), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UnknownCandidateException.class)
    public ResponseEntity<String> unknownCandidateException(UnknownCandidateException ex) {
        return new ResponseEntity<>(toJson(new ExceptionMessage(ex.getMessage())), HttpStatus.NOT_FOUND);
    }

    @SneakyThrows
    private String toJson(ExceptionMessage error) {
        return objectMapper.writeValueAsString(error);
    }
}
