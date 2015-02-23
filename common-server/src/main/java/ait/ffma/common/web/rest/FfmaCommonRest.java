package ait.ffma.common.web.rest;

import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * Handle the rest interface for Ffma Common
 * 
 * @author GordeaS
 */

// @Controller
@Path("/rest")
@Produces(MediaType.TEXT_PLAIN)
public class FfmaCommonRest extends BaseFfmaRest {

	/*
	 * (non-Javadoc)
	 * @see ait.ffma.common.web.rest.BaseFfmaRest#getComponentName()
	 */
	@Override
	public String getComponentName() {
		return "Ffmacommon";
	}

}
