package ait.ffma.service.preservation.riskmanagement.api.lod;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import ait.ffma.domain.FfmaDomainObject;
import ait.ffma.domain.preservation.riskmanagement.DipFormatId;
import ait.ffma.domain.preservation.riskmanagement.DipSoftwareId;
import ait.ffma.domain.preservation.riskmanagement.DipVendorId;
import ait.ffma.domain.preservation.riskmanagement.LODFormat;
import ait.ffma.domain.preservation.riskmanagement.LODSoftware;
import ait.ffma.domain.preservation.riskmanagement.LODVendor;
import ait.ffma.service.preservation.riskmanagement.PreservationRiskmanagementConfiguration;
import ait.ffma.service.preservation.riskmanagement.api.lod.LODConstants.PreservationStatisticsTypes;
import ait.ffma.service.preservation.riskmanagement.api.riskanalysis.risk.ReportConstants;
import ait.ffma.service.preservation.riskmanagement.api.riskanalysis.risk.RiskConstants;
import ait.ffma.service.preservation.riskmanagement.dao.PreservationRiskmanagementDao;

/**
 * This class provides generation of different kinds of reports for data extracted from LOD repositories.
 */
public class LODReportGenerator {

	/**
	 * The data access object for preservation risk management
	 */
	@Autowired
	private PreservationRiskmanagementDao preservationRiskmanagementDao;

	/**
	 * @return
	 */
	public PreservationRiskmanagementDao getPreservationRiskmanagementDao() {
		return preservationRiskmanagementDao;
	}

	/**
	 * @param preservationRiskmanagementDao
	 */
	public void setPreservationRiskmanagementDao(PreservationRiskmanagementDao preservationRiskmanagementDao) {
		this.preservationRiskmanagementDao = preservationRiskmanagementDao;
	}
	
	@Autowired
	private PreservationRiskmanagementConfiguration configuration;
	
	/**
	 * @param configuration
	 */
	public void setConfiguration(PreservationRiskmanagementConfiguration configuration) {
		this.configuration = configuration;
	}
	
	/**
	 * @return
	 */
	public PreservationRiskmanagementConfiguration getConfiguration() {
		if (configuration == null) {
			configuration = new PreservationRiskmanagementConfiguration();
		}
		return configuration;
	}
	
	/**
	 * A logger object.
	 */
	private Logger log = Logger.getLogger(getClass());

	/**
	 * @param report
	 * @param ext
	 * @return
	 */
	public String createHtmlForLODSoftware(String report, String ext) {
		StringBuffer buf = new StringBuffer();
		List<String> softwareObjList = printLodSoftwareHeader(report, ext, buf);		
		Iterator<String> swObjIter = softwareObjList.iterator();
		while (swObjIter.hasNext()) {
			String swObjStr = swObjIter.next();
			try {
				LODSoftware lodSoftware = new LODSoftware();
				JSONObject json = new JSONObject(swObjStr);
				lodSoftware.initDomainObject(json,
						LODSoftware.FieldsEnum.values());
				buf.append(ReportConstants.TR);
				buf.append(LODUtils.insertValue(lodSoftware.getSoftwareName()));
				buf.append(LODUtils.insertValue(lodSoftware.getRepository()));
				buf.append(LODUtils.insertValue(lodSoftware.getRepositoryId()));
				buf.append(LODUtils.insertValue(lodSoftware.getSoftwareLatestVersion()));
				buf.append(LODUtils.insertValue(lodSoftware.getSoftwareReleaseDate()));
				buf.append(LODUtils.insertValue(lodSoftware.getSoftwareHomepage()));
				buf.append(LODUtils.insertValue(lodSoftware.getDescription()));
				buf.append(ReportConstants.TREND);
			} catch (Exception e) {
				log.info(ReportConstants.CANNOT_PARSE_STRING_TO_JSON_OBJECT
								+ swObjStr, e);
			}
		}
		buf.append(ReportConstants.TABLEEND);
		buf.append(ReportConstants.BODYEND);
		buf.append(ReportConstants.HTMLEND);
		return buf.toString();
	}

	/**
	 * @param report
	 * @param ext
	 * @param buf
	 * @return
	 */
	private List<String> printLodSoftwareHeader(String report, String ext,
			StringBuffer buf) {
		buf.append(LODConstants.HTML_HEADER);
		buf.append("<title>Software for extension (");
		buf.append(ext);
		buf.append(") report</title></head><body>");	
		List<String> softwareObjList = Arrays.asList(report.split(LODConstants.OBJ_SEPARATOR));
		buf.append("<h2>For file format extension '");
		buf.append(ext);
		buf.append(ReportConstants.FOUND);
		buf.append(softwareObjList.size());
		buf.append("' software</h2>");
		buf.append(ReportConstants.TABLE_BORDER_1);
		buf.append(ReportConstants.TR);
		buf.append("<th>SoftwareName</th>");
		buf.append("<th>Repository</th>");
		buf.append("<th>RepositoryId</th>");
		buf.append("<th>SoftwareLatestVersion</th>");
		buf.append("<th>SoftwareReleaseDate</th>");
		buf.append("<th>SoftwareHomepage</th>");
		buf.append("<th>Description</th>");
		buf.append(ReportConstants.TREND);
		return softwareObjList;
	}
	
	/**
	 * @param report
	 * @param ext
	 * @return
	 */
	public String createHtmlForLODVendor(String report, String ext) {
		StringBuffer buf = new StringBuffer();
		List<String> vendorsObjList = printLodVendorHeader(report, ext, buf);		
		Iterator<String> vendorsObjIter = vendorsObjList.iterator();
		while (vendorsObjIter.hasNext()) {
			String vendorsObjStr = vendorsObjIter.next();
			try {
				LODVendor lodVendor = new LODVendor();
				JSONObject json = new JSONObject(vendorsObjStr);
				lodVendor.initDomainObject(json,
						LODVendor.FieldsEnum.values());
				buf.append(ReportConstants.TR);
				buf.append(LODUtils.insertValue(lodVendor.getOrganisationName()));
				buf.append(LODUtils.insertValue(lodVendor.getRepository()));
				buf.append(LODUtils.insertValue(lodVendor.getRepositoryId()));
				buf.append(LODUtils.insertValue(lodVendor.getBusinessStatus()));
				buf.append(LODUtils.insertValue(lodVendor.getCurrentFfma()));
				buf.append(LODUtils.insertValue(lodVendor.getStockIssues()));
				buf.append(LODUtils.insertValue(lodVendor.getRankedList()));
				buf.append(LODUtils.insertValue(lodVendor.getFoundationDate()));
				buf.append(LODUtils.insertValue(lodVendor.getCountry()));
				buf.append(LODUtils.insertValue(lodVendor.getHomepage()));
				buf.append(LODUtils.insertValue(lodVendor.getDescription()));
				buf.append(ReportConstants.TREND);
			} catch (Exception e) {
				log.info(ReportConstants.CANNOT_PARSE_STRING_TO_JSON_OBJECT
								+ vendorsObjStr, e);
			}
		}
		buf.append(ReportConstants.TABLEEND);
		buf.append(ReportConstants.BODYEND);
		buf.append(ReportConstants.HTMLEND);
		return buf.toString();
	}

