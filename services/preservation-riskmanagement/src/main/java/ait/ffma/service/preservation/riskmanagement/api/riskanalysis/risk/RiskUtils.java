package ait.ffma.service.preservation.riskmanagement.api.riskanalysis.risk;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import ait.ffma.service.preservation.riskmanagement.api.lod.LODStatisticsDBPediaUtils;
import ait.ffma.service.preservation.riskmanagement.api.preservationwatch.DBPediaConnector;
import ait.ffma.service.preservation.riskmanagement.api.preservationwatch.FreebaseConnector;
import ait.ffma.service.preservation.riskmanagement.api.preservationwatch.RepositoryDescription;
import ait.ffma.service.preservation.riskmanagement.api.riskanalysis.profile.DataItemProfile;
import ait.ffma.service.preservation.riskmanagement.api.riskanalysis.risk.riskmodel.RiskAnalysis;
import ait.ffma.service.preservation.riskmanagement.api.riskanalysis.risk.riskmodel.RiskProperty;
import ait.ffma.service.preservation.riskmanagement.api.riskanalysis.risk.riskmodel.RiskPropertySet;

/**
 * @author Maria Egly (AIT) - maria.egly@ait.ac.at
 * @since 14.12.2010
 ********************************************************* 
 *        Copyright (c) 2010, 2011 The Ffma4Europeana Project Partners.
 * 
 *        All rights reserved. This program and the accompanying materials are
 *        made available under the terms of the European Union Public Licence
 *        (EUPL), version 1.1 which accompanies this distribution, and is
 *        available at http://ec.europa.eu/idabc/eupl.html
 * 
 *********************************************************** 
 *        Parts of this work is based on The Planets Project Copyright
 *        www.openplanetsfoundation.org Apache License, Version 2.0
 *        http://www.apache.org/licenses/LICENSE-2.0.txt
 *********************************************************** 
 */

public final class RiskUtils {

	/**
	 * To prevent public constructor
	 */
	private RiskUtils() {}
	
	/**
	 * A logger object.
	 */
	private static final Logger LOG = Logger.getLogger(RiskUtils.class
			.getName());

	/**
	 * general property file name
	 */
	private static String generalPropertiesFile = "Ffma-preservation-riskmanagement-riskanalysis.general.properties";
	
	/**
	 * risk property file
	 */
	private static String riskPropertiesFile = "Ffma-preservation-riskmanagement-riskanalysis.risk.properties";
	
	/**
	 * risk analysis object
	 */
	private volatile static RiskAnalysis ra = null;

	/**
	 * general properties
	 */
	private static Properties generalProps = null;
	
	/**
	 * risk properties
	 */
	private static Properties riskProps = null;

	/**
	 * risk property map
	 */
	private static Map<String, RiskProperty> riskPropertyMap = new HashMap<String, RiskProperty>();

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
	 * @param key
	 * @param value
	 */
	public static void setRiskProperty(String key, String value) {
		riskProps.put(key, value);
	}

	/**
	 * @param key
	 * @return
	 */
	public static String getRiskProperty(String key) {
		if (riskProps == null
				|| RiskUtils.getGeneralProperty(
						"RiskAnalysis.ReloadRiskProperties").equalsIgnoreCase(
						"true")) {
			loadProperties();
			RiskUtils.setGeneralProperty("RiskAnalysis.ReloadRiskProperties",
					"false");
		}
		return riskProps.getProperty(key);
	}

	/**
	 * This method loads properties from property file.
	 */
	private static void loadProperties() {
		// load general properties
		generalProps = new Properties();
		InputStream in = null;
		try {
			try {
				in = RiskUtils.class.getResourceAsStream
					(RiskUtils.generalPropertiesFile);
				generalProps.load(in);
				in.close();
			} finally {
                in.close();
			}
		} catch (FileNotFoundException e) {
			LOG.log(Level.SEVERE, "File not found! " + e.getMessage());
		} catch (IOException e) {
			LOG.log(Level.SEVERE, "General properties could not be loaded! "
					+ e.getMessage());
		}
		// load risk properties
		riskProps = new Properties();
		try {
			try {
				in = RiskUtils.class.getResourceAsStream
							(RiskUtils.riskPropertiesFile);
				riskProps.load(in);
				in.close();
			} finally {
                in.close();
			}
		} catch (FileNotFoundException e) {
			LOG.log(Level.SEVERE, "File not found! " + e.getMessage());
		} catch (IOException e) {
			LOG.log(Level.SEVERE,
					"Risk properties could not be loaded! " + e.getMessage());
		}
	}

