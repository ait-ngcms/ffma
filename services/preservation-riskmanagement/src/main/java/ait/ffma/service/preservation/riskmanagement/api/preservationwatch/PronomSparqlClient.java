package ait.ffma.service.preservation.riskmanagement.api.preservationwatch;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.xml.sax.helpers.DefaultHandler;

import ait.ffma.service.preservation.riskmanagement.api.lod.LODConstants;
import ait.ffma.service.preservation.riskmanagement.api.lod.lodmodel.LODPronomSoftware;

/**
 * This class provides Pronom SPARQL request functionality.
 */
public class PronomSparqlClient extends DefaultHandler {
	
    /**
     * A logger object.
     */
    private static final Logger log = Logger.getLogger(PronomSparqlClient.class.getName());

	private Map<String, List<String>> sparqlResult = new HashMap<String, List<String>>();
	private String responseBody = null;

	/**
	 * @param query
	 */
	public PronomSparqlClient(String query) {
		HttpClient client = new HttpClient();
		client.getHttpConnectionManager().getParams().
		setConnectionTimeout(10000);
		String url = query;
		try {
			HttpMethod method = new GetMethod(url);
			method.setFollowRedirects(false);
//		try {
			client.executeMethod(method);
			responseBody = method.getResponseBodyAsString();
			log.info("Response body: " + responseBody);
			method.releaseConnection();
		} catch (HttpException he) {
			log.fine("Http error connecting to ’" + query + "’");
		} catch (IOException ioe) {
			log.fine("Unable to connect to ’" + query + "’");
		} catch (Exception e) {
			log.fine("SPARQL client exception. Unable to parse response from ’" + query + "’ (Exception: " + e.getLocalizedMessage() + ")");
		}
//		method.releaseConnection();
	}
	
	/**
	 * @return SPARQL response
	 */
	public Map<String, List<String>> getSparqlResult() {
		sparqlResult = parseHtml();
		return sparqlResult;
	}
	
	public Map<String, List<String>> parseHtml() {
		Map<String, List<String>> res = new HashMap<String, List<String>>();
		if (responseBody != null) {
		int beginIndex = responseBody.indexOf(LODConstants.INFO_BEGIN);
		int endIndex = responseBody.indexOf(LODConstants.INFO_END, beginIndex);
		if (beginIndex != -1 && endIndex != -1) {
			String responseBodyInfo = responseBody.substring(beginIndex + LODConstants.INFO_BEGIN.length(), endIndex);
			int tableBeginIndex = responseBodyInfo.indexOf(LODConstants.TABLE_BEGIN);
			int tableEndIndex = responseBodyInfo.indexOf(LODConstants.TABLE_END, tableBeginIndex);
			responseBodyInfo = responseBodyInfo.substring(tableBeginIndex + LODConstants.TABLE_BEGIN.length(), tableEndIndex);
			
	    	List<String> headers = new ArrayList<String>();
	        String[] rows = responseBodyInfo.split(LODConstants.TABLE_ROW_BEGIN);	
	        for (int r = 1; r < rows.length; r++) {
	        	String currentRow = rows[r].replaceAll(LODConstants.TABLE_ROW_END, "");
	    		if (currentRow.contains(LODConstants.TABLE_HEADER_BEGIN)) {
	    			String[] values = currentRow.split(LODConstants.TABLE_HEADER_BEGIN);	
	    	        for (int v = 1; v < values.length; v++) {
	//            		List<String> valueList = new ArrayList<String>();
		    			String currentValue = values[v].replaceAll(LODConstants.TABLE_HEADER_END, "");
	//	    			res.put(currentValue, valueList);
		    			res.put(currentValue, new ArrayList<String>());
		    			headers.add(currentValue);
	    	        }
	    		} else {
	    			String[] values = currentRow.split(LODConstants.TABLE_VALUE_BEGIN);	
	    	        for (int v = 1; v < values.length; v++) {
		    			String currentValue = values[v].replaceAll(LODConstants.TABLE_VALUE_END, "");
		    			List<String> valList = res.get(headers.get(v - 1));
	        			valList.add(currentValue);
	    	        }
	    		}
	        }
		} else {
			log.info("Error: HTML string could not be parsed successfully!");
		}
		} // response body existance check
		return res;
	}
	
	/**
	 * This method iterates through the repository response for all 
	 * stored objects filtering by search value.
	 * @param searchValue
	 * @return objects that match search value
	 */
	public Map<String, List<String>> retrieveAll() {
		if (sparqlResult == null || sparqlResult.isEmpty()) {
			getSparqlResult();
		}

		return sparqlResult;
	}
	
	/**
	 * This method iterates through the repository response for all 
	 * stored objects filtering by search value. Iteration stops if
	 * searched object is found.
	 * @param searchValue
	 * @return objects that match search value
	 */
	public List<String> retrieve(String columnName) {
		if (sparqlResult == null || sparqlResult.isEmpty()) {
			getSparqlResult();
		}
		return sparqlResult.get(columnName);
	}
	
