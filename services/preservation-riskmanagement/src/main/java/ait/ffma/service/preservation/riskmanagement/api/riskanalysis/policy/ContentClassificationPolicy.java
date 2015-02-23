package ait.ffma.service.preservation.riskmanagement.api.riskanalysis.policy;

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

public interface ContentClassificationPolicy {
	
	/**
	 * Sets the inspection depth, i.e. defines how many services are fired on
	 * a individual record. e.g. select the most speediest services, etc.
	 * 
	 **/
	enum ProfilePolicy{
		simple, extended, fastest, complete
	}
}
