package ait.ffma.common.bootstrap;

import java.io.IOException;

import org.mortbay.jetty.webapp.WebAppContext;

public class FfmaSolrStarter extends BaseFfmaServerStarter {

	public static int DEFAULT_PORT = 8989;
	public static int DEFAULT_HEADER_BUFFER_SIZE = 16384;
	
	public static void main(String... args) throws Exception {
		FfmaSolrStarter starter = new FfmaSolrStarter();
		starter.runBackendStarter(starter, args);
	}
	
	@Override
	protected void initProperties() throws IOException {
		System.setProperty("solr.solr.home", getRoot()
				+ "/common-server/src/test/solr/home");
		if (System.getProperty("solr.data.dir") == null) {
			System.setProperty("solr.data.dir", getRoot()
					+ "/common-server/target/solrdata");
		}
		
	}

	@Override
	protected void initApplicationHandlers() throws IOException {
		server.addHandler(new WebAppContext(getRoot()
				+ "/common-server/src/test/solr/solr.war", "/solr"));
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
