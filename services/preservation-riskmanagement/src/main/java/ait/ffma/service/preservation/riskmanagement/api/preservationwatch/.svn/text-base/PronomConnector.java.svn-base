package ait.ffma.service.preservation.riskmanagement.api.preservationwatch;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.logging.Logger;

import org.json.JSONObject;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;

import ait.ffma.service.preservation.riskmanagement.api.riskanalysis.risk.Connector;
import ait.ffma.service.preservation.riskmanagement.api.riskanalysis.risk.RiskConstants;
import ait.ffma.service.preservation.riskmanagement.api.riskanalysis.risk.RiskConstants.ConnectorMode;

/**
 * This class manages connection to PRONOM repository
 */
public class PronomConnector implements RepositoryDescription {

	/**
     * A logger object.
     */
    private final Logger log = Logger.getLogger(getClass().getName());

	/**
	 * Request query to Pronom
	 */
	private String query;
	
	/**
	 * Connector manages particular search data
	 */
	private Connector connector;
	
	/**
	 * This mode controls if PRONOM connector is in SPARQL mode or in HTTP request mode 
	 */
	private ConnectorMode connectorMode = ConnectorMode.Http;
	
	/**
	 * This constructor initializes Pronom connector with search data
	 * @param connector
	 */
	public PronomConnector(Connector connector, ConnectorMode connectorMode) {
		this.connector = connector;
		query = connector.getQuery();
		this.connectorMode = connectorMode;
	}
	
	/**
	 * This constructor initializes Pronom connector with search data
	 * @param connector
	 */
	public PronomConnector(Connector connector) {
		this.connector = connector;
		query = connector.getQuery();
	}
	
