package by.andd3dfx.voting.exceptions;

public class DoubleVoteException extends RuntimeException {

    public DoubleVoteException() {
        super("User already voted!");
    }
}
