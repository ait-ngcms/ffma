package ait.ffma.service.preservation.riskmanagement.api.lod;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import ait.ffma.domain.FfmaDomainObject;
import ait.ffma.domain.preservation.riskmanagement.AitFileFormat;
import ait.ffma.domain.preservation.riskmanagement.DBPediaFileFormat;
import ait.ffma.domain.preservation.riskmanagement.DipFormatId;
import ait.ffma.domain.preservation.riskmanagement.FreebaseFileFormat;
import ait.ffma.domain.preservation.riskmanagement.PronomFileFormat;
import ait.ffma.service.preservation.riskmanagement.api.lod.lodmodel.LODPronomSoftware;
import ait.ffma.service.preservation.riskmanagement.api.lod.lodmodel.LODProperties;
import ait.ffma.service.preservation.riskmanagement.api.lod.lodmodel.LODProperty;
import ait.ffma.service.preservation.riskmanagement.api.lod.lodmodel.LODSource;
import ait.ffma.service.preservation.riskmanagement.api.preservationwatch.DBPediaConnector;
import ait.ffma.service.preservation.riskmanagement.api.preservationwatch.FreebaseConnector;
import ait.ffma.service.preservation.riskmanagement.api.preservationwatch.PronomSparqlConnector;
import ait.ffma.service.preservation.riskmanagement.api.preservationwatch.RepositoryDescription;
import ait.ffma.service.preservation.riskmanagement.api.riskanalysis.risk.ReportConstants;
import ait.ffma.service.preservation.riskmanagement.api.riskanalysis.risk.RiskConstants;
import ait.ffma.service.preservation.riskmanagement.api.riskanalysis.risk.riskmodel.RiskAnalysis;


/**
 * This is a help class for linked open data utils.
 *
 */
public final class LODUtils {

	/**
	 * to prevent public constructor
	 */
	private LODUtils() {} 
	
	private static final String HTTP_HOTSHOT_DFHUYNH_USER_DEV_FREEBASEAPPS_COM_HTML_ID_GUID = "http://hotshot.dfhuynh.user.dev.freebaseapps.com/html?id=/guid/";

	private static final int TENSEC = 10000;

	private static final String SUB_LIST_SIZE = "sub list size: ";

	private static final String RESULT_FOUND = "Result found: ";

	private static final String LIST_SIZE = "list size: ";

	private static final String SEARCH_VALUE = "searchValue: ";

	private static final String TRUE = "true";

	private static final String FALSE = "false";

	private static final String LOD_PROPERTIES_RELOAD_XML = "LODProperties.ReloadXML";

	private static final String COMMASPACE = ", ";

	private static final String EMPTYSTRING = "";

	private static final String LEFTARROWEND = "</";

	private static final String RIGHTARROW = ">";

	private static final String LEFTARROW = "<";

	private static final String PROPERTY_QUERY = "Property query: ";

	private static final String FREEBASE_QUERY_JSON_OBJECT_CREATION_ERROR = "Freebase query JSON object creation error: ";

	private static final String FREEBASE_JSON_RESULT_OBJECT_PARSING_ERROR = "Freebase JSON result object parsing error: ";

	private static final String FOUND = "found: ";

	private static final String ORG_JSON_JSON_OBJECT$_NULL = "org.json.JSONObject$Null";

	private static final String VALUE_FOUND = "value found: ";

	/**
	 * A logger object.
	 */
	private static final Logger log = Logger.getLogger(LODUtils.class
			.getName());

	/**
	 * the general properties file name
	 */
	private static String generalPropertiesFile = "Ffma-preservation-riskmanagement-riskanalysis.general.properties";
	
	/**
	 * the LOD properties object 
	 */
	private volatile static LODProperties lodProperties = null;

	/**
	 * general properties object
	 */
	private static Properties generalProps = null;

	/**
	 * LOD property mapping
	 */
	private static Map<String, LODProperty> lodPropertyMap = new HashMap<String, LODProperty>();

	/**
	 * @param key
	 * @param value
	 */
	public static void setGeneralProperty(String key, String value) {
		generalProps.put(key, value);
	}

	/**
	 * @param key
	 * @return
	 */
	public static String getGeneralProperty(String key) {
		if (generalProps == null) {
			loadProperties();
		}
		return generalProps.getProperty(key);
	}

	/**
	 * This method evaluates general properties.
	 */
	private static void loadProperties() {
		generalProps = new Properties();
		InputStream in = null;
		try {
			try {
				in = LODUtils.class.getResourceAsStream
					(LODUtils.generalPropertiesFile);
				generalProps.load(in);
				in.close();
			} finally {
                in.close();
			}
		} catch (FileNotFoundException e) {
			log.log(Level.SEVERE, "File not found! " + e.getMessage());
		} catch (IOException e) {
			log.log(Level.SEVERE, "General properties could not be loaded! "
					+ e.getMessage());
		}
	}

	/**
	 * This method initializes calculation model that registers LOD properties
	 * @return calculation model instance
	 */
	public static LODCalculationModel initCalculationModel() {
		LODCalculationModel calculationModel = new LODCalculationModel();
		LODProperties lodPropertiesObj = calculationModel.analyze();
		calculationModel.setLODProperties(lodPropertiesObj);
		return calculationModel;
	}

	/**
	 * This method evaluates LOD properties from XML file defined in general properties.
	 * @return evaluated LOD properties
	 */
	public static LODProperties loadLODProperties() {
		if (LODUtils.lodProperties == null
				|| LODUtils.getGeneralProperty(LOD_PROPERTIES_RELOAD_XML)
						.equalsIgnoreCase(TRUE)) {
			try {
				JAXBContext jc = JAXBContext.newInstance(RiskAnalysis.class);
				Unmarshaller u = jc.createUnmarshaller();
				InputStream in = LODUtils.class.getResourceAsStream
						(LODUtils.getGeneralProperty("LODProperties.XML"));
				lodProperties = (LODProperties) u.unmarshal(in);
			} catch (JAXBException e) {
				log.log(Level.SEVERE, e.getMessage());
				return null;
			}
			LODUtils.setGeneralProperty(LOD_PROPERTIES_RELOAD_XML, FALSE);
		}
		return lodProperties;
	}

	/**
	 * This method initializes a set of risk properties loaded from XML file.
	 * 
	 * @param xmlFile
	 *            The XML file containing properties definitions
	 * @return property set in risk analysis format
	 */
	public static LODProperties loadLODPropertiesFromXML(String xmlFile) {
		try {
			JAXBContext jc = JAXBContext.newInstance(LODProperties.class);
			Unmarshaller u = jc.createUnmarshaller();
			InputStream in = LODUtils.class.getResourceAsStream(xmlFile);
			lodProperties = (LODProperties) u.unmarshal(in);
		} catch (JAXBException e) {
			log.log(Level.SEVERE, e.getMessage());
			return null;
		}
		return lodProperties;
	}