	/**
	 * This method returns risk analysis object.
	 * @return
	 */
	public static RiskAnalysis loadRiskAnalysis() {
		if (RiskUtils.ra == null
				|| RiskUtils.getGeneralProperty("RiskAnalysis.ReloadXML")
						.equalsIgnoreCase("true")) {
			try {
				JAXBContext jc = JAXBContext.newInstance(RiskAnalysis.class);
				Unmarshaller u = jc.createUnmarshaller();
				InputStream in = RiskUtils.class.getResourceAsStream
						(RiskUtils.getGeneralProperty("Riskanalysis.XML"));
				ra = (RiskAnalysis) u.unmarshal(in);
			} catch (JAXBException e) {
				LOG.log(Level.SEVERE, e.getMessage());
				return null;
			}
			RiskUtils.setGeneralProperty("RiskAnalysis.ReloadXML", "false");
		}
		resetProperties(ra.getRiskFactors());
		return ra;
	}

	/**
	 * This method initializes a set of risk properties loaded from XML file.
	 * 
	 * @param xmlFile
	 *            The XML file containing properties definitions
	 * @return property set in risk analysis format
	 */
	public static RiskAnalysis loadRiskPropertiesFromXML(String xmlFile) {
		try {
			JAXBContext jc = JAXBContext.newInstance(RiskAnalysis.class);
			Unmarshaller u = jc.createUnmarshaller();
			InputStream in = RiskUtils.class.getResourceAsStream(xmlFile);
			ra = (RiskAnalysis) u.unmarshal(in);
		} catch (JAXBException e) {
			LOG.log(Level.SEVERE, e.getMessage());
			return null;
		}
		// resetProperties(ra.getRiskFactors());
		return ra;
	}

	/**
	 * This method initializes risk property map that maps IDs to the risk
	 * property objects
	 * 
	 * @param propertyList
	 */
	public static void setRiskPropertiesMap(List<RiskProperty> propertyList) {
		Iterator<RiskProperty> iter = propertyList.iterator();
		while (iter.hasNext()) {
			RiskProperty riskProperty = iter.next();
			riskPropertyMap.put(riskProperty.getId(), riskProperty);
		}
	}

	/**
	 * This method returns risk property object identified by passed ID
	 * 
	 * @param id
	 *            The risk property object ID
	 * @return risk property object
	 */
	public static RiskProperty getRiskPropertyById(String id) {
		return riskPropertyMap.get(id);
	}

	/**
	 * This method writes java object to XML file
	 * 
	 * @param object
	 *        The Java object that should be converted to XML format
	 * @param filePath
	 *        The path to the place where created XML file should be stored
	 */
	public static void storeRiskDataInXML(Object object,
			String filePath) {
		try {
			FileWriter fileWriter = new FileWriter(filePath);
			try {
				JAXBContext context = JAXBContext.newInstance(object.getClass());
				Marshaller marshaller = context.createMarshaller();
				marshaller.marshal(object, fileWriter);
				fileWriter.close();
			} finally {
                fileWriter.close();
			}
		} catch (JAXBException e) {
			LOG.log(Level.SEVERE, e.getMessage());
		} catch (IOException e) {
			LOG.log(Level.SEVERE, e.getMessage());
		}
	}

	/**
	 * @param in
	 */
	public static void setRiskAnalysis(InputStream in) {
		try {
			JAXBContext jc = JAXBContext.newInstance(RiskAnalysis.class);
			Unmarshaller u = jc.createUnmarshaller();
			ra = (RiskAnalysis) u.unmarshal(in);
		} catch (JAXBException e) {
			LOG.log(Level.SEVERE, e.getMessage());
		}
		resetProperties(ra.getRiskFactors());
	}

	/**
	 * reset all property values to null and all risk scores of property sets to -1
	 * @param set
	 */
	private static void resetProperties(RiskPropertySet set) {
		for (RiskPropertySet ps : set.getPropertySets().getPropertySet()) {
			resetProperties(ps);
		}
		for (RiskProperty p : set.getProperties().getProperty()) {
			p.setValue(null);
		}
		set.setRiskScore(-1);
	}

	/**
	 * This method generates unique IDs for risk properties and risk property
	 * sets.
	 * 
	 * @return unique ID as a string
	 */
	public static String generateID() {
		return UUID.randomUUID().toString();
	}

	/**
	 * This method evaluates value for passed risk property from repository
	 * identified by passed parameter. Connector used to hold property data for
	 * risk analysis and to define request query.
	 * 
	 * @param propertyId
	 *            The risk property
	 * @param columnNames
	 *            The column names in repository
	 * @param searchValue
	 *            The request value
	 * @param repository
	 * @param query
	 *            The request query
	 * @return connector filled with found value
	 */
	public static Connector updateConnector(String propertyId,
			List<String> columnNames, String searchValue,
			RepositoryDescription repository, String query) {
		RiskProperty property = getRiskPropertyById(propertyId);
		ConnectorImpl connector = new ConnectorImpl(property);
		if (columnNames != null) {
			for (int i = 0; i < columnNames.size(); i++) {
				connector.getColumnNames().add(columnNames.get(i));
			}
		}
		connector.setSearchValue(searchValue);
		repository.setConnector(connector);
		if (query != null) {
			repository.setQuery(query);
		}
		return repository.update();
	}

