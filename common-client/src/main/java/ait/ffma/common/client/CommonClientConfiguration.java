package ait.ffma.common.client;


/**
 * This interface defines property names and methods needed for properties evaluation from configuration file.
 * @author GordeaS
 * @author GrafR
 *
 */
public interface CommonClientConfiguration {

	public static final String COMPONENT_NAME_CLIENT = "common-client";
	
	//TODO: remove duplications between the definition of the property names between this interface and Configuration interface
	//add the Configuration interface as base class for this 
	public static final String SERVER_URL = "server_url";
	public static final String APLICATION_NAME = "application_name";
	public static final String DEFAULT_SERVICE_PATH = "default_service_path";
	public static final String METADATA_STORE_SERVICE_PATH = "metadata_store_service_path";
	public static final String METADATA_UPDATE_SERVICE_PATH = "metadata_update_service_path";
	public static final String MEDIADATA_STORE_SERVICE_PATH = "mediadata_store_service_path";
	
	/**
	 * 
	 * @return the URL to server
	 */
	public String getServerUrl();
	
	/**
	 * 
	 * @return the application name
	 */
	public String getApplicationName();
	
	/**
	 * 
	 * @return the path to the server
	 */
	public String getDefaultServicePath();
	
	/**
	 * 
	 * @return the path to the metadata store service
	 */
	public String getMetadataStoreServicePath();
	
	/**
	 * 
	 * @return the path to the media data store service
	 */
	public String getMediaDataStoreServicePath();

	public abstract String getMetadataUpdateServicePath();
	
}
