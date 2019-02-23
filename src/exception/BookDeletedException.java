package exception;

public class BookDeletedException extends Exception {

	private static final long serialVersionUID = -645200404181643048L;
	public BookDeletedException() {
	}

	public BookDeletedException(String message) {
		super(message);
	}
}