	/**
	 * @param report
	 * @param ext
	 * @param buf
	 * @return
	 */
	private List<String> printLodVendorHeader(String report, String ext,
			StringBuffer buf) {
		buf.append(LODConstants.HTML_HEADER);
		buf.append("<title>Vendors for extension (");
		buf.append(ext);
		buf.append(") report</title></head><body>");
		List<String> vendorsObjList = Arrays.asList(report.split(LODConstants.OBJ_SEPARATOR));
		buf.append("<h2>For file format extension '");
		buf.append(ext);
		buf.append(ReportConstants.FOUND);
		buf.append(vendorsObjList.size());
		buf.append("' vendors</h2>");
		buf.append(ReportConstants.TABLE_BORDER_1);
		buf.append(ReportConstants.TR);
		buf.append("<th>OrganisationName</th>");
		buf.append("<th>Repository</th>");
		buf.append("<th>RepositoryId</th>");
		buf.append("<th>BusinessStatus</th>");
		buf.append("<th>CurrentFfma</th>");
		buf.append("<th>StockIssues</th>");
		buf.append("<th>RankedList</th>");
		buf.append("<th>FoundationDate</th>");
		buf.append("<th>Country</th>");
		buf.append("<th>Homepage</th>");
		buf.append("<th>Description</th>");
		buf.append(ReportConstants.TREND);
		return vendorsObjList;
	}

	/**
	 * @param ext
	 * @param preservationRiskmanagementDao
	 * @return
	 */
	public String retrieveDipFormatId(String ext) {
		StringBuffer buf = new StringBuffer();
		buf.append(LODConstants.HTML_HEADER);
		buf.append("<title>DipFormatId for extension (");
		buf.append(ext);
		buf.append(") report</title></head><body>");
		DipFormatId dipFormatId = new DipFormatId();
		dipFormatId.setDipId(LODConstants.DIP + ext);
		List<String> exclusionsList = new ArrayList<String>();
		exclusionsList.add(RiskConstants.DOMAIN_OBJECT_NAME);
		List<? extends FfmaDomainObject> dipFormats = preservationRiskmanagementDao
			.retrieveCollection((FfmaDomainObject) dipFormatId, exclusionsList);
		if (dipFormats != null && dipFormats.size() > 0) {
			DipFormatId dipFormatIdObj = (DipFormatId) dipFormats.get(0);
			buf.append(generateHtmlReportBegin());				
			printDipFormatIdRow(ext, buf, dipFormatIdObj);
			buf.append(generateHtmlReportEnd());				

		}
//		buf.append(ReportConstants.TABLEEND);
//		buf.append(ReportConstants.BODYEND);
//		buf.append(ReportConstants.HTMLEND);
		return buf.toString();
	}

	/**
	 * @param ext
	 * @param preservationRiskmanagementDao
	 * @return
	 */
	public DipFormatId retrieveDipFormatIdObj(String ext) {
		DipFormatId dipFormatId = new DipFormatId();
		dipFormatId.setDipId(LODConstants.DIP + ext);
		List<String> exclusionsList = new ArrayList<String>();
		exclusionsList.add(RiskConstants.DOMAIN_OBJECT_NAME);
		List<? extends FfmaDomainObject> dipFormats = preservationRiskmanagementDao
			.retrieveCollection((FfmaDomainObject) dipFormatId, exclusionsList);
		if (dipFormats != null && dipFormats.size() > 0) {
			dipFormatId = (DipFormatId) dipFormats.get(0);
		}
		return dipFormatId;
	}

	/**
	 * This method generates header for HTML risk report
	 * @return HTML report header
	 */
	public String generateHtmlReportBegin() {
		
		StringBuffer buf = new StringBuffer();
		String path = getConfiguration().getServiceUrl().replace("rest", "");
//		if (!path.contains("8983")) {
//			path = path.replace("/ffma", ":8983/ffma");
//		}
		buf.append(LODConstants.HTML_HEADER);
		buf.append("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" />" + 
  	               "<title>Format Risk Analysis Report</title>" +
  	               "<link href=\"" + path + "ffmastyle.css\" rel=\"stylesheet\" type=\"text/css\" />");
		buf.append("<div id=\"wrapper\">" +  
	"<div id=\"container\">" + 
		"<div id=\"mainblock\">" + 
					"<div id=\"content\">" + 
						"<div class=\"box\">" + 
							"<h2>Aggregated File Format Description</h2>");
	    return buf.toString();
	}

	/**
	 * This method generates end for HTML risk report
	 * @return HTML report end
	 */
	public String generateHtmlReportEnd() {
		StringBuffer buf = new StringBuffer();
		buf.append("</div></div></div><hr/></div>");
	    return buf.toString();
	}

	/**
	 * @param ext
	 * @param buf
	 * @param dipFormatIdObj
	 */
	private void printDipFormatIdRow(String ext, StringBuffer buf,
			DipFormatId dipFormatIdObj) {
		buf.append("<div class=\"box mezzi\">" + 
			 	"<h3>DipFormat report for file format extension '" + ext + "'</h3>" +
			    "</div>" +
				"<table width=\"100%\">");
//		buf.append("<h2><a name=\"vendors_report\">DipFormatId Object Report for file format extension '");
//		buf.append(ext);
//		buf.append("'</a></h2>");
//		buf.append(ReportConstants.TABLE_BORDER_1);
		buf.append(ReportConstants.TR);
		buf.append("<th>DipId</th>");
		buf.append("<th>Extension</th>");
		buf.append("<th>PronomId</th>");
		buf.append("<th>FreebaseId</th>");
		buf.append("<th>DBPediaId</th>");
		buf.append("<th>AitId</th>");
//		buf.append("<th>Software</th>");
//		buf.append("<th>Vendors</th>");
//		buf.append("<th>Description</th>");
		buf.append(ReportConstants.TREND);
		
		buf.append(ReportConstants.TR);
		buf.append(LODUtils.insertValue(dipFormatIdObj.getDipId()));
	    buf.append(LODUtils.insertValue(dipFormatIdObj.getExtension()));
	    buf.append(LODUtils.insertValueArray(dipFormatIdObj.getPronomId()));
	    buf.append(LODUtils.insertValueArray(dipFormatIdObj.getFreebaseId()));
	    buf.append(LODUtils.insertValueArray(dipFormatIdObj.getDBPediaId()));
	    buf.append(LODUtils.insertValueArray(dipFormatIdObj.getAitId()));
//	    buf.append(LODUtils.insertValueArray(dipFormatIdObj.getDipSoftwareId()));
//	    buf.append(LODUtils.insertValueArray(dipFormatIdObj.getDipVendorId()));
//	    buf.append(LODUtils.insertValueArray(dipFormatIdObj.getDescription()));
	    buf.append(ReportConstants.TREND);
		buf.append(ReportConstants.TABLEEND);
	    
		buf.append("<div class=\"box mezzi\">" + 
			 	"<h3>Software</h3>" +
			    "</div>" +
				"<table width=\"100%\">");
		buf.append(ReportConstants.TR);
//		buf.append("<th>Software</th>");
	    buf.append(LODUtils.insertValueArrayExt(dipFormatIdObj.getDipSoftwareId()));
		buf.append(ReportConstants.TREND);
		buf.append(ReportConstants.TABLEEND);

		buf.append("<div class=\"box mezzi\">" + 
			 	"<h3>Vendors</h3>" +
			    "</div>" +
				"<table width=\"100%\">");
		buf.append(ReportConstants.TR);
//		buf.append("<th>Vendors</th>");
	    buf.append(LODUtils.insertValueArrayExt(dipFormatIdObj.getDipVendorId()));
		buf.append(ReportConstants.TREND);
		buf.append(ReportConstants.TABLEEND);

		buf.append("<div class=\"box mezzi\">" + 
			 	"<h3>Description</h3>" +
			    "</div>" +
				"<table width=\"100%\">");
		buf.append(ReportConstants.TR);
//		buf.append("<th>Description</th>");
	    buf.append(LODUtils.insertValueArray(dipFormatIdObj.getDescription()));
		buf.append(ReportConstants.TREND);
		buf.append(ReportConstants.TABLEEND);
	}
	
