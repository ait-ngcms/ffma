package ait.ffma.domain;


import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.bson.BSONObject;
import org.bson.types.ObjectId;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;

import ait.ffma.common.exception.FfmaTechnicalRuntimeException;
import ait.ffma.domain.preservation.riskmanagement.PreservationDimension;
import ait.ffma.utils.JSONMappingHelper;


/**
 * This class is the base class for generated class.
 */
public abstract class BaseFfmaDomainObject extends BasicDBObject implements FfmaDomainObject, MongoDbDomainObject {

	private static final long serialVersionUID = -1023310180589500099L;
	
	private String[] emptyStringArray = new String[]{" "};

	/**
	 * This method initializes BaseFfmaDomainObject instance with default values.
	 */
	public void initDomainObject(){
		setLastUpdateTime(System.currentTimeMillis());
		setCreateTime(System.currentTimeMillis());
	}	
	
	/**
	 * This method initializes BaseFfmaDomainObject instance with map data from BasicDBObject object.
	 * @param json
	 *        The BasicDBObject from Mongo database
	 */
	public void initDomainObject(BasicDBObject mongo){
		this.putAll((BSONObject)mongo);
	}
	
		
	public enum CommonFieldsEnum implements FieldDefEnum {
		CreateTime          { public Class<?> evalType() { return Long.class; } },
		LastUpdateTime      { public Class<?> evalType() { return Long.class; } },
		_id                 { public Class<?> evalType() { return String.class; } },
		ComponentName       { public Class<?> evalType() { return String.class; } },
		DomainObjectName    { public Class<?> evalType() { return String.class; } };

		public String evalName() { return this.name(); }
	}
	
	public FieldDefEnum[] getCommonFieldsEnum(){
		return CommonFieldsEnum.values();
	}
	
	/**
	 * This method initializes BaseFfmaDomainObject instance with map data from JSON object.
	 * @param json
	 *        The JSON object
	 * @param enumFields
	 *        The enumeration fields of the class that extends BaseFfmaDomainObject
	 */
	@SuppressWarnings("rawtypes")
	public void initDomainObject(JSONObject json, FieldDefEnum[] enumFields) {
		try {
			try {
				//TODO: set all common fields
				//String id = json.getString(CommonFieldsEnum._id.evalName());
				String id = json.getString("id");
				setId(id);
			}catch(Exception e){
				log.trace("Create id failed", e);
			}
			try{
				setCreateTime(json.getLong(CommonFieldsEnum.CreateTime.name()));
			}catch(Exception e){
				log.trace("Create Time Not Available", e);
			}
			try{
				setLastUpdateTime(json.getLong(CommonFieldsEnum.LastUpdateTime.name()));
			}catch(Exception e){
				//if not know set the current time
				setLastUpdateTime(System.currentTimeMillis());
			}
			
			Iterator itr = json.keys();
	        while (itr.hasNext()) {
	        	String key = (String) itr.next();
	        	String currentFieldName = null;
	        	String fieldReturnType = null;

	        	
	    		for (FieldDefEnum field : enumFields) {
					String fieldName = key.substring(0,1).toUpperCase() + key.substring(1);
	    			if (field.evalName().equals(fieldName)) {
	    				fieldReturnType = field.evalType().getSimpleName();
	    				currentFieldName = fieldName;
	    				break;
	    			}
	    		}
	        	
				try {
					// check if JSONArray then convert to String array and to BasicDBList, then put in the map
					Object val = json.get(key);
					//convert JSON Arrays to string arrays, this could create problems if the JsonArrays are not of type String
					if(val instanceof JSONArray){
						String[] strArray = JSONMappingHelper.getStringArrayFromJSONArray((JSONArray) val);
						val = toList(strArray);
					} else {
						if (fieldReturnType != null) {
							if (fieldReturnType.equals(JSONMappingHelper.STRING_ARRAY)) {
								String[] stringArray = new String[]{(String) val};
								val = toList(stringArray);
							}
							if (fieldReturnType.equals(Boolean.class.getSimpleName())) {
								val = Boolean.valueOf((String) val);
							}
						}
					}
					if (currentFieldName != null) {
						log.debug("currentFieldName: " + currentFieldName + ", val: " + val);
						put(currentFieldName, val);
					}
				} catch (JSONException e) {
					log.info("JSON error: " + e);
				}
	        }
		} catch (SecurityException e) {
			log.info("JSONObject constructor error: " + e);
		} catch (IllegalArgumentException e) {
			log.info("JSONObject constructor error: " + e);
		} catch (Exception e) {
			log.info("constructor error: " + e);
		}
		
		//
	}