	/**
	 * This method initializes risk property map that maps IDs to the risk
	 * property objects
	 * 
	 * @param propertyList
	 */
	public static void setLODPropertiesMap(List<LODProperty> propertyList) {
		Iterator<LODProperty> iter = propertyList.iterator();
		while (iter.hasNext()) {
			LODProperty lodProperty = iter.next();
			lodPropertyMap.put(lodProperty.getId(), lodProperty);
		}
	}

	/**
	 * This method inserts original value in LOD property identified by passed ID 
	 * for passed source repository name.
	 * @param lodPropertyId
	 * @param sourceRepositoryName
	 * @param originalValue
	 * @return resulting LOD property
	 */
	public static LODProperty insertLODSourceOriginalValue(
			String lodPropertyId, String sourceRepositoryName,
			List<String> originalValues) {
		LODProperty lodProperty = getLODPropertyById(lodPropertyId);
		Iterator<LODSource> iter = lodProperty.getLODSources().getLODSource()
				.iterator();
		while (iter.hasNext()) {
			LODSource lodSource = iter.next();
			if (lodSource.getSource().equals(sourceRepositoryName)) {
				lodSource.getOriginalValue().setOriginalValues(originalValues);
				break;
			}
		}
		return lodProperty;
	}

	/**
	 * This method retrieves original value from LOD property for passed source repository
	 * @param lodProperty
	 * @param sourceRepositoryName
	 * @return original value list 
	 */
	public static List<String> getLODSourceOriginalValue(
			LODProperty lodProperty, String sourceRepositoryName) {
		List<String> res = null;
		Iterator<LODSource> iter = lodProperty.getLODSources().getLODSource()
				.iterator();
		while (iter.hasNext()) {
			LODSource lodSource = iter.next();
			if (lodSource.getSource().equals(sourceRepositoryName)) {
				if (lodSource.getOriginalValue() != null) {
				    res = lodSource.getOriginalValue().getOriginalValues();
					break;
				}
			}
		}
		return res;
	}

	/**
	 * This method returns LOD property object identified by passed ID
	 * 
	 * @param id
	 *            The LOD property object ID
	 * @return LOD property object
	 */
	public static LODProperty getLODPropertyById(String id) {
		return lodPropertyMap.get(id);
	}

	/**
	 * @param in
	 */
	public static void setLODProperties(InputStream in) {
		try {
			JAXBContext jc = JAXBContext.newInstance(RiskAnalysis.class);
			Unmarshaller u = jc.createUnmarshaller();
			lodProperties = (LODProperties) u.unmarshal(in);
		} catch (JAXBException e) {
			log.log(Level.SEVERE, e.getMessage());
		}
	}

	/**
	 * This method generates unique IDs for LOD properties.
	 * 
	 * @return unique ID as a string
	 */
	public static String generateID() {
		return UUID.randomUUID().toString();
	}

	/**
	 * This method splits the source string by separator string.
	 * @param source
	 *        The initial string
	 * @param separator
	 *        The separator string between tokens
	 * @return string list
	 */
	public static List<String> splitString(String source, String separator) {
		List<String> result = new ArrayList<String>();
        StringTokenizer st = new StringTokenizer(source, separator);	 
    	while(st.hasMoreElements()){
    		String token = (String) st.nextElement();
    		result.add(token);
    	}
    	return result;
	}
	
	/**
	 * This method initializes risk property map that maps IDs to the risk
	 * property objects
	 * 
	 * @param propertyList
	 */
	public static String getQueryFromLODProperty(List<LODSource> sourceList, String sourceRepositoryName) {
		String res = EMPTYSTRING;
		Iterator<LODSource> iter = sourceList.iterator();
		while (iter.hasNext()) {
			LODSource lodSource = iter.next();
			if (lodSource.getSource().equals(sourceRepositoryName)) {
				res = lodSource.getQuery();
				break;
			}
		}
		return res;
	}

	/**
	 * This method searches in Freebase repository for passed parameters.
	 * 
	 * @param property
	 *            The risk property
	 * @param queryValue
	 *            The query value as string
	 * @param searchColumn
	 *            The name of the search column that contains query value
	 * @param resultColumn
	 *            The name of the result column that contains result values
	 * @param sourceRepositoryName
	 * @return list containing found values
	 */
	public static List<String> searchJSON(String property, String queryValue,
			List<String> searchColumns, List<String> resultColumns, String sourceRepositoryName) {
		List<String> resultList = new ArrayList<String>();
		RepositoryDescription lodRepository = null;
		if (sourceRepositoryName != null && sourceRepositoryName.length() > 0) {
			if (LODConstants.FREEBASE.equals(sourceRepositoryName)) {
				lodRepository = new FreebaseConnector();
			}
			readLODProperty(property, queryValue, searchColumns,
					sourceRepositoryName, lodRepository);			
			readJsonArray(resultColumns, resultList, lodRepository);
		}
		return resultList;
	}

	/**
	 * @param resultColumns
	 * @param resultList
	 * @param lodRepository
	 */
	private static void readJsonArray(List<String> resultColumns,
			List<String> resultList, RepositoryDescription lodRepository) {
		if (lodRepository != null) {
			JSONObject resultJSONObject = lodRepository.retrieveJSON();
			JSONArray jsonArray;
			try {
				jsonArray = (JSONArray) resultJSONObject.get(LODConstants.FB_RESULT);
			    for (int i=0; i < jsonArray.length(); i++) {
			    	JSONObject currentJsonObject = (JSONObject) jsonArray.get(i);
					Object value = currentJsonObject.get(resultColumns.get(0));
					log.info(FOUND + value); 
					parseJsonValue(resultColumns, resultList, value);
			    }
			} catch (JSONException e) {
				log.info(FREEBASE_JSON_RESULT_OBJECT_PARSING_ERROR + e);
			}
		}
	}

	/**
	 * @param resultColumns
	 * @param resultList
	 * @param value
	 * @throws JSONException
	 */
	private static void parseJsonValue(List<String> resultColumns,
			List<String> resultList, Object value) throws JSONException {
		if (!LODConstants.EMPTY_ARRAY.equals(value.toString())) {
			// check if value is JSONArray or JSONObject
			if(value instanceof JSONArray){
				computeJsonArray(resultColumns, resultList, value);
			} else {
		    	log.info(VALUE_FOUND + value);
		    	if (!ReportConstants.NULL.equals(value.toString())) {
//					    	LOG.info("value found: " + value.toString() + ", " + value.getClass().getName());
		    		resultList.add((String) value);
		    	}
			}
		}
	}