	/**
	 * @param type
	 * @param ext
	 * @param preservationRiskmanagementDao
	 * @return
	 */
	public String retrievePreservationStatistic(String type, String ext) {
		StringBuffer buf = new StringBuffer();
		buf.append(ReportConstants.EMPTYSTRING);
		if (type.equals(PreservationStatisticsTypes.All.name())) {
			List<String> listOfSoftwareRepositoryIds = new ArrayList<String>();
			List<String> exclusionsList = printLodFormats(ext, buf, listOfSoftwareRepositoryIds);
			printLodSoftware(buf, listOfSoftwareRepositoryIds, exclusionsList);
			printLodVendors(buf, exclusionsList);
		}
		getReferencesToLodRepositories(type, ext, buf);
		getTextualFormatDescriptions(type, ext, buf);
		printRetrievedSoftwareAndVendors(type, ext, buf);
		return buf.toString();
	}

	/**
	 * @param ext
	 * @param buf
	 * @param listOfSoftwareRepositoryIds
	 * @return
	 */
	private List<String> printLodFormats(String ext, StringBuffer buf,
			List<String> listOfSoftwareRepositoryIds) {
		buf.append(LODConstants.CSV_FILE_FORMAT_TITLE);
		buf.append(LODConstants.CSV_FILE_FORMAT_HEADER);
		LODFormat lodFormatObj = new LODFormat();
		List<String> exclusionsList = new ArrayList<String>();
		exclusionsList.add(RiskConstants.DOMAIN_OBJECT_NAME);
		List<? extends FfmaDomainObject> lodFormatList = preservationRiskmanagementDao
			.retrieveCollection((FfmaDomainObject) lodFormatObj, exclusionsList);
		Iterator<? extends FfmaDomainObject> iter = lodFormatList.iterator();
		while (iter.hasNext()) {
			LODFormat lodFormat = (LODFormat) iter.next();
			checkAndEvaluateLodFormat(ext, buf, listOfSoftwareRepositoryIds, lodFormat);
		}
		return exclusionsList;
	}

	/**
	 * @param ext
	 * @param buf
	 * @param listOfSoftwareRepositoryIds
	 * @param lodFormat
	 */
	private void checkAndEvaluateLodFormat(String ext, StringBuffer buf,
			List<String> listOfSoftwareRepositoryIds, LODFormat lodFormat) {
		if (checkLodFormatName(ext, lodFormat) || checkLodFormatFileExtension(ext, lodFormat)) {
			evaluateAndPrintLodFormat(buf, listOfSoftwareRepositoryIds, lodFormat);
		}
	}

	/**
	 * @param ext
	 * @param lodFormat
	 * @return
	 */
	private boolean checkLodFormatName(String ext, LODFormat lodFormat) {
		boolean res = false;
		if (lodFormat.getFormatName() != null
				&& lodFormat.getFormatName().length() > 0
				&& checkStringContainsExtension(ext, lodFormat.getFormatName())) {
			res = true;
		}
		return res;
	}

	/**
	 * @param ext
	 * @param lodFormat
	 * @return
	 */
	private boolean checkLodFormatFileExtension(String ext, LODFormat lodFormat) {
		boolean res = false;
		if (lodFormat.getFileExtensions() != null
				&& lodFormat.getFileExtensions().length() > 0
				&& checkStringContainsExtension(ext, lodFormat.getFileExtensions())) {
			res = true;
		}
		return res;
	}

	/**
	 * @param buf
	 * @param listOfSoftwareRepositoryIds
	 * @param lodFormat
	 */
	private void evaluateAndPrintLodFormat(StringBuffer buf,
			List<String> listOfSoftwareRepositoryIds, LODFormat lodFormat) {
		setLodFormatRepositoryIds(listOfSoftwareRepositoryIds, lodFormat);
		String name = setLodFormatName(lodFormat);
		String versionReleaseDate = setLodFormatReleaseDate(lodFormat);
		String softwareCount = setLodFormatSoftwareCount(lodFormat);
		String software = setLodFormatSoftware(lodFormat);
		String currentFormatVersion = setLodFormatVersion(lodFormat);
		String license = setLodFormatLicense(lodFormat);
		String limitations = setLodFormatLimitations(lodFormat);
		String puid = setLodFormatPuid(lodFormat);
		String homepage = setLodFormatHomepage(lodFormat);
		String mimetype = setLodFormatMimetype(lodFormat);
		String genre = setLodFormatGenre(lodFormat);
		String creator = setLodFormatCreator(lodFormat);
		String open = setLodFormatOpen(lodFormat);
		String extension = setLodFormatExtension(lodFormat);
		String vendors = setLodFormatVendors(lodFormat);
		String standards = setLodFormatStandards(lodFormat);
		printSoftwareRow(buf, name, versionReleaseDate,
				softwareCount, software, currentFormatVersion,
				license, limitations, puid, homepage, mimetype,
				genre, creator, open, extension, vendors, standards);
	}

	/**
	 * @param buf
	 * @param exclusionsList
	 */
	private void printLodVendors(StringBuffer buf, List<String> exclusionsList) {
		buf.append(ReportConstants.NEWLINE);
		buf.append(LODConstants.CSV_VENDOR_TITLE);
		buf.append(LODConstants.CSV_VENDOR_HEADER);
		LODVendor lodVendorObj = new LODVendor();
		List<? extends FfmaDomainObject> lodVendorList = preservationRiskmanagementDao
			.retrieveCollection((FfmaDomainObject) lodVendorObj, exclusionsList);
		Iterator<? extends FfmaDomainObject> iterVendor = lodVendorList.iterator();
		while (iterVendor.hasNext()) {
			evaluateAndPrintLodVendor(buf, iterVendor);
		}
	}

	/**
	 * @param type
	 * @param ext
	 * @param buf
	 */
	private void getReferencesToLodRepositories(String type, String ext,
			StringBuffer buf) {
		if (type.equals(PreservationStatisticsTypes.ReferencesToLodRepositories.name())) {
			List<String> listOfSoftwareRepositoryIds = new ArrayList<String>();
			List<String> listOfVendorRepositoryIds = new ArrayList<String>();
			DipFormatId dipFormatId = LODUtils.setDipFormatId(ext);
			List<String> exclusionsList = new ArrayList<String>();
			exclusionsList.add(RiskConstants.DOMAIN_OBJECT_NAME);
			retrieveDipFormatIds(buf, listOfSoftwareRepositoryIds,
					listOfVendorRepositoryIds, dipFormatId, exclusionsList);
			buf.append(ReportConstants.NEWLINE);
			appendSoftware(buf, listOfSoftwareRepositoryIds, exclusionsList);
			buf.append(ReportConstants.NEWLINE);
			appendVendors(buf, listOfVendorRepositoryIds, exclusionsList);
		}
	}

