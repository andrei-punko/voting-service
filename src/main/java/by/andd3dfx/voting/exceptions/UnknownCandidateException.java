package by.andd3dfx.voting.exceptions;

public class UnknownCandidateException extends RuntimeException {

    public UnknownCandidateException() {
        super("Unknown candidate id!");
    }
}
