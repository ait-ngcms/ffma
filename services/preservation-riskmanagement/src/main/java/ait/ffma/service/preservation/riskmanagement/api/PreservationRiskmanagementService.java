package ait.ffma.service.preservation.riskmanagement.api;

import java.util.List;

import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.common.params.ModifiableSolrParams;
import org.apache.solr.common.util.NamedList;

import ait.ffma.common.exception.FfmaCommonException;
import ait.ffma.common.service.api.FfmaService;
import ait.ffma.domain.FfmaDomainObject;
import ait.ffma.domain.preservation.riskmanagement.DipFormatId;
import ait.ffma.service.preservation.riskmanagement.api.riskanalysis.risk.RiskConstants.RiskReportTypesEnum;


public interface PreservationRiskmanagementService extends FfmaService {

	
	/**
	 * This method computes risk score HTML report for particular collection applying analysis configuration
	 * and customized classification
	 * @param id
	 *        The Europeana collection ID
	 * @param config
	 *        The analysis configuration containing the names of the preservation dimensions
	 * @param classification
	 *        The path to the XML file that comprises risk analysis classifications like
	 *        weight and risk score depending on value
	 * @param reportType
	 *        The type of the risk report
	 * @return risk score report
	 */
//	public String computeRiskReportHtml(
//			int id, String config, String classification, RiskReportTypesEnum reportType);
	
	/**
	 * This method computes risk score object for particular collection applying analysis configuration
	 * and customized classification
	 * @param id
	 *        The Europeana collection ID
	 * @param config
	 *        The analysis configuration containing the names of the preservation dimensions
	 * @param classification
	 *        The path to the XML file that comprises risk analysis classifications like
	 *        weight and risk score depending on value
	 * @param reportType
	 *        The type of the risk report
	 * @return risk score report
	 */
//	public PreservationDimensions computeRiskReport(
//			int id, String config, String classification, RiskReportTypesEnum reportType);
	
	/**
	 * This method computes risk score object for particular collection by collection name 
	 * applying analysis configuration and customized classification
	 * @param collectionName
	 *        The Europeana collection name
	 * @param config
	 *        The analysis configuration containing the names of the preservation dimensions
	 * @param classification
	 *        The path to the XML file that comprises risk analysis classifications like
	 *        weight and risk score depending on value
	 * @param reportType
	 *        The type of the risk report
	 * @return risk score report
	 */
//	public PreservationDimensions computeRiskReport(
//			String collectionName, String config, String classification, RiskReportTypesEnum reportType);
	
	/**
	 * This method computes risk score for particular collection applying analysis configuration
	 * and customized classification. File format name used as identifier.
	 * @param name
	 *        The file format extension name
	 * @param config
	 *        The analysis configuration containing the names of the preservation dimensions
	 * @param classification
	 *        The path to the XML file that comprises risk analysis classifications like
	 *        weight and risk score depending on value
	 * @param reportType
	 *        The type of the risk report
	 * @return risk score report
	 */
	public String computeRiskReportHtml(
			String name, String config, String classification,
			RiskReportTypesEnum reportType);

	/**
	 * This method computes overall risk score report object for particular collection applying analysis configuration
	 * and customized classification
	 * @param id
	 *        The Europeana collection ID
	 * @param config
	 *        The analysis configuration containing the names of the preservation dimensions
	 * @param classification
	 *        The path to the XML file that comprises risk analysis classifications like
	 *        weight and risk score depending on value
	 * @param reportType
	 *        The type of the risk report
	 * @return risk score report
	 */
//	public OverallRiskScoreReport computeOverallRiskScore(
//			int id, String config, String classification, RiskReportTypesEnum reportType);
	
	/**
	 * This method computes overall risk score report object for particular collection applying analysis configuration
	 * and customized classification
	 * @param collectionName
	 *        The Europeana collection name
	 * @param config
	 *        The analysis configuration containing the names of the preservation dimensions
	 * @param classification
	 *        The path to the XML file that comprises risk analysis classifications like
	 *        weight and risk score depending on value
	 * @param reportType
	 *        The type of the risk report
	 * @return risk score report
	 */
//	public OverallRiskScoreReport computeOverallRiskScore(
//			String collectionName, String config, String classification, RiskReportTypesEnum reportType);
	