	/**
	 * @param resultColumns
	 * @param resultList
	 * @param value
	 * @throws JSONException
	 */
	private static void computeJsonArray(List<String> resultColumns,
			List<String> resultList, Object value) throws JSONException {
		JSONArray jsonObjArray = (JSONArray) value;
		for (int n=0; n < jsonObjArray.length(); n++) {
			Object obj = jsonObjArray.get(n);
			String subValue = null;
			if (obj instanceof JSONObject) {
		    	JSONObject jsonObj = (JSONObject) jsonObjArray.get(n);
		    	subValue = jsonObj.get(resultColumns.get(1)).toString();
			} else {
				if (!obj.getClass().getName().equals(ORG_JSON_JSON_OBJECT$_NULL)) {
					subValue = (String) obj;
				}
			}
			log.info(VALUE_FOUND + subValue);
			if (subValue != null && !resultList.contains(subValue)) {
				resultList.add(subValue);
			}
		}
	}

	/**
	 * This method reads LOD property from LOD repository (e.g. FREEBASE).
	 * @param property
	 * @param queryValue
	 * @param searchColumns
	 * @param sourceRepositoryName
	 * @param lodRepository
	 */
	private static void readLODProperty(String property, String queryValue,
			List<String> searchColumns, String sourceRepositoryName,
			RepositoryDescription lodRepository) {
		LODProperty lodProperty = LODUtils.getLODPropertyById(property);
		JSONObject json;
		try {
			String query = getQueryFromLODProperty(lodProperty.getLODSources().getLODSource(), sourceRepositoryName);
			if (lodRepository != null) {
				lodRepository.setQuery(query);
				json = new JSONObject(query);
				log.info(PROPERTY_QUERY + json);
				if (searchColumns.size() == 2) {
					if (searchColumns.get(1) != null) {
						JSONObject subJson = new JSONObject();
						JSONArray subJsonArray = new JSONArray();
						subJson.put(searchColumns.get(1), queryValue);
						subJsonArray.put(subJson);
						json.put(searchColumns.get(0), subJsonArray);
					}
				} else {
					json.put(searchColumns.get(0), queryValue);
				}
				log.info("Freebase query: " + json);
				lodRepository.setQuery(json.toString());
			}
		} catch (JSONException e) {
			log.info(FREEBASE_QUERY_JSON_OBJECT_CREATION_ERROR + e);
		} catch (Exception ex) {
			log.info("LODProperty error. " + FREEBASE_QUERY_JSON_OBJECT_CREATION_ERROR + ex);
		}
	}

	/**
	 * This method searches in Freebase repository for complex list entries using passed parameters.
	 * 
	 * @param property
	 *            The risk property
	 * @param queryValue
	 *            The query value as string
	 * @param searchColumn
	 *            The name of the search column that contains query value
	 * @param resultColumn
	 *            The name of the result column that contains result values
	 * @return list containing found values
	 */
	public static List<List<String>> searchJSONExt(String property, String queryValue,
			List<String> searchColumns, List<String> resultColumns, String sourceRepositoryName) {
		List<List<String>> resultList = new ArrayList<List<String>>();
		RepositoryDescription lodRepository = null;
		if (sourceRepositoryName != null && sourceRepositoryName.length() > 0) {
			if (LODConstants.FREEBASE.equals(sourceRepositoryName)) {
				lodRepository = new FreebaseConnector();
			}
			readLODProperty(property, queryValue, searchColumns,
					sourceRepositoryName, lodRepository);
			
			if (lodRepository != null) {
				JSONObject resultJSONObject = lodRepository.retrieveJSON();
				readJsonObjectsFromJsonArray(resultColumns, resultList, resultJSONObject);
			}
		}
		return resultList;
	}

	/**
	 * @param resultColumns
	 * @param resultList
	 * @param resultJSONObject
	 */
	private static void readJsonObjectsFromJsonArray(
			List<String> resultColumns, List<List<String>> resultList,
			JSONObject resultJSONObject) {
		JSONArray jsonArray;
		try {
			jsonArray = (JSONArray) resultJSONObject.get(LODConstants.FB_RESULT);
		    for (int i=0; i < jsonArray.length(); i++) {
		    	JSONObject currentJsonObject = (JSONObject) jsonArray.get(i);
				Object value = currentJsonObject.get(resultColumns.get(0));
				log.info(FOUND + value); 
				fillSubList(resultColumns, resultList, value);
		    }
		} catch (JSONException e) {
			log.info(FREEBASE_JSON_RESULT_OBJECT_PARSING_ERROR + e);
		} catch (Exception ex) {
			log.info("Read JSON error. " + FREEBASE_JSON_RESULT_OBJECT_PARSING_ERROR + ex);
		}
	}

	/**
	 * @param resultColumns
	 * @param resultList
	 * @param value
	 * @throws JSONException
	 */
	private static void fillSubList(List<String> resultColumns,
			List<List<String>> resultList, Object value) throws JSONException {
		if (!LODConstants.EMPTY_ARRAY.equals(value.toString())) {
			// check if value is JSONArray or JSONObject
			List<String> subValueList = new ArrayList<String>();
			if(value instanceof JSONArray){
				readJsonObject(resultColumns, resultList,
						value, subValueList);
			} else {
				subValueList.add((String) value);
		    	log.info(VALUE_FOUND + value);
		    	resultList.add(subValueList);
			}
		}
	}

	/**
	 * @param resultColumns
	 * @param resultList
	 * @param value
	 * @param subValueList
	 * @throws JSONException
	 */
	private static void readJsonObject(List<String> resultColumns,
			List<List<String>> resultList, Object value,
			List<String> subValueList) throws JSONException {
		JSONArray jsonObjArray = (JSONArray) value;
		for (int n=0; n < jsonObjArray.length(); n++) {
			Object obj = jsonObjArray.get(n);
			if (subValueList != null) {
				subValueList.clear();
			}
			for (int c = 1; c < resultColumns.size(); c++) {
		    	String subValue = extractJsonObject(
						resultColumns, jsonObjArray, n, obj, c);
		    	log.info(VALUE_FOUND + subValue);
		    	if (subValue != null) {
		    		subValueList.add(subValue);
		    	}
			}
			if (subValueList != null) {
				String[] tmp = (String[]) subValueList.toArray(new String[subValueList.size()]);
				resultList.add(Arrays.asList(tmp));
			}
		}
	}

