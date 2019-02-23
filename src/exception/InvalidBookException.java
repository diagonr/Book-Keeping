package exception;

public class InvalidBookException extends Exception{

	private static final long serialVersionUID = 1L;
	public InvalidBookException() {
		
	}

	public InvalidBookException(String message) {
		super(message);
	}
}
