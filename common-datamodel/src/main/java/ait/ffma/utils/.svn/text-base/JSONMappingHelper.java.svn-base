package ait.ffma.utils;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;

public abstract class JSONMappingHelper {
	
	/**
	 * Definitions for reflection methods
	 */
	public static final String SETTER_METHOD = "set";
	public static final String GETTER_METHOD = "get";
	public static final String CLASS_METHOD = "Class";
	public static final String DOC_TYPE = "DocType";
	public static final String STRING_ARRAY = "String[]";
	public static final String JSON_MAP = "myHashMap";
	public static final String ARRAY_SEPARATOR = ", ";
	public static final String LIST_SEPARATOR = "},";
	public static final String MAP_PAIR_SEPARATOR = "=";
	public static final String TOKEN_SEPARATOR = "#";

	/**
	 * The minimal size of database object ID string
	 */
	public static final int DB_OBJECT_ID_MIN = 24;
	public static final String EMPTY_PLACE_HOLDER = "0";
	
	/**
	 * Ffma domain object methods
	 */
	public static String DOMAIN_OBJECT_INIT_METHOD = "initDomainObjectExt";
	public static String DOMAIN_OBJECT_GET_ENUM_METHOD = "getFieldsEnum";

	/**
	 * FfFfmaain object complex class types
	 */
	public static String DOMAIN_OBJECT_LIST = "List";
	public static String DOMAIN_OBJECT_MAP = "Map";

	/**
	 * @param jsonArray
	 * @return String[]
	 * @throws JSONException
	 */
	public static String[] getStringArrayFromJSONArray(JSONArray jsonArray) throws JSONException {
		String[] stringArray = null;
		if (jsonArray != null) {
		    stringArray = new String[jsonArray.length()];
		    for (int i=0; i<jsonArray.length(); i++) {
		    	stringArray[i] = (String) jsonArray.get(i);
		    }
		}
		return stringArray;
	}
	
	/**
	 * This method creates string array from the complex (e.g. map) strings placed in JSONArray
	 * @param jsonArray
	 *        The JSONArray object that contains complex strings
	 * @return String[]
	 * @throws JSONException
	 */
	public static String[] getStringArrayFromJSONArrayExt(JSONArray jsonArray) throws JSONException {
		String[] stringArray = null;
		if (jsonArray != null) {
		    stringArray = new String[jsonArray.length()];
		    for (int i=0; i<jsonArray.length(); i++) {
		    	stringArray[i] = jsonArray.getString(i);
		    }
		}
		return stringArray;
	}
	
	/**
	 * @param jsonArray
	 * @return List<String>
	 * @throws JSONException
	 */
	public static List<String> getStringListFromJSONArray(JSONArray jsonArray) throws JSONException {
		List<String> list = new ArrayList<String>();
		if (jsonArray != null) {
		    for (int i=0; i<jsonArray.length(); i++) {
		    	list.add((String) jsonArray.get(i));
		    }
		}
		return list;
	}
	
}