	/**
	 * @param parameterName
	 * @return
	 */
	public String readValue(String parameterName) {
		String res = "";
		if (parameterName != null && responseBody != null && responseBody.contains(parameterName)) {
			int beginIndex = responseBody.indexOf("<" + parameterName + ">"); 
			int endIndex = responseBody.indexOf("</" + parameterName + ">"); 
			if (beginIndex > 0 && endIndex > 0 && endIndex > beginIndex) {
				res = responseBody.substring(beginIndex + parameterName.length() + 2, endIndex);
			}
		}
		return res;
	}
	
	/**
	 * @param parameterName
	 * @param body
	 * @return
	 */
	public String readValueExt(String parameterName, String body) {
		String res = "";
		if (body.contains(parameterName)) {
			int beginIndex = body.indexOf("<" + parameterName + ">"); 
			int endIndex = body.indexOf("</" + parameterName + ">"); 
			res = body.substring(beginIndex + parameterName.length() + 2, endIndex);
		}
		return res;
	}
	
	/**
	 * @return
	 */
	public LODPronomSoftware parseXml() {
		LODPronomSoftware res = new LODPronomSoftware();
		setName(res);
		setVersion(res);
		setSoftwareId(res);
		setSoftwareTypes(res);
		setMediaFormat(res);
		setReleaseDate(res);
		setIdentifier(res);
		setDescriptions(res);		
		setVendorName(res);
		setVendorId(res);
		
		if (responseBody != null) {
	        String[] rows = responseBody.split("<FileFormat>");	
	        
	        Map<String, String> fileFormatMap = new HashMap<String, String>();
	        for (int r = 1; r < rows.length; r++) {
	        	String currentRow = rows[r];
	    		String formatName = readValueExt("FormatName", currentRow);
	    		String extensions = readValueExt("Extensions", currentRow);
	    		if (formatName.length() > 0 && extensions.length() > 0) {
	    			fileFormatMap.put(extensions, formatName);
	    		}
	        }
			res.setFileFormatMap(fileFormatMap);
		}
		return res;
	}

	/**
	 * @param res
	 */
	private void setVendorId(LODPronomSoftware res) {
		String vendorId = readValue("VendorID");
		if (vendorId.length() > 0) { 
			List<String> vendorIdList = new ArrayList<String>();
			vendorIdList.add(vendorId);
			res.setVendorsIdList(vendorIdList);
		}
	}

	/**
	 * @param res
	 */
	private void setVendorName(LODPronomSoftware res) {
		String vendorName = readValue("VendorName");
		if (vendorName.length() > 0) { 
			List<String> vendorList = new ArrayList<String>();
			vendorList.add(vendorName);
			res.setVendorsList(vendorList);
		}
	}

	/**
	 * @param res
	 */
	private void setDescriptions(LODPronomSoftware res) {
		String description = readValue("Description");
		if (description.length() > 0) {
			res.setDescription(description);
		}
	}

	/**
	 * @param res
	 */
	private void setIdentifier(LODPronomSoftware res) {
		String identifier = readValue("Identifier");
		if (identifier.length() > 0) {
			res.setIdentifier(identifier);
		}
	}

	/**
	 * @param res
	 */
	private void setReleaseDate(LODPronomSoftware res) {
		String releaseDate = readValue("ReleaseDate");
		if (releaseDate.length() > 0) {
			res.setReleaseDate(releaseDate);
		}
	}

	/**
	 * @param res
	 */
	private void setMediaFormat(LODPronomSoftware res) {
		String mediaFormat = readValue("MediaFormat");
		if (mediaFormat.length() > 0) {
			res.setMediaFormat(mediaFormat);
		}
	}

	/**
	 * @param res
	 */
	private void setSoftwareTypes(LODPronomSoftware res) {
		String type = readValue("SoftwareTypes");
		if (type.length() > 0) {
			res.setType(type);
		}
	}

	/**
	 * @param res
	 */
	private void setSoftwareId(LODPronomSoftware res) {
		String id = readValue("SoftwareID");
		if (id.length() > 0) {
			res.setNumber(id);
		}
	}

	/**
	 * @param res
	 */
	private void setVersion(LODPronomSoftware res) {
		String version = readValue("Version");
		if (version.length() > 0) {
			res.setVersion(version);
		}
	}

	/**
	 * @param res
	 */
	private void setName(LODPronomSoftware res) {
		String name = readValue("SoftwareName");
		if (name.length() > 0) {
			res.setName(name);
		}
	}
	
	/**
	 * @return
	 */
	public LODPronomSoftware retrievePronomSoftwareEntry() {
		LODPronomSoftware res = null;
		if (sparqlResult == null || sparqlResult.isEmpty()) {
			res = parseXml();
		}
		return res;
	}
	
}
