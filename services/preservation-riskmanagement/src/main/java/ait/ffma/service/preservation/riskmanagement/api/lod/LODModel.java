package ait.ffma.service.preservation.riskmanagement.api.lod;

import java.util.List;

import ait.ffma.service.preservation.riskmanagement.api.lod.lodmodel.LODProperties;


public interface LODModel {
	
	/**
	 * This method analyzes XML LOD properties file defined in project configuration file.
	 * @return evaluated LOD properties
	 */
	public LODProperties analyze();
	
	/**
	 * This method retrieves LOD analysis structure.
	 * @return
	 */
	public LODProperties getLODProperties();
	
	/**
	 * This method initializes a LOD analysis structure.
	 * @param lod Linked open data
	 */
	public void setLODProperties(LODProperties lod);

	/**
	 * This method initializes property value.
	 * @param set
	 * @param propertyId
	 * @param propertyValue
	 */
	public void setPropertyValue(LODProperties lodProperties, String propertyId, List<String> propertyValues);
}
