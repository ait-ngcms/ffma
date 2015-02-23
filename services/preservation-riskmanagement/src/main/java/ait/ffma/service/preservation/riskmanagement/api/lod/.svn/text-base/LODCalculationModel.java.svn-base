package ait.ffma.service.preservation.riskmanagement.api.lod;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import ait.ffma.service.preservation.riskmanagement.api.lod.lodmodel.LODProperties;
import ait.ffma.service.preservation.riskmanagement.api.lod.lodmodel.LODProperty;

/**
 * Calculation model manages LOD properties. 
 */
public class LODCalculationModel implements LODModel {
	
    /**
     * A logger object.
     */
    private final Logger log = Logger.getLogger(getClass().getName());
    
    /**
     * LOD properties structure that contains property sets, properties
     * and correspondent risk classifications extracted from XML files.
     */
    private LODProperties lodProperties = null;
       
	/**
	 * This is a regular way to initialize calculation model.
	 */
	public LODCalculationModel() {
	}

	/* (non-Javadoc)
	 * @see ait.ffma.service.preservation.riskmanagement.api.lod.LODModel#getLODProperties()
	 */
	public LODProperties getLODProperties() {
		lodProperties = LODUtils.loadLODProperties();
		if (lodProperties == null) {
			log.log(Level.SEVERE, "File lodProperties.xml could not be loaded!");
		}
		return lodProperties;
	}

	/* (non-Javadoc)
	 * @see ait.ffma.service.preservation.riskmanagement.api.lod.LODModel#analyze()
	 */
	public LODProperties analyze() {
		LODProperties lodPropertiesTmp = null;
		
		lodPropertiesTmp = LODUtils.loadLODPropertiesFromXML(LODConstants.PROPERTIES_XML);
		if (lodPropertiesTmp == null) {
			log.log(Level.SEVERE, "LOD properties xml could not be loaded!");
		}
		
		// evaluate property list
		setPropertyId(lodPropertiesTmp);
		log.info("LOD properties count: " + lodPropertiesTmp.getLODProperty().size());
		LODUtils.setLODPropertiesMap(lodPropertiesTmp.getLODProperty());
		
		return lodPropertiesTmp;
	}

	/* (non-Javadoc)
	 * @see ait.ffma.service.preservation.riskmanagement.api.lod.LODModel#setLODProperties(ait.ffma.service.preservation.riskmanagement.api.lod.lodmodel.LODProperties)
	 */
	public void setLODProperties(LODProperties lodPropertiesTmp) {
		this.lodProperties = lodPropertiesTmp;
	}

	/* (non-Javadoc)
	 * @see ait.ffma.service.preservation.riskmanagement.api.lod.LODModel#setPropertyValue(ait.ffma.service.preservation.riskmanagement.api.lod.lodmodel.LODProperties, java.lang.String, java.util.List)
	 */
	public void setPropertyValue(LODProperties lodProperties, String propertyId, List<String> propertyValues) {
		for (LODProperty p : lodProperties.getLODProperty()) {
			if (p.getId().equals(propertyId)) {
				p.getValue().setValues(propertyValues);
				return;
			}
		}
	}
	
	/**
	 * This method generates property IDs if they are not exist
	 * @param lodProperties
	 */
	private void setPropertyId(LODProperties lodProperties) {
		for (LODProperty lodProperty : lodProperties.getLODProperty()) {
			if (lodProperty.getId() == null || lodProperty.getId().equals("")) {
				lodProperty.setId(LODUtils.generateID());
				log.info("property: " + lodProperty.getName() + ", id: " + lodProperty.getId());
			}
		}
	}
	
}