	/**
	 * @param resultColumns
	 * @param jsonObjArray
	 * @param n
	 * @param obj
	 * @param c
	 * @return
	 * @throws JSONException
	 */
	private static String extractJsonObject(List<String> resultColumns,
			JSONArray jsonObjArray, int n, Object obj, int c)
			throws JSONException {
		String subValue = null;
		if (obj instanceof JSONObject) {
			JSONObject jsonObj = (JSONObject) jsonObjArray.get(n);
			subValue = jsonObj.get(resultColumns.get(c)).toString();
		} else {
			if (!obj.getClass().getName().equals(ORG_JSON_JSON_OBJECT$_NULL)) {
				subValue = (String) obj;
			}
		}
		return subValue;
	}

	/**
	 * This method searches in Freebase repository for JSONObject.
	 * 
	 * @param property
	 *            The risk property
	 * @param sourceRepositoryName
	 *            The name of the repository
	 * @return list containing found values
	 */
	public static JSONObject searchJSONObject(String property, String sourceRepositoryName) {
		JSONObject res = null;
		RepositoryDescription lodRepository = null;
		if (sourceRepositoryName != null && sourceRepositoryName.length() > 0) {
			if (LODConstants.FREEBASE.equals(sourceRepositoryName)) {
				lodRepository = new FreebaseConnector();
			}
			LODProperty lodProperty = LODUtils.getLODPropertyById(property);
			JSONObject json;
			try {
				String query = getQueryFromLODProperty(lodProperty.getLODSources().getLODSource(), sourceRepositoryName);
				if (lodRepository != null) {
					lodRepository.setQuery(query);
					json = new JSONObject(query);
					log.info(PROPERTY_QUERY + json);
					lodRepository.setQuery(json.toString());
					res = lodRepository.retrieveJSON();
				}
			} catch (JSONException e) {
				log.info(FREEBASE_QUERY_JSON_OBJECT_CREATION_ERROR + e);
			}	
		}
		return res;
	}

	/**
	 * This method retrieves data from Freebase applying passed LOD property ID,
	 * search value and correspondent JSON column names. LOD property source is enriched
	 * with evaluated values.
	 * @param propertyId
	 * @param searchValue
	 * @param searchColumn
	 * @param resultColumn
	 * @param resultSubColumn
	 * @return a list of retrieved values
	 */
	public static List<String> searchInFreebase(String propertyId,
			String searchValue, String searchColumn, String resultColumn,
			String resultSubColumn) {
		/**
		 * This list contains column names of search columns to support JSON
		 * request object creation using passed search values.
		 */
		List<String> searchColumns = new ArrayList<String>();

		/**
		 * This list contains column names of result columns to find searched
		 * value in resulting JSON object.
		 */
		List<String> resultColumns = new ArrayList<String>();

		log.info(SEARCH_VALUE + searchValue);
		searchColumns.clear();
		searchColumns.add(searchColumn);
		resultColumns.clear();
		resultColumns.add(resultColumn);
		resultColumns.add(resultSubColumn);
		List<String> resultList = LODUtils.searchJSON(propertyId, searchValue,
				searchColumns, resultColumns, LODConstants.FREEBASE);

		Iterator<String> resultIter = resultList.iterator();
		log.info("Result list: " + resultList.size());
		while (resultIter.hasNext()) {
			String str = resultIter.next();
			log.info(RESULT_FOUND + str);
		}

		LODUtils.insertLODSourceOriginalValue(propertyId,
				LODConstants.FREEBASE, resultList);

		return resultList;
	}

	/**
	 * This method retrieves complex list data from Freebase applying passed LOD property ID,
	 * search value and correspondent JSON column names. LOD property source is enriched
	 * with evaluated values.
	 * @param propertyId
	 * @param searchValue
	 * @param searchColumn
	 * @param resultColumn
	 * @param resultSubColumn
	 * @return a list of complex retrieved values. Each of the values is also a list.
	 */
	public static List<List<String>> searchInFreebaseExt(String propertyId,
			String searchValue, String searchColumn, List<String> resultColumns) {
		/**
		 * This list contains column names of search columns to support JSON
		 * request object creation using passed search values.
		 */
		List<String> searchColumns = new ArrayList<String>();

		log.info(SEARCH_VALUE + searchValue);
		searchColumns.clear();
		searchColumns.add(searchColumn);
		List<List<String>> resultList = LODUtils.searchJSONExt(propertyId, searchValue,
				searchColumns, resultColumns, LODConstants.FREEBASE);

		Iterator<List<String>> resultIter = resultList.iterator();
		log.info(LIST_SIZE + resultList.size());
		while (resultIter.hasNext()) {
			List<String> subList = resultIter.next();
			Iterator<String> subIter = subList.iterator();
			log.info(SUB_LIST_SIZE + subList.size());
			while (subIter.hasNext()) {
				String str = subIter.next();
				log.info(RESULT_FOUND + str);
			}
			LODUtils.insertLODSourceOriginalValue(propertyId,
					LODConstants.FREEBASE, subList);			
		}

		return resultList;
	}

	/**
	 * This method retrieves file format information for particular column name and format extension passed 
	 * as parameters
	 * @param searchFormatName
	 * @param columnName
	 * @return value list
	 */
	public static List<String> searchPronomInfoForFileFormat(
			String searchFormatName, String columnName) {
		PronomSparqlConnector lodRepository = new PronomSparqlConnector();

		LODProperty lodProperty = getLODPropertyById(LODConstants.LOD_SOFTWARE_BY_FORMAT_PROPERTY_ID);
		String query = getQueryFromLODProperty(lodProperty.getLODSources().getLODSource(), LODConstants.PRONOM);
		query = query.replaceAll(LODConstants.FORMAT_PLACEHOLDER, searchFormatName);
		lodRepository.setQuery(query); 
		lodRepository.setColumnName(columnName);
		return lodRepository.retrieve();
	}

	/**
	 * This method retrieves all PRONOM file formats
	 * @return file formats list
	 */
	public static List<PronomFileFormat> searchAllPronomFileFormats() {
		PronomSparqlConnector lodRepository = new PronomSparqlConnector();

		LODProperty lodProperty = getLODPropertyById(LODConstants.LOD_FILE_FORMAT_LIST_PROPERTY_ID);
		String query = getQueryFromLODProperty(lodProperty.getLODSources().getLODSource(), LODConstants.PRONOM);
		lodRepository.setQuery(query); 

		Map<String, List<String>> fileFormatMap = lodRepository.retrieveAll();
		List<PronomFileFormat> fileFormatList = new ArrayList<PronomFileFormat>();

		for (Entry<String, List<String>> entry : fileFormatMap.entrySet()) {
			String key = entry.getKey();
			List<String> valuesList = entry.getValue();
			Iterator<String> resultIter = valuesList.iterator();
			int index = 0;
			while (resultIter.hasNext()) {
				String value = resultIter.next();
				if (fileFormatList != null && (fileFormatList.isEmpty() || fileFormatList.size() <= index)) {
					PronomFileFormat fileFormat = new PronomFileFormat();
					fileFormat.setIndex(index);
					fileFormatList.add(fileFormat);
				}
				fileFormatList.get(index).setField(key, value);
				index++;
			}
		}
		
		return fileFormatList;
	}