	/**
	 * @param buf
	 * @param iterVendor
	 */
	private void evaluateAndPrintLodVendor(StringBuffer buf,
			Iterator<? extends FfmaDomainObject> iterVendor) {
		LODVendor lodVendor = (LODVendor) iterVendor.next();
		String name = setLodVendorName(lodVendor);
		String numberOfEmployees = setLodVendorNumOfEmployees(lodVendor);
		String businessStatus = setLodVendorBusinessStatus(lodVendor);
		String currentFfma = setLodVendorCurrentFfma(lodVendor);
		String stockIssues = setLodVendorStockIssues(lodVendor);
		String rankedList = setLodVendorRankedList(lodVendor);
		String country = setLodVendorCountry(lodVendor);
		String foundationDate = setLodVendorFoundationDate(lodVendor);
		String homepage = setLodVendorHomepage(lodVendor);
		printVendorRow(buf, name, numberOfEmployees, businessStatus,
				currentFfma, stockIssues, rankedList, country,
				foundationDate, homepage);
	}

	/**
	 * @param lodVendor
	 * @return
	 */
	private String setLodVendorHomepage(LODVendor lodVendor) {
		String homepage = ReportConstants.EMPTYSTRING;
		if (lodVendor.getHomepage() != null) {
			homepage = lodVendor.getHomepage();
		}
		return homepage;
	}

	/**
	 * @param lodVendor
	 * @return
	 */
	private String setLodVendorFoundationDate(LODVendor lodVendor) {
		String foundationDate = ReportConstants.EMPTYSTRING;
		if (lodVendor.getFoundationDate() != null) {
			foundationDate = lodVendor.getFoundationDate();
		}
		return foundationDate;
	}

	/**
	 * @param lodVendor
	 * @return
	 */
	private String setLodVendorCountry(LODVendor lodVendor) {
		String country = ReportConstants.EMPTYSTRING;
		if (lodVendor.getCountry() != null) {
			country = lodVendor.getCountry();
		}
		return country;
	}

	/**
	 * @param lodVendor
	 * @return
	 */
	private String setLodVendorRankedList(LODVendor lodVendor) {
		String rankedList = ReportConstants.EMPTYSTRING;
		if (lodVendor.getRankedList() != null) {
			rankedList = lodVendor.getRankedList();
		}
		return rankedList;
	}

	/**
	 * @param lodVendor
	 * @return
	 */
	private String setLodVendorStockIssues(LODVendor lodVendor) {
		String stockIssues = ReportConstants.EMPTYSTRING;
		if (lodVendor.getStockIssues() != null) {
			stockIssues = lodVendor.getStockIssues();
		}
		return stockIssues;
	}

	/**
	 * @param lodVendor
	 * @return
	 */
	private String setLodVendorCurrentFfma(LODVendor lodVendor) {
		String currentFfma = ReportConstants.EMPTYSTRING;
		if (lodVendor.getCurrentFfma() != null) {
			currentFfma = lodVendor.getCurrentFfma();
		}
		return currentFfma;
	}

	/**
	 * @param lodVendor
	 * @return
	 */
	private String setLodVendorBusinessStatus(LODVendor lodVendor) {
		String businessStatus = ReportConstants.EMPTYSTRING;
		if (lodVendor.getBusinessStatus() != null) {
			businessStatus = lodVendor.getBusinessStatus();
		}
		return businessStatus;
	}

	/**
	 * @param lodVendor
	 * @return
	 */
	private String setLodVendorNumOfEmployees(LODVendor lodVendor) {
		String numberOfEmployees = ReportConstants.EMPTYSTRING;
		if (lodVendor.getNumberOfEmployees() != null) {
			numberOfEmployees = lodVendor.getNumberOfEmployees().toString();
		}
		return numberOfEmployees;
	}

	/**
	 * @param lodVendor
	 * @return
	 */
	private String setLodVendorName(LODVendor lodVendor) {
		String name = ReportConstants.EMPTYSTRING;
		if (lodVendor.getOrganisationName() != null) {
			name = lodVendor.getOrganisationName();
		}
		return name;
	}

	/**
	 * @param buf
	 * @param listOfSoftwareRepositoryIds
	 * @param listOfVendorRepositoryIds
	 * @param dipFormatId
	 * @param exclusionsList
	 */
	private void retrieveDipFormatIds(StringBuffer buf,
			List<String> listOfSoftwareRepositoryIds,
			List<String> listOfVendorRepositoryIds, DipFormatId dipFormatId,
			List<String> exclusionsList) {
		List<? extends FfmaDomainObject> dipFormats = preservationRiskmanagementDao
			.retrieveCollection((FfmaDomainObject) dipFormatId, exclusionsList);
		if (dipFormats != null && dipFormats.size() > 0) {
			Iterator<? extends FfmaDomainObject> iterFormats = dipFormats.iterator();
			while (iterFormats.hasNext()) {
				DipFormatId format = (DipFormatId) iterFormats.next();
				buf.append(format);
				buf.append(ReportConstants.SEMICOLONNEWLINE);
				getLinksToSoftwareRepositoryIds(listOfSoftwareRepositoryIds, format);
				getLinksToVendorRepositoryIds(listOfVendorRepositoryIds, format);
			}
		}
	}

	/**
	 * @param listOfVendorRepositoryIds
	 * @param format
	 */
	private void getLinksToVendorRepositoryIds(
			List<String> listOfVendorRepositoryIds, DipFormatId format) {
		if (format.getDipVendorId() != null && format.getDipVendorId().length > 0) {
			List<String> vendorRepositoryIds = Arrays.asList(format.getDipVendorId());
			listOfVendorRepositoryIds.addAll(vendorRepositoryIds);
		}
	}

	/**
	 * @param listOfSoftwareRepositoryIds
	 * @param format
	 */
	private void getLinksToSoftwareRepositoryIds(
			List<String> listOfSoftwareRepositoryIds, DipFormatId format) {
		if (format.getDipSoftwareId() != null && format.getDipSoftwareId().length > 0) {
			List<String> softwareRepositoryIds = Arrays.asList(format.getDipSoftwareId());
			listOfSoftwareRepositoryIds.addAll(softwareRepositoryIds);
		}
	}

	/**
	 * @param buf
	 * @param listOfSoftwareRepositoryIds
	 * @param exclusionsList
	 */
	private void appendSoftware(StringBuffer buf,
			List<String> listOfSoftwareRepositoryIds,
			List<String> exclusionsList) {
		DipSoftwareId dipSoftwareId = new DipSoftwareId();
		List<? extends FfmaDomainObject> dipSoftwares = preservationRiskmanagementDao
			.retrieveCollection((FfmaDomainObject) dipSoftwareId, exclusionsList);
		if (dipSoftwares != null && dipSoftwares.size() > 0) {
			Iterator<? extends FfmaDomainObject> iterSoftwares = dipSoftwares.iterator();
			while (iterSoftwares.hasNext()) {
				DipSoftwareId software = (DipSoftwareId) iterSoftwares.next();
				getSoftwareRepositoryIds(buf, listOfSoftwareRepositoryIds, software);
			}
		}
	}

	/**
	 * @param buf
	 * @param listOfVendorRepositoryIds
	 * @param exclusionsList
	 */
	private void appendVendors(StringBuffer buf,
			List<String> listOfVendorRepositoryIds, List<String> exclusionsList) {
		DipVendorId dipVendorId = new DipVendorId();
		List<? extends FfmaDomainObject> dipVendors = preservationRiskmanagementDao
			.retrieveCollection((FfmaDomainObject) dipVendorId, exclusionsList);
		if (dipVendors != null && dipVendors.size() > 0) {
			Iterator<? extends FfmaDomainObject> iterVendors = dipVendors.iterator();
			while (iterVendors.hasNext()) {
				DipVendorId vendor = (DipVendorId) iterVendors.next();
				getListOfVendorRepositoryIds(buf, listOfVendorRepositoryIds, vendor);
			}
		}
	}

