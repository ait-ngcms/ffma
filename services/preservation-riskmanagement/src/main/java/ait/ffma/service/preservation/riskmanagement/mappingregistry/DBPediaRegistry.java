package ait.ffma.service.preservation.riskmanagement.mappingregistry;

import ait.ffma.service.preservation.riskmanagement.api.riskanalysis.risk.RiskConstants;


/**
 * This class is an internal risk mapping registry for DBPedia data registry
 */
public class DBPediaRegistry extends BaseRegistry {
	
	public DBPediaRegistry() {
		// mapping initializing could be made automatically
		getSoftwareVendorMap().put(RiskConstants.REG_ADOBE, "Adobe_Systems#Adobe#Adobe Systems#Adobe Systems Incorporated");
		getGraphicsFileFormatsMap().put(RiskConstants.REG_JPG, "Joint Photographic Experts Group#Joint_Photographic_Experts_Group");
		getGraphicsFileFormatsMap().put(RiskConstants.REG_PNG, "Portable Network Graphics#Portable_Network_Graphics");
		getGraphicsFileFormatsMap().put(RiskConstants.REG_CSV, "Comma-separated_values");
	}

}