	/**
	 * This method evaluates value for passed risk property from repository
	 * identified by passed parameter. Connector used to hold property data for
	 * risk analysis and to define request query.
	 * 
	 * @param propertyId
	 *            The risk property
	 * @param columnNames
	 *            The column names in repository
	 * @param searchValue
	 *            The request value
	 * @param repository
	 * @return connector filled with found value
	 */
	public static Connector updateConnector(String propertyId,
			List<String> columnNames, String searchValue,
			RepositoryDescription repository) {
		return updateConnector(propertyId, columnNames, searchValue,
				repository, null);
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
	 * @return list containing found values
	 */
	public static List<String> searchJSON(String property, String queryValue,
			List<String> searchColumns, List<String> resultColumns) {
		List<String> resultList = new ArrayList<String>();
		RepositoryDescription freebase = new FreebaseConnector();
		RiskProperty riskProperty = RiskUtils.getRiskPropertyById(property);
		ConnectorImpl connector = new ConnectorImpl(riskProperty);
		extractJsonObject(queryValue, searchColumns, connector);		
		freebase.setConnector(connector);
		JSONObject resultJSONObject = freebase.retrieveJSON();
		JSONArray jsonArray;
		try {
			jsonArray = (JSONArray) resultJSONObject.get(RiskConstants.FB_RESULT);
		    for (int i=0; i < jsonArray.length(); i++) {
		    	JSONObject currentJsonObject = (JSONObject) jsonArray.get(i);
				Object value = currentJsonObject.get(resultColumns.get(0));
				LOG.info("found: " + value); 
				if (!RiskConstants.EMPTY_ARRAY.equals(value.toString())) {
					// check if value is JSONArray or JSONObject
					readJsonValue(resultColumns, resultList, value);
				}
		    }
		} catch (JSONException e) {
			LOG.info("Freebase JSON result object parsing error: " + e);
		}
		return resultList;
	}

	/**
	 * @param queryValue
	 * @param searchColumns
	 * @param connector
	 */
	private static void extractJsonObject(String queryValue,
			List<String> searchColumns, ConnectorImpl connector) {
		JSONObject json;
		try {
			json = new JSONObject(connector.getQuery());
			LOG.info("Property query: " + json);
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
			LOG.info("Freebase query: " + json);
			connector.setQuery(json.toString());
		} catch (JSONException e) {
			LOG.info("Freebase query JSON object creation error: " + e);
		}
	}

	/**
	 * @param resultColumns
	 * @param resultList
	 * @param value
	 * @throws JSONException
	 */
	private static void readJsonValue(List<String> resultColumns,
			List<String> resultList, Object value) throws JSONException {
		if(value instanceof JSONArray){
			JSONArray jsonObjArray = (JSONArray) value;
		    for (int n=0; n < jsonObjArray.length(); n++) {
		    	Object obj = jsonObjArray.get(n);
		    	String subValue = readJsonObject(resultColumns,
						jsonObjArray, n, obj);
		    	LOG.info("name found: " + subValue);
		    	if (subValue != null && !resultList.contains(subValue)) {
		    		resultList.add(subValue);
		    	}
		    }
		} else {
			LOG.info("value found: " + value);
			resultList.add((String) value);
		}
	}

	/**
	 * @param resultColumns
	 * @param jsonObjArray
	 * @param n
	 * @param obj
	 * @return
	 * @throws JSONException
	 */
	private static String readJsonObject(List<String> resultColumns,
			JSONArray jsonObjArray, int n, Object obj)
			throws JSONException {
		String subValue = null;
		if (obj instanceof JSONObject) {
			JSONObject jsonObj = (JSONObject) jsonObjArray.get(n);
			subValue = jsonObj.get(resultColumns.get(1)).toString();
		} else {
			if (!obj.getClass().getName().equals("org.json.JSONObject$Null")) {
				subValue = (String) obj;
			}
		}
		return subValue;
	}

	/**
	 * This method searches in DBPedia for passed parameters.
	 * 
	 * @param property
	 *            The risk property
	 * @param queryValue
	 *            The query value as string
	 * @param searchColumn
	 *            The name of the search column that contains query value
	 * @param resultColumn
	 *            The name of the result column that contains result values
	 * @param filter
	 *            Filter response list if true
	 * @return list containing found values
	 */
	public static List<String> search(String property, String queryValue,
			String searchColumn, String resultColumn, boolean filter) {
		List<String> resultList = new ArrayList<String>();
		RepositoryDescription dbpedia = new DBPediaConnector();

		RiskProperty riskProperty = RiskUtils.getRiskPropertyById(property);
		ConnectorImpl connector = new ConnectorImpl(riskProperty);

		if (filter) {
			connector.setSearchValue(queryValue);
		}
		dbpedia.setConnector(connector);
		List<Map<String, String>> dbpediaList = dbpedia.retrieveAll();

		Iterator<Map<String, String>> iter = dbpediaList.iterator();
		while (iter.hasNext()) {
			Map<String, String> dbpediaMap = iter.next();
			LODStatisticsDBPediaUtils.getDBPediaValuesList(queryValue, searchColumn, resultColumn, resultList, dbpediaMap);
		}
		return resultList;
	}

	/**
	 * This method searches in DBPedia for software that is compatible with
	 * passed extension.
	 * 
	 * @param extension
	 *            The file format extension like "PDF"
	 * @return software names
	 */
	public static List<String> searchCompatibleSoftwareForExtension(
			String extension) {
		return search(RiskConstants.SOFTWARE_PROPERTY_ID,
				extension, RiskConstants.ABSTRACT,
				RiskConstants.LABEL, false);
	}

	/**
	 * This method searches in DBPedia for software that is compatible with
	 * passed mime type.
	 * 
	 * @param mime 
	 *        The mime type like "image/tiff"
	 * @return software names
	 */
	public static List<String> searchSoftwareForMime(String mime) {
		return search(RiskConstants.GRAPHICS_FILE_FORMATS_PROPERTY_ID,
				mime, RiskConstants.MIME,
				RiskConstants.COLUMN_NAME, false);
	}

	/**
	 * This method searches in DBPedia for software that is compatible with
	 * passed standard.
	 * 
	 * @param standard
	 *        The standard like "ISO/IEC 10918"
	 * @return software names
	 */
	public static List<String> searchSoftwareForStandard(String standard) {
		return search(RiskConstants.GRAPHICS_FILE_FORMATS_PROPERTY_ID,
				standard, RiskConstants.STANDARD,
				RiskConstants.COLUMN_NAME, false);
	}

	/**
	 * This method searches in DBPedia for software that is compatible with
	 * passed mime type.
	 * 
	 * @param mime 
	 *        The mime type like "image/tiff"
	 * @param version 
	 *        The software last released version like "1"
	 * @return software names
	 */
	public static List<String> searchSoftwareForMimeAndVersion(String mime, String version) {
		List<String> resultList = new ArrayList<String>();
		RepositoryDescription dbpedia = new DBPediaConnector();

		RiskProperty riskProperty = RiskUtils.getRiskPropertyById(RiskConstants.GRAPHICS_FILE_FORMATS_PROPERTY_ID);
		ConnectorImpl connector = new ConnectorImpl(riskProperty);

		if (mime != null) {
			connector.setSearchValue(mime);
		}
		dbpedia.setConnector(connector);
		List<Map<String, String>> dbpediaList = dbpedia.retrieveAll();

		Iterator<Map<String, String>> iter = dbpediaList.iterator();
		while (iter.hasNext()) {
			Map<String, String> dbpediaMap = iter.next();
			if (dbpediaMap.get(RiskConstants.MIME).contains(mime) && dbpediaMap.get(RiskConstants.LATEST_RELEASE_VERSION).contains(version)) {
				if (!resultList.contains(dbpediaMap.get(RiskConstants.COLUMN_NAME))) {
					resultList.add(dbpediaMap.get(RiskConstants.COLUMN_NAME));
				}
			}
		}
		return resultList;
	}

	/**
	 * This method searches in DBPedia for software that was released after passed date.
	 * 
	 * @param data 
	 *        The year of the latest release
	 * @return software names
	 */
	public static List<String> searchSoftwareReleasedAfter(String date) {
		List<String> resultList = new ArrayList<String>();
		RepositoryDescription dbpedia = new DBPediaConnector();

		RiskProperty riskProperty = RiskUtils.getRiskPropertyById(RiskConstants.GRAPHICS_FILE_FORMATS_PROPERTY_ID);
		ConnectorImpl connector = new ConnectorImpl(riskProperty);

		dbpedia.setConnector(connector);
		List<Map<String, String>> dbpediaList = dbpedia.retrieveAll();

		Iterator<Map<String, String>> iter = dbpediaList.iterator();
		while (iter.hasNext()) {
			Map<String, String> dbpediaMap = iter.next();
			String value = dbpediaMap.get(RiskConstants.COLUMN_RELEASED);
			if (value != null && value.compareTo(date) > 0) {
				if (!resultList.contains(dbpediaMap.get(RiskConstants.COLUMN_NAME))) {
					resultList.add(dbpediaMap.get(RiskConstants.COLUMN_NAME));
				}
			}
		}
		return resultList;
	}

	/**
	 * This method searches in DBPedia for file extensions that are compatible
	 * with passed software name.
	 * 
	 * @param softwareName
	 *            The software name like "Adobe Flash"
	 * @return file extension names
	 */
	public static List<String> searchFileExtensionsForSoftware(
			String softwareName) {
		return search(RiskConstants.GRAPHICS_FILE_FORMATS_PROPERTY_ID,
				softwareName, RiskConstants.COLUMN_NAME,
				RiskConstants.EXTENSION, true);
	}

	/**
	 * This method searches in DBPedia for file extensions that are compatible
	 * with passed vendor name.
	 * 
	 * @param vendorName
	 *            The vendor name like "Adobe Systems"
	 * @return file extension names
	 */
	public static List<String> searchFileExtensionsForVendor(String vendorName) {
		return search(RiskConstants.GRAPHICS_FILE_FORMATS_PROPERTY_ID,
				vendorName, RiskConstants.OWNER, RiskConstants.EXTENSION, true);
	}

	/**
	 * This method searches in DBPedia for vendors that are compatible
	 * with passed file extension.
	 * 
	 * @param extension
	 *            The file extension like ".pdf"
	 * @return vendor names
	 */
	public static List<String> searchVendorForFileExtension(String extension) {
		return search(RiskConstants.GRAPHICS_FILE_FORMATS_PROPERTY_ID,
				extension, RiskConstants.EXTENSION, RiskConstants.OWNER, true);
	}

	/**
	 * This method searches in DBPedia for vendors that are compatible
	 * with passed format name.
	 * 
	 * @param formatName
	 *            The format name like "Portable Document Format"
	 * @return vendor names
	 */
	public static List<String> searchVendorForFormatName(String formatName) {
		return search(RiskConstants.GRAPHICS_FILE_FORMATS_PROPERTY_ID,
				formatName, RiskConstants.COLUMN_NAME, RiskConstants.OWNER, true);
	}

	/**
	 * This method searches in DBPedia for vendors that are compatible
	 * with passed format code.
	 * 
	 * @param formatCode
	 *            The format code like "GIF"
	 * @return vendor names
	 */
	public static List<String> searchVendorForFormatCode(String formatCode) {
		return search(RiskConstants.GRAPHICS_FILE_FORMATS_PROPERTY_ID,
				formatCode, RiskConstants.TYPE_CODE, RiskConstants.OWNER, true);
	}

	/**
	 * This method searches in DBPedia for file formats that are compatible
	 * with passed vendor name.
	 * 
	 * @param vendorName
	 *            The vendor name like "Adobe Systems"
	 * @return file format names
	 */
	public static List<String> searchFileFormatsForVendor(String vendorName) {
		return search(RiskConstants.GRAPHICS_FILE_FORMATS_PROPERTY_ID,
				vendorName, RiskConstants.OWNER, RiskConstants.COLUMN_NAME, true);
	}

	/**
	 * This method searches in DBPedia for format extensions matching to passed
	 * format name.
	 * 
	 * @param searchFormatName
	 *            The format name (like "Windows Bitmap" for type code BMP or
	 *            "Tagged Image File Format" for type code TIFF)
	 * @return extensions string list - each string can contain multiple formats
	 */
	public static List<String> searchExtensionFromFormatName(
			String searchFormatName) {
		return search(RiskConstants.GRAPHICS_FILE_FORMATS_PROPERTY_ID,
				searchFormatName, RiskConstants.COLUMN_NAME,
				RiskConstants.EXTENSION, true);
	}

	/**
	 * This method searches in DBPedia for format extensions matching to passed
	 * format code.
	 * 
	 * @param searchFormatCode
	 *            The format code (like "TIFF")
	 * @return extensions string list - each string can contain multiple formats
	 */
	public static List<String> searchExtensionFromFormatCode(
			String searchFormatCode) {
		return search(RiskConstants.GRAPHICS_FILE_FORMATS_PROPERTY_ID,
				searchFormatCode, RiskConstants.TYPE_CODE,
				RiskConstants.EXTENSION, true);
	}

	/**
	 * This method searches in DBPedia for format names matching to passed
	 * extension.
	 * 
	 * @param searchExtension
	 *            The extension (like ".gif")
	 * @return format name string list
	 */
	public static List<String> searchFormatNameForExtension(
			String searchExtension) {
		return search(RiskConstants.GRAPHICS_FILE_FORMATS_PROPERTY_ID,
				searchExtension, RiskConstants.EXTENSION,
				RiskConstants.COLUMN_NAME, false);
	}

	/**
	 * This method initializes calculation model what registers risk properties
	 * 
	 * @return calculation model instance
	 */
	public static CalculationModel initCalculationModel() {
		CalculationModel calculationModel = new CalculationModel();
		RiskAnalysis riskAnalysis = calculationModel.analyze(null, null, null);
		calculationModel.setRiskAnalysis(riskAnalysis);
		return calculationModel;
	}

	/**
	 * This method initializes calculation model what registers risk properties
	 * @param profile
	 *        Risk data profile filled out with measurements data
	 * @return calculation model instance
	 */
	public static CalculationModel initCalculationModel(DataItemProfile profile) {
		CalculationModel calculationModel = new CalculationModel(profile);
		RiskAnalysis riskAnalysis = calculationModel.analyze(null, null, null);
		calculationModel.setRiskAnalysis(riskAnalysis);
		return calculationModel;
	}

	/**
	 * This method initializes calculation model applying passed properties
	 * @param profile
	 *        The measurement profile
	 * @param file
	 *        The file to analyze
	 * @param propertySetXml
	 *        The path to the XML file that defines properties, property sets and their dependencies
	 * @param classificationXml
	 *        The path to the XML file that defines risk score classifications for risk properties
	 * @return risk analysis calculation model
	 */
	public static CalculationModel initCalculationModelExt(DataItemProfile profile, File file, String propertySetXml, String classificationXml) {
		CalculationModel calculationModel = new CalculationModel(profile);
		RiskAnalysis riskAnalysis = calculationModel.analyze(file, propertySetXml, classificationXml);
		calculationModel.setRiskAnalysis(riskAnalysis);
		return calculationModel;
	}

	/**
	 * This method searches in DBPedia for format code matching to passed format
	 * extension.
	 * 
	 * @param searchExtension
	 *            The extension (like "GIF")
	 * @return format code string list
	 */
	public static List<String> searchFormatCodeFromExtension(
			String searchExtension) {
		return search(RiskConstants.GRAPHICS_FILE_FORMATS_PROPERTY_ID,
				searchExtension, RiskConstants.EXTENSION,
				RiskConstants.TYPE_CODE, false);
	}

	/**
	 * This method searches in DBPedia for format code matching to passed format
	 * format name.
	 * 
	 * @param searchFormatName
	 *            The format name (like "Tagged Image File Format" for TIFF or
	 *            "Windows Bitmap" for BMP)
	 * @return format code string list
	 */
	public static List<String> searchFormatCodeFromFormatName(
			String searchFormatName) {
		return search(RiskConstants.GRAPHICS_FILE_FORMATS_PROPERTY_ID,
				searchFormatName, RiskConstants.COLUMN_NAME,
				RiskConstants.TYPE_CODE, false);
	}

	/**
	 * This method searches in DBPedia for format name matching to passed format
	 * code.
	 * 
	 * @param searchFormatCode
	 *            The format code (like "TIFF")
	 * @return format name string list
	 */
	public static List<String> searchFormatNameFromFormatCode(
			String searchFormatCode) {
		return search(RiskConstants.GRAPHICS_FILE_FORMATS_PROPERTY_ID,
				searchFormatCode, RiskConstants.TYPE_CODE,
				RiskConstants.COLUMN_NAME, true);
	}

	/**
	 * This method searches in DBPedia for formats matching to passed format
	 * format name.
	 * 
	 * @param searchFormatName
	 *            The format name (like "Tagged Image File Format" for TIFF or
	 *            "Windows Bitmap" for BMP)
	 * @return format code string list
	 */
	public static List<Map<String, String>> searchFormatsFromFormatName(
			String searchFormatName) {
		List<Map<String, String>> res = new ArrayList<Map<String, String>>();
		RepositoryDescription dbpedia = new DBPediaConnector();
		RiskProperty fileFormatsProperty = RiskUtils
				.getRiskPropertyById(RiskConstants.GRAPHICS_FILE_FORMATS_PROPERTY_ID);
		ConnectorImpl fileFormatsConnector = new ConnectorImpl(
				fileFormatsProperty);

		((DBPediaConnector) dbpedia).setConnector(fileFormatsConnector);
		List<Map<String, String>> dbpediaList = ((DBPediaConnector) dbpedia)
				.retrieveAll();

		Iterator<Map<String, String>> iter = dbpediaList.iterator();
		while (iter.hasNext()) {
			Map<String, String> dbpediaMap = iter.next();
			Set<Map.Entry<String, String>> dbpediaSet = dbpediaMap.entrySet();
			for (Map.Entry<String, String> entry : dbpediaSet) {
				String key = entry.getKey();
				String value = entry.getValue();
				LOG.info("DBPedia response: key=" + key + " , value=" + value);
				if (key.equals(RiskConstants.COLUMN_NAME) && value.contains(searchFormatName)) {
					res.add(dbpediaMap);
				}
			}
		}
		return res;
	}

	/**
	 * This method searches in DBPedia for mime type matching to passed format
	 * extension.
	 * 
	 * @param searchExtension
	 *            The extension (like "GIF")
	 * @return mime type string list
	 */
	public static List<String> searchMimeTypeFromExtension(
			String searchExtension) {
		return search(RiskConstants.GRAPHICS_FILE_FORMATS_PROPERTY_ID,
				searchExtension, RiskConstants.EXTENSION, RiskConstants.MIME,
				false);
	}

	/**
	 * This method searches in DBPedia for mime type matching to passed format
	 * format name.
	 * 
	 * @param searchFormatName
	 *            The format name (like "Tagged Image File Format" for TIFF or
	 *            "Windows Bitmap" for BMP)
	 * @return format code string list
	 */
	public static List<String> searchMimeTypeFromFormatName(
			String searchFormatName) {
		return search(RiskConstants.GRAPHICS_FILE_FORMATS_PROPERTY_ID,
				searchFormatName, RiskConstants.COLUMN_NAME,
				RiskConstants.MIME, false);
	}

	/**
	 * This method searches in DBPedia for mime type matching to passed format
	 * code.
	 * 
	 * @param searchFormatCode
	 *            The format code (like "TIFF")
	 * @return mime type string list
	 */
	public static List<String> searchMimeTypeFromFormatCode(
			String searchFormatCode) {
		return search(RiskConstants.GRAPHICS_FILE_FORMATS_PROPERTY_ID,
				searchFormatCode, RiskConstants.TYPE_CODE, RiskConstants.MIME,
				true);
	}

	/**
	 * This method searches in DBPedia for software names matching to passed
	 * vendor name.
	 * 
	 * @param searchVendorName
	 *            The vendor name (like "Adobe_Systems")
	 * @return software name string list
	 */
	public static List<String> searchSoftwareForVendor(String searchVendorName) {
		return search(RiskConstants.SOFTWARE_DEVELOPER_PROPERTY_ID,
				searchVendorName, RiskConstants.DEVELOPER, RiskConstants.LABEL,
				true);
	}

	/**
	 * This method searches in DBPedia for vendor names matching to passed
	 * software name.
	 * 
	 * @param searchSoftwareName
	 *            The vendor name (like "Adobe Systems")
	 * @return vendor name string list
	 */
	public static List<String> searchVendorForSoftware(String searchVendorName) {
		return search(RiskConstants.SOFTWARE_DEVELOPER_PROPERTY_ID,
				searchVendorName, RiskConstants.LABEL, RiskConstants.DEVELOPER,
				true);
	}

	/**
	 * This method searches in DBPedia for vendor revenue matching to passed
	 * vendor name.
	 * 
	 * @param vendorName
	 *            The vendor name (like "Apple Inc")
	 * @return revenue list
	 */
	public static List<String> evaluateVendorRevenueByName(String searchVendorName) {
		return search(RiskConstants.VENDOR_DBPEDIA_YAGO_PROPERTY_ID,
				searchVendorName, RiskConstants.COLUMN_NAME, RiskConstants.REVENUE,
				true);
	}

	/**
	 * This method searches in DBPedia for vendor location matching to passed
	 * vendor name.
	 * 
	 * @param vendorName
	 *            The vendor name (like "Apple Inc")
	 * @return location list
	 */
	public static List<String> evaluateVendorLocationByName(String searchVendorName) {
		return search(RiskConstants.VENDOR_DBPEDIA_YAGO_PROPERTY_ID,
				searchVendorName, RiskConstants.COLUMN_NAME, RiskConstants.LOCATION,
				true);
	}

	/**
	 * This method searches in DBPedia for vendor key people matching to passed
	 * vendor name.
	 * 
	 * @param vendorName
	 *            The vendor name (like "Apple Inc")
	 * @return key people list
	 */
	public static List<String> evaluateVendorKeyPeopleByName(String searchVendorName) {
		return search(RiskConstants.VENDOR_DBPEDIA_YAGO_PROPERTY_ID,
				searchVendorName, RiskConstants.COLUMN_NAME, RiskConstants.KEY_PEOPLE,
				true);
	}

	/**
	 * This method searches in DBPedia for vendor homepage matching to passed
	 * vendor name.
	 * 
	 * @param vendorName
	 *            The vendor name (like "Apple Inc")
	 * @return homepage list
	 */
	public static List<String> evaluateVendorHomepageByName(String searchVendorName) {
		return search(RiskConstants.VENDOR_DBPEDIA_YAGO_PROPERTY_ID,
				searchVendorName, RiskConstants.COLUMN_NAME, RiskConstants.HOMEPAGE,
				true);
	}

	/**
	 * This method searches in DBPedia for vendor employees number matching to passed
	 * vendor name.
	 * 
	 * @param vendorName
	 *            The vendor name (like "Apple Inc")
	 * @return employees number list
	 */
	public static List<String> evaluateVendorEmployeesNumberByName(String searchVendorName) {
		return search(RiskConstants.VENDOR_DBPEDIA_YAGO_PROPERTY_ID,
				searchVendorName, RiskConstants.COLUMN_NAME, RiskConstants.EMPLOYEES,
				true);
	}

	/**
	 * This method searches in DBPedia for software computer platform matching to passed
	 * software name.
	 * 
	 * @param softwareName
	 *            The software name (like "Google Docs")
	 * @return computer platform list
	 */
	public static List<String> evaluateSoftwareComputerPlatformByName(String softwareName) {
		return search(RiskConstants.SOFTWARE_PLATFORM_PROPERTY_ID,
				softwareName, RiskConstants.LABEL, RiskConstants.PLATFORM,
				true);
	}

	/**
	 * This method searches in DBPedia for software programming language matching to passed
	 * software name.
	 * 
	 * @param softwareName
	 *            The software name (like "Mozilla Firefox")
	 * @return programming language list
	 */
	public static List<String> evaluateSoftwareProgrammingLanguageByName(String softwareName) {
		return search(RiskConstants.SOFTWARE_PLATFORM_PROPERTY_ID,
				softwareName, RiskConstants.LABEL, RiskConstants.PROGRAMMING_LANGUAGE,
				true);
	}

	/**
	 * This method searches in DBPedia for software homepage matching to passed
	 * software name.
	 * 
	 * @param softwareName
	 *            The software name (like "Mozilla Firefox")
	 * @return homepage list
	 */
	public static List<String> evaluateSoftwareHomepageByName(String softwareName) {
		return search(RiskConstants.SOFTWARE_LICENSE_PROPERTY_ID,
				softwareName, RiskConstants.LABEL, RiskConstants.HOMEPAGE,
				true);
	}

	/**
	 * This method searches in DBPedia for software names matching to passed
	 * software version.
	 * 
	 * @param softwareVersion
	 *            The software version (like "1")
	 * @return software names list
	 */
	public static List<String> evaluateSoftwareNameByVersion(String softwareVersion) {
		return search(RiskConstants.SOFTWARE_LICENSE_PROPERTY_ID,
				softwareVersion, RiskConstants.LATEST_RELEASE_VERSION, RiskConstants.LABEL,
				true);
	}

	/**
	 * This method searches in DBPedia for software latest release version matching to passed
	 * software name.
	 * 
	 * @param softwareName
	 *            The software name (like "FileMaker")
	 * @return software latest release version list
	 */
	public static List<String> evaluateSoftwareLatestReleaseVersionByName(String softwareName) {
		return search(RiskConstants.SOFTWARE_LICENSE_PROPERTY_ID,
				softwareName, RiskConstants.LABEL, RiskConstants.LATEST_RELEASE_VERSION,
				true);
	}

	/**
	 * This method searches in DBPedia for software license matching to passed
	 * software name.
	 * 
	 * @param softwareName
	 *            The software name (like "FileMaker")
	 * @return software license list
	 */
	public static List<String> evaluateSoftwareLicenseByName(String softwareName) {
		return search(RiskConstants.SOFTWARE_LICENSE_PROPERTY_ID,
				softwareName, RiskConstants.LABEL, RiskConstants.LICENSE,
				true);
	}

	/**
	 * This method searches in DBPedia for software genre matching to passed
	 * software name.
	 * 
	 * @param softwareName
	 *            The software name (like "FileMaker")
	 * @return software genre list
	 */
	public static List<String> evaluateSoftwareGenreByName(String softwareName) {
		return search(RiskConstants.SOFTWARE_DEVELOPER_PROPERTY_ID,
				softwareName, RiskConstants.LABEL, RiskConstants.GENRE,
				true);
	}

	/**
	 * This method retrieves configuration list from the string array
	 * @param config
	 * @return configuration list
	 */
	public static List<String> getConfigurationList(String config) {
		List<String> configList = null;
		if (config != null && config.length() > 0) {
			String[] configArray = config.split(ReportConstants.COMMA);
			configList = Arrays.asList(configArray);
		}
		return configList;
	}
	

}
