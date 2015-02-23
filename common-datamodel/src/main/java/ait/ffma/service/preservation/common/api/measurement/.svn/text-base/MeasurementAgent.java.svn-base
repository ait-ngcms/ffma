package ait.ffma.service.preservation.common.api.measurement;

import java.net.URL;

/**
 * @author Andrew Lindley (AIT) - andrew.lindley@ait.ac.at
 * @since 13.12.2010 
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
 * 
 */
public interface MeasurementAgent {
	
	/** */
    public static enum AgentType { 
        /** This measurement event was carried out by a human user. */
        USER,
        /** This measurement event was carried within a preservation service. */
        SERVICE,
        /** This measurement event was carried out by the service's execution handler. */
        SERVICEEXECUTIONMANAGER,
        /** This measurement event was carried out by an external system*/
        EXTERNALSYSTEM
    }

	/**
	 * @return the agentType
	 */
	public AgentType getType();

	/**
	 * @param agentType the agentType to set
	 */
	public void setType(AgentType agentType);

	/**
	 * @return the username
	 */
	public String getUsername();

	/**
	 * @param username the username to set
	 */
	public void setUsername(String username);

	/**
	 * @return the userEnvironmentDescription
	 */
	public String getUserEnvironmentDescription();

	/**
	 * @param userEnvironmentDescription the userEnvironmentDescription to set
	 */
	public void setUserEnvironmentDescription(String userEnvironmentDescription);

	/**
	 * @return the serviceName
	 */
	public String getServiceName();

	/**
	 * @return the serviceEndpoint
	 */
	public URL getServiceEndpoint();

	/**
	 * @return
	 */
	public String getName();

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString();

	public String getAgentID();

}