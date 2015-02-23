package ait.ffma.preservation.riskmanagement;

import ait.ffma.common.exception.client.FfmaClientException;

public interface PreservationRiskmanagementLodDataAnalysis {

	/**
	 * This method informs client about existence of LOD data in service database. If data is not existing.
	 * It checks if following collections exist in database and contain data:
     *	1. file extension collections PronomFileFormat, DBPediaFileFormat and FreebaseFileFormat;
     *  2. summarized over all LOD repositories LOD formats, software and vendors collections (LODFormat, LODSoftware and LODVendor);
     *  3. file formats mapping collection DipFormatId (contains unique generated DIP identifier and maps file formats 
     *          identifiers and descriptions from all LOD repositories; contains mapping to DipSoftwareId collection);
     *  4. software mapping collection DipSoftwareId (contains unique generated DIP identifier and maps software identifiers 
     *          and description from all LOD repositories; contains mapping to DipFormatId collection);
     *  5. vendor mapping collection DipVendorId (contains unique generated DIP identifier and maps vendor identifiers and 
     *          description from all LOD repositories; contains mapping to DipFormatId collection)
	 * @return information about data existence - a list of existing collections
	 */
	public String checkLodData() throws FfmaClientException ;

	/** 
	 * This method retrieves LOD data from LOD repositories and store in database
	 * @param type
	 *        The type of storing. Retrieve data from all LOD repositories if type is 'All' or use repository name
	 * @param overwriteRepositoryFormats
	 *        Overwrite repository formats collections FreebaseFileFormat, DBPediaFileFormat and PronomFileFormat if true
	 * @param overwriteLodData
	 *        Overwrite summarized LOD data collections LODFormat, LODSoftware and LODVendor if true
	 * @return the list of updated collections
	 */
	public String storeAllExtensions(String type,
			boolean overwriteRepositoryFormats, boolean overwriteLodData)
			throws FfmaClientException;
	
	/**
	 * This method retrieves associated software for particular file format extension	
	 * @param ext
	 *        The file format extension
	 * @return
	 */
	public String retrieveSoftware(String ext) throws FfmaClientException;
	
	/**
	 * This method retrieves associated vendor for particular file format extension	
	 * @param ext
	 *        The file format extension
	 * @return
	 */
	public String retrieveVendor(String ext) throws FfmaClientException;
	
	/**
	 * This method retrieves preservation statistics for particular type and file format extension	
	 * Type 1 – all statistics; (All)
	 * Type 2 - references to LOD repository descriptions (PRONOM/DBPEDIA/FREEBASE); (ReferencesToLodRepositories)
	 * Type 3 - textual format descriptions; (TextualFormatDescriptions)
	 * Type 4 - software and Vendors supporting the given format. (SoftwareAndVendorsForFormat)
	 * @param type
	 *        The type of the preservation statistic report
	 * @param ext
	 *        The file format extension
	 * @return report in CSV format
	 */
	public String retrievePreservationStatistic(String type, String ext) throws FfmaClientException;
	
	/**
	 * This method enables configuration setting.
	 * @param configuration
	 */
	public void setConfiguration(PreservationRiskmanagementClientConfiguration configuration);
	
	/**
	 * This method returns risk management configuration.
	 * @return
	 */
	public PreservationRiskmanagementClientConfiguration getConfiguration();
	
}
