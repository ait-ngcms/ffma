package ait.ffma.common.dao.mongodb;

/**
 * This class is a Mongo database manager for Ffma
 */

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.MongoException;

import ait.ffma.common.CommonApiServerConfiguration;
import ait.ffma.common.exception.FfmaTechnicalRuntimeException;
import ait.ffma.common.exception.CollectionNotRemovedException;
import ait.ffma.common.exception.ObjectNotRemovedException;
import ait.ffma.factory.FfmaAbstractFactory;

/**
 * This class manages lookup, connection and sessions to a Mongo database.
 */
public class MongoDbManager extends BaseMongoDbManager {
	
	/**
	 * Indexing specification constant
	 */
	private static final int ASCENDING_INDEXING = 1;
	
	/**
	 * Mongo database variables
	 */
	protected Mongo m = null;
	
	@Autowired
	FfmaAbstractFactory objectFactory;
	
	/**
	 * This is common configuration instance that comprises port and IP address of Mongo database.
	 */
	//@Autowired
	protected
	CommonApiServerConfiguration commonApiServerConfiguration;
								
	private Logger log = Logger.getLogger(getClass());
		
	/**
	 * Constructor for MongoDbManager, connects to database and initializes
	 * object.       
	 */
	public MongoDbManager() {
	}


	/**
	 * This method initializes Mongo database instance by given port and address from configuration file.
	 */
	public void init() {
		try {
			
			m = new Mongo(commonApiServerConfiguration.getMongoDbAddress(), commonApiServerConfiguration.getMongoDbPort());
			db = m.getDB(commonApiServerConfiguration.getMongoDbName());

		} catch (Exception e) {
			throw new FfmaTechnicalRuntimeException("Cannot start mongo database!", e);
		}
	}

	/**
	 * This method reports the status of the database. True if it is running, false if not.
	 * @return the isDbRunning
	 */
	public boolean isDbRunning() {
		boolean res = false;
		try {
			db.getCollectionNames();
			res = true;
		} catch (Exception e) {
			log.debug("Mongo database is not running! " + e);
		}
		return res;
	}


	/**
	 * This method removes all objects from database.
	 * 
	 * @return result of remove method
	 * @throws ObjectNotRemovedException
	 */
	public void removeAll() throws ObjectNotRemovedException {
		try {
			m.dropDatabase(commonApiServerConfiguration.getMongoDbName());
		} catch (MongoException e) {
			throw new ObjectNotRemovedException(
					MongoDbConstants.DIGITAL_OBJECT_NOT_REMOVED, e);
		}
	}

	public void setObjectFactory(FfmaAbstractFactory objectFactory) {
		this.objectFactory = objectFactory;
	}

	public FfmaAbstractFactory getObjectFactory() {
		return objectFactory;
	}


	public void setCommonApiServerConfiguration(
			CommonApiServerConfiguration commonApiServerConfiguration) {
		this.commonApiServerConfiguration = commonApiServerConfiguration;
	}
	
	/**
	 * This method removes database collection
	 * @param collectionName
	 * @throws CollectionNotRemovedException
	 */
	public void removeCollectionByName(String collectionName)
			throws CollectionNotRemovedException {
		try {
			DBCollection collection = db.getCollectionFromString(collectionName);
			collection.drop();
		} catch (MongoException e) {
			throw new CollectionNotRemovedException(
					MongoDbConstants.COLLECTION_NOT_REMOVED, e);
		}
	}
		
}
