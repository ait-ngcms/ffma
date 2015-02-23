package ait.ffma.common.exception;

/**
 * Exception thrown when a DigitalObject cannot be removed in the Data Registry.
 */
public class ObjectNotRemovedException extends FfmaFunctionalException {
	static final long serialVersionUID = 1469131144643980203L;

	/**
	 * @param message
	 * 		The message for the exception
	 * @param excep
	 * 		The exception that prompted the creation of this exception
	 */
	public ObjectNotRemovedException(String message, Throwable excep) {
		super(message, excep);
	}
	
	/**
	 * @param message
	 * 		The message for the exception
	 */
	public ObjectNotRemovedException(String message) {
		super(message);
	}
	
}