	/*********************
	 * LOD DATA ANALYSIS
	 *********************/

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
	public String checkLodData();

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
	public String storeAllExtensions(String type, boolean overwriteRepositoryFormats, boolean overwriteLodData);
	
	/**
	 * This method retrieves associated software for particular file format extension	
	 * @param ext
	 *        The file format extension
	 * @return
	 */
	public String retrieveSoftware(String ext);
	
	/**
	 * This method retrieves associated vendor for particular file format extension	
	 * @param ext
	 *        The file format extension
	 * @return
	 */
	public String retrieveVendor(String ext);
	
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
	public String retrievePreservationStatistic(String type, String ext);
	
	/**
	 * This method creates HTML report for passed file format extension
	 * and software (LODSoftware) list passed as string
	 * @param report
	 *        The LODSoftware list in text format
	 * @param ext
	 *        The file format extension
	 * @return HTML report
	 */
	public String createHtmlForLODSoftware(String report, String ext);

	/**
	 * This method creates HTML report for passed file format extension
	 * and vendors (LODVendor) list passed as string
	 * @param report
	 *        The LODVendor list in text format
	 * @param ext
	 *        The file format extension
	 * @return HTML report
	 */
	public String createHtmlForLODVendor(String report, String ext);

	/**
	 * This method retrieves DipFormatId object for particular file format extension	
	 * @param ext
	 *        The file format extension
	 * @return the DipFormatId object in HTML format
	 */
	public String retrieveDipFormatId(String ext);

	/**
	 * This method updates DipFormatId fields in solr.
	 * @param dipFormatId
	 * @return
	 * @throws FfmaCommonException
	 */
	public DipFormatId updateFFMetadata(DipFormatId dipFormatId) throws FfmaCommonException;
	
	/**
	 * This method retrieves DipFormatId object from database for given extension.
	 * @param ext
	 * @return
	 */
	public DipFormatId retrieveDipFormatIdObj(String ext);

	/**
	 * Store DipFormatId document to solr.
	 * @param dipFormatId
	 * @throws FfmaCommonException
	 */
	public void storeFFDoc(DipFormatId dipFormatId) throws FfmaCommonException;

	/**
	 * Read file format document from solr.
	 * @param id
	 * @return
	 * @throws FfmaCommonException
	 */
	public DipFormatId readFFDoc(String id) throws FfmaCommonException;

	/**
	 * This method sends query to solr and returns result document.
	 * @param query
	 * @return
	 * @throws FfmaCommonException
	 */
	public SolrInputDocument querySolr(String query) throws FfmaCommonException;

	/**
	 * This method sends query to solr using parameters and returns result document.
	 * @param params
	 * @return
	 */
//	public List<SolrInputDocument> querySolrParams(ModifiableSolrParams params) throws FfmaCommonException;
	public NamedList<Object> querySolrParams(ModifiableSolrParams params) throws FfmaCommonException;
	
	/**
	 * @return
	 * @throws FfmaCommonException
	 */
	public List<FfmaDomainObject> retrieveLodFormatCollection() throws FfmaCommonException;

	/**
	 * @param lodFormatList
	 * @throws FfmaCommonException
	 */
	public void storeLODFormatsInSolr(List<FfmaDomainObject> lodFormatList) throws FfmaCommonException;

	/**
	 * @return
	 * @throws FfmaCommonException
	 */
	public List<FfmaDomainObject> retrieveDipFormatIdCollection() throws FfmaCommonException;
	
	/**
	 * @return
	 * @throws FfmaCommonException
	 */
	public List<FfmaDomainObject> retrieveDipSoftwareIdCollection() throws FfmaCommonException;
	
	/**
	 * @param dipFormatId
	 * @throws FfmaCommonException
	 */
	public void storeDipFormatIdCollectionInSolr(DipFormatId dipFormatId) throws FfmaCommonException;
	
	/**
	 * @param params
	 * @return
	 * @throws FfmaCommonException
	 */
	public NamedList<Object> queryDipFormatIdSolrParams(ModifiableSolrParams params) throws FfmaCommonException;
	

}