	/**
	 * This method initializes BaseFfmaDomainObject instance with map data from JSON object.
	 * @param json
	 *        The JSON object
	 * @param enumFields
	 *        The enumeration fields of the class that extends BaseFfmaDomainObject
	 */
	@SuppressWarnings("rawtypes")
	public void initDomainObjectExt(JSONObject json, FieldDefEnum[] enumFields) {
		try {
			try {
				//TODO: set all common fields
				//String id = json.getString(CommonFieldsEnum._id.evalName());
				String id = json.getString("id");
				setId(id);
			}catch(Exception e){
				log.trace("Create id failed", e);
			}
			try{
				setCreateTime(json.getLong(CommonFieldsEnum.CreateTime.name()));
			}catch(Exception e){
				log.trace("Create Time Not Available", e);
			}
			try{
				setLastUpdateTime(json.getLong(CommonFieldsEnum.LastUpdateTime.name()));
			}catch(Exception e){
				//if not know set the current time
				setLastUpdateTime(System.currentTimeMillis());
			}
			
			Iterator itr = json.keys();
	        while (itr.hasNext()) {
	        	String key = (String) itr.next();
	        	String currentFieldName = null;
	        	String fieldReturnType = null;
	        	FieldDefEnum currentEnumField = null;

	        	
	    		for (FieldDefEnum field : enumFields) {
					String fieldName = key.substring(0,1).toUpperCase() + key.substring(1);
	    			if (field.evalName().equals(fieldName)) {
	    				fieldReturnType = field.evalType().getSimpleName();
	    				currentFieldName = fieldName;
	    				currentEnumField = field;
	    				break;
	    			}
	    		}

	    		try {
					// check if JSONArray then convert to String array and to BasicDBList, then put in the map
					Object val = json.get(key);
					//convert JSON Arrays to string arrays, this could create problems if the JsonArrays are not of type String
					if(val instanceof JSONArray){
						String[] strArray = JSONMappingHelper.getStringArrayFromJSONArrayExt((JSONArray) val);
						if (fieldReturnType.equals(JSONMappingHelper.DOMAIN_OBJECT_LIST)) {
							val = toListExt(strArray,
									((ClassDefEnum) currentEnumField)
											.evalListType().getName());
						} else {
							val = toList(strArray);
						}
					} else {
						if (fieldReturnType != null) {
							if (fieldReturnType.equals(JSONMappingHelper.STRING_ARRAY)) {
								String[] stringArray = new String[]{(String) val};
								val = toList(stringArray);
							}
							if (fieldReturnType.equals(Boolean.class.getSimpleName())) {
								val = Boolean.valueOf((String) val);
							}
						}
					}
					if(val instanceof JSONObject){
						val = toMap((JSONObject) val);
					}
					if (currentFieldName != null) {
						log.debug("currentFieldName: " + currentFieldName + ", val: " + val);
						put(currentFieldName, val);
					}
					val = null;
				} catch (JSONException e) {
					log.info("JSON error: " + e);
				}
	        }
		} catch (SecurityException e) {
			log.info("JSONObject constructor error: " + e);
		} catch (IllegalArgumentException e) {
			log.info("JSONObject constructor error: " + e);
		} catch (Exception e) {
			log.info("constructor error: " + e);
		}
		
		//
	}

    /**
     * Logger 
     */
	protected Logger log = Logger.getLogger(getClass());

	/**
	 * This flag defines storage strategy used for this class.
	 */
	private static final boolean EMBEDDED = false;
	
	/**
     * This method defines which of two possible storage strategy is currently used.
     * @return true if used embedded storage and false if not
     */
    public boolean isEmbedded() {
    	return EMBEDDED;
    }
       
	/**
	 * @return the ComponentName
	 */
	public String getComponentName() {
		return getString(CommonFieldsEnum.ComponentName.name());
	}

	/**
	 * @param ComponentName the ComponentName to set
	 */
	public void setComponentName(String componentName) {
		put(CommonFieldsEnum.ComponentName.name(), componentName);
	}

	/**
	 * @return the DomainObjectName
	 */
	public String getDomainObjectName() {
		return getString(CommonFieldsEnum.DomainObjectName.name());
	}

	/**
	 * @param DomainObjectName the DomainObjectName to set
	 */
	public void setFfmaObjectName(String FfmaObjectName) {
		put(CommonFieldsEnum.DomainObjectName.name(), FfmaObjectName);
	}

    /**
	 * @return the objectId
	 */
	public String getId() {
		return getString(CommonFieldsEnum._id.evalName());
	}

	
	/**
	 * @param objectId the objectId to set
	 */
	public void setId(String id) {
		if (id != null && id.length() < JSONMappingHelper.DB_OBJECT_ID_MIN) {
			StringBuffer sb = new StringBuffer(); 
			for (int i = 0 ; i < JSONMappingHelper.DB_OBJECT_ID_MIN - id.length(); i++) {
				sb.append(JSONMappingHelper.EMPTY_PLACE_HOLDER);
			}
			sb.append(id);
			id = sb.toString();
		}
		ObjectId objId = new ObjectId(id);
		put(CommonFieldsEnum._id.evalName(), objId);
	}
	
 
	
    /**
	 * @return the CreateTime
	 */
	public Long getCreateTime() {
		try{
			return getLong(CommonFieldsEnum.CreateTime.name());
		}catch(NullPointerException e){
			return null;
		}	
	}

