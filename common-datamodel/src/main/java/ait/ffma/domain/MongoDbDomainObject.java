package ait.ffma.domain;

import com.mongodb.BasicDBObject;

public interface MongoDbDomainObject {

	/**
	 * Retrieve identifier for the object stored in database
	 * @return id
	 */
	public String getId();
	
	/**
	 * Sets the identifier for the object
	 * This method is supposed to be used internally by the DataStorage Service 
	 * @return id
	 */
	public void setId(String id);
	
	/**
     * This method defines which of two possible storage strategy is currently used.
     * @return true if used embedded storage and false if not
     */
    public boolean isEmbedded();

    public void initDomainObject(BasicDBObject mongo);
    
    public FieldDefEnum[] getCommonFieldsEnum();
    
    public void updateCommonFields();
    

}