	/**
	 * @param buf
	 * @param listOfVendorRepositoryIds
	 * @param vendor
	 */
	private void getListOfVendorRepositoryIds(StringBuffer buf,
			List<String> listOfVendorRepositoryIds, DipVendorId vendor) {
		if (vendor != null) {
			boolean read = false; // read only if repository id stored in previously read LODFormat collection
			Iterator<String> iterStoredVendorId = listOfVendorRepositoryIds.iterator();
			while (iterStoredVendorId.hasNext()) {
				String currentRegistryId = (String) iterStoredVendorId.next();
				if (vendor.getDipId() != null
						&& vendor.getDipId().length() > 0
						&& vendor.getDipId().equals(
								currentRegistryId)) {
					read = true;
					break;
				}
			}
			if (read) {
				buf.append(vendor);
				buf.append(ReportConstants.SEMICOLONNEWLINE);
			}
		}
	}

	/**
	 * @param type
	 * @param ext
	 * @param buf
	 */
	private void getTextualFormatDescriptions(String type, String ext,
			StringBuffer buf) {
		if (type.equals(PreservationStatisticsTypes.TextualFormatDescriptions.name())) {
			List<String> listOfFormatDescriptions = new ArrayList<String>();
			DipFormatId dipFormatId = LODUtils.setDipFormatId(ext);
			List<String> exclusionsList = new ArrayList<String>();
			exclusionsList.add(RiskConstants.DOMAIN_OBJECT_NAME);
			List<? extends FfmaDomainObject> dipFormats = preservationRiskmanagementDao
				.retrieveCollection((FfmaDomainObject) dipFormatId, exclusionsList);
			if (dipFormats != null && dipFormats.size() > 0) {
				evaluateListOfFormatDescriptions(listOfFormatDescriptions, dipFormats);
				appendFormatDescriptions(buf, listOfFormatDescriptions);
			}
		}
	}

	/**
	 * @param listOfFormatDescriptions
	 * @param dipFormats
	 */
	private void evaluateListOfFormatDescriptions(
			List<String> listOfFormatDescriptions,
			List<? extends FfmaDomainObject> dipFormats) {
		Iterator<? extends FfmaDomainObject> iterFormats = dipFormats.iterator();
		while (iterFormats.hasNext()) {
			DipFormatId format = (DipFormatId) iterFormats.next();
			if (format != null) {
				if (format.getDescription() != null && format.getDescription().length > 0) {
					List<String> formatDescriptions = Arrays.asList(format.getDescription());
					listOfFormatDescriptions.addAll(formatDescriptions);
				}
			}
		}
	}

	/**
	 * @param buf
	 * @param listOfFormatDescriptions
	 */
	private void appendFormatDescriptions(StringBuffer buf,
			List<String> listOfFormatDescriptions) {
		Iterator<String> iterDescriptions = listOfFormatDescriptions.iterator();
		while (iterDescriptions.hasNext()) {
			String description = iterDescriptions.next();
			buf.append(description);
			buf.append(ReportConstants.SEMICOLONNEWLINE);
		}
	}

	/**
	 * @param type
	 * @param ext
	 * @param buf
	 */
	private void printRetrievedSoftwareAndVendors(String type, String ext,
			StringBuffer buf) {
		if (type.equals(PreservationStatisticsTypes.SoftwareAndVendorsForFormat.name())) {
			if (ext != null && ext.length() > 0) {
				buf.append(retrieveSoftware(ext));
				buf.append(ReportConstants.NEWLINE);
				buf.append(retrieveVendor(ext));
				buf.append(ReportConstants.NEWLINE);
			}
		}
	}

	/**
	 * @param buf
	 * @param name
	 * @param versionReleaseDate
	 * @param softwareCount
	 * @param software
	 * @param currentFormatVersion
	 * @param license
	 * @param limitations
	 * @param puid
	 * @param homepage
	 * @param mimetype
	 * @param genre
	 * @param creator
	 * @param open
	 * @param extension
	 * @param vendors
	 * @param standards
	 */
	private void printSoftwareRow(StringBuffer buf, String name,
			String versionReleaseDate, String softwareCount, String software,
			String currentFormatVersion, String license, String limitations,
			String puid, String homepage, String mimetype, String genre,
			String creator, String open, String extension, String vendors,
			String standards) {
		buf.append(name);
		buf.append(ReportConstants.SEMICOLON);
		buf.append(versionReleaseDate);
		buf.append(ReportConstants.SEMICOLON);
		buf.append(softwareCount);
		buf.append(ReportConstants.SEMICOLON);
		buf.append(software); 
		buf.append(ReportConstants.SEMICOLON);
		buf.append(currentFormatVersion);
		buf.append(ReportConstants.SEMICOLON);
		buf.append(license);
		buf.append(ReportConstants.SEMICOLON);
		buf.append(limitations);
		buf.append(ReportConstants.SEMICOLON);
		buf.append(puid);
		buf.append(ReportConstants.SEMICOLON);
		buf.append(homepage);
		buf.append(ReportConstants.SEMICOLON);
		buf.append(mimetype);
		buf.append(ReportConstants.SEMICOLON);
		buf.append(genre);
		buf.append(ReportConstants.SEMICOLON);
		buf.append(creator);
		buf.append(ReportConstants.SEMICOLON);
		buf.append(open);
		buf.append(ReportConstants.SEMICOLON);
		buf.append(extension);
		buf.append(ReportConstants.SEMICOLON);
		buf.append(vendors);
		buf.append(ReportConstants.SEMICOLON);
		buf.append(standards);
		buf.append(ReportConstants.SEMICOLON);
		buf.append(ReportConstants.NEWLINE);
	}

	/**
	 * @param buf
	 * @param name
	 * @param numberOfEmployees
	 * @param businessStatus
	 * @param currentFfma
	 * @param stockIssues
	 * @param rankedList
	 * @param country
	 * @param foundationDate
	 * @param homepage
	 */
	private void printVendorRow(StringBuffer buf, String name,
			String numberOfEmployees, String businessStatus,
			String currentFfma, String stockIssues, String rankedList,
			String country, String foundationDate, String homepage) {
		buf.append(name);
		buf.append(ReportConstants.SEMICOLON);
		buf.append(numberOfEmployees);
		buf.append(ReportConstants.SEMICOLON);
		buf.append(businessStatus);
		buf.append(ReportConstants.SEMICOLON);
		buf.append(currentFfma);
		buf.append(ReportConstants.SEMICOLON);
		buf.append(stockIssues);
		buf.append(ReportConstants.SEMICOLON);
		buf.append(rankedList);
		buf.append(ReportConstants.SEMICOLON);
		buf.append(country);
		buf.append(ReportConstants.SEMICOLON);
		buf.append(foundationDate);
		buf.append(ReportConstants.SEMICOLON);
		buf.append(homepage);
		buf.append(ReportConstants.SEMICOLON);
		buf.append(ReportConstants.NEWLINE);
	}

