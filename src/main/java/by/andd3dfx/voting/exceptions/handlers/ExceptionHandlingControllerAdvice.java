package by.andd3dfx.voting.exceptions.handlers;

import by.andd3dfx.voting.exceptions.DoubleVoteException;
import by.andd3dfx.voting.exceptions.UnknownCandidateException;
import by.andd3dfx.voting.dto.exceptions.ExceptionMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@RequiredArgsConstructor
public class ExceptionHandlingControllerAdvice {

    private final ObjectMapper objectMapper;

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