	/**
	 * This method iterates through the repository response for all 
	 * stored objects filtering by search value.
	 * @param searchValue
	 * @return objects that match search value
	 */
	public static List<Map<String, String>> filterResponse(List<Map<String, String>> response, String searchValue) {
		List<Map<String, String>> list = new ArrayList<Map<String, String>>(); 
		for (Map<String, String> sparqlResult : response) {
			LODStatisticsDBPediaUtils.parseSparqlResponse(searchValue, list, sparqlResult);
		}
		return list;
	}

	/**
	 * This method retrieves all DBPedia file formats
	 * @return file formats list
	 */
	public static List<DBPediaFileFormat> searchAllDBPediaFileFormats() {
		LODProperty lodProperty = LODUtils.getLODPropertyById(LODConstants.LOD_FILE_FORMAT_LIST_PROPERTY_ID);
		String query = LODUtils.getQueryFromLODProperty(lodProperty.getLODSources().getLODSource(), LODConstants.DBPEDIA);
		DBPediaConnector dbpedia = new DBPediaConnector();
		dbpedia.setQuery(query); 
		
		List<Map<String, String>> dbpediaList = null;
		List<Map<String, String>> dbpediaListAll = null;
		dbpediaListAll = dbpedia.retrieveLODAll(null);
		if (dbpediaListAll != null) {
			dbpediaList = filterResponse(dbpediaListAll, null);
		}

		List<DBPediaFileFormat> dbpediaFileFormatList = new ArrayList<DBPediaFileFormat>();
		Iterator<Map<String, String>> iter = dbpediaList.iterator();
		while (iter.hasNext()) {
			Map<String, String> dbpediaMap = iter.next();
			Set<String> dbpediaSet = dbpediaMap.keySet();
			DBPediaFileFormat dbpediaFileFormat = new DBPediaFileFormat();
			for (String key : dbpediaSet) {
				String value = dbpediaMap.get(key);
//				log.info("DBPedia response: key=" + key + " , value=" + value);
				if (key.equals(RiskConstants.EXTENSION)) {
					dbpediaFileFormat.setExtension(value);
				}
				if (key.equals(RiskConstants.COLUMN_NAME)) {
					dbpediaFileFormat.setName(value);
				}
				if (key.equals(RiskConstants.MIME)) {
					dbpediaFileFormat.setMimetype(value);
				}
				if (key.equals(RiskConstants.LATEST_RELEASE_VERSION)) {
					dbpediaFileFormat.setVersion(value);
				}
				if (key.equals(RiskConstants.DBPEDIA_ID)) {
					dbpediaFileFormat.setPuid(value);
				}
				if (key.equals(RiskConstants.DBPEDIA_DESCRIPTION)) {
					dbpediaFileFormat.setDescription(value);
				}
			}
			dbpediaFileFormatList.add(dbpediaFileFormat);
		}
		return dbpediaFileFormatList;
	}

	/**
	 * This method evaluates if extension already exists in the file format list
	 * @param list
	 *        The file format list
	 * @param format
	 *        The query format
	 * @return true if format exists in the list
	 */
	public static boolean extensionExists(List<FreebaseFileFormat> list, String format) {
		boolean res = false;
	
		Iterator<FreebaseFileFormat> listIter = list.iterator();
		while (listIter.hasNext()) {
			FreebaseFileFormat feebaseFileFormat = listIter.next();
			if (feebaseFileFormat.getExtension().equals(format)) {
				res = true;
				break;
			}
		}
        return res;
	}
	
	/**
	 * This method evaluates Freebase format description for passed guid number.
	 * @param guid
	 * @return description text
	 */
	public static String getFreebaseFormatDescription(String guid) {
		String res = EMPTYSTRING;
		if (guid != null) {
//			try {
//				Thread.sleep(5000); // otherwise user rate limit exceeded
//			} catch (InterruptedException e1) {
//				// TODO Auto-generated catch block
//				e1.printStackTrace();
//			}
			String guidNew = guid.replaceAll(LODConstants.STRING_SEPARATOR, EMPTYSTRING);
	
			HttpClient client = new HttpClient();
			client.getHttpConnectionManager().getParams().
			setConnectionTimeout(TENSEC);
			String url = HTTP_HOTSHOT_DFHUYNH_USER_DEV_FREEBASEAPPS_COM_HTML_ID_GUID + guidNew;
			HttpMethod method = new GetMethod(url);
			method.setFollowRedirects(false);
			try {
				client.executeMethod(method);
				String responseBody = method.getResponseBodyAsString();
				log.info("Response body: " + responseBody);
				String descriptionStr = responseBody.substring(
						responseBody.indexOf(LODConstants.DESC_BEGIN) + LODConstants.DESC_BEGIN.length(),
						responseBody.indexOf(LODConstants.DESC_P_END));
				res = descriptionStr.replaceAll(LODConstants.DESC_P, EMPTYSTRING);
			} catch (HttpException he) {
				log.fine("Http error connecting to ’" + url + "’");
			} catch (IOException ioe) {
				log.fine("Unable to connect to ’" + url + "’");
			} catch (Exception e) {
				log.fine("Description retrieval exception. Unable to parse response from ’"
						+ url + "’ (Exception: " + e.getLocalizedMessage() + ")");
			}
			method.releaseConnection();
		}
		return res;
	}