	/**
	 * @param buf
	 * @param listOfSoftwareRepositoryIds
	 * @param software
	 */
	private void getSoftwareRepositoryIds(StringBuffer buf,
			List<String> listOfSoftwareRepositoryIds, DipSoftwareId software) {
		if (software != null) {
			boolean read = false; // read only if repository id stored in previously read LODFormat collection
			Iterator<String> iterStoredSoftwareId = listOfSoftwareRepositoryIds.iterator();
			while (iterStoredSoftwareId.hasNext()) {
				String currentRegistryId = (String) iterStoredSoftwareId.next();
				if (software.getDipId() != null
						&& software.getDipId().length() > 0
						&& software.getDipId().equals(
								currentRegistryId)) {
					read = true;
					break;
				}
			}
			if (read) {
				buf.append(software);
				buf.append(ReportConstants.SEMICOLONNEWLINE);
			}
		}
	}

	/**
	 * @param lodFormat
	 * @return
	 */
	private String setLodFormatStandards(LODFormat lodFormat) {
		String standards = ReportConstants.EMPTYSTRING;
		if (lodFormat.getStandards() != null) {
			standards = lodFormat.getStandards();
		}
		return standards;
	}

	/**
	 * @param lodFormat
	 * @return
	 */
	private String setLodFormatVendors(LODFormat lodFormat) {
		String vendors = ReportConstants.EMPTYSTRING;
		if (lodFormat.getVendors() != null) {
			vendors = lodFormat.getVendors();
		}
		return vendors;
	}

	/**
	 * @param lodFormat
	 * @return
	 */
	private String setLodFormatExtension(LODFormat lodFormat) {
		String extension = ReportConstants.EMPTYSTRING;
		if (lodFormat.getFileExtensions() != null) {
			extension = lodFormat.getFileExtensions();
		}
		return extension;
	}

	/**
	 * @param lodFormat
	 * @return
	 */
	private String setLodFormatOpen(LODFormat lodFormat) {
		String open = ReportConstants.EMPTYSTRING;
		if (lodFormat.getOpenFormat() != null) {
			open = lodFormat.getOpenFormat();
		}
		return open;
	}

	/**
	 * @param lodFormat
	 * @return
	 */
	private String setLodFormatCreator(LODFormat lodFormat) {
		String creator = ReportConstants.EMPTYSTRING;
		if (lodFormat.getFormatCreator() != null) {
			creator = lodFormat.getFormatCreator();
		}
		return creator;
	}

	/**
	 * @param lodFormat
	 * @return
	 */
	private String setLodFormatGenre(LODFormat lodFormat) {
		String genre = ReportConstants.EMPTYSTRING;
		if (lodFormat.getFormatGenre() != null) {
			genre = lodFormat.getFormatGenre();
		}
		return genre;
	}

	/**
	 * @param lodFormat
	 * @return
	 */
	private String setLodFormatMimetype(LODFormat lodFormat) {
		String mimetype = ReportConstants.EMPTYSTRING;
		if (lodFormat.getMimeType() != null) {
			mimetype = lodFormat.getMimeType();
		}
		return mimetype;
	}

	/**
	 * @param lodFormat
	 * @return
	 */
	private String setLodFormatHomepage(LODFormat lodFormat) {
		String homepage = ReportConstants.EMPTYSTRING;
		if (lodFormat.getFormatHomepage() != null) {
			homepage = lodFormat.getFormatHomepage();
		}
		return homepage;
	}

	/**
	 * @param lodFormat
	 * @return
	 */
	private String setLodFormatPuid(LODFormat lodFormat) {
		String puid = ReportConstants.EMPTYSTRING;
		if (lodFormat.getPuid() != null) {
			puid = lodFormat.getPuid();
		}
		return puid;
	}

	/**
	 * @param lodFormat
	 * @return
	 */
	private String setLodFormatLimitations(LODFormat lodFormat) {
		String limitations = ReportConstants.EMPTYSTRING;
		if (lodFormat.getLimitations() != null) {
			limitations = lodFormat.getLimitations();
		}
		return limitations;
	}

	/**
	 * @param lodFormat
	 * @return
	 */
	private String setLodFormatLicense(LODFormat lodFormat) {
		String license = ReportConstants.EMPTYSTRING;
		if (lodFormat.getFormatLicense() != null) {
			license = lodFormat.getFormatLicense();
		}
		return license;
	}

	/**
	 * @param lodFormat
	 * @return
	 */
	private String setLodFormatVersion(LODFormat lodFormat) {
		String currentFormatVersion = ReportConstants.EMPTYSTRING;
		if (lodFormat.getCurrentFormatVersion() != null) {
			currentFormatVersion = lodFormat.getCurrentFormatVersion();
		}
		return currentFormatVersion;
	}

	/**
	 * @param lodFormat
	 * @return
	 */
	private String setLodFormatSoftware(LODFormat lodFormat) {
		String software = ReportConstants.EMPTYSTRING;
		if (lodFormat.getSoftware() != null) {
			software = lodFormat.getSoftware();
		}
		return software;
	}

	/**
	 * @param lodFormat
	 * @return
	 */
	private String setLodFormatSoftwareCount(LODFormat lodFormat) {
		String softwareCount = ReportConstants.EMPTYSTRING;
		if (lodFormat.getSoftwareCount() != null) {
			softwareCount = lodFormat.getSoftwareCount().toString();
		}
		return softwareCount;
	}

	/**
	 * @param lodFormat
	 * @return
	 */
	private String setLodFormatReleaseDate(LODFormat lodFormat) {
		String versionReleaseDate = ReportConstants.EMPTYSTRING;
		if (lodFormat.getCurrentVersionReleaseDate() != null) {
			versionReleaseDate = lodFormat.getCurrentVersionReleaseDate();
		}
		return versionReleaseDate;
	}

	/**
	 * @param lodFormat
	 * @return
	 */
	private String setLodFormatName(LODFormat lodFormat) {
		String name = ReportConstants.EMPTYSTRING;
		if (lodFormat.getFormatName() != null) {
			name = lodFormat.getFormatName();
		}
		return name;
	}

	/**
	 * @param listOfSoftwareRepositoryIds
	 * @param lodFormat
	 */
	private void setLodFormatRepositoryIds(
			List<String> listOfSoftwareRepositoryIds, LODFormat lodFormat) {
		if (lodFormat.getSoftwareId() != null) {
			List<String> softwareRepositoryIds = Arrays.asList(lodFormat.getSoftwareId());
			listOfSoftwareRepositoryIds.addAll(softwareRepositoryIds);
		}
	}

