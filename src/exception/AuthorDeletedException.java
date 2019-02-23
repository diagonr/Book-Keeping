package exception;

public class AuthorDeletedException extends Exception{

	private static final long serialVersionUID = 8297452220463133005L;
	public AuthorDeletedException() {
	}

	public AuthorDeletedException(String message) {
		super(message);
	}
}
