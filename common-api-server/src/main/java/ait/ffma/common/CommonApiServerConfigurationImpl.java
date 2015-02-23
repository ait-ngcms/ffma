package ait.ffma.common;


public class CommonApiServerConfigurationImpl extends CommonApiConfigurationImpl implements CommonApiServerConfiguration {

	/*
	 * (non-Javadoc)
	 * @see ait.ffma.common.CommonApiServerConfiguration#getMongoDbAddress()
	 */
	public String getMongoDbAddress() {
		return getConfigProperty(PROP_MONGO_DB_ADDRESS, DEFAULT_MONGO_ADDRESS);
	}

	/*
	 * (non-Javadoc)
	 * @see ait.ffma.common.CommonApiServerConfiguration#getMongoDbPort()
	 */
	public int getMongoDbPort() {
		String port = getConfigProperty(PROP_MONGO_DB_PORT);
		return (port != null) ? Integer.valueOf(port) : DEFAULT_MONGO_PORT;
	}

	/*
	 * (non-Javadoc)
	 * @see ait.ffma.common.CommonApiServerConfiguration#getMongoDbName()
	 */
	public String getMongoDbName() {
		return MONGO_DB_NAME;
	}
	
	@Override
	public void init() {
		// TODO Auto-generated method stub
		super.init();
	}
		
}