	/**
	 * @param buf
	 * @param listOfSoftwareRepositoryIds
	 * @param exclusionsList
	 */
	private void printLodSoftware(StringBuffer buf,
			List<String> listOfSoftwareRepositoryIds,
			List<String> exclusionsList) {
		buf.append(ReportConstants.NEWLINE);
		buf.append(LODConstants.CSV_SOFTWARE_TITLE);
		buf.append(LODConstants.CSV_SOFTWARE_HEADER);
		LODSoftware lodSoftwareObj = new LODSoftware();
		List<? extends FfmaDomainObject> lodSoftwareList = preservationRiskmanagementDao
			.retrieveCollection((FfmaDomainObject) lodSoftwareObj, exclusionsList);
		Iterator<? extends FfmaDomainObject> iterSoftware = lodSoftwareList.iterator();
		while (iterSoftware.hasNext()) {
			LODSoftware lodSoftware = (LODSoftware) iterSoftware.next();
			boolean read = false; // read only if repository id stored in previously read LODFormat collection
			Iterator<String> iterStoredSoftwareId = listOfSoftwareRepositoryIds.iterator();
			while (iterStoredSoftwareId.hasNext()) {
				String currentRegistryId = (String) iterStoredSoftwareId.next();
				if (lodSoftware.getRepositoryId() != null
						&& lodSoftware.getRepositoryId().length() > 0
						&& lodSoftware.getRepositoryId().equals(
								currentRegistryId)) {
					read = true;
					break;
				}
			}
			if (read) {
				evaluateAndPrintLodSoftware(buf, lodSoftware);
			}
		}
	}
	/**
	 * @param buf
	 * @param lodSoftware
	 */
	private void evaluateAndPrintLodSoftware(StringBuffer buf,
			LODSoftware lodSoftware) {
		String name = setLodSoftwareName(lodSoftware);
		String softwareLicense = setLodSoftwareLicense(lodSoftware);
		String softwareHomepage = setLodSoftwareHomepage(lodSoftware);
		String softwareGenre = setLodSoftwareGenre(lodSoftware);
		String os = setLodSoftwareOs(lodSoftware);
		String protocols = setLodSoftwareProtocols(lodSoftware);
		String programmingLanguage = setLodSoftwareProgrammingLanguage(lodSoftware);
		String softwareLatestVersion = setLodSoftwareLatestVersion(lodSoftware);
		String softwareReleaseDate = setLodSoftwareReleaseDate(lodSoftware);
		printLodSoftwareRow(buf, name, softwareLicense,
				softwareHomepage, softwareGenre, os, protocols,
				programmingLanguage, softwareLatestVersion,
				softwareReleaseDate);
	}

	/**
	 * @param lodSoftware
	 * @return
	 */
	private String setLodSoftwareReleaseDate(LODSoftware lodSoftware) {
		String softwareReleaseDate = ReportConstants.EMPTYSTRING;
		if (lodSoftware.getSoftwareReleaseDate() != null) {
			softwareReleaseDate = lodSoftware.getSoftwareReleaseDate();
		}
		return softwareReleaseDate;
	}

	/**
	 * @param lodSoftware
	 * @return
	 */
	private String setLodSoftwareLatestVersion(LODSoftware lodSoftware) {
		String softwareLatestVersion = ReportConstants.EMPTYSTRING;
		if (lodSoftware.getSoftwareLatestVersion() != null) {
			softwareLatestVersion = lodSoftware.getSoftwareLatestVersion();
		}
		return softwareLatestVersion;
	}

	/**
	 * @param lodSoftware
	 * @return
	 */
	private String setLodSoftwareProgrammingLanguage(LODSoftware lodSoftware) {
		String programmingLanguage = ReportConstants.EMPTYSTRING;
		if (lodSoftware.getProgrammingLanguage() != null) {
			programmingLanguage = lodSoftware.getProgrammingLanguage();
		}
		return programmingLanguage;
	}

	/**
	 * @param lodSoftware
	 * @return
	 */
	private String setLodSoftwareProtocols(LODSoftware lodSoftware) {
		String protocols = ReportConstants.EMPTYSTRING;
		if (lodSoftware.getProtocols() != null) {
			protocols = lodSoftware.getProtocols();
		}
		return protocols;
	}

	/**
	 * @param lodSoftware
	 * @return
	 */
	private String setLodSoftwareOs(LODSoftware lodSoftware) {
		String os = ReportConstants.EMPTYSTRING;
		if (lodSoftware.getOperatingSystem() != null) {
			os = lodSoftware.getOperatingSystem();
		}
		return os;
	}

	/**
	 * @param lodSoftware
	 * @return
	 */
	private String setLodSoftwareGenre(LODSoftware lodSoftware) {
		String softwareGenre = ReportConstants.EMPTYSTRING;
		if (lodSoftware.getGenre() != null) {
			softwareGenre = lodSoftware.getGenre();
		}
		return softwareGenre;
	}

	/**
	 * @param lodSoftware
	 * @return
	 */
	private String setLodSoftwareHomepage(LODSoftware lodSoftware) {
		String softwareHomepage = ReportConstants.EMPTYSTRING;
		if (lodSoftware.getSoftwareHomepage() != null) {
			softwareHomepage = lodSoftware.getSoftwareHomepage();
		}
		return softwareHomepage;
	}

	/**
	 * @param lodSoftware
	 * @return
	 */
	private String setLodSoftwareLicense(LODSoftware lodSoftware) {
		String softwareLicense = ReportConstants.EMPTYSTRING;
		if (lodSoftware.getSoftwareLicense() != null) {
			softwareLicense = lodSoftware.getSoftwareLicense();
		}
		return softwareLicense;
	}

	/**
	 * @param lodSoftware
	 * @return
	 */
	private String setLodSoftwareName(LODSoftware lodSoftware) {
		String name = ReportConstants.EMPTYSTRING;
		if (lodSoftware.getSoftwareName() != null) {
			name = lodSoftware.getSoftwareName();
		}
		return name;
	}

	/**
	 * @param buf
	 * @param name
	 * @param softwareLicense
	 * @param softwareHomepage
	 * @param softwareGenre
	 * @param os
	 * @param protocols
	 * @param programmingLanguage
	 * @param softwareLatestVersion
	 * @param softwareReleaseDate
	 */
	private void printLodSoftwareRow(StringBuffer buf, String name,
			String softwareLicense, String softwareHomepage,
			String softwareGenre, String os, String protocols,
			String programmingLanguage, String softwareLatestVersion,
			String softwareReleaseDate) {
		buf.append(name);
		buf.append(ReportConstants.SEMICOLON);
		buf.append(softwareLicense);
		buf.append(ReportConstants.SEMICOLON);
		buf.append(softwareHomepage);
		buf.append(ReportConstants.SEMICOLON);
		buf.append(softwareGenre);
		buf.append(ReportConstants.SEMICOLON);
		buf.append(os);
		buf.append(ReportConstants.SEMICOLON);
		buf.append(protocols);
		buf.append(ReportConstants.SEMICOLON);
		buf.append(programmingLanguage);
		buf.append(ReportConstants.SEMICOLON);
		buf.append(softwareLatestVersion);
		buf.append(ReportConstants.SEMICOLON);
		buf.append(softwareReleaseDate);
		buf.append(ReportConstants.SEMICOLON);
		buf.append(ReportConstants.NEWLINE);
	}

	/**
	 * @param ext
	 * @return
	 */
	public String retrieveSoftware(String ext) {
		StringBuffer buf = new StringBuffer();
		buf.append(ReportConstants.EMPTYSTRING);
		DipFormatId dipFormatId = new DipFormatId();
		dipFormatId.setDipId(LODConstants.DIP + ext);
		List<String> exclusionsList = new ArrayList<String>();
		exclusionsList.add(RiskConstants.DOMAIN_OBJECT_NAME);
		List<? extends FfmaDomainObject> dipFormats = preservationRiskmanagementDao
			.retrieveCollection((FfmaDomainObject) dipFormatId, exclusionsList);
		if (dipFormats != null && dipFormats.size() > 0) {
			DipFormatId dipFormat = (DipFormatId) dipFormats.get(0);
			List<String> listOfSoftwareDipIds = Arrays.asList(dipFormat.getDipSoftwareId());
			Iterator<String> iterSoftwareDipIds = listOfSoftwareDipIds.iterator();
			while (iterSoftwareDipIds.hasNext()) {
				String softwareDipId = iterSoftwareDipIds.next();
				DipSoftwareId dipSoftwareId = new DipSoftwareId();
				dipSoftwareId.setDipId(softwareDipId);
				List<? extends FfmaDomainObject> dipSoftwares = preservationRiskmanagementDao
					.retrieveCollection((FfmaDomainObject) dipSoftwareId, exclusionsList);
				if (dipSoftwares != null && dipSoftwares.size() > 0) {
					DipSoftwareId dipSoftware = (DipSoftwareId) dipSoftwares.get(0);
					buf.append(getSoftwareList(dipSoftware.getFreebaseId()));
					buf.append(getSoftwareList(dipSoftware.getDBPediaId()));
					buf.append(getSoftwareList(dipSoftware.getPronomId()));
					buf.append(getSoftwareList(dipSoftware.getAitId()));
				}
			}
		}
		return buf.toString();
	}

