/**
 * 
 */
package ait.ffma.service.preservation.riskmanagement.api.preservationwatch;

import java.net.URL;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import ait.ffma.service.preservation.riskmanagement.api.riskanalysis.risk.Connector;

/**
 * @author Andrew Lindley (AIT) - andrew.lindley@ait.ac.at
 * @since 14.12.2010 
 *********************************************************
 * Copyright (c) 2010, 2011 The Ffma4Europeana Project Partners.
 *
 * All rights reserved. This program and the accompanying 
 * materials are made available under the terms of the 
 * European Union Public Licence (EUPL), version 1.1 which 
 * accompanies this distribution, and is available at 
 * http://ec.europa.eu/idabc/eupl.html
 *
 ***********************************************************
 * Parts of this work is based on The Planets Project
 * Copyright www.openplanetsfoundation.org
 * Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 ***********************************************************
 */
public interface RepositoryDescription {

	/**
	 * This is a unique repository ID.
	 * @return
	 */
	public String getID();
	
	/**
	 * This is a repository name.
	 * @return
	 */
	public String getName();
	
	/**
	 * This is a URL to repository.
	 * @return
	 */
	public URL getLocation();
	
	/**
	 * This is used protocol.
	 * @return
	 */
	public String getProtocol();
	
	/**
	 * This is a update policy definition.
	 * @return
	 */
	public String getUpdatePolicy();
	
	/**
	 * This is repository name space.
	 * @return
	 */
	public String getNameSpace();
	
	/**
	 * This method retrieves preselected fields from repository for passed query and search data.
	 * It supports requests for particular request columns, 
	 * retrieves matching value from repository and updates risk 
	 * factor value in the risk analysis model.
	 * @return The risk analysis model connector object with value 
	 */
	public Connector update();
	
	/**
	 * This method retrieves all fields from repository for passed query and search data.
	 * @return The resulting value map 
	 */
	public Map<String, String> retrieve();
	
	/**
	 * This method retrieves all fields from repository for passed query as a JSON object.
	 * @return The resulting JSON object
	 */
	public JSONObject retrieveJSON();
	
	/**
	 * This method retrieves all fields from repository for passed query without search data.
	 * A caller will analyze retrieved data.
	 * @return all entries for particular query
	 */
	public List<Map<String, String>> retrieveAll();

	/**
	 * This method initializes search connector instance
	 * @param connector
	 */
	public void setConnector(Connector connector);
	
	/**
	 * This method initializes request query
	 * @param query
	 */
	public void setQuery(String query);
	
}