	/**
	 * This method retrieves all Freebase file formats
	 * @return file formats list
	 */
	public static List<FreebaseFileFormat> searchAllFreebaseFileFormats() {
		
		List<FreebaseFileFormat> resultList = new ArrayList<FreebaseFileFormat>();
		List<String> queryFields = Arrays.asList(LODConstants.FB_EXTENSION,
				LODConstants.FB_NAME, LODConstants.FB_ID, LODConstants.FB_GUID,
				LODConstants.FB_MIME_TYPE);
		JSONObject response = LODUtils.searchJSONObject(LODConstants.LOD_FILE_FORMAT_LIST_PROPERTY_ID, LODConstants.FREEBASE);

		JSONArray jsonArray;
		try {
			jsonArray = (JSONArray) response.get(LODConstants.FB_RESULT);
		    for (int i=0; i < jsonArray.length(); i++) {
		    	FreebaseFileFormat freebaseFileFormat = new FreebaseFileFormat();
		    	JSONObject currentJsonObject = (JSONObject) jsonArray.get(i);
				Iterator<String> queryFieldsIter = queryFields.iterator();
				computeFreebaseFileFormatList(freebaseFileFormat, currentJsonObject, queryFieldsIter);
				resultList.add(freebaseFileFormat);
		    }
		} catch (JSONException e) {
			log.info(FREEBASE_JSON_RESULT_OBJECT_PARSING_ERROR + e);
		}
		return resultList;
	}

	/**
	 * @param freebaseFileFormat
	 * @param currentJsonObject
	 * @param queryFieldsIter
	 * @throws JSONException
	 */
	private static void computeFreebaseFileFormatList(
			FreebaseFileFormat freebaseFileFormat,
			JSONObject currentJsonObject, Iterator<String> queryFieldsIter)
			throws JSONException {
		while (queryFieldsIter.hasNext()) {
			String queryField = queryFieldsIter.next();
			Object value = currentJsonObject.get(queryField);
			log.info("queryField: " + queryField + ", value: " + value); 
			if (!LODConstants.EMPTY_ARRAY.equals(value.toString())) {
				// check if value is JSONArray or JSONObject
				parseFreebaseValue(freebaseFileFormat, queryField, value);
			}
		}
	}

	/**
	 * @param freebaseFileFormat
	 * @param queryField
	 * @param value
	 * @throws JSONException
	 */
	private static void parseFreebaseValue(
			FreebaseFileFormat freebaseFileFormat, String queryField,
			Object value) throws JSONException {
		if(value instanceof JSONArray){
			JSONArray jsonObjArray = (JSONArray) value;
		    for (int n=0; n < jsonObjArray.length(); n++) {
		    	Object obj = jsonObjArray.get(n);
		    	String subValue = evaluateFreebaseSubValue(jsonObjArray, n, obj);
		    	log.info(VALUE_FOUND + subValue);
		    	if (subValue != null) {
		    		if (queryField.equals(LODConstants.FB_EXTENSION)) {
						freebaseFileFormat.setExtension(subValue);
		    		}
		    		if (queryField.equals(LODConstants.FB_MIME_TYPE)) {
						freebaseFileFormat.setMimetype(subValue);
		    		}
		    	}
		    }
		} else {
			log.info(VALUE_FOUND + value);
			setFreebaseFields(freebaseFileFormat, queryField, value);
		}
	}

	/**
	 * @param jsonObjArray
	 * @param n
	 * @param obj
	 * @return
	 * @throws JSONException
	 */
	private static String evaluateFreebaseSubValue(JSONArray jsonObjArray,
			int n, Object obj) throws JSONException {
		String subValue = null;
		if (obj instanceof JSONObject) {
			JSONObject jsonObj = (JSONObject) jsonObjArray.get(n);
			subValue = jsonObj.get(LODConstants.FB_ID).toString();
		} else {
			if (!obj.getClass().getName().equals(ORG_JSON_JSON_OBJECT$_NULL)) {
				subValue = (String) obj;
			}
		}
		return subValue;
	}

	/**
	 * @param freebaseFileFormat
	 * @param queryField
	 * @param value
	 */
	private static void setFreebaseFields(
			FreebaseFileFormat freebaseFileFormat, String queryField,
			Object value) {
		if (queryField.equals(LODConstants.FB_NAME)) {
			freebaseFileFormat.setName((String) value);
		}
		if (queryField.equals(LODConstants.FB_GUID)) {
			String descriptionStr = getFreebaseFormatDescription((String) value);
			freebaseFileFormat.setDescription(descriptionStr);
		}
		if (queryField.equals(LODConstants.FB_ID)) {
			freebaseFileFormat.setPuid((String) value);
		}
	}

	/**
	 * @return
	 */
	public static List<LODPronomSoftware> retrievePronomSoftwareInfo() {
		PronomSparqlConnector lodRepository = new PronomSparqlConnector();
		LODProperty lodProperty = getLODPropertyById(LODConstants.LOD_VENDOR_BY_SOFTWARE_PROPERTY_ID);
		List<LODPronomSoftware> res = new ArrayList<LODPronomSoftware>();

		for (int i = 1; i < LODConstants.MAX_PRONOM_SOFTWARE_COUNT; i++) {
			String query = getQueryFromLODProperty(lodProperty.getLODSources().getLODSource(), LODConstants.PRONOM);
			query = query.replaceAll(LODConstants.FORMAT_PLACEHOLDER, Integer.toString(i));
			lodRepository.setQuery(query); 
			LODPronomSoftware software = lodRepository.retrievePronomSoftware();
			res.add(software);
		}
		return res;
	}

	/**
	 * @param infoList
	 * @return
	 */
	public static String getStringFromPronomList(List<String> infoList) {
		StringBuffer buf = new StringBuffer();
		buf.append(EMPTYSTRING);
		if (infoList != null) {
			Iterator<String> resultIter = infoList.iterator();
			while (resultIter.hasNext()) {
				buf.append(resultIter.next() + COMMASPACE);
			}
		}
	//	log.info("res list: " + buf.toString());
		return buf.toString();
	}

	/**
	 * This method retrieves extension list from PRONOM
	 * @param fileFormatsList
	 * @return
	 */
	public static List<String> getFileFormatsList(List<? extends FfmaDomainObject> fileFormatsList) {
		List<String> res = new ArrayList<String>();
		
		Iterator<? extends FfmaDomainObject> iter = fileFormatsList.iterator();
		while (iter.hasNext()) {
			PronomFileFormat pronomFileFormatObject = (PronomFileFormat) iter.next();
			log.info("retrieved pronomFileFormat object: " + pronomFileFormatObject);
			if (pronomFileFormatObject != null && pronomFileFormatObject.getExtension() != null && !res.contains(pronomFileFormatObject.getExtension())) {
				if (!pronomFileFormatObject.getExtension().equals(RiskConstants.UNKNOWN)) {
					res.add(pronomFileFormatObject.getExtension());
				}
			}
		}
		return res;
	}

