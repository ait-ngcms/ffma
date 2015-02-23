package ait.ffma.common.exception.client;

/**
 * Exception thrown when a DigitalObject cannot be stored in the Data Registry.
 */
public class DigitalObjectNotStoredException extends FfmaClientException{
	static final long serialVersionUID = 1469131144643980203L;

	/**
	 * @param message
	 * 		The message for the exception
	 * @param excep
	 * 		The exception that prompted the creation of this exception
	 */
	public DigitalObjectNotStoredException(String message, Throwable excep) {
		super(message, excep);
	}
	
	/**
	 * @param message
	 * 		The message for the exception
	 */
	public DigitalObjectNotStoredException(String message) {
		super(message);
	}
	
	
}