	/**
	 * @param ext
	 * @return
	 */
	public String retrieveVendor(String ext) {
		StringBuffer buf = new StringBuffer();
		buf.append(ReportConstants.EMPTYSTRING);
		DipFormatId dipFormatId = new DipFormatId();
		dipFormatId.setDipId(LODConstants.DIP + ext);
		List<String> exclusionsList = new ArrayList<String>();
		exclusionsList.add(RiskConstants.DOMAIN_OBJECT_NAME);
		List<? extends FfmaDomainObject> dipFormats = preservationRiskmanagementDao
			.retrieveCollection((FfmaDomainObject) dipFormatId, exclusionsList);
		if (dipFormats != null && dipFormats.size() > 0) {
			DipFormatId dipFormat = (DipFormatId) dipFormats.get(0);
			List<String> listOfVendorDipIds = Arrays.asList(dipFormat.getDipVendorId());
			Iterator<String> iterVendorDipIds = listOfVendorDipIds.iterator();
			while (iterVendorDipIds.hasNext()) {
				String vendorDipId = iterVendorDipIds.next();
				DipVendorId dipVendorId = new DipVendorId();
				dipVendorId.setDipId(vendorDipId);
				List<? extends FfmaDomainObject> dipVendors = preservationRiskmanagementDao
					.retrieveCollection((FfmaDomainObject) dipVendorId, exclusionsList);
				if (dipVendors != null && dipVendors.size() > 0) {
					DipVendorId dipVendor = (DipVendorId) dipVendors.get(0);
					buf.append(getVendorList(dipVendor.getFreebaseId()));
					buf.append(getVendorList(dipVendor.getDBPediaId()));
					buf.append(getVendorList(dipVendor.getPronomId()));
					buf.append(getVendorList(dipVendor.getAitId()));
				}
			}
		}
		return buf.toString();
	}
	
	/**
	 * This method aggregates found LODVendor objects from passed repository and
	 * attaches it to the resulting vendors list
	 * @param vendorRepositoryIds
	 *        Repository IDs stored in DipVendorId collection
	 * @return LODVendor objects for particular repository
	 */
	public String getVendorList(String[] vendorRepositoryIds) {
		StringBuffer buf = new StringBuffer();
		buf.append(ReportConstants.EMPTYSTRING);
		List<String> exclusionsList = new ArrayList<String>();
		exclusionsList.add(RiskConstants.DOMAIN_OBJECT_NAME);
		if (vendorRepositoryIds != null && vendorRepositoryIds.length > 0) {
			List<String> listOfVendorRepositoryIds = Arrays.asList(vendorRepositoryIds);
			Iterator<String> iterVendorRepositoryIds = listOfVendorRepositoryIds.iterator();
			while (iterVendorRepositoryIds.hasNext()) {
				String vendorRepositoryId = iterVendorRepositoryIds.next();
				LODVendor lodVendor = new LODVendor();
				lodVendor.setRepositoryId(vendorRepositoryId);
				List<? extends FfmaDomainObject> lodRepositoryVendors = preservationRiskmanagementDao
					.retrieveCollection((FfmaDomainObject) lodVendor, exclusionsList);
				if (lodRepositoryVendors != null && lodRepositoryVendors.size() > 0) {
					Iterator<? extends FfmaDomainObject> iterLodVendorRepository = lodRepositoryVendors.iterator();
					while(iterLodVendorRepository.hasNext()) {
						LODVendor currentLodVendor = (LODVendor) iterLodVendorRepository.next();
						buf.append(currentLodVendor.toString());
						buf.append(LODConstants.OBJ_SEPARATOR);
					}
				}
			}
		}
		return buf.toString();
	}
	
	/**
	 * This method aggregates found LODSoftware objects from passed repository and
	 * attaches it to the resulting software list
	 * @param softwareRepositoryIds
	 *        Repository IDs stored in DipSoftwareId collection
	 * @return LODSoftware objects for particular repository
	 */
	public String getSoftwareList(String[] softwareRepositoryIds) {
		StringBuffer buf = new StringBuffer();
		buf.append(ReportConstants.EMPTYSTRING);
		List<String> exclusionsList = new ArrayList<String>();
		exclusionsList.add(RiskConstants.DOMAIN_OBJECT_NAME);
		if (softwareRepositoryIds != null && softwareRepositoryIds.length > 0) {
			List<String> listOfSoftwareRepositoryIds = Arrays.asList(softwareRepositoryIds);
			Iterator<String> iterSoftwareRepositoryIds = listOfSoftwareRepositoryIds.iterator();
			while (iterSoftwareRepositoryIds.hasNext()) {
				String softwareRepositoryId = iterSoftwareRepositoryIds.next();
				LODSoftware lodSoftware = new LODSoftware();
				lodSoftware.setRepositoryId(softwareRepositoryId);
				createSoftwareList(buf, exclusionsList, lodSoftware);
			}
		}
		return buf.toString();
	}

	/**
	 * @param buf
	 * @param exclusionsList
	 * @param lodSoftware
	 */
	private void createSoftwareList(StringBuffer buf,
			List<String> exclusionsList, LODSoftware lodSoftware) {
		List<? extends FfmaDomainObject> lodRepositorySoftwares = preservationRiskmanagementDao
			.retrieveCollection((FfmaDomainObject) lodSoftware, exclusionsList);
		if (lodRepositorySoftwares != null && lodRepositorySoftwares.size() > 0) {
			Iterator<? extends FfmaDomainObject> iterLodSoftwareRepository = lodRepositorySoftwares.iterator();
			while(iterLodSoftwareRepository.hasNext()) {
				LODSoftware currentLodSoftware = (LODSoftware) iterLodSoftwareRepository.next();
				buf.append(currentLodSoftware.toString());
				buf.append(LODConstants.OBJ_SEPARATOR);
			}
		}
	}
	
	/**
	 * @param ext
	 * @param str
	 * @return
	 */
	public boolean checkStringContainsExtension(String ext, String str) {
		boolean res = false;
		if (ext != null && ext.length() > 0 && str.toLowerCase().contains(ext.toLowerCase())) {
			res = true;
		}
		return res;
	}

//	public List<? extends FfmaDomainObject>  retrieveDipFormatIdCollection() {
//		DipFormatId dipFormatId = new DipFormatId();
//		dipFormatId.setDipId(LODConstants.DIP + ext);
//		List<String> exclusionsList = new ArrayList<String>();
//		exclusionsList.add(RiskConstants.DOMAIN_OBJECT_NAME);
//		List<? extends FfmaDomainObject> dipFormats = preservationRiskmanagementDao
//			.retrieveCollection((FfmaDomainObject) dipFormatId, exclusionsList);
//		return dipFormats;
//	}
}