	/**
	 * This method retrieves extension list from DBPedia
	 * @param fileFormatsList
	 * @return
	 */
	public static List<String> getDBPediaFileFormatsList(List<? extends FfmaDomainObject> fileFormatsList) {
		List<String> res = new ArrayList<String>();
		
		Iterator<? extends FfmaDomainObject> iter = fileFormatsList.iterator();
		while (iter.hasNext()) {
			DBPediaFileFormat dbpediaFileFormatObject = (DBPediaFileFormat) iter.next();
			log.info("retrieved dbpediaFileFormat object: " + dbpediaFileFormatObject);
			if (dbpediaFileFormatObject != null && dbpediaFileFormatObject.getExtension() != null && !res.contains(dbpediaFileFormatObject.getExtension())) {
				res.add(dbpediaFileFormatObject.getExtension());
			}
		}
		return res;
	}

	/**
	 * This method retrieves extension list from Freebase
	 * @param fileFormatsList
	 * @return
	 */
	public static List<String> getFreebaseFileFormatsList(List<? extends FfmaDomainObject> fileFormatsList) {
		List<String> res = new ArrayList<String>();
		
		Iterator<? extends FfmaDomainObject> iter = fileFormatsList.iterator();
		while (iter.hasNext()) {
			FreebaseFileFormat freebaseFileFormatObject = (FreebaseFileFormat) iter.next();
			log.info("retrieved freebaseFileFormat object: " + freebaseFileFormatObject);
			if (freebaseFileFormatObject != null && freebaseFileFormatObject.getExtension() != null && !res.contains(freebaseFileFormatObject.getExtension())) {
				res.add(freebaseFileFormatObject.getExtension());
			}
		}
		return res;
	}

	/**
	 * This method retrieves extension list from AIT
	 * @param fileFormatsList
	 * @return
	 */
	public static List<DipFormatId> getAitDipFileFormatsList(List<? extends FfmaDomainObject> fileFormatsList) {
		List<DipFormatId> res = new ArrayList<DipFormatId>();
		
		Iterator<? extends FfmaDomainObject> iter = fileFormatsList.iterator();
		while (iter.hasNext()) {
			AitFileFormat aitFileFormatObject = (AitFileFormat) iter.next();
			log.info("retrieved aitFileFormat object: " + aitFileFormatObject);
			if (aitFileFormatObject != null
					&& aitFileFormatObject.getExtension() != null
					&& !checkDipFormatIdsForExtension(res,
							aitFileFormatObject.getExtension())) {
				if (!aitFileFormatObject.getExtension().equals(RiskConstants.UNKNOWN)) {
					String currentFileFormat = aitFileFormatObject.getExtension();
					DipFormatId dipFormatId = initSearchDipFormatId(currentFileFormat);
					List<String> idList = new ArrayList<String>();
					String puid = aitFileFormatObject.getPuid();
					String xpuid = aitFileFormatObject.getXpuid();
					puid = computeIdFromPuidAndXpuid(aitFileFormatObject,
							dipFormatId, idList, puid, xpuid);
					addDescriptionList(dipFormatId, puid,
							aitFileFormatObject.getDescription(),
							LODConstants.AIT_BEGIN, LODConstants.AIT_END);
					res.add(dipFormatId);
				}
			}
		}
		return res;
	}

	/**
	 * @param aitFileFormatObject
	 * @param dipFormatId
	 * @param idList
	 * @param puid
	 * @param xpuid
	 * @return
	 */
	private static String computeIdFromPuidAndXpuid(
			AitFileFormat aitFileFormatObject, DipFormatId dipFormatId,
			List<String> idList, String puidStr, String xpuid) {
		String puid = puidStr;
		if (puid != null && puid.length() > 0) {
			if (!puid.equals(LODConstants.EMPTY_PUID)) {					
				idList.add(aitFileFormatObject.getPuid());
			}
			if (xpuid != null && xpuid.length() > 0 && puid.equals(LODConstants.EMPTY_PUID)) {
				idList.add(aitFileFormatObject.getXpuid());
				puid = xpuid;
			}
			dipFormatId.setAitId(idList.toArray(new String[idList.size()]));
		}
		return puid;
	}

	/**
	 * This method adds description list to the DipFormatId object.
	 * @param dipFormatId
	 * @param puid
	 * @param description
	 * @param beginConst
	 * @param endConst
	 */
	protected static void addDescriptionList(DipFormatId dipFormatId,
			String puid, String description, String beginConst, String endConst) {
		List<String> descList = new ArrayList<String>();
		if (description != null && description.length() > 0) {
			descList.add(beginConst + LEFTARROW
					+ puid + RIGHTARROW
					+ description + LEFTARROWEND
					+ puid + RIGHTARROW
					+ endConst);
			dipFormatId.setDescription(descList.toArray(new String[descList.size()]));
		}
	}

	/**
	 * This method retrieves extension list from PRONOM
	 * @param fileFormatsList
	 * @return
	 */
	public static List<DipFormatId> getPronomDipFileFormatsList(List<? extends FfmaDomainObject> fileFormatsList) {
		List<DipFormatId> res = new ArrayList<DipFormatId>();
		
		Iterator<? extends FfmaDomainObject> iter = fileFormatsList.iterator();
		while (iter.hasNext()) {
			PronomFileFormat pronomFileFormatObject = (PronomFileFormat) iter.next();
			log.info("retrieved pronomFileFormat object: " + pronomFileFormatObject);
			if (pronomFileFormatObject != null
					&& pronomFileFormatObject.getExtension() != null
					&& !checkDipFormatIdsForExtension(res,
							pronomFileFormatObject.getExtension())) {
				if (!pronomFileFormatObject.getExtension().equals(RiskConstants.UNKNOWN)) {
					String currentFileFormat = pronomFileFormatObject.getExtension();
					DipFormatId dipFormatId = initSearchDipFormatId(currentFileFormat);
					List<String> idList = new ArrayList<String>();
					String puid = pronomFileFormatObject.getPuid();
					String xpuid = pronomFileFormatObject.getXpuid();
					puid = computeIdFromPuidAndXpuidForPronom(
							pronomFileFormatObject, dipFormatId, idList, puid,
							xpuid);
					addDescriptionList(dipFormatId, puid,
							pronomFileFormatObject.getDescription(),
							LODConstants.PRONOM_BEGIN, LODConstants.PRONOM_END);
					res.add(dipFormatId);
				}
			}
		}
		return res;
	}

	/**
	 * @param pronomFileFormatObject
	 * @param dipFormatId
	 * @param idList
	 * @param puid
	 * @param xpuid
	 * @return
	 */
	private static String computeIdFromPuidAndXpuidForPronom(
			PronomFileFormat pronomFileFormatObject, DipFormatId dipFormatId,
			List<String> idList, String puidStr, String xpuid) {
		String puid = puidStr;
		if (puid != null && puid.length() > 0) {
			if (!puid.equals(LODConstants.EMPTY_PUID)) {					
				idList.add(pronomFileFormatObject.getPuid());
			}
			if (xpuid != null && xpuid.length() > 0 && puid.equals(LODConstants.EMPTY_PUID)) {
				idList.add(pronomFileFormatObject.getXpuid());
				puid = xpuid;
			}
			dipFormatId.setPronomId(idList.toArray(new String[idList.size()]));
		}
		return puid;
	}

