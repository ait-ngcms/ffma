package ait.ffma.service.preservation.riskmanagement.api.preservationwatch;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import org.json.JSONObject;

import ait.ffma.service.preservation.riskmanagement.api.riskanalysis.risk.Connector;
import ait.ffma.service.preservation.riskmanagement.api.riskanalysis.risk.RiskConstants;

/**
 * This class manages connection to DBPedia repository
 */
public class DBPediaConnector implements RepositoryDescription {

	/**
     * A logger object.
     */
    private final Logger log = Logger.getLogger(getClass().getName());

	/**
	 * Request query to DBPedia
	 */
	private String query;
	
	/**
	 * Connector manages particular search data
	 */
	private Connector connector;
	
	/**
	 * This constructor initializes DBPedia connector with search data
	 * @param connector
	 */
	public DBPediaConnector(Connector connector) {
		this.connector = connector;
		query = connector.getQuery();
	}
	
	public DBPediaConnector() {
	}
	
	/**
	 * This method returns search connector instance
	 * @return
	 */
	public Connector getConnector() {
		return connector;
	}

	/* (non-Javadoc)
	 * @see ait.ffma.service.preservation.riskmanagement.api.preservationwatch.RepositoryDescription#setConnector(ait.ffma.service.preservation.riskmanagement.api.riskanalysis.risk.Connector)
	 */
	public void setConnector(Connector connector) {
		this.connector = connector;
		setQuery(connector.getQuery());
	}

	/**
	 * This method returns DBPedia query 
	 * @return
	 */
	public String getQuery() {
		return query;
	}

	/**
	 * This method initializes DBPedia query
	 * @param query
	 */
	public void setQuery(String query) {
		this.query = query;
	}

	/* (non-Javadoc)
	 * @see ait.ffma.service.preservation.riskmanagement.api.preservationwatch.RepositoryDescription#retrieve()
	 */
	public Map<String, String> retrieve() {
		Map<String, String> res = new HashMap<String, String>();
		log.info("location: " + getLocation().toString() + ", updatePolicy" + getUpdatePolicy());
		SPARQLClient sparqlClient = new SPARQLClient(getLocation().toString(), getUpdatePolicy());
		if (sparqlClient != null) {
			res = sparqlClient.retrieve(getConnector().getSearchValue());
		}
		return res;
	}
	
	/* (non-Javadoc)
	 * @see ait.ffma.service.preservation.riskmanagement.api.preservationwatch.RepositoryDescription#retrieveAll()
	 */
	public List<Map<String, String>> retrieveAll() {
		List<Map<String, String>> list = new ArrayList<Map<String, String>>(); 
		log.info("location: " + getLocation().toString() + ", updatePolicy" + getUpdatePolicy());
		SPARQLClient sparqlClient = new SPARQLClient(getLocation().toString(), getUpdatePolicy());
		if (sparqlClient != null) {
			list = sparqlClient.retrieveAll(getConnector().getSearchValue());
		}
		return list;
	}
	
	/**
	 * This method retrieves LOD data without using connector.
	 * @param searchValue
	 * @return
	 */
//	public List<Map<String, String>> retrieveLODAll() {
	public List<Map<String, String>> retrieveLODAll(String searchValue) {
		List<Map<String, String>> list = new ArrayList<Map<String, String>>(); 
//		log.info("location: " + getLocation().toString() + ", updatePolicy" + getUpdatePolicy());
		SPARQLClient sparqlClient = new SPARQLClient(getLocation().toString(), getUpdatePolicy());
		if (sparqlClient != null) {
//			list = sparqlClient.retrieveAll(null);
			list = sparqlClient.retrieveAll(searchValue);
		}
		return list;
	}
	
	/* (non-Javadoc)
	 * @see ait.ffma.service.preservation.riskmanagement.api.preservationwatch.RepositoryDescription#update()
	 */
	public Connector update() {
		log.info("location: " + getLocation().toString() + ", updatePolicy" + getUpdatePolicy());
		SPARQLClient sparqlClient = new SPARQLClient(getLocation().toString(), getUpdatePolicy());
		if (sparqlClient != null) {
			for (Map<String, String> sparqlResult : sparqlClient.getSparqlResult()) {
				if (isUpdatedSparqlValueByColumns(sparqlResult)) {
					break;
				}
			}
		}
		return getConnector();
	}

	/**
	 * This method searches for matching value from Sparql result
	 * applying given column names and associated values.
	 * @param sparqlResult
	 * @return true if value was updated and we can stop searching
	 */
	private boolean isUpdatedSparqlValueByColumns(Map<String, String> sparqlResult) {
		boolean res = false;
		String searchValue = "";
		String currentValue = "";
		log.info("result:");
		Set<Map.Entry<String, String>> sparqlSet = sparqlResult.entrySet();
		for (Map.Entry<String, String> entry : sparqlSet) {
			String variableName = entry.getKey();
			String value = entry.getValue();
			log.info(" " + variableName + ": " + value);
			if (variableName.equals(getConnector().getColumnNames().get(0))) {
				searchValue = value;
			}
			if (variableName.equals(getConnector().getColumnNames().get(1))) {
				currentValue = value;
			}
			log.info("searchValue: " + searchValue + ", currentValue: " + currentValue);
		}
		
		// fill tree with values retrieved from DBPedia
		if (searchValue.contains(getConnector().getSearchValue())) {
			log.info("found risk factor: " + getConnector().getSearchValue());
			getConnector().setValue(currentValue);
			res = true;
		}
		return res;
	}

	/* (non-Javadoc)
	 * @see ait.ffma.service.preservation.riskmanagement.api.preservationwatch.RepositoryDescription#getID()
	 */
	public String getID() {
		return "dbpedia";
	}

	/* (non-Javadoc)
	 * @see ait.ffma.service.preservation.riskmanagement.api.preservationwatch.RepositoryDescription#getName()
	 */
	public String getName() {
		return "DBPedia";
	}

	/* (non-Javadoc)
	 * @see ait.ffma.service.preservation.riskmanagement.api.preservationwatch.RepositoryDescription#getLocation()
	 */
	public URL getLocation() {
		URL retVal = null;
		try {
			retVal = new URL("http://dbpedia.org/sparql/");
		} catch (MalformedURLException e) {
			log.fine(e.getMessage());
		}
		return retVal;
	}

	/* (non-Javadoc)
	 * @see ait.ffma.service.preservation.riskmanagement.api.preservationwatch.RepositoryDescription#getProtocol()
	 */
	public String getProtocol() {
		return "SPARQL";
	}

	/* (non-Javadoc)
	 * @see ait.ffma.service.preservation.riskmanagement.api.preservationwatch.RepositoryDescription#getUpdatePolicy()
	 */
	public String getUpdatePolicy() {
		String sparql = RiskConstants.DBPEDIA_PREFIXES + "\n" + getQuery();
		String encodedRequest = null;
		try {
			encodedRequest = URLEncoder.encode(sparql, "utf-8");
		} catch (UnsupportedEncodingException e) {
			log.fine("Unable to encode sparql query string");
		}
		return RiskConstants.DBPEDIA_DEFAULT_URI + encodedRequest;
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
