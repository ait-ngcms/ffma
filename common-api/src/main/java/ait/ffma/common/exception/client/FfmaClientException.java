package ait.ffma.common.exception.client;

public class FfmaClientException extends Exception{

	/**
	 * Generated serial version number
	 */
	private static final long serialVersionUID = -4818491543070680721L;

	public static final String REASON_CANNOT_RETRIEVE_MEDATADA = "Cannot retrieve metadata object (from solr index)";
	public static final String REASON_CANNOT_FIND_OBJECT_ID = "Cannot find object with the given Id: ";
	
	public FfmaClientException(String message, Throwable th) {
		super(message, th);
	}

	public FfmaClientException(String message) {
		super(message);
	}

}
