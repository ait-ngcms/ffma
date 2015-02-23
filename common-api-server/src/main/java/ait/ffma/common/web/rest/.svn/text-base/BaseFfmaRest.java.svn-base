package ait.ffma.common.web.rest;

import javax.ws.rs.GET;
import javax.ws.rs.WebApplicationException;

import ait.ffma.common.exception.FfmaFunctionalException;
import ait.ffma.common.exception.FfmaTechnicalRuntimeException;

public abstract class BaseFfmaRest {

	protected WebApplicationException convertToWebException(
			FfmaFunctionalException e) {
		return new WebApplicationException(e);
	}

	protected WebApplicationException convertToWebException(
			FfmaTechnicalRuntimeException e) {
		return new WebApplicationException(e);
	}

	protected WebApplicationException convertToWebException(Exception e) {
		return new WebApplicationException(e);
	}

	@GET
	public String legend() {
		return "There is no default action for Rest Interface of -"
				+ getComponentName() + "- component!";
	}

	/**
	 * Implement this method to return the name of your component
	 * 
	 * @return
	 */
	public abstract String getComponentName();
}
