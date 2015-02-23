package ait.ffma.service.preservation.riskmanagement.api.preservationwatch;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.ws.rs.core.MediaType;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;

import ait.ffma.service.preservation.riskmanagement.api.riskanalysis.risk.Connector;
import ait.ffma.service.preservation.riskmanagement.api.riskanalysis.risk.RiskConstants;

/**
 * This class manages connection to Freebase repository
 */
public class FreebaseConnector implements RepositoryDescription {

	/**
     * A logger object.
     */
    private final Logger log = Logger.getLogger(getClass().getName());

	/**
	 * Request query to Freebase
	 */
	private String query;
	
	/**
	 * Connector manages particular search data
	 */
	private Connector connector;
	
	/**
	 * This constructor initializes Freebase connector with search data
	 * @param connector
	 */
	public FreebaseConnector(Connector connector) {
		this.connector = connector;
		query = connector.getQuery();
	}
	
	public FreebaseConnector() {
	}
	
	/**
	 * This method returns search connector instance
	 * @return
	 */
	public Connector getConnector() {
		return connector;
	}

	/**
	 * This method initializes search connector instance
	 * @param connector
	 */
	public void setConnector(Connector connector) {
		this.connector = connector;
		setQuery(connector.getQuery());
	}

	/**
	 * This method returns Freebase query 
	 * @return
	 */
	public String getQuery() {
		return query;
	}

	/**
	 * This method initializes Freebase query
	 * @param query
	 */
	public void setQuery(String query) {
		try {
			String query1 = query.replace("\"[", "[");
			String query2 = query1.replace("]\"", "]");
			String query3 = query2.replace("\"null\"", "null");
			String query4 = "[" + query3 + "]";
			String queryenvelope = "{\"query\":" + query4 + "}";
			String query5 = URLEncoder.encode(queryenvelope, "UTF-8");
			this.query = query5;
		} catch (UnsupportedEncodingException e) {
			log.info("Freebase query encoding error: " + e);
		}
	}

	/**
	 * This method sets Freebase response value for particular key.
	 * @param key
	 * @param response
	 */
	private void setPropertyResponseValue(String key, String response) {
		if (getConnector().getSearchValue().equals(key)) {
			getConnector().setValue(response);
		} 		
	}
	
	/* (non-Javadoc)
	 * @see ait.ffma.service.preservation.riskmanagement.api.preservationwatch.RepositoryDescription#retrieveJSON()
	 */
	public JSONObject retrieveJSON() {
		JSONObject resultJsonObject = null;
		WebResource resource = Client.create().resource(getUpdatePolicy());
		resource.accept(MediaType.APPLICATION_JSON);	
		try {
			String response = resource.get(String.class);
			log.info("Freebase: " + response);
			resultJsonObject = new JSONObject(response);
		} catch (JSONException e) {
			log.info("Freebase response JSON object retrieve error: " + e);
		} catch (Exception ex) {
			log.info("Freebase response error. " + ex);
		}
		return resultJsonObject;
	}

	/* (non-Javadoc)
	 * @see ait.ffma.service.preservation.riskmanagement.api.preservationwatch.RepositoryDescription#retrieveAll()
	 */
	public List<Map<String, String>> retrieveAll() {
		List<Map<String, String>> list = new ArrayList<Map<String, String>>(); 
		list.add(retrieve());
		return list;
	}
	
	/* (non-Javadoc)
	 * @see ait.ffma.service.preservation.riskmanagement.api.preservationwatch.RepositoryDescription#retrieve()
	 */
	public Map<String, String> retrieve() {
		Map<String, String> res = new HashMap<String, String>();
		
		JSONObject resultJsonObject = retrieveJSON();
    	try {
			JSONArray jsonArray = (JSONArray)resultJsonObject.get(RiskConstants.FB_RESULT);
		    for (int i=0; i < jsonArray.length(); i++) {
	    		JSONObject currentJsonObject = (JSONObject) jsonArray.get(i);
	    		String key = getConnector().getColumnNames().get(0);
	    		String value = (String) currentJsonObject.get(getConnector().getColumnNames().get(0));
				log.info("Response software key: " + key + ", value: " + value);
				res.put(key, value);
		    }
		} catch (JSONException e) {
			log.info("Freebase response update error: " + e);
		}

		return res;
	}
	
	/* (non-Javadoc)
	 * @see ait.ffma.service.preservation.riskmanagement.api.preservationwatch.RepositoryDescription#update()
	 */
	public Connector update() {
		JSONObject resultJsonObject = retrieveJSON();
    	try {
			JSONArray jsonArray = (JSONArray)resultJsonObject.get(RiskConstants.FB_RESULT);
		    for (int i=0; i < jsonArray.length(); i++) {
	    		JSONObject currentJsonObject = (JSONObject) jsonArray.get(i);
				log.info("Response software key: " + getConnector().getColumnNames().get(0) +
				", value: " + currentJsonObject.get(getConnector().getColumnNames().get(0)));
				setPropertyResponseValue(
						getConnector().getColumnNames().get(0),
						(String) currentJsonObject.get(getConnector()
								.getColumnNames().get(0)));
		    }
		} catch (JSONException e) {
			log.info("Freebase response update error: " + e);
		}
		return getConnector();
	}

	/* (non-Javadoc)
	 * @see ait.ffma.service.preservation.riskmanagement.api.preservationwatch.RepositoryDescription#getID()
	 */
	public String getID() {
		return "Freebase";
	}

	/* (non-Javadoc)
	 * @see ait.ffma.service.preservation.riskmanagement.api.preservationwatch.RepositoryDescription#getName()
	 */
	public String getName() {
		return "Freebase";
	}

	/* (non-Javadoc)
	 * @see ait.ffma.service.preservation.riskmanagement.api.preservationwatch.RepositoryDescription#getLocation()
	 */
	public URL getLocation() {
		URL retVal = null;
		try {
			retVal = new URL(RiskConstants.FREEBASE_DEFAULT_URI);
		} catch (MalformedURLException e) {
			log.fine(e.getMessage());
		}
		return retVal;
	}

	/* (non-Javadoc)
	 * @see ait.ffma.service.preservation.riskmanagement.api.preservationwatch.RepositoryDescription#getProtocol()
	 */
	public String getProtocol() {
		return "HTML";
	}

	/* (non-Javadoc)
	 * @see ait.ffma.service.preservation.riskmanagement.api.preservationwatch.RepositoryDescription#getUpdatePolicy()
	 */
	public String getUpdatePolicy() {
		return RiskConstants.FREEBASE_DEFAULT_URI + getQuery();
	}

	/* (non-Javadoc)
	 * @see ait.ffma.service.preservation.riskmanagement.api.preservationwatch.RepositoryDescription#getSoftwareVendors()
	 */
	public String getSoftwareVendors() {
		return null;
	}

	/* (non-Javadoc)
	 * @see ait.ffma.service.preservation.riskmanagement.api.preservationwatch.RepositoryDescription#getNameSpace()
	 */
	public String getNameSpace() {
		return null;
	}
		
}
