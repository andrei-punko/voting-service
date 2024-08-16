package by.andd3dfx.voting.exceptions;

public class UnknownCandidateException extends RuntimeException {

    public UnknownCandidateException(String candidateId) {
        super("Candidate id=%s is unknown".formatted(candidateId));
    }
}
