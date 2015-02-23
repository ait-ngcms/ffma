package ait.ffma.service.preservation.riskmanagement.api.riskanalysis.risk;

import ait.ffma.service.preservation.riskmanagement.api.riskanalysis.risk.riskmodel.RiskPropertySet;

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

public interface RiskScore {
	
	/**
	 * Returns a set of all RiskProperties that have been taken into account
	 * for the given data item profile to calculate the risk score
	 * @return
	 */
	public RiskPropertySet getRiskScoreBreakdown();
	
	/**
	 * Returns a set of all RiskProperties
	 * @return
	 */
	public RiskPropertySet getRiskProperties();
	
	/**
	 * Returns the risk score of a given data item profile. 
	 * Risk score values are integers between 0 (no risk) and 100 (highest risk)
	 * @return
	 */
	public Integer getRiskScore();
	
}
