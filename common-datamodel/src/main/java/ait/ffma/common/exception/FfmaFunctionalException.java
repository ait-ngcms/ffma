package ait.ffma.common.exception;

public class FfmaFunctionalException extends Exception {

	/**
	 * This is the base class for all (Ffma) checked exceptions.
	 * These exceptions must carry a message that is understandable for the end user.  
	 * 
	 */
	private static final long serialVersionUID = -111992954712646400L;

	/**
	 * 
	 * @param message
	 */
	public FfmaFunctionalException(String message){
		this(message, null);
	}
	
	/**
	 * 
	 * @param message
	 * @param th
	 */
	public FfmaFunctionalException(String message, Throwable th){
		super(message, th);
	}
}
