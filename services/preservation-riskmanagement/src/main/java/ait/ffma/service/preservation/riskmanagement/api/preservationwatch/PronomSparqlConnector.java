package ait.ffma.service.preservation.riskmanagement.api.preservationwatch;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import org.json.JSONObject;

import ait.ffma.service.preservation.riskmanagement.api.lod.LODConstants;
import ait.ffma.service.preservation.riskmanagement.api.lod.lodmodel.LODPronomSoftware;
import ait.ffma.service.preservation.riskmanagement.api.riskanalysis.risk.Connector;

/**
 * This class manages connection to PRONOM repository using SPARQL
 */
public class PronomSparqlConnector { //implements RepositoryDescription {

	/**
     * A logger object.
     */
    private final Logger log = Logger.getLogger(getClass().getName());

	/**
	 * Request query to Pronom
	 */
	private String query;
	
	/**
	 * Request column name to Pronom
	 */
	private String columnName;
	
	/**
	 * Connector manages particular search data
	 */
	private Connector connector;
	
	/**
	 * This constructor initializes DBPedia connector with search data
	 * @param connector
	 */
	public PronomSparqlConnector(Connector connector) {
		this.connector = connector;
		query = connector.getQuery();
	}
	
	public PronomSparqlConnector() {
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
	public List<String> retrieve() {
		List<String> res = new ArrayList<String>();
		String queryLocal = getLocation().toString() + getUpdatePolicy();
		setQuery(queryLocal);
		log.info("query: " + getQuery());
		try {
			PronomSparqlClient sparqlClient = new PronomSparqlClient(getQuery());
			if (sparqlClient != null) {
				res = sparqlClient.retrieve(columnName);
			}
		}
		catch (Exception e) {
			log.info("Pronom query error: " + e.getMessage());
		}
		return res;
	}
	
	/* (non-Javadoc)
	 * @see ait.ffma.service.preservation.riskmanagement.api.preservationwatch.RepositoryDescription#retrieveAll()
	 */
	public Map<String, List<String>> retrieveAll() {
		Map<String, List<String>> res = new HashMap<String, List<String>>();
		String queryLocal = getLocation().toString() + getUpdatePolicy();
		setQuery(queryLocal);
		log.info("query: " + getQuery());
		PronomSparqlClient sparqlClient = new PronomSparqlClient(getQuery());
		if (sparqlClient != null) {
			res = sparqlClient.retrieveAll();
		}
		return res;
	}
	
	/**
	 * @return
	 */
	public LODPronomSoftware retrievePronomSoftware() {
		LODPronomSoftware res = null;
//		setQuery("http://test.linkeddatapronom.nationalarchives.gov.uk/sparql/endpoint.php?query=select+distinct+%3Fname+%3Fpuid+%3Fxpuid+where+{%0D%0A+%0D%0A+%3Fs+%3Chttp%3A%2F%2Fwww.w3.org%2F2000%2F01%2Frdf-schema%23label%3E+%3Fname+.%0D%0A+%3Fs+%3Chttp%3A%2F%2Freference.data.gov.uk%2Ftechnical-registry%2Fextension%3E+%22tif%22+.%0D%0A+OPTIONAL+{+%3Fs+%3Chttp%3A%2F%2Freference.data.gov.uk%2Ftechnical-registry%2FPUID%3E+%3Fpuid+.+}%0D%0A+OPTIONAL+{+%3Fs+%3Chttp%3A%2F%2Freference.data.gov.uk%2Ftechnical-registry%2FXPUID%3E+%3Fxpuid+.+}%0D%0A+%0D%0A}%0D%0Alimit+250&output=htmltab&jsonp=&key=&show_inline=1");
		log.info("query: " + getQuery());
		PronomSparqlClient sparqlClient = new PronomSparqlClient(getQuery());
		if (sparqlClient != null) {
			res = sparqlClient.retrievePronomSoftwareEntry();
		}
		return res;
	}
	
	/**
	 * @param columnName
	 */
	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}
	
	/**
	 * @return
	 */
	public String getColumnName() {
		return columnName;
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
		return "pronom";
	}

	/* (non-Javadoc)
	 * @see ait.ffma.service.preservation.riskmanagement.api.preservationwatch.RepositoryDescription#getName()
	 */
	public String getName() {
		return "PRONOM";
	}

	/* (non-Javadoc)
	 * @see ait.ffma.service.preservation.riskmanagement.api.preservationwatch.RepositoryDescription#getLocation()
	 */
	public URL getLocation() {
		URL retVal = null;
		try {
			retVal = new URL(LODConstants.PRONOM_LOD_LOCATION);
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
		return LODConstants.PRONOM_LOD_DEFAULT_URI + getQuery() + LODConstants.PRONOM_OUTPUT_FORMAT;
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
