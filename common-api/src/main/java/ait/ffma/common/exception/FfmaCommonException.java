package ait.ffma.common.exception;

public class FfmaCommonException extends FfmaFunctionalException {

	/**
	 * Generated serial version number
	 */
	private static final long serialVersionUID = -4818491543070680721L;

	public static final String REASON_CANNOT_RETRIEVE_MEDATADA = "Cannot retrieve metadata object (from solr index)";
	public static final String REASON_CANNOT_FIND_OBJECT_ID = "Cannot find object with the given Id: ";
	
	public FfmaCommonException(String message, Throwable th) {
		super(message, th);
	}

	public FfmaCommonException(String message) {
		super(message);
	}

}
