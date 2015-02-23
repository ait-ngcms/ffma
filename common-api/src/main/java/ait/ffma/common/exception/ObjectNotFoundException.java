package ait.ffma.common.exception;

/**
 * Exception thrown when a DigitalObject requested by URI cannot be found in the Data Registry.
 */
public class ObjectNotFoundException extends FfmaFunctionalException {

		static final long serialVersionUID = 3120789461926213247L;
		
		/**
		 * @param message
		 * 		The message for the exception
		 * @param excep
		 * 		The exception that prompted the creation of this exception
		 */
		public ObjectNotFoundException(String message, Throwable excep) {
			super(message, excep);
		}
		
		/**
		 * @param message
		 * 		The message for the exception
		 */
		public ObjectNotFoundException(String message) {
			super(message);
		}
}
