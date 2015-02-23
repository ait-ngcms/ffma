package ait.ffma.service.preservation.riskmanagement.api.preservationwatch;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import ait.ffma.service.preservation.riskmanagement.api.lod.LODStatisticsDBPediaUtils;

/**
 * This class provides SPARQL request functionality.
 */
public class SPARQLClient extends DefaultHandler {
	
    /**
     * A logger object.
     */
    private static final Logger log = Logger.getLogger(SPARQLClient.class.getName());

	private List<Map<String, String>> sparqlResult = new ArrayList<Map<String, String>>();
	private Map<String, String> tempBinding = null;
	private String tempVariableName = null;
	private String lastElementName = null;
		
	/**
	 * @param endpointURL
	 * @param sparql
	 */
	public SPARQLClient(String endpointURL, String sparql) {
		HttpClient client = new HttpClient();
		client.getHttpConnectionManager().getParams().
		setConnectionTimeout(10000);
		String url = endpointURL + sparql;
		HttpMethod method = new GetMethod(url);
		method.setFollowRedirects(false);
		try {
			client.executeMethod(method);
//			log.info("Response body: " + method.getResponseBodyAsString());
			InputStream ins = new ByteArrayInputStream(method.getResponseBodyAsString().getBytes("UTF-8"));
			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser sax = factory.newSAXParser();
			sax.parse(ins, this);
		} catch (HttpException he) {
			log.fine("Http error connecting to ’" + endpointURL + "’");
		} catch (IOException ioe) {
			log.fine("Unable to connect to ’" + endpointURL + "’");
		} catch (SAXException se) {
			log.fine("Unable to parse response from ’" + endpointURL + "’ (Exception: " + se.getLocalizedMessage() + ")");
		} catch (ParserConfigurationException pce) {
			log.fine("Unable to parse response from ’" + endpointURL + "’ (Exception: " + pce.getLocalizedMessage() + ")");
		}
		method.releaseConnection();
	}
	
//	public SPARQLClient(String query) {
//		HttpClient client = new HttpClient();
//		client.getHttpConnectionManager().getParams().
//		setConnectionTimeout(10000);
//		String url = query;
//		HttpMethod method = new GetMethod(url);
//		method.setFollowRedirects(false);
//		try {
//			client.executeMethod(method);
////			log.info("Response body: " + method.getResponseBodyAsString());
////			InputStream ins = new ByteArrayInputStream(method.getResponseBodyAsString().getBytes());
//			InputStream ins = new ByteArrayInputStream(method.getResponseBodyAsString().getBytes("UTF-8"));
////			InputStream ins = new ByteArrayInputStream(method.getResponseBodyAsString().getBytes("UTF-16"));
//			SAXParserFactory factory = SAXParserFactory.newInstance();
//			SAXParser sax = factory.newSAXParser();
//			sax.parse(ins, this);
//		} catch (HttpException he) {
//			log.fine("Http error connecting to ’" + query + "’");
//		} catch (IOException ioe) {
//			log.fine("Unable to connect to ’" + query + "’");
//		} catch (SAXException se) {
//			log.fine("Unable to parse response from ’" + query + "’ (Exception: " + se.getLocalizedMessage() + ")");
//		} catch (ParserConfigurationException pce) {
//			log.fine("Unable to parse response from ’" + query + "’ (Exception: " + pce.getLocalizedMessage() + ")");
//		} catch (Exception e) {
//			log.fine("SPARQL client exception. Unable to parse response from ’" + query + "’ (Exception: " + e.getLocalizedMessage() + ")");
//		}
//		method.releaseConnection();
//	}
	
	/* (non-Javadoc)
	 * @see org.xml.sax.helpers.DefaultHandler#startElement(java.lang.String, java.lang.String, java.lang.String, org.xml.sax.Attributes)
	 */
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		if (qName.equalsIgnoreCase("result")) {
			tempBinding = new HashMap<String, String>();
		}
		if (qName.equalsIgnoreCase("binding")) {
			tempVariableName = attributes.getValue("name");
		}
		lastElementName = qName;
	}
	
	/* (non-Javadoc)
	 * @see org.xml.sax.helpers.DefaultHandler#endElement(java.lang.String, java.lang.String, java.lang.String)
	 */
	public void endElement(String uri, String localName, String qName) throws SAXException {
		if (qName.equalsIgnoreCase("result")) {
			if (!sparqlResult.contains(tempBinding)) {
				sparqlResult.add(tempBinding);
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see org.xml.sax.helpers.DefaultHandler#characters(char[], int, int)
	 */
	public void characters(char[] ch, int start, int length) throws SAXException {
		String s = new String(ch, start, length).trim();
		if (s.length() > 0) {
			if ("literal".equals(lastElementName)) {
				String existingValue = "";
				String resStr = "";
				if (tempBinding.containsKey(tempVariableName)) {
					existingValue = tempBinding.get(tempVariableName);
					resStr = existingValue + " " + s;
				} else {
					resStr = s;
				}
				tempBinding.put(tempVariableName, resStr);
			}
			if ("uri".equals(lastElementName)) {
				tempBinding.put(tempVariableName, "<" + s + ">");
			}
		}
	}

	/**
	 * @return SPARQL response
	 */
	public List<Map<String, String>> getSparqlResult() {
		return sparqlResult;
	}
	
	/**
	 * This method iterates through the repository response for all 
	 * stored objects filtering by search value.
	 * @param searchValue
	 * @return objects that match search value
	 */
	public List<Map<String, String>> retrieveAll(String searchValue) {
		List<Map<String, String>> list = new ArrayList<Map<String, String>>(); 

		for (Map<String, String> sparqlResultTmp : getSparqlResult()) {
//			log.info("result:");
			LODStatisticsDBPediaUtils.parseSparqlResponse(searchValue, list, sparqlResultTmp);
		}
		return list;
	}

	/**
	 * This method iterates through the repository response for all 
	 * stored objects filtering by search value. Iteration stops if
	 * searched object is found.
	 * @param searchValue
	 * @return objects that match search value
	 */
	public Map<String, String> retrieve(String searchValue) {
		for (Map<String, String> sparqlResultTmp : getSparqlResult()) {
			Set<Map.Entry<String, String>> sparqlSet = sparqlResultTmp.entrySet();
			for (Map.Entry<String, String> entry : sparqlSet) {
				String value = entry.getValue();
				if (value.contains(searchValue)) {
					log.info("found risk factor: " + searchValue);
					return sparqlResultTmp;
				}
			}
		}
		return new HashMap<String, String>();
	}
	
}