	/**
	 * @param DomainObjectName the DomainObjectName to set
	 */
	public void setCreateTime(Long createTime) {
		put(CommonFieldsEnum.CreateTime.name(), createTime);
	}

	
	/**
	 * @return the LastUpdateTime
	 */
	public Long getLastUpdateTime() {
		return getLong(CommonFieldsEnum.LastUpdateTime.name());
	}

	/**
	 * @param DomainObjectName the DomainObjectName to set
	 */
	public void setLastUpdateTime(Long lastUpdateTime) {
		put(CommonFieldsEnum.LastUpdateTime.name(), lastUpdateTime);
	}
	
	public void updateCommonFields() {
    	
    	long currentTime = System.currentTimeMillis();
    	
		if(!containsField( CommonFieldsEnum.CreateTime.evalName()))
    	    	put(CommonFieldsEnum.CreateTime.evalName(), currentTime);
    	
    	put(CommonFieldsEnum.LastUpdateTime.evalName(), currentTime);
    	//TODO These operations are  REDUNDENT they copy from one field in the same field  
    	//put(CommonFieldsEnum.ComponentName.evalName(), getComponentName());
    	//put(CommonFieldsEnum.DomainObjectName.evalName(), getDomainObjectName());
    	// the _id will be set by mongodb itself
		
	}
	
	/**
	 * Create database list object from String array.
	 * 
	 * @param arr 
	 *        The String array
	 * @return BasicDBList object
	 */
	public BasicDBList toList(String[] arr) {
	    BasicDBList res = new BasicDBList();
	    for (String str : arr) {
			res.add(str);
  	    }
	    return res;
    }
	
	/**
	 * This is a generic method for Ffma domain objects that creates list from string array 
	 * @param <T>
	 *        The type of Ffma domain object
	 * @param arr
	 *        String array
	 * @param listClassName
	 *        The class name of the object of generic type
	 * @return a list of generic objects
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws ClassNotFoundException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 */
	@SuppressWarnings("unchecked")
	public <T> List<T> toListExt(String[] arr, String listClassName)
			throws InstantiationException, IllegalAccessException,
			ClassNotFoundException, IllegalArgumentException,
			InvocationTargetException {
		List<T> res = new ArrayList<T>();
		FieldDefEnum[] enumValues = null;
		T objTmp = (T) Class.forName(listClassName).newInstance();
		Method[] methods = objTmp.getClass().getMethods();
		for (Method m : methods) {
			if (m.getName().contains(JSONMappingHelper.DOMAIN_OBJECT_GET_ENUM_METHOD)) {
				enumValues = (FieldDefEnum[]) m.invoke(objTmp);
				break;
			}
		}

		for (String str : arr) {
			JSONObject json;
			try {
				json = new JSONObject(str);
				for (Method m : methods) {
					if (m.getName().contains(JSONMappingHelper.DOMAIN_OBJECT_INIT_METHOD)) {
						T obj = (T) Class.forName(listClassName).newInstance();
			            Object arglist[] = new Object[2];
			            arglist[0] = json;
			            arglist[1] = enumValues;					
//						log.info("method name: " + m.getName());
						m.invoke(obj, arglist);
						res.add(obj);
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
  	    }
	    return res;
    }
	
	/**
	 * Create hash map from JSONObject value.
	 * 
	 * @param val 
	 *        The JSONObject 
	 * @return hash map object
	 */
	@SuppressWarnings("unchecked")
	public Map<String, String> toMap(JSONObject val) {
		Map<String, String> res = new HashMap<String, String>();
		Iterator<String> iter = val.keys();
		while (iter.hasNext()) {
			String key = iter.next().toString();
			try {
				res.put(key, val.get(key).toString());
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	    return res;
    }
	
	/**
	 * Create String array from database list.
	 * 
	 * @param o
	 *        The database list object
	 * @return String array
	 */
	public String[] toStringArray(Object o) {
		if( o == null)
			return emptyStringArray;
		else if( o instanceof String[])	
			return (String[])o;
		else if( o instanceof String)	
			return new String[]{(String)o};
		else if( o instanceof BasicDBList)
			return (((BasicDBList) o).toArray(new String[((BasicDBList) o).size()]));
		else if( o instanceof PreservationDimension[])
			return Arrays.asList(o).toArray(new String[((PreservationDimension[]) o).length]);
//		return (((PreservationDimension[]) o).toArray(new String[((PreservationDimension[]) o).length]));
		else{
			throw new FfmaTechnicalRuntimeException("The type cannot be converted to String Array: " + o.getClass());
		}	
    }
	
    /**
     * Setter for fields in database.
     * 
     * @param fieldName
     *        The field name in database
     * @param o
     *        The object to insert
     */
    public void setField(String fieldName, Object o) {
        put(fieldName, o);
    }
    
    public Object getAttributeValue(String attributeName) {
		//the following syntax is used for checking attribute names automatically
		//return (String) get(FieldsEnum.valueOf(attributeName).name());
		//or, the better performing solution is would be ..
		return get(attributeName);
	}

}

