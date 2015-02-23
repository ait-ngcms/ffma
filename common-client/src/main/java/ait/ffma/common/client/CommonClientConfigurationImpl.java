package ait.ffma.common.client;

import ait.ffma.BaseFfmaConfiguration;

/**
 * This is a common client configuration class that provides access to configuration data.
 */
public class CommonClientConfigurationImpl extends BaseFfmaConfiguration implements CommonClientConfiguration {

	public static final String _DEFAULT_SERVER_URL = "http://localhost";
	public static final String _DEFAULT_APLICATION_NAME = "/ffma";
	public static final String _DEFAULT_SERVICE_PATH = "/common/rest";
	public static final String _METADATA_STORE_SERVICE_PATH = "/object/store/metadata";
	public static final String _METADATA_UPDATE_SERVICE_PATH = "/object/update/metadata";
	public static final String _MEDIADATA_STORE_SERVICE_PATH = "/object/store/mediadata";

	
	public CommonClientConfigurationImpl() {
		init();
	}
	
	/* (non-Javadoc)
	 * @see ait.ffma.Configuration#getComponentName()
	 */
	public String getComponentName() {
		return COMPONENT_NAME_CLIENT;
	}
	
	/* (non-Javadoc)
	 * @see ait.ffma.BaseFfmaConfiguration#getServerUrl()
	 */
	public String getServerUrl() {
		return getConfigProperty(SERVER_URL, _DEFAULT_SERVER_URL);
	}
	
	/* (non-Javadoc)
	 * @see ait.ffma.common.client.CommonClientConfiguration#getDefaultApplicationName()
	 */
	public String getApplicationName() {
		return getConfigProperty(APLICATION_NAME, _DEFAULT_APLICATION_NAME);
	}
	
	/* (non-Javadoc)
	 * @see ait.ffma.common.client.CommonClientConfiguration#getDefaultServicePath()
	 */
	public String getDefaultServicePath() {
		return getConfigProperty(DEFAULT_SERVICE_PATH, _DEFAULT_SERVICE_PATH);
	}
	
	/* (non-Javadoc)
	 * @see ait.ffma.common.client.CommonClientConfiguration#getMetadataStoreServicePath()
	 */
	public String getMetadataStoreServicePath() {
		return getConfigProperty(METADATA_STORE_SERVICE_PATH, _METADATA_STORE_SERVICE_PATH);
	}

	/*
	 * (non-Javadoc)
	 * @see ait.ffma.common.client.CommonClientConfiguration#getMetadataUpdateServicePath()
	 */
	public String getMetadataUpdateServicePath() {
		return getConfigProperty(METADATA_UPDATE_SERVICE_PATH, _METADATA_UPDATE_SERVICE_PATH);
	}
	
	/* (non-Javadoc)
	 * @see ait.ffma.common.client.CommonClientConfiguration#getMediaDataStoreServicePath()
	 */
	public String getMediaDataStoreServicePath() {
		return getConfigProperty(MEDIADATA_STORE_SERVICE_PATH, _MEDIADATA_STORE_SERVICE_PATH);
	}

	
}