package ait.ffma.preservation.riskmanagement.bootstrap;

import java.io.IOException;

import org.mortbay.jetty.webapp.WebAppContext;

import ait.ffma.common.bootstrap.FfmaBackendStarter;

public class FfmaPreservationRiskmanagementBackendStarter extends FfmaBackendStarter {

	public static void main(String... args) throws Exception {
		FfmaPreservationRiskmanagementBackendStarter starter = new FfmaPreservationRiskmanagementBackendStarter();
		starter.runBackendStarter(starter);
	}
	
	@Override
	protected void initApplicationHandlers() throws IOException {
		//add application handlers for common backend
		super.initApplicationHandlers();
		//add application handlers for component backend
//		getServer().addHandler(new WebAppContext(getRoot() + "/src/main/webapp",
		getServer().addHandler(new WebAppContext(getRoot() + "/services/preservation-riskmanagement/src/main/webapp",
			"/ffma/preservation-riskmanagement"));
	}
}
