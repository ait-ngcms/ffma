package ait.ffma.common.exception;


/**
 * Exception thrown when a requested property cannot be found in the property file.
 */
public class PropertyNotFoundException extends FfmaFunctionalException {

	private static final long serialVersionUID = 5661857051856421401L;

	/**
	 * @param message
	 * 		The message for the exception
	 * @param excep
	 * 		The exception that prompted the creation of this exception
	 */
	public PropertyNotFoundException(String message, Throwable excep) {
		super(message, excep);
	}
	
	/**
	 * @param message
	 * 		The message for the exception
	 */
	public PropertyNotFoundException(String message) {
		super(message);
	}
}

