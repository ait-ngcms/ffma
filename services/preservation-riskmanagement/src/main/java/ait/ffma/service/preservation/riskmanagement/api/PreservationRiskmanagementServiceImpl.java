package ait.ffma.service.preservation.riskmanagement.api;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.StatusLine;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.log4j.Logger;
import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.common.params.ModifiableSolrParams;
import org.apache.solr.common.util.NamedList;
import org.springframework.beans.factory.annotation.Autowired;

import ait.ffma.common.dao.mongodb.MongoDbConstants;
import ait.ffma.common.exception.FfmaCommonException;
import ait.ffma.common.service.api.BaseFfmaService;
import ait.ffma.common.service.api.DipFormatIdSynchronizer;
import ait.ffma.common.service.api.FFSynchronizer;
import ait.ffma.common.service.api.LODFormatSynchronizer;
import ait.ffma.domain.FfmaDomainObject;
import ait.ffma.domain.preservation.riskmanagement.DBPediaFileFormat;
import ait.ffma.domain.preservation.riskmanagement.DipFormatId;
import ait.ffma.domain.preservation.riskmanagement.DipSoftwareId;
import ait.ffma.domain.preservation.riskmanagement.FormatRiskAnalysisReport;
import ait.ffma.domain.preservation.riskmanagement.FreebaseFileFormat;
import ait.ffma.domain.preservation.riskmanagement.LODFormat;
import ait.ffma.domain.preservation.riskmanagement.PronomFileFormat;
import ait.ffma.service.preservation.riskmanagement.PreservationRiskmanagementConfiguration;
import ait.ffma.service.preservation.riskmanagement.api.fingerdetection.FingerDetector;
import ait.ffma.service.preservation.riskmanagement.api.lod.LODConstants;
import ait.ffma.service.preservation.riskmanagement.api.lod.LODCreator;
import ait.ffma.service.preservation.riskmanagement.api.lod.LODReportGenerator;
import ait.ffma.service.preservation.riskmanagement.api.riskanalysis.risk.ReportConstants;
import ait.ffma.service.preservation.riskmanagement.api.riskanalysis.risk.RiskConstants;
import ait.ffma.service.preservation.riskmanagement.api.riskanalysis.risk.RiskConstants.RiskReportTypesEnum;
import ait.ffma.service.preservation.riskmanagement.api.riskanalysis.risk.RiskReportGenerator;
import ait.ffma.service.preservation.riskmanagement.dao.PreservationRiskmanagementDao;


/**
 * This class implements risk management methods on the server side.
 *
 */
public class PreservationRiskmanagementServiceImpl extends BaseFfmaService implements
		PreservationRiskmanagementService {

	/**
	 * Logging variable
	 */
	private Logger log = Logger.getLogger(getClass());

	/**
	 * The data access object for preservation risk management
	 */
	@Autowired
	private PreservationRiskmanagementDao preservationRiskmanagementDao;

	/**
	 * The LOD creator object
	 */
	@Autowired
	private LODCreator lodCreator;

	/**
	 * The risk report generator object
	 */
	@Autowired
	private RiskReportGenerator riskReportGenerator;

	/**
	 * @return
	 */
	public RiskReportGenerator getRiskReportGenerator() {
		return riskReportGenerator;
	}

	/**
	 * @param riskReportGenerator
	 */
	public void setRiskReportGenerator(
			RiskReportGenerator riskReportGenerator) {
		this.riskReportGenerator = riskReportGenerator;
	}


	/**
	 * @return
	 */
	public LODCreator getLodCreator() {
		return lodCreator;
	}

	/**
	 * @param lodCreator
	 */
	public void setLodCreator(LODCreator lodCreator) {
		this.lodCreator = lodCreator;
	}

	/**
	 * @return
	 */
	public PreservationRiskmanagementDao getPreservationRiskmanagementDao() {
		return preservationRiskmanagementDao;
	}

	/**
	 * @return
	 */
	public LODReportGenerator getLodReportGenerator() {
		return lodReportGenerator;
	}

	/**
	 * @param lodReportGenerator
	 */
	public void setLodReportGenerator(LODReportGenerator lodReportGenerator) {
		this.lodReportGenerator = lodReportGenerator;
	}

	/**
	 * The LOD report generator object
	 */
	@Autowired
	private LODReportGenerator lodReportGenerator;

	/**
	 * The configuration object for preservation risk management service
	 */
	@Autowired
	private PreservationRiskmanagementConfiguration configuration;

	/**
	 * @param preservationRiskmanagementDao
	 */
	public void setPreservationRiskmanagementDao(PreservationRiskmanagementDao preservationRiskmanagementDao) {
		this.preservationRiskmanagementDao = preservationRiskmanagementDao;
	}

	/**
	 * @return
	 */
	public PreservationRiskmanagementConfiguration getConfiguration() {
		return configuration;
	}

	/**
	 * @param configuration
	 */
	public void setConfiguration(PreservationRiskmanagementConfiguration configuration) {
		this.configuration = configuration;
	}
	

	@Autowired
	private FFSynchronizer ffDocIndexer;

	public static final int DEFAULT_MAX_RESULTS = 100;

	/**
	 * getter method for ffDocIndexer
	 * @return
	 */
	public FFSynchronizer getFFDocIndexer() {
		return ffDocIndexer;
	}

	@Autowired
	private LODFormatSynchronizer lodFormatIndexer;

	/**
	 * getter method for ffDocIndexer
	 * @return
	 */
	public LODFormatSynchronizer getLodFormatIndexer() {
		return lodFormatIndexer;
	}

	@Autowired
	private DipFormatIdSynchronizer dipFormatIdIndexer;

	/**
	 * getter method for DipFormatId
	 * @return
	 */
	public DipFormatIdSynchronizer getDipFormatIdIndexer() {
		return dipFormatIdIndexer;
	}

	/**
	 * This method tries to establish HTTP connection for passed URI
	 * @param uri The URI to verify
	 * @param responseLineList 
	 *        This list collects response lines for broken URIs
	 * @param brokenUriList
	 *        This list collects broken URIs
	 */
	private void verifyUri(String uri, List<String> responseLineList, List<String> brokenUriList) {
		HttpClient client = new HttpClient();
		client.getHttpConnectionManager().getParams().
		setConnectionTimeout(10000);
		try {
			HttpMethod method = new GetMethod(uri);
			method.setFollowRedirects(true);
			client.executeMethod(method);
			int response = method.getStatusCode();
			if (response != 200) {
				StatusLine responseLine = method.getStatusLine();
				log.info("uri: " + uri + ", response: " + response + ", responseLine: " + responseLine.toString());
				brokenUriList.add(uri);
				responseLineList.add(responseLine.toString());
			}
			method.releaseConnection();
		} catch (IOException e) {
			log.info("Unable to connect to ’" + uri + "’ verification error: " + e);
			brokenUriList.add(uri);
			responseLineList.add(e.getMessage());
		} 
	}

	/**
	 * This method analyzes string arrays regarding missing fields.
	 * @param array
	 *        The string array
	 * @return analyzing result - true if field exists
	 */
	private boolean analyseEuropeanaField(String[] array) {
		boolean res = true;
		if (array == null) {
			res = false;
		} else {
			List<String> list = Arrays.asList(array);
			Iterator<String> iter = list.iterator();
			while (iter.hasNext()) {
				String field = iter.next();
				if (field.length() == 0 || field.equals(RiskConstants.EMPTY_STRING)) {
					res = false;
					break;
				}
			}
		}
		return res;
	}
	
	/**
	 * @param buf
	 * @param totalCount
	 * @param totalFields
	 * @param disseminationMap
	 */
	private void printDisseminationMap(StringBuffer buf, int totalCount,
			int totalFields, Map<String, Integer> disseminationMap) {
		Iterator<String> it = disseminationMap.keySet().iterator(); 
		while(it.hasNext()) { 
			String key = (String) it.next(); 
			printDisseminationValue(buf, totalFields, disseminationMap, key); 
			buf.append(totalFields);
			buf.append(RiskConstants.CSV_SEPARATOR); 
			buf.append(totalCount);
			buf.append(RiskConstants.CSV_SEPARATOR); 
			buf.append(RiskConstants.LINE_END);
		}
	}

	/**
	 * @param buf
	 * @param totalFields
	 * @param disseminationMap
	 * @param key
	 */
	private void printEmptyFieldsValue(StringBuffer buf, int totalFields,
			Map<String, Integer> disseminationMap, String key) {
		int val = disseminationMap.get(key); 
		buf.append(key);
		buf.append(RiskConstants.CSV_SEPARATOR); 
		buf.append(Math.round(((float) val*ReportConstants.INTHUNDRED/totalFields) * ReportConstants.INTHUNDRED)/ReportConstants.FLOATHUNDRED);
		buf.append(RiskConstants.CSV_SEPARATOR);
		buf.append(val);
		buf.append(RiskConstants.CSV_SEPARATOR); 		
	}

	/**
	 * @param buf
	 * @param totalFields
	 * @param disseminationMap
	 * @param key
	 */
	private void printDisseminationValue(StringBuffer buf, int totalFields,
			Map<String, Integer> disseminationMap, String key) {
		int val = disseminationMap.get(key); 
		buf.append(key);
		buf.append(RiskConstants.CSV_SEPARATOR); 
		buf.append(val);
		buf.append(RiskConstants.CSV_SEPARATOR); 
		buf.append(Math.round(((float) val*ReportConstants.INTHUNDRED/totalFields) * ReportConstants.INTHUNDRED)/ReportConstants.FLOATHUNDRED);
		buf.append(RiskConstants.CSV_SEPARATOR);
	}
	/**
	 * @param buf
	 * @param totalCount
	 * @param disseminationRaster
	 * @param disseminationMap
	 */
	private void printDisseminationMap(StringBuffer buf, int totalCount,
			List<Integer> disseminationRaster,
			Map<Integer, Integer> disseminationMap) {
		for (Integer i : disseminationRaster) {  
			buf.append(i);
			buf.append(RiskConstants.CSV_SEPARATOR); 
			buf.append(disseminationMap.get(i));
			buf.append(RiskConstants.CSV_SEPARATOR); 
			buf.append(totalCount);
			buf.append(RiskConstants.CSV_SEPARATOR); 
			buf.append(RiskConstants.LINE_END);
		}
	}

	/**
	 * @param disseminationMap
	 * @param totalFields
	 * @param totalEmptyFields
	 */
	private void computeDisseminationMap(
			Map<Integer, Integer> disseminationMap, int totalFields,
			int totalEmptyFields) {
		int dissemination = (totalFields - totalEmptyFields)*ReportConstants.INTHUNDRED/totalFields;
		if (dissemination <= 20) {
			disseminationMap.put(20, disseminationMap.get(20) + ReportConstants.INIT_VAL);
		} 
		if (dissemination > 20 && dissemination <= 40) {
			disseminationMap.put(40, disseminationMap.get(40) + ReportConstants.INIT_VAL);
		}
		if (dissemination > 40 && dissemination <= 60) {
			disseminationMap.put(60, disseminationMap.get(60) + ReportConstants.INIT_VAL);
		}
		if (dissemination > 60 && dissemination <= 80) {
			disseminationMap.put(80, disseminationMap.get(80) + ReportConstants.INIT_VAL);
		}
		if (dissemination > 80) {
			disseminationMap.put(100, disseminationMap.get(100) + ReportConstants.INIT_VAL);
		}
	}

	/**
	 * @param totalEmptyFields
	 * @param value
	 * @return
	 */
	private int calculateDisseminationEmptyFieldsCount(int totalEmptyFields,
			Object value) {
		int totalEmptyFieldsRes = totalEmptyFields;
		if (value != null) {
			if (value instanceof String) {
				if (RiskConstants.EMPTY_STRING.equals(value)) {
					totalEmptyFieldsRes++;
				}
			}
			if (value instanceof String[]) {											
				String[] valueStrArray = ((String[]) value);
				if (valueStrArray[0] == null || valueStrArray[0].equals(RiskConstants.EMPTY_STRING)) {
					totalEmptyFieldsRes++;
				}
			} 
		} else {
			totalEmptyFieldsRes++;
		}
		return totalEmptyFieldsRes;
	}

	/**
	 * @param buf
	 * @param totalCount
	 * @param totalFields
	 * @param totalEmptyFields
	 * @param emptyFieldsMap
	 */
	private void printEmptyFieldsEvaluationResults(StringBuffer buf,
			int totalCount, int totalFields, int totalEmptyFields,
			Map<String, Integer> emptyFieldsMap) {
		Iterator<String> it = emptyFieldsMap.keySet().iterator(); 
		while(it.hasNext()) { 
			String key = (String) it.next(); 
			printEmptyFieldsValue(buf, totalFields, emptyFieldsMap, key); 
			buf.append(totalEmptyFields);
			buf.append(RiskConstants.CSV_SEPARATOR); 
			buf.append(totalFields);
			buf.append(RiskConstants.CSV_SEPARATOR); 
			buf.append(totalCount);
			buf.append(RiskConstants.CSV_SEPARATOR); 
			buf.append(RiskConstants.LINE_END);
		}
	}

	/**
	 * @param totalEmptyFields
	 * @param emptyFieldsMap
	 * @param m
	 * @param value
	 * @return
	 */
	private int calculateEmptyFieldsForStringArray(int totalEmptyFields,
			Map<String, Integer> emptyFieldsMap, Method m, Object value) {
		int totalEmptyFieldsRes = totalEmptyFields;
		if (value instanceof String[]) {											
			String[] valueStrArray = ((String[]) value);
			if (valueStrArray[0] == null || RiskConstants.EMPTY_STRING.equals(valueStrArray[0])) {
				String field = m.getName().substring(ReportConstants.GETSTRLEN);
				if (!emptyFieldsMap.containsKey(field)) {
					emptyFieldsMap.put(field, ReportConstants.INIT_VAL);
				} else {
					emptyFieldsMap.put(field, emptyFieldsMap.get(field) + ReportConstants.INIT_VAL);
				}
				totalEmptyFieldsRes++;
			}
		}
		return totalEmptyFieldsRes;
	}

	/**
	 * @param totalEmptyFields
	 * @param emptyFieldsMap
	 * @param m
	 * @param value
	 * @return
	 */
	private int calculateEmptyFieldsForString(int totalEmptyFields,
			Map<String, Integer> emptyFieldsMap, Method m, Object value) {
		int totalEmptyFieldsRes = totalEmptyFields;
		if (value instanceof String) {
			if (RiskConstants.EMPTY_STRING.equals(value)) {
				String field = m.getName().substring(ReportConstants.GETSTRLEN);
				if (!emptyFieldsMap.containsKey(field)) {
					emptyFieldsMap.put(field, ReportConstants.INIT_VAL);
				} else {
					emptyFieldsMap.put(field, emptyFieldsMap.get(field) + ReportConstants.INIT_VAL);
				}
				totalEmptyFieldsRes++;
			}
		}
		return totalEmptyFieldsRes;
	}

	/*********************
	 * LOD DATA ANALYSIS
	 *********************/

	/* (non-Javadoc)
	 * @see ait.ffma.service.preservation.riskmanagement.api.PreservationRiskmanagementService#checkLodData()
	 */
	public String checkLodData() {
        return getLodCreator().checkLodData();
	}

	/* (non-Javadoc)
	 * @see ait.ffma.service.preservation.riskmanagement.api.PreservationRiskmanagementService#retrieveSoftware(java.lang.String)
	 */
	public String retrieveSoftware(String ext) {
		return getLodReportGenerator().retrieveSoftware(ext);
	}

	/* (non-Javadoc)
	 * @see ait.ffma.service.preservation.riskmanagement.api.PreservationRiskmanagementService#retrieveVendor(java.lang.String)
	 */
	public String retrieveVendor(String ext) {
		return getLodReportGenerator().retrieveVendor(ext);
	}
	
	/* (non-Javadoc)
	 * @see ait.ffma.service.preservation.riskmanagement.api.PreservationRiskmanagementService#retrievePreservationStatistic(java.lang.String, java.lang.String)
	 */
	public String retrievePreservationStatistic(String type, String ext) {
		return getLodReportGenerator().retrievePreservationStatistic(type, ext);
	}

	/* (non-Javadoc)
	 * @see ait.ffma.service.preservation.riskmanagement.api.PreservationRiskmanagementService#createHtmlForLODSoftware(java.lang.String, java.lang.String)
	 */
	public String createHtmlForLODSoftware(String report, String ext) {
		return getLodReportGenerator().createHtmlForLODSoftware(report, ext);
	}
	
	/* (non-Javadoc)
	 * @see ait.ffma.service.preservation.riskmanagement.api.PreservationRiskmanagementService#createHtmlForLODSoftware(java.lang.String, java.lang.String)
	 */
	public String createHtmlForLODVendor(String report, String ext) {
		return getLodReportGenerator().createHtmlForLODVendor(report, ext);
	}

	/* (non-Javadoc)
	 * @see ait.ffma.service.preservation.riskmanagement.api.PreservationRiskmanagementService#retrieveDipFormatId(java.lang.String)
	 */
	public String retrieveDipFormatId(String ext) {
		return getLodReportGenerator().retrieveDipFormatId(ext);
	}

	/* (non-Javadoc)
	 * @see ait.ffma.service.preservation.riskmanagement.api.PreservationRiskmanagementService#retrieveDipFormatIdObj(java.lang.String)
	 */
	public DipFormatId retrieveDipFormatIdObj(String ext) {
		return getLodReportGenerator().retrieveDipFormatIdObj(ext);
	}

	/* (non-Javadoc)
	 * @see ait.ffma.service.preservation.riskmanagement.api.PreservationRiskmanagementService#storeAllExtensions(java.lang.String, boolean, boolean)
	 */
	public String storeAllExtensions(String type,
			boolean overwriteRepositoryFormats, boolean overwriteLodData) {
		return getLodCreator().storeAllExtensions(type,	overwriteRepositoryFormats, overwriteLodData);
	}

	/* (non-Javadoc)
	 * @see ait.ffma.service.preservation.riskmanagement.api.PreservationRiskmanagementService#updateFFMetadata(ait.ffma.domain.preservation.riskmanagement.DipFormatId)
	 */
	public DipFormatId updateFFMetadata(DipFormatId dipFormatId)
			throws FfmaCommonException {
		try {
//			DipFormatId storedObject = storeFFMetadata(dipFormatId);
		
			// update semantic links 
			// send the afd and not storedobject since the links are not persisted in FfmaFullDoc Db collection
//			List<SemanticLink> links = updateSemanticLinks(afd);
//			resetCategoryIds(storedObject, links);
		
			// TODO implement indexing, add configurations to disable reindexing
//			EuropeanaId euId = getCollectionObject(Long.valueOf(afd
//					.getEuropeanaId()));
//			getFFDocIndexer().updateFFDoc(euId, storedObject);
			getFFDocIndexer().updateFFDoc(dipFormatId.getDipId(), dipFormatId);
		
			// retrieve
//			return storedObject;
			return dipFormatId;
		
		} catch (Exception e) {
			throw new FfmaCommonException(
					"Cannot store DipFormatId: " + dipFormatId, e);
		}
	}

	/* (non-Javadoc)
	 * @see ait.ffma.service.preservation.riskmanagement.api.PreservationRiskmanagementService#storeFFDoc(ait.ffma.domain.preservation.riskmanagement.DipFormatId)
	 */
	public void storeFFDoc(DipFormatId dipFormatId)
			throws FfmaCommonException {
		try {
			getFFDocIndexer().storeFFDoc(dipFormatId);
		} catch (Exception e) {
			throw new FfmaCommonException(
					"Cannot store DipFormatId: " + dipFormatId, e);
		}
	}

	/* (non-Javadoc)
	 * @see ait.ffma.service.preservation.riskmanagement.api.PreservationRiskmanagementService#readFFDoc(java.lang.String)
	 */
	public DipFormatId readFFDoc(String id)
			throws FfmaCommonException {
		try {
			return (DipFormatId) getFFDocIndexer().readFFDoc(id);
		} catch (Exception e) {
			throw new FfmaCommonException(
					"Cannot read DipFormatId: " + id, e);
		}
	}

	/* (non-Javadoc)
	 * @see ait.ffma.service.preservation.riskmanagement.api.PreservationRiskmanagementService#querySolrParams(org.apache.solr.common.params.ModifiableSolrParams)
	 */
	public NamedList<Object> querySolrParams(ModifiableSolrParams params) throws FfmaCommonException {
//		public List<SolrInputDocument> querySolrParams(ModifiableSolrParams params) throws FfmaCommonException {
		try {
			return (NamedList<Object>) getFFDocIndexer().createInputDocumentByQuery(params);
//			return (List<SolrInputDocument>) getFFDocIndexer().createInputDocumentByQuery(params);
		} catch (Exception e) {
			throw new FfmaCommonException(
					"Cannot read DipFormatId: " + params, e);
		}
	}

	public NamedList<Object> queryDipFormatIdSolrParams(ModifiableSolrParams params) throws FfmaCommonException {
//		public List<SolrInputDocument> querySolrParams(ModifiableSolrParams params) throws FfmaCommonException {
		try {
			return (NamedList<Object>) getDipFormatIdIndexer().createInputDocumentByQuery(params);
//			return (List<SolrInputDocument>) getFFDocIndexer().createInputDocumentByQuery(params);
		} catch (Exception e) {
			throw new FfmaCommonException(
					"Cannot read DipFormatId: " + params, e);
		}
	}

	/* (non-Javadoc)
	 * @see ait.ffma.service.preservation.riskmanagement.api.PreservationRiskmanagementService#querySolr(java.lang.String)
	 */
	public SolrInputDocument querySolr(String query)
			throws FfmaCommonException {
		try {
			return (SolrInputDocument) getFFDocIndexer().createInputDocumentByQuery(query);
		} catch (Exception e) {
			throw new FfmaCommonException(
					"Cannot read DipFormatId: " + query, e);
		}
	}

	/* (non-Javadoc)
	 * @see ait.ffma.service.preservation.riskmanagement.api.PreservationRiskmanagementService#retrieveLodFormatCollection()
	 */
	public List<FfmaDomainObject> retrieveLodFormatCollection() throws FfmaCommonException {
		LODFormat lodFormat = new LODFormat();
		String fieldName = lodFormat.getDomainObjectName();
		List<String> nameList = new ArrayList<String>();
		nameList.add(fieldName);
		return preservationRiskmanagementDao.retrieveObjectsListByField(
				(FfmaDomainObject) lodFormat,
				RiskConstants.DOMAIN_OBJECT_NAME,
				MongoDbConstants.IN_QUERY, nameList);
	}

	/* (non-Javadoc)
	 * @see ait.ffma.service.preservation.riskmanagement.api.PreservationRiskmanagementService#storeLODFormatsInSolr(java.util.List)
	 */
	public void storeLODFormatsInSolr(List<FfmaDomainObject> lodFormatList)
			throws FfmaCommonException {
		try {
			getLodFormatIndexer().storeLODFormatsInSolr(lodFormatList);
		} catch (Exception e) {
			throw new FfmaCommonException(
					"Cannot store DipFormatId: " + lodFormatList, e);
		}
	}

	/* (non-Javadoc)
	 * @see ait.ffma.service.preservation.riskmanagement.api.PreservationRiskmanagementService#retrieveDipFormatIdCollection()
	 */
	public List<FfmaDomainObject> retrieveDipFormatIdCollection() throws FfmaCommonException {
		DipFormatId dipFormatId = new DipFormatId();
		String fieldName = dipFormatId.getDomainObjectName();
		List<String> nameList = new ArrayList<String>();
		nameList.add(fieldName);
		return preservationRiskmanagementDao.retrieveObjectsListByField(
				(FfmaDomainObject) dipFormatId,
				RiskConstants.DOMAIN_OBJECT_NAME,
				MongoDbConstants.IN_QUERY, nameList);
	}

	/* (non-Javadoc)
	 * @see ait.ffma.service.preservation.riskmanagement.api.PreservationRiskmanagementService#retrieveDipSoftwareIdCollection()
	 */
	public List<FfmaDomainObject> retrieveDipSoftwareIdCollection() throws FfmaCommonException {
		DipSoftwareId dipSoftwareId = new DipSoftwareId();
		String fieldName = dipSoftwareId.getDomainObjectName();
		List<String> nameList = new ArrayList<String>();
		nameList.add(fieldName);
		return preservationRiskmanagementDao.retrieveObjectsListByField(
				(FfmaDomainObject) dipSoftwareId,
				RiskConstants.DOMAIN_OBJECT_NAME,
				MongoDbConstants.IN_QUERY, nameList);
	}

	/* (non-Javadoc)
	 * @see ait.ffma.service.preservation.riskmanagement.api.PreservationRiskmanagementService#storeDipFormatIdCollectionInSolr(ait.ffma.domain.preservation.riskmanagement.DipFormatId)
	 */
	public void storeDipFormatIdCollectionInSolr(DipFormatId dipFormatId)
			throws FfmaCommonException {
		try {
			String softwareIdStr = "";
			String formatNameStr = "";
			String mimeTypeStr = "";
			String availabilityStr = "";
			boolean isAIT = false;
			if (dipFormatId.getAitId() != null && dipFormatId.getAitId().length > 0 && !dipFormatId.getAitId()[0].equals(" ")) {
				isAIT = true;
			}
			if (dipFormatId.getExtension().equals("jbig") || dipFormatId.getExtension().equals("jls")) { // to delete
				int ii = 0;
			}
			if (dipFormatId.getDipSoftwareId() != null) {
				List<String> listOfSoftwareDipIds = Arrays.asList(dipFormatId.getDipSoftwareId());
				Iterator<String> iterSoftwareDipIds = listOfSoftwareDipIds.iterator();
				while (iterSoftwareDipIds.hasNext()) {
					String softwareDipId = iterSoftwareDipIds.next();
					DipSoftwareId dipSoftwareId = new DipSoftwareId();
					dipSoftwareId.setDipId(softwareDipId);
					List<String> exclusionsList = new ArrayList<String>();
					exclusionsList.add(RiskConstants.DOMAIN_OBJECT_NAME);
					List<? extends FfmaDomainObject> dipSoftwares = preservationRiskmanagementDao
						.retrieveCollection((FfmaDomainObject) dipSoftwareId, exclusionsList);
					if (dipSoftwares != null && dipSoftwares.size() > 0) {
						DipSoftwareId dipSoftware = (DipSoftwareId) dipSoftwares.get(0);
						if (softwareIdStr.equals("")) {
							softwareIdStr = dipSoftware.getName();
						} else {
							softwareIdStr = softwareIdStr + ", " + dipSoftware.getName();
						}
					}
				}
			}
			if (dipFormatId.getDBPediaId() != null) {
				List<String> listOfDBPediaIds = Arrays.asList(dipFormatId.getDBPediaId());
				Iterator<String> iterDBPediaIds = listOfDBPediaIds.iterator();
				while (iterDBPediaIds.hasNext()) {
					String dbpediaId = iterDBPediaIds.next();
					DBPediaFileFormat dbpediaFileFormat = new DBPediaFileFormat();
					dbpediaFileFormat.setPuid(dbpediaId);
					List<String> exclusionsList = new ArrayList<String>();
					exclusionsList.add(RiskConstants.DOMAIN_OBJECT_NAME);
					List<? extends FfmaDomainObject> dbpediaFFs = preservationRiskmanagementDao
						.retrieveCollection((FfmaDomainObject) dbpediaFileFormat, exclusionsList);
					if (dbpediaFFs != null && dbpediaFFs.size() > 0) {
						DBPediaFileFormat dbpediaFF = (DBPediaFileFormat) dbpediaFFs.get(0);
						if (dbpediaFF.getName() != null) {
							if (formatNameStr.equals("")) {
								formatNameStr = dbpediaFF.getName();
							} else {
								formatNameStr = formatNameStr + ", " + dbpediaFF.getName();
							}
						}
						if (!availabilityStr.contains("DBPedia")) {
							if (availabilityStr.equals("")) {
								availabilityStr = "DBPedia";
							} else {
								availabilityStr = availabilityStr + ", " + "DBPedia";
							}
						}
						if (dbpediaFF.getMimetype() != null) { 
							if (mimeTypeStr.equals("")) {
								mimeTypeStr = dbpediaFF.getMimetype();
							} else {
								mimeTypeStr = mimeTypeStr + ", " + dbpediaFF.getMimetype();
							}
						}
					}
				}
			}
			if (dipFormatId.getFreebaseId() != null) {
				List<String> listOfFreebaseIds = Arrays.asList(dipFormatId.getFreebaseId());
				Iterator<String> iterFreebaseIds = listOfFreebaseIds.iterator();
				while (iterFreebaseIds.hasNext()) {
					String freebaseId = iterFreebaseIds.next();
					FreebaseFileFormat freebaseFileFormat = new FreebaseFileFormat();
					freebaseFileFormat.setPuid(freebaseId);
					List<String> exclusionsList = new ArrayList<String>();
					exclusionsList.add(RiskConstants.DOMAIN_OBJECT_NAME);
					List<? extends FfmaDomainObject> freebaseFFs = preservationRiskmanagementDao
						.retrieveCollection((FfmaDomainObject) freebaseFileFormat, exclusionsList);
					if (freebaseFFs != null && freebaseFFs.size() > 0) {
						FreebaseFileFormat freebaseFF = (FreebaseFileFormat) freebaseFFs.get(0);
						if (formatNameStr.equals("")) {
							formatNameStr = freebaseFF.getName();
						} else {
							formatNameStr = formatNameStr + ", " + freebaseFF.getName();
						}
						if (!availabilityStr.contains("Freebase")) {
							if (availabilityStr.equals("")) {
								availabilityStr = "Freebase";
							} else {
								availabilityStr = availabilityStr + ", " + "Freebase";
							}
						}
						if (mimeTypeStr.equals("")) {
							mimeTypeStr = freebaseFF.getMimetype();
						} else {
							mimeTypeStr = mimeTypeStr + ", " + freebaseFF.getMimetype();
						}
					}
				}
			}
			if (dipFormatId.getPronomId() != null) {
				List<String> listOfPronomIds = Arrays.asList(dipFormatId.getPronomId());
				Iterator<String> iterPronomIds = listOfPronomIds.iterator();
				while (iterPronomIds.hasNext()) {
					String pronomId = iterPronomIds.next();
					PronomFileFormat pronomFileFormat = new PronomFileFormat();
					if (pronomId.contains("x-fmt")) {
						pronomFileFormat.setXpuid(pronomId);
					} else {
						pronomFileFormat.setPuid(pronomId);
					}
					List<String> exclusionsList = new ArrayList<String>();
					exclusionsList.add(RiskConstants.DOMAIN_OBJECT_NAME);
					List<? extends FfmaDomainObject> pronomFFs = preservationRiskmanagementDao
						.retrieveCollection((FfmaDomainObject) pronomFileFormat, exclusionsList);
					if (pronomFFs != null && pronomFFs.size() > 0) {
						PronomFileFormat pronomFF = (PronomFileFormat) pronomFFs.get(0);
						if (formatNameStr.equals("")) {
							formatNameStr = pronomFF.getName();
						} else {
							formatNameStr = formatNameStr + ", " + pronomFF.getName();
						}
						if (!availabilityStr.contains("Pronom")) {
							if (availabilityStr.equals("")) {
								availabilityStr = "Pronom";
							} else {
								availabilityStr = availabilityStr + ", " + "Pronom";
							}
						}
						if (mimeTypeStr.equals("")) {
							mimeTypeStr = pronomFF.getMimetype();
						} else {
							mimeTypeStr = mimeTypeStr + ", " + pronomFF.getMimetype();
						}
					}
				}
			}
			if (!isAIT) {
				getDipFormatIdIndexer().storeDipFormatIdCollectionInSolr(dipFormatId, softwareIdStr, formatNameStr, mimeTypeStr, availabilityStr);
			}
		} catch (Exception e) {
			throw new FfmaCommonException(
					"Cannot store DipFormatId: " + dipFormatId, e);
		}
	}

	/**
	 * This method stores format risk analysis report and prints out resulting object
	 * @param res
	 *        The format risk analysis report to store
	 * @throws FfmaCommonException
	 */
	private void storeFormatRiskAnalysisReport(FormatRiskAnalysisReport res)
			throws FfmaCommonException {
		//store format risk analysis report in db
		FormatRiskAnalysisReport storedCollectionAnalysisReport = preservationRiskmanagementDao
				.storeFormatRiskAnalysisReport(res);
		FormatRiskAnalysisReport retrievedCollectionAnalysisReport = 
			preservationRiskmanagementDao.getFormatRiskAnalysisReport(storedCollectionAnalysisReport.getExtension().toString());
		log.info(ReportConstants.RETRIEVED_COLLECTION_ANALYSIS_REPORT + retrievedCollectionAnalysisReport);
	}


	/**
	 * This method generates description count report. First we retrieve linked open data from 
	 * open repositories using SPARQL, MQL and HTML parsing. We store and organize retrieved data
	 * in Mongo database in FormatRiskAnalysisReport collection. In order to generate risk report 
	 * for particular rule we iterate through the stored data. 
	 * @param id The file format identifier
	 * @return format risk analysis report for given extension
	 */
	private FormatRiskAnalysisReport generateFormatRiskAnalysisReport(String id) {
		FormatRiskAnalysisReport res = new FormatRiskAnalysisReport();
		try {
			DipFormatId dipFormatId = new DipFormatId();
			dipFormatId.setDipId(LODConstants.DIP + id);
			List<String> exclusionsList = new ArrayList<String>();
			exclusionsList.add(RiskConstants.DOMAIN_OBJECT_NAME);
			List<? extends FfmaDomainObject> dipFormats = preservationRiskmanagementDao
				.retrieveCollection((FfmaDomainObject) dipFormatId, exclusionsList);

			if (dipFormats.size() > 0) {
				DipFormatId dipFormatObj = (DipFormatId) dipFormats.get(0);
//				if (id.equals("tif")) {
//					int kk = 0;
//				}
				if (dipFormatObj.getDescription() != null && dipFormatObj.getDescription()[0].length() > 0) {
					int descCount = 0;
					for (int i = 0; i < dipFormatObj.getDescription().length; i++) {
						String desc = (String) dipFormatObj.getDescription()[i];
						if (desc != null && desc.length() > 0 && !desc.equals(" ")) {
							descCount++;
						}
					}
					res.setDescriptionCount(descCount);
//					res.setDescriptionCount(dipFormatObj.getDescription().length);
				}
				if (dipFormatObj.getDipSoftwareId() != null && dipFormatObj.getDipSoftwareId()[0].length() > 0) {
					res.setSoftwareCount(dipFormatObj.getDipSoftwareId().length);
				}
				if (dipFormatObj.getDipVendorId() != null && dipFormatObj.getDipVendorId()[0].length() > 0) {
					res.setVendorsCount(dipFormatObj.getDipVendorId().length);
					// future work
					res.setIsSupportedByVendor(true);
				}
			}
			
			PronomFileFormat pronomFileFormat = new PronomFileFormat();
			pronomFileFormat.setExtension(id);
			List<String> exclusionsList2 = new ArrayList<String>();
			exclusionsList2.add(RiskConstants.DOMAIN_OBJECT_NAME);
			List<? extends FfmaDomainObject> pronomFormats = preservationRiskmanagementDao
				.retrieveCollection((FfmaDomainObject) pronomFileFormat, exclusionsList2);
			if (pronomFormats.size() > 0) {
				res.setVersions(pronomFormats.size());
			}
			
			LODFormat lodFormat = new LODFormat();
			lodFormat.setFileExtensions(id);
			List<String> exclusionsList3 = new ArrayList<String>();
			exclusionsList3.add(RiskConstants.DOMAIN_OBJECT_NAME);
			List<? extends FfmaDomainObject> lodFormats = preservationRiskmanagementDao
				.retrieveCollection((FfmaDomainObject) lodFormat, exclusionsList3);
			if (lodFormats.size() > 0) {
				LODFormat c_lodFormat = (LODFormat) lodFormats.get(0);
				boolean hasCreationDate = false;
				if (c_lodFormat.getCurrentVersionReleaseDate() != null && c_lodFormat.getCurrentVersionReleaseDate().length() > 0) {
					hasCreationDate = true;
				}
				res.setHasCreationDate(hasCreationDate);
				boolean hasCreatorInformation = false;
				if (c_lodFormat.getFormatCreator() != null && c_lodFormat.getFormatCreator().length() > 0) {
					hasCreatorInformation = true;
				}
				res.setHasCreatorInformation(hasCreatorInformation);
				// currently publisher and creator is the same - future work
				res.setHasPublisherInformation(hasCreatorInformation);
				boolean hasDigitalRightsInformation = false;
				if (c_lodFormat.getFormatLicense() != null && c_lodFormat.getFormatLicense().length() > 0) {
					hasDigitalRightsInformation = true;
				}
				// currently default is false - future work possibly from LODSoftware
				res.setHasDigitalRightsInformation(hasDigitalRightsInformation);
				// currently default is false - future work
				res.setIsCompressedFileFormat(false);
				boolean isFrequentlyUsedVersion = false;
				if (c_lodFormat.getCurrentFormatVersion() != null && c_lodFormat.getCurrentFormatVersion().length() > 0) {
//					String version = c_lodFormat.getCurrentFormatVersion();
//					if version.contains(") // future work
					isFrequentlyUsedVersion = true;
				}
				res.setIsFrequentlyUsedVersion(isFrequentlyUsedVersion);
				boolean hasHomepage = false;
				if (c_lodFormat.getFormatHomepage() != null && c_lodFormat.getFormatHomepage().length() > 0) {
					hasHomepage = true;
				}
				res.setHasHomepage(hasHomepage);
				boolean hasGenre = false;
				if (c_lodFormat.getFormatGenre() != null && c_lodFormat.getFormatGenre().length() > 0) {
					hasGenre = true;
				}
				res.setHasGenre(hasGenre);
				boolean hasMimeType = false;
				if (c_lodFormat.getMimeType() != null && c_lodFormat.getMimeType().length() > 0) {
					hasMimeType = true;
				}
				res.setMimeType(hasMimeType);
			}

			Iterator<? extends FfmaDomainObject> iterLodFormats = lodFormats.iterator();
			while (iterLodFormats.hasNext()) {
				LODFormat cur_lodFormat = (LODFormat) iterLodFormats.next();
				if (res.getMimeType() == false) {
					if (cur_lodFormat.getMimeType() != null && cur_lodFormat.getMimeType().length() > 0) {
						res.setMimeType(true);
					}
				}
				if (res.getHasGenre() == false) {
					if (cur_lodFormat.getFormatGenre() != null && cur_lodFormat.getFormatGenre().length() > 0) {
						res.setHasGenre(true);
					}
				}
				if (res.getHasHomepage() == false) {
					if (cur_lodFormat.getFormatHomepage() != null && cur_lodFormat.getFormatHomepage().length() > 0) {
						res.setHasHomepage(true);
					}
				}
				if (res.getHasCreatorInformation() == false) {
					if (cur_lodFormat.getFormatCreator() != null && cur_lodFormat.getFormatCreator().length() > 0) {
						res.setHasCreatorInformation(true);
					}
				}
				if (res.getHasCreationDate() == false) {
					if (cur_lodFormat.getCurrentVersionReleaseDate() != null && cur_lodFormat.getCurrentVersionReleaseDate().length() > 0) {
						res.setHasCreationDate(true);
					}
				}
				if (res.getHasDigitalRightsInformation() == false) {
					if (cur_lodFormat.getFormatLicense() != null && cur_lodFormat.getFormatLicense().length() > 0) {
						res.setHasDigitalRightsInformation(true);
					}
				}
			}

			// currently default is true - future work
			res.setIsFileMigrationSupported(true);
			
			// currently default is false - future work 
			res.setHasObjectPreview(false);
			
			// currently default is true - future work 
			res.setIsSupportedByWebBrowsers(true);
						
//			public static final String WELL_DOCUMENTED_SCORE_PROPERTY_ID = "WELL_DOCUMENTED_SCORE_PROPERTY_ID";
//			public static final String LAST_UPDATE_PROPERTY_ID = "LAST_UPDATE_PROPERTY_ID"; // w 1.0
			
			boolean existencePeriod = false;
			ArrayList<String> existencePeriodList = new ArrayList<String>(Arrays.asList(
					"bmp", "mp3", "xml", "gif", "png", "tif", "tiff", "jpg", "jpeg", "dxf", "jp2", "html", "mac", "css", "pdf", "ppt", "doc", "sxw", "psd", "pcd", "wma", "wmv"));
			for (String curVal : existencePeriodList){
				  if (curVal.contains(id)){
					  existencePeriod = true;
				      break;
				  }
			}
			res.setExistencePeriod(existencePeriod);
			boolean complexity = false;
			ArrayList<String> complexityList = new ArrayList<String>(Arrays.asList(
					"xml", "tif", "jp2", "pdf", "html", "sxw", "dxf"));
			for (String curVal : complexityList){
				  if (curVal.contains(id)){
					  complexity = true;
				      break;
				  }
			}
			res.setComplexity(complexity);
			boolean dissemination = false;
			ArrayList<String> disseminationList = new ArrayList<String>(Arrays.asList(
					"mp3", "xml", "jpg", "tif", "jp2", "xbm", "ppt", "doc", "gif", "png", "jpeg", "pdf", "bmp", "html"));
			for (String curVal : disseminationList){
				  if (curVal.contains(id)){
					  dissemination = true;
				      break;
				  }
			}
			res.setDissemination(dissemination);
			boolean outdated = false;
			ArrayList<String> outdatedList = new ArrayList<String>(Arrays.asList(
					"dxf", "html", "css", "ppt", "doc", "sxw", "psd", "pcd", "wma", "wmv", "ra", "rm", "ram", "mac"));
			for (String curVal : outdatedList){
				  if (curVal.contains(id)){
					  outdated = true;
				      break;
				  }
			}
		    res.setOutdated(outdated);
			boolean standardisation = false;
			ArrayList<String> standardisedList = new ArrayList<String>(Arrays.asList(
					"mp3", "xml", "jp2", "gif", "png", "jpg", "jpeg", "pdf", "html"));
			for (String curVal : standardisedList){
				  if (curVal.contains(id)){
					  standardisation = true;
				      break;
				  }
			}
			res.setStandardisation(standardisation);

			boolean isPopular = false;
			ArrayList<String> popularList = new ArrayList<String>(Arrays.asList(
					"mp3", "xml", "gif", "jpg", "jpeg", "jp2", "html", "css", "pdf", "ppt", "doc", "png", "bmp", "tif"));
			for (String curVal : popularList){
				  if (curVal.contains(id)){
					  isPopular = true;
				      break;
				  }
			}
			res.setIsFrequentlyUsedVersion(isPopular);

			boolean hasCompression = false;
			ArrayList<String> compressionList = new ArrayList<String>(Arrays.asList(
					"tif", "tiff", "jp2"));
			for (String curVal : compressionList){
				  if (curVal.contains(id)){
					  hasCompression = true;
				      break;
				  }
			}
			res.setIsCompressedFileFormat(hasCompression);
			
			if (res.getMimeType() == false) {
				boolean hasMimeType = false;
				ArrayList<String> mimeTypeList = new ArrayList<String>(Arrays.asList(
						"ppt", "bmp"));
				for (String curVal : mimeTypeList){
					  if (curVal.contains(id)){
						  hasMimeType = true;
					      break;
					  }
				}
				res.setMimeType(hasMimeType);
			}
			
			res.setExtension(id);
			storeFormatRiskAnalysisReport(res);
		} catch (FfmaCommonException e) {
			log.info(ReportConstants.MEDIA_MAPPING_RETRIEVE_ERROR + e);
		} catch (Exception ex) {
			log.info(ReportConstants.FORMAT_RISK_ANALYSIS_STORAGE_ERROR + ex);
		}
		return res;
	}

	/* (non-Javadoc)
	 * @see ait.ffma.service.preservation.riskmanagement.api.PreservationRiskmanagementService#computeRiskReportHtml(java.lang.String, java.lang.String, java.lang.String, ait.ffma.service.preservation.riskmanagement.api.riskanalysis.risk.RiskConstants.RiskReportTypesEnum)
	 */
	public String computeRiskReportHtml(
			String id, String config, String classification,
			RiskReportTypesEnum reportType) {
		generateFormatRiskAnalysisReport(id);
		return getRiskReportGenerator().generateHtmlReport(id,
				config, classification, reportType);		
	}
	
}
