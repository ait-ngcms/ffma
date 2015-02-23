package ait.ffma.common.exception;

/**
 * Exception thrown when a DigitalObject is too large.
 */
public class ObjectTooLargeException extends FfmaTechnicalRuntimeException 
{
	static final long serialVersionUID = 1469131144643980204L;

	/**
	 * @param message
	 * 		The message for the exception
	 * @param excep
	 * 		The exception that prompted the creation of this exception
	 */
	public ObjectTooLargeException(String message, Throwable excep) {
		super(message, excep);
	}
	
	/**
	 * @param message
	 * 		The message for the exception
	 */
	public ObjectTooLargeException(String message) {
		super(message);
	}
	
}	
