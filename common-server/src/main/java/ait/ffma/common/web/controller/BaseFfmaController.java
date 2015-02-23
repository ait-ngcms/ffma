package ait.ffma.common.web.controller;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import ait.ffma.common.exception.FfmaFunctionalException;
import ait.ffma.common.exception.FfmaTechnicalRuntimeException;

public class BaseFfmaController {

	private Logger log = Logger.getLogger(getClass());

	 // ===== exceptions ======
    /**
     * method for exception handling in rest service
     */
	@ExceptionHandler({Throwable.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String unknownProblem(Exception e) {
        log.warn("problem", e);
        return e.toString();
    }

	/**
	 * method for handling the technical runtime exceptions
	 * @param e
	 * @return
	 */
    @ExceptionHandler({FfmaTechnicalRuntimeException.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String technicalProblem(FfmaTechnicalRuntimeException e) {
        StringWriter sw = new StringWriter();
    	PrintWriter writer = new PrintWriter(sw);
        e.printStackTrace(writer);
        writer.flush();
        
        String fullMessage = "Technical Problem:" + e.getMessage() + "\n" +
        sw.toString();
        	        
        log.warn(fullMessage);
        return fullMessage;
    }
    
    /**
     * method for handlig functional exceptions
     * @param e
     * @return
     */
    @ExceptionHandler({FfmaFunctionalException.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String functionalProblem(Exception e) {
        log.info("problem", e);
        return e.toString();
    }
  
    
}
