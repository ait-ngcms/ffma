package ait.ffma.common;

/**
 * This interface defines property names and methods needed for properties evaluation from configuration file.
 * @author GordeaS
 * @author GrafR
 *
 */
public interface CommonApiServerConfiguration extends CommonApiConfiguration {

	public static final String PROP_MONGO_DB_ADDRESS = "mongo_db_address";
	public static final String PROP_MONGO_DB_PORT = "mongo_db_port";
	
	public static String DEFAULT_MONGO_ADDRESS = "127.0.0.1";
	public static int DEFAULT_MONGO_PORT = 8060;

	/**
	 * This is a name of Mongo database
	 */
	public static final String MONGO_DB_NAME = "Ffma";
	
	/**
	 * 
	 * @return the IP address on which the MongoDb server runs
	 */
	public String getMongoDbAddress();
	
	/**
	 * 
	 * @return the Port on which the MongoDb server runs
	 */
	public int getMongoDbPort();
	
	/**
	 * 
	 * @return the Port on which the MongoDb server runs
	 */
	public String getMongoDbName();
	

}
