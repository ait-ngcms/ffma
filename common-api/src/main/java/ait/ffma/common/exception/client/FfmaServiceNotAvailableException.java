package ait.ffma.common.exception.client;

public class FfmaServiceNotAvailableException extends FfmaClientException {

	public static final String REASON_SERVICE_EXCEPTION = "Service not available! Thrown Exception: ";
	
	
	/**
	 * generated serial version id
	 */
	private static final long serialVersionUID = -7802104290143173416L;

	public FfmaServiceNotAvailableException(String service, Throwable th) {
		this(service, REASON_SERVICE_EXCEPTION , th);
	}

	public FfmaServiceNotAvailableException(String message) {
		super(message);
	}
	
	public FfmaServiceNotAvailableException(String service, String message, Throwable th) {
		super(service + message, th);
	}

}