	/**
	 * This method initializes DipFormatId object for the search in database.
	 * @param currentFileFormat
	 * @return DipFormatId object
	 */
	private static DipFormatId initSearchDipFormatId(String currentFileFormat) {
		DipFormatId dipFormatId = new DipFormatId();
		dipFormatId.setDipId(LODConstants.DIP + currentFileFormat);
		dipFormatId.setExtension(currentFileFormat);
		return dipFormatId;
	}

	/**
	 * This method evaluates if current extension already exists in the list.
	 * @param dipFormatIdList
	 * @param extension
	 * @return
	 */
	public static boolean checkDipFormatIdsForExtension(List<DipFormatId> dipFormatIdList, String extension) {
		boolean res = false;
		Iterator<DipFormatId> iter = dipFormatIdList.iterator();
		while (iter.hasNext()) {
			DipFormatId dipFormatId = iter.next();
			if (dipFormatId.getExtension().contains(extension)) {
				res = true;
			}
		}
		return res;
	}
	
	/**
	 * This method retrieves extension list from DBPedia
	 * @param fileFormatsList
	 * @return
	 */
	public static List<DipFormatId> getDBPediaDipFileFormatsList(List<? extends FfmaDomainObject> fileFormatsList) {
		List<DipFormatId> res = new ArrayList<DipFormatId>();
		
		Iterator<? extends FfmaDomainObject> iter = fileFormatsList.iterator();
		while (iter.hasNext()) {
			DBPediaFileFormat dbpediaFileFormatObject = (DBPediaFileFormat) iter.next();
			log.info("retrieved dbpediaFileFormat object: " + dbpediaFileFormatObject);
			if (dbpediaFileFormatObject != null
					&& dbpediaFileFormatObject.getExtension() != null
					&& !checkDipFormatIdsForExtension(res,
							dbpediaFileFormatObject.getExtension())) {
				String currentFileFormat = dbpediaFileFormatObject.getExtension();
				DipFormatId dipFormatId = initSearchDipFormatId(currentFileFormat);
				List<String> idList = new ArrayList<String>();
				String puid = dbpediaFileFormatObject.getPuid();
				if (puid != null && puid.length() > 0) {
					idList.add(puid);
					dipFormatId.setDBPediaId(idList.toArray(new String[idList.size()]));
				}
				addDescriptionList(dipFormatId, puid,
						dbpediaFileFormatObject.getDescription(),
						LODConstants.DBPEDIA_BEGIN, LODConstants.DBPEDIA_END);
				res.add(dipFormatId);
			}
		}
		return res;
	}

	/**
	 * This method sets DipId for DipFormatId object.
	 * @param ext
	 * @return dipFormatId object
	 */
	public static DipFormatId setDipFormatId(String ext) {
		DipFormatId dipFormatId = new DipFormatId();
		if (ext != null && ext.length() > 0) {
			dipFormatId.setDipId(LODConstants.DIP + ext);
		}
		return dipFormatId;
	}
	
	/**
	 * Check value for null and insert in HTML table
	 * @param value
	 *        The value to insert
	 * @return resulting value
	 */
	public static String insertValue(String value) {
		String res = ReportConstants.TD_TD;
		if (value != null && value.length() > 0) {
			String valueRes = value;
			if (value.contains(LODConstants.LEFT_BRACE) && value.contains(LODConstants.RIGHT_BRACE)) {
				valueRes = value.replaceAll(LODConstants.LEFT_BRACE,
						LODConstants.LEFT_BRACE_HTML)
						.replaceAll(LODConstants.RIGHT_BRACE,
								LODConstants.RIGHT_BRACE_HTML);
			}
		    res = ReportConstants.TD + valueRes + ReportConstants.TDEND;
		}
		return res;
	}
	
	/**
	 * Check string array value for null and insert in HTML table
	 * @param value
	 *        The value to insert
	 * @return resulting value
	 */
	public static String insertValueArray(String[] value) {
		String res = ReportConstants.TD_TD;
		if (value != null && value.length > 0) {
			StringBuffer sumStr = new StringBuffer();
			sumStr.append(ReportConstants.EMPTYSTRING);
			List<String> valueList = Arrays.asList(value);
			Iterator<String> valueIter = valueList.iterator();
			while (valueIter.hasNext()) {
				String valueEntry = valueIter.next();
				if (valueEntry.contains(LODConstants.LEFT_BRACE) && valueEntry.contains(LODConstants.RIGHT_BRACE)) {
					valueEntry = valueEntry.replaceAll(LODConstants.LEFT_BRACE,
							LODConstants.LEFT_BRACE_HTML)
							.replaceAll(LODConstants.RIGHT_BRACE,
									LODConstants.RIGHT_BRACE_HTML);
				}
				sumStr.append(valueEntry);
				sumStr.append(ReportConstants.P);
			}
		    res = ReportConstants.TD + sumStr.toString() + ReportConstants.TDEND;
		}
		return res;
	}
	
	/**
	 * Check string array value for null and insert in HTML table
	 * @param value
	 *        The value to insert
	 * @return resulting value
	 */
	public static String insertValueArrayExt(String[] value) {
		String res = ReportConstants.TD_TD;
		if (value != null && value.length > 0) {
			StringBuffer sumStr = new StringBuffer();
			sumStr.append(ReportConstants.EMPTYSTRING);
			List<String> valueList = Arrays.asList(value);
			Iterator<String> valueIter = valueList.iterator();
			int count = 1;
			while (valueIter.hasNext()) {
				String valueEntry = valueIter.next();
				if (valueEntry.contains(LODConstants.LEFT_BRACE) && valueEntry.contains(LODConstants.RIGHT_BRACE)) {
					valueEntry = valueEntry.replaceAll(LODConstants.LEFT_BRACE,
							LODConstants.LEFT_BRACE_HTML)
							.replaceAll(LODConstants.RIGHT_BRACE,
									LODConstants.RIGHT_BRACE_HTML);
				}
				sumStr.append("  " + count + ". ");
				sumStr.append(valueEntry);
//				sumStr.append(ReportConstants.P);
				count++;
			}
		    res = ReportConstants.TD + sumStr.toString() + ReportConstants.TDEND;
		}
		return res;
	}
	

}
