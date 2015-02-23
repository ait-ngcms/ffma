package ait.ffma.common.bootstrap;

import java.io.File;
import java.io.IOException;

import org.mortbay.jetty.Connector;
import org.mortbay.jetty.Server;

public abstract class BaseFfmaServerStarter {

	private String root = null;
	Server server = null;
	
	public BaseFfmaServerStarter() {
		super();
	}

	public Server getServer() {
		if(server == null)
			initServer(new String[]{""+getDefaultServerPort()});
		return server;
	}

	protected void runBackendStarter(BaseFfmaServerStarter starter, String... args)
			throws IOException, Exception {
				starter.init(args);
				starter.getServer().start();
			}

	protected void init(String... args) throws IOException {
		initServer(args);
		initProperties();
		initApplicationHandlers();
	
	}

	protected abstract void initProperties() throws IOException; 

	protected abstract void initApplicationHandlers() throws IOException;
	
	protected abstract int getDefaultServerPort();
	
	protected abstract int getDefaultHeaderBufferSize();

	protected Server initServer(String... args) {
		int port = getDefaultServerPort();
		if (args.length > 0) {
			port = Integer.parseInt(args[0]);
		}
		server = new Server(port);
		for (Connector connector : server.getConnectors()) {
			connector.setHeaderBufferSize(getDefaultHeaderBufferSize());
			connector.setRequestBufferSize(getDefaultHeaderBufferSize());
		}
		return server;
	}

	
	public void setRoot(String root) {
		this.root = root;
	}

	public String getRoot() throws IOException {
		if(root==null)
			setRoot("" + new File(".").getCanonicalFile());
		return root;
	}

}