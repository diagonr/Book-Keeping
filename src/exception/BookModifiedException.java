package exception;

public class BookModifiedException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2230314008777179588L;

	public BookModifiedException() {
	}

	public BookModifiedException(String message) {
		super(message);
	}
}