	/**
	 * Default constructor
	 */
	public PronomConnector() {
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
	 * This method returns Pronom query 
	 * @return
	 */
	public String getQuery() {
		return query;
	}

	/**
	 * This method initializes Pronom query
	 * @param query
	 */
	public void setQuery(String query) {
		this.query = query;
	}

	/**
	 * @return
	 */
	public ConnectorMode getConnectorMode() {
		return connectorMode;
	}

	/**
	 * @param connector mode
	 */
	public void setConnectorMode(ConnectorMode connectorMode) {
		this.connectorMode = connectorMode;
	}

	/**
	 * Parse response from PRONOM.
	 * @param response
	 * @param field
	 * @return PRONOM characteristic value
	 */
	private String evaluatePronomCharacteristic(String response, String field) {
		String res = "";
		res = response.substring(response.indexOf(RiskConstants.FIELD_PREFIX
				+ field + RiskConstants.FIELD_SUFFIX));
		res = res.substring(res.indexOf(RiskConstants.VALUE_NAME_BEGIN)
				+ RiskConstants.VALUE_NAME_BEGIN.length(),
				res.indexOf(RiskConstants.VALUE_NAME_END));
		if (res.contains(RiskConstants.LINK)) {
			res = res.substring(res.indexOf(RiskConstants.LINK_BEGIN)
					+ RiskConstants.LINK_BEGIN.length(),
					res.indexOf(RiskConstants.LINK_END));
		}
		log.info("PRONOM field: " + field + ", value: " + res);
		return res;
	}
	
	/**
	 * This method sets PRONOM response value for particular key.
	 * @param key
	 * @param response
	 */
	private void setPronomResponseValue(String key, String response) {
		if (getConnector().getSearchValue().equals(key)) {
			getConnector().setValue(evaluatePronomCharacteristic(response, key));
		} 		
	}
	
	/* (non-Javadoc)
	 * @see ait.ffma.service.preservation.riskmanagement.api.preservationwatch.RepositoryDescription#retrieveAll()
	 */
	public List<Map<String, String>> retrieveAll() {
		List<Map<String, String>> list = new ArrayList<Map<String, String>>(); 

		if (getConnectorMode() == ConnectorMode.Http) {
	    	// Split by comma '#' 
	        StringTokenizer st = new StringTokenizer(getQuery(), "#");	
	 
	    	while(st.hasMoreElements()){
	    		Map<String, String> res = new HashMap<String, String>();
	    		
	    		WebResource resource = Client.create().resource(RiskConstants.PRONOM_DEFAULT_URI + st.nextElement());
	    		String response = resource.get(String.class);
	    		log.info("Pronom: " + response);
	
	    		res.put(RiskConstants.DEVELOPED_BY, evaluatePronomCharacteristic(response, RiskConstants.DEVELOPED_BY));
	    		res.put(RiskConstants.VERSION, evaluatePronomCharacteristic(response, RiskConstants.VERSION));
	    		res.put(RiskConstants.ORIENTATION, evaluatePronomCharacteristic(response, RiskConstants.ORIENTATION));
	    		res.put(RiskConstants.BYTE_ORDER, evaluatePronomCharacteristic(response, RiskConstants.BYTE_ORDER));
	    		res.put(RiskConstants.RELEASED, evaluatePronomCharacteristic(response, RiskConstants.RELEASED));
	    		res.put(RiskConstants.DESCRIPTION, evaluatePronomCharacteristic(response, RiskConstants.DESCRIPTION));
	    		res.put(RiskConstants.IDENTIFIERS, evaluatePronomCharacteristic(response, RiskConstants.IDENTIFIERS));
	    		res.put(RiskConstants.OTHER_NAMES, evaluatePronomCharacteristic(response, RiskConstants.OTHER_NAMES));
	    		res.put(RiskConstants.FORMAT_RISK, evaluatePronomCharacteristic(response, RiskConstants.FORMAT_RISK));
	    		res.put(RiskConstants.SUPPORTED_UNTIL, evaluatePronomCharacteristic(response, RiskConstants.SUPPORTED_UNTIL));
	
	    		list.add(res);
	    	}		
		} else {	    	
			log.info("location: " + getLocation().toString() + ", updatePolicy" + getUpdatePolicy());
			SPARQLClient sparqlClient = new SPARQLClient(getLocation().toString(), getUpdatePolicy());
			if (sparqlClient != null) {
				list = sparqlClient.retrieveAll(getConnector().getSearchValue());
			}
		}
		return list;
	}
	
	/* (non-Javadoc)
	 * @see ait.ffma.service.preservation.riskmanagement.api.preservationwatch.RepositoryDescription#retrieve()
	 */
	public Map<String, String> retrieve() {
		Map<String, String> res = new HashMap<String, String>();
		
		if (getConnectorMode() == ConnectorMode.Http) {
			WebResource resource = Client.create().resource(getUpdatePolicy());
			String response = resource.get(String.class);
			log.info("Pronom: " + response);
	
			res.put(RiskConstants.DEVELOPED_BY, evaluatePronomCharacteristic(response, RiskConstants.DEVELOPED_BY));
			res.put(RiskConstants.VERSION, evaluatePronomCharacteristic(response, RiskConstants.VERSION));
			res.put(RiskConstants.ORIENTATION, evaluatePronomCharacteristic(response, RiskConstants.ORIENTATION));
			res.put(RiskConstants.BYTE_ORDER, evaluatePronomCharacteristic(response, RiskConstants.BYTE_ORDER));
			res.put(RiskConstants.RELEASED, evaluatePronomCharacteristic(response, RiskConstants.RELEASED));
			res.put(RiskConstants.DESCRIPTION, evaluatePronomCharacteristic(response, RiskConstants.DESCRIPTION));
			res.put(RiskConstants.IDENTIFIERS, evaluatePronomCharacteristic(response, RiskConstants.IDENTIFIERS));
			res.put(RiskConstants.OTHER_NAMES, evaluatePronomCharacteristic(response, RiskConstants.OTHER_NAMES));
			res.put(RiskConstants.FORMAT_RISK, evaluatePronomCharacteristic(response, RiskConstants.FORMAT_RISK));
			res.put(RiskConstants.SUPPORTED_UNTIL, evaluatePronomCharacteristic(response, RiskConstants.SUPPORTED_UNTIL));
		} else {
			log.info("location: " + getLocation().toString() + ", updatePolicy" + getUpdatePolicy());
			SPARQLClient sparqlClient = new SPARQLClient(getLocation().toString(), getUpdatePolicy());
			if (sparqlClient != null) {
				res = sparqlClient.retrieve(getConnector().getSearchValue());
			}
		}
		return res;
	}
	
	/* (non-Javadoc)
	 * @see ait.ffma.service.preservation.riskmanagement.api.preservationwatch.RepositoryDescription#update()
	 */
	public Connector update() {
		if (getConnectorMode() == ConnectorMode.Http) {
			WebResource resource = Client.create().resource(getUpdatePolicy());
			String response = resource.get(String.class);
			log.info("Pronom: " + response);
	
			setPronomResponseValue(RiskConstants.DEVELOPED_BY, response);
			setPronomResponseValue(RiskConstants.VERSION, response);
			setPronomResponseValue(RiskConstants.ORIENTATION, response);
			setPronomResponseValue(RiskConstants.BYTE_ORDER, response);
			setPronomResponseValue(RiskConstants.RELEASED, response);
			setPronomResponseValue(RiskConstants.DESCRIPTION, response);
			setPronomResponseValue(RiskConstants.IDENTIFIERS, response);
			setPronomResponseValue(RiskConstants.OTHER_NAMES, response);
			setPronomResponseValue(RiskConstants.FORMAT_RISK, response);
			setPronomResponseValue(RiskConstants.SUPPORTED_UNTIL, response);
		} else {
			log.info("location: " + getLocation().toString() + ", updatePolicy" + getUpdatePolicy());
			new SPARQLClient(getLocation().toString(), getUpdatePolicy());
		}
		
		return getConnector();
	}

	/* (non-Javadoc)
	 * @see ait.ffma.service.preservation.riskmanagement.api.preservationwatch.RepositoryDescription#getID()
	 */
	public String getID() {
		return "Pronom";
	}

	/* (non-Javadoc)
	 * @see ait.ffma.service.preservation.riskmanagement.api.preservationwatch.RepositoryDescription#getName()
	 */
	public String getName() {
		return "Pronom";
	}

	/* (non-Javadoc)
	 * @see ait.ffma.service.preservation.riskmanagement.api.preservationwatch.RepositoryDescription#getLocation()
	 */
	public URL getLocation() {
		URL retVal = null;
		try {
			retVal = new URL(RiskConstants.PRONOM_DEFAULT_URI);
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
		return RiskConstants.PRONOM_DEFAULT_URI + getQuery();
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
		
	/* (non-Javadoc)
	 * @see ait.ffma.service.preservation.riskmanagement.api.preservationwatch.RepositoryDescription#retrieveJSON()
	 */
	public JSONObject retrieveJSON() {
		return null;
	}
	
}
