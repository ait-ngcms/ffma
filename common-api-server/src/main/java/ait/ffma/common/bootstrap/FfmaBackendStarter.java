package ait.ffma.common.bootstrap;

import java.io.IOException;

import org.mortbay.jetty.webapp.WebAppContext;



public class FfmaBackendStarter extends BaseFfmaServerStarter {

	public final static int DEFAULT_PORT = 8983;
	public final static int DEFAULT_HEADER_BUFFER_SIZE = 16384; 

	public static void main(String... args) throws Exception {
		FfmaBackendStarter starter = new FfmaBackendStarter();
		starter.runBackendStarter(starter, args);
	}
	
	protected void initApplicationHandlers() throws IOException {
		server.addHandler(new WebAppContext(getRoot() + "/common-server/src/main/webapp",
				"/ffma/common"));
	}
	
	protected void initProperties() throws IOException {
	}

	@Override
	protected int getDefaultServerPort() {
		return DEFAULT_PORT;
	}
	
	@Override
	protected int getDefaultHeaderBufferSize() {
		return DEFAULT_HEADER_BUFFER_SIZE;
	}
}
