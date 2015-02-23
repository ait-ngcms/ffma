package ait.ffma.service.preservation.riskmanagement.api.lod;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import ait.ffma.domain.FfmaDomainObject;
import ait.ffma.domain.preservation.riskmanagement.AitFileFormat;
import ait.ffma.domain.preservation.riskmanagement.DBPediaFileFormat;
import ait.ffma.domain.preservation.riskmanagement.DipFormatId;
import ait.ffma.domain.preservation.riskmanagement.FreebaseFileFormat;
import ait.ffma.domain.preservation.riskmanagement.LODFormat;
import ait.ffma.domain.preservation.riskmanagement.LODSoftware;
import ait.ffma.domain.preservation.riskmanagement.LODVendor;
import ait.ffma.domain.preservation.riskmanagement.PronomFileFormat;
import ait.ffma.service.preservation.riskmanagement.api.lod.lodmodel.LODPronomSoftware;
import ait.ffma.service.preservation.riskmanagement.api.riskanalysis.risk.ReportConstants;
import ait.ffma.service.preservation.riskmanagement.api.riskanalysis.risk.RiskConstants;

/**
 * This is a helper class for LOD statistics calculations
 */
public final class LODStatisticsUtils {

	private LODStatisticsUtils() {} 

	/**
	 * Logger
	 */
	private static Logger log = Logger.getLogger(LODStatisticsUtils.class.getName());

	/**
	 * Helper list to fill DipFormatId collection
	 */
	private static List<DipFormatId> dipFormatIdList = new ArrayList<DipFormatId>();

	/**
	 * Helper variables for vendor description
	 */
	private static String vendorListCsv = ReportConstants.EMPTYSTRING; //FB

	/**
	 * @return
	 */
	public static String getVendorListCsv() {
		return vendorListCsv;
	}

	/**
	 * @param vendorListCsv
	 */
	public static void setVendorListCsv(String vendorListCsv) {
		LODStatisticsUtils.vendorListCsv = vendorListCsv;
	}

	private static String vendorDescriptionCsv = ReportConstants.EMPTYSTRING;
	
	/**
	 * These maps are used to bind vendor to file format
	 */
	private static Map<String, String> mapFileFormatToVendor = new HashMap<String, String>();
	private static Map<String, String> mapVendorNameToVendorId = new HashMap<String, String>();

	/**
	 * Harvested data for format, software and vendor
	 */
	private static List<LODFormat> lodFormatList = new ArrayList<LODFormat>();
	private static List<LODSoftware> lodSoftwareList = new ArrayList<LODSoftware>();
	private static List<LODVendor> lodVendorList = new ArrayList<LODVendor>();

	/**
	 * This method clears helper lists and maps
	 */
	public static void clearCache() {
		lodFormatList.clear();
		lodSoftwareList.clear();
		lodVendorList.clear();
		mapFileFormatToVendor.clear();
		mapVendorNameToVendorId.clear();
		LODStatisticsDBPediaUtils.getDbpediaCache().clear();
	}

	/**
	 * @return DipFormatId list 
	 */
	public static List<DipFormatId> getDipFormatIdList() {
		return dipFormatIdList;
	}

	/**
	 * @return
	 */
	public static List<LODFormat> getLodFormats() {
		return lodFormatList;
	}

	/**
	 * @return
	 */
	public static List<LODSoftware> getLodSoftware() {
		return lodSoftwareList;
	}

	/**
	 * @return
	 */
	public static List<LODVendor> getLodVendors() {
		return lodVendorList;
	}

	/**
	 * Check if extension is already in the list
	 * @param extension
	 * @return
	 */
	private static boolean extensionExists(String extension) {
		boolean res = false;
		Iterator<DipFormatId> resultIter = dipFormatIdList.iterator();
		while (resultIter.hasNext()) {
			DipFormatId dipFormatId = resultIter.next();
			String currentExtension = dipFormatId.getExtension();
			res = checkEquality(extension, currentExtension);
		}
		return res;
	}

	/**
	 * @param extension
	 * @param currentExtension
	 * @return
	 */
	public static boolean checkEquality(String extension, String currentExtension) {
		boolean res = false;
		if (currentExtension != null && extension != null) {
			if (currentExtension.length() > 0 && extension.length() > 0
				&& currentExtension.equals(extension)) {
				res = true;
			}
		}
		return res;
	}
	
	/**
	 * Retrieve DipFormatId by extension 
	 * @param extension
	 * @return
	 */
	private static DipFormatId getDipFormatIdByExtension(String extension) {
		DipFormatId res = null;
		Iterator<DipFormatId> resultIter = dipFormatIdList.iterator();
		while (resultIter.hasNext()) {
			DipFormatId dipFormatId = resultIter.next();
			String currentExtension = dipFormatId.getExtension();
			if (checkEquality(extension, currentExtension)) {
				res = dipFormatId;
			}
		}
		return res;
	}
	
	/**
	 * This method merges string arrays to one string array
	 * @param existingValues
	 * @param newValues
	 * @return
	 */
	private static String[] addArrays(String[] existingValues, String[] newValues) {
		List<String> existingIds = Arrays.asList(existingValues);
		List<String> newIds = Arrays.asList(newValues);
		List<String> idList = new ArrayList<String>();
		idList.addAll(existingIds);
		idList.addAll(newIds);
		List<String> emptyStrings = new ArrayList<String>();
		emptyStrings.add(ReportConstants.SPACE);
		idList.removeAll(emptyStrings);
		return idList.toArray(new String[idList.size()]);
	}

	/**
	 * @param dipFormatId
	 * @param format
	 */
	private static void evaluateDipForamtId(DipFormatId dipFormatId, String format) {
		if (format != null && format.length() > 0 && !format.equals("or")) {
			if (!extensionExists(format)) {
				dipFormatId.setExtension(format);
				dipFormatId.setDipId(LODConstants.DIP + format);
				dipFormatIdList.add(dipFormatId);
			} else {
				DipFormatId existingDipFormatId = getDipFormatIdByExtension(format);
				evaluateAitIds(dipFormatId, existingDipFormatId);
				evaluateDBPediaIds(dipFormatId, existingDipFormatId);
				evaluatePronomIds(dipFormatId, existingDipFormatId);
				evaluateFreebaseIds(dipFormatId, existingDipFormatId);
				evaluateDescriptions(dipFormatId, existingDipFormatId);
				dipFormatId.setExtension(format);
				dipFormatId.setDipId(LODConstants.DIP + format);
			}
		}
	}

	/**
	 * @param dipFormatId
	 * @param existingDipFormatId
	 */
	private static void evaluateDescriptions(DipFormatId dipFormatId,
			DipFormatId existingDipFormatId) {
		if (dipFormatId.getDescription() != null && dipFormatId.getDescription().length > 0) {
			String[] resDescription = addArrays(existingDipFormatId.getDescription(), dipFormatId.getDescription());
			if (resDescription != null && resDescription.length > 0) {
				existingDipFormatId.setDescription(resDescription);
			}
		}
	}

	/**
	 * @param dipFormatId
	 * @param existingDipFormatId
	 */
	private static void evaluateFreebaseIds(DipFormatId dipFormatId,
			DipFormatId existingDipFormatId) {
		if (dipFormatId.getFreebaseId() != null && dipFormatId.getFreebaseId().length > 0) {
			String[] resFreebase = addArrays(existingDipFormatId.getFreebaseId(), dipFormatId.getFreebaseId());
			if (resFreebase != null && resFreebase.length > 0) {
				existingDipFormatId.setFreebaseId(resFreebase);
			}
		}
	}

	/**
	 * @param dipFormatId
	 * @param existingDipFormatId
	 */
	private static void evaluatePronomIds(DipFormatId dipFormatId,
			DipFormatId existingDipFormatId) {
		if (dipFormatId.getPronomId() != null && dipFormatId.getPronomId().length > 0) {
			String[] resPronom = addArrays(existingDipFormatId.getPronomId(), dipFormatId.getPronomId());
			if (resPronom != null && resPronom.length > 0) {
				existingDipFormatId.setPronomId(resPronom);
			}
		}
	}

	/**
	 * @param dipFormatId
	 * @param existingDipFormatId
	 */
	private static void evaluateDBPediaIds(DipFormatId dipFormatId,
			DipFormatId existingDipFormatId) {
		if (dipFormatId.getDBPediaId() != null && dipFormatId.getDBPediaId().length > 0) {
			String[] resDBPedia = addArrays(existingDipFormatId.getDBPediaId(), dipFormatId.getDBPediaId());
			if (resDBPedia != null && resDBPedia.length > 0) {
				existingDipFormatId.setDBPediaId(resDBPedia);
			}
		}
	}

	/**
	 * @param dipFormatId
	 * @param existingDipFormatId
	 */
	private static void evaluateAitIds(DipFormatId dipFormatId,
			DipFormatId existingDipFormatId) {
		if (dipFormatId.getAitId() != null && dipFormatId.getAitId().length > 0) {
			String[] resAit = addArrays(existingDipFormatId.getAitId(), dipFormatId.getAitId());
			if (resAit != null && resAit.length > 0) {
				existingDipFormatId.setAitId(resAit);
			}
		}
	}
	
	/**
	 * This method aggregates file formats from different LOD repositories
	 * @param formatList
	 */
	private static void aggregateFileFormats(List<DipFormatId> formatList) {
		Iterator<DipFormatId> iter = formatList.iterator();
		while (iter.hasNext()) {
			String format = null;
			DipFormatId dipFormatId = (DipFormatId) iter.next();
			String formatStr = dipFormatId.getExtension();
			if (formatStr.contains(ReportConstants.SPACE)) {
				String[] formats = formatStr.split(ReportConstants.SPACE);
				for (int i = 0; i < formats.length; i++) {
					format = formats[i]
							.replace(ReportConstants.POINT,
									ReportConstants.EMPTYSTRING)
							.replace(ReportConstants.COMMA,
									ReportConstants.EMPTYSTRING)
							.replace(ReportConstants.SPACE, ReportConstants.EMPTYSTRING);
					format = format.toLowerCase();
					evaluateDipForamtId(dipFormatId, format);
				}
			} else {
				if (formatStr.contains(ReportConstants.POINT)) {
					format = formatStr.replace(ReportConstants.POINT,
							ReportConstants.EMPTYSTRING).replace(
							ReportConstants.SPACE, ReportConstants.EMPTYSTRING);
				} else {
					format = formatStr;
				}
				format = format.toLowerCase();
				evaluateDipForamtId(dipFormatId, format);
			}
		}
	}
	
	/**
	 * This method initializes extensions list aggregated from all LOD repositories.
	 * @param aitFileFormats
	 * @param pronomFileFormats
	 * @param dbpediaFileFormats
	 * @param freebaseFileFormats
	 */
	public static void initFileFormatListFromAllRepositories(
			List<? extends FfmaDomainObject> aitFileFormats,
			List<? extends FfmaDomainObject> pronomFileFormats,
			List<? extends FfmaDomainObject> dbpediaFileFormats,
			List<? extends FfmaDomainObject> freebaseFileFormats) {
		if (aitFileFormats != null && aitFileFormats.size() > 0) {
			List<DipFormatId> aitFileFormatList = LODUtils.getAitDipFileFormatsList(aitFileFormats);
			aggregateFileFormats(aitFileFormatList);
		}
		if (pronomFileFormats != null && pronomFileFormats.size() > 0) {
			List<DipFormatId> pronomFileFormatList = LODUtils.getPronomDipFileFormatsList(pronomFileFormats);
			aggregateFileFormats(pronomFileFormatList);
		}
		if (dbpediaFileFormats != null && dbpediaFileFormats.size() > 0) {
			List<DipFormatId> dbpediaFileFormatList = LODUtils.getDBPediaDipFileFormatsList(dbpediaFileFormats);
			aggregateFileFormats(dbpediaFileFormatList);
		}
		if (freebaseFileFormats != null && freebaseFileFormats.size() > 0) {
			List<DipFormatId> freebaseFileFormatList = LODStatisticsFreebaseUtils.getFreebaseDipFileFormatsList(freebaseFileFormats);
			aggregateFileFormats(freebaseFileFormatList);
		}
	}
		
	/**
	 * This method initializes summarized LOD formats list 
	 * @param fileFormatsList
	 */
	public static void setLodFormats(List<? extends FfmaDomainObject> fileFormatsList) {
		lodFormatList.clear();
		Iterator<? extends FfmaDomainObject> iter = fileFormatsList.iterator();
		while (iter.hasNext()) {
			LODFormat fileFormatObject = (LODFormat) iter.next();
			lodFormatList.add(fileFormatObject);
		}
	}

	/**
	 * This method initializes summarized LOD software list 
	 * @param softwareList
	 */
	public static void setLodSoftware(List<? extends FfmaDomainObject> softwareList) {
		lodSoftwareList.clear();
		Iterator<? extends FfmaDomainObject> iter = softwareList.iterator();
		while (iter.hasNext()) {
			LODSoftware softwareObject = (LODSoftware) iter.next();
			lodSoftwareList.add(softwareObject);
		}
	}

	/**
	 * This method initializes summarized LOD vendors list 
	 * @param vendorsList
	 */
	public static void setLodVendors(List<? extends FfmaDomainObject> vendorsList) {
		lodVendorList.clear();
		Iterator<? extends FfmaDomainObject> iter = vendorsList.iterator();
		while (iter.hasNext()) {
			LODVendor fileVendorObject = (LODVendor) iter.next();
			lodVendorList.add(fileVendorObject);
		}
	}

	/**
	 * This method evaluates file format information from FREEBASE linked open data repository 
	 * to store results in database in LODFormat, LODSoftware, LODVendor and DipFormatId collections.
	 */
	public static void searchFreebaseFileFormatInformation() {
		int counter = 0; // limit to test
		Iterator<DipFormatId> fileFormatIter = dipFormatIdList.iterator();
		while (fileFormatIter.hasNext()) {
			DipFormatId dipFormatId = fileFormatIter.next();
			String currentFileFormat = dipFormatId.getExtension();
			StringBuffer softwareCsv = new StringBuffer();
			softwareCsv.append(ReportConstants.EMPTYSTRING);
			vendorListCsv = ReportConstants.EMPTYSTRING; // FB
			List<String> fields = Arrays.asList(LODConstants.FB_READ_BY, LODConstants.FB_NAME, LODConstants.FB_ID);
			List<List<String>> resultList = LODUtils.searchInFreebaseExt(
					LODConstants.LOD_SOFTWARE_BY_FORMAT_PROPERTY_ID,
					currentFileFormat, LODConstants.FB_EXTENSION,
					fields);
			Iterator<List<String>> resultIter = resultList.iterator();
			List<String> softwareNames = new ArrayList<String>();
			List<String> softwareIDs = new ArrayList<String>();
			buildFreebaseSoftware(softwareCsv, fields, resultIter, softwareNames, softwareIDs, currentFileFormat);

			String creationDateStr = getCreationDate(currentFileFormat);
			String nameStr = getName(currentFileFormat);
			String mimeTypeStr = getMimeType(currentFileFormat);
			List<String> repositoryIDs = getRepositoryIds(currentFileFormat);
			List<String> descriptions = getGuidAndDescriptions(currentFileFormat);
			String genreStr = getGenre(currentFileFormat);
			String creatorStr = getCreator(currentFileFormat);

			String vendorCsv = ReportConstants.EMPTYSTRING;
			if (mapFileFormatToVendor != null && mapFileFormatToVendor.size() > 0) {
				vendorCsv = mapFileFormatToVendor.get(currentFileFormat);
			}
			List<String> vendorNames = new ArrayList<String>();
			List<String> vendorIDs = new ArrayList<String>();
			getVendorIdsAndNames(vendorCsv, vendorNames, vendorIDs);

			LODFormat lodFormat = createLodFormat(currentFileFormat,
					softwareCsv, resultList, creationDateStr, nameStr,
					mimeTypeStr, repositoryIDs, genreStr, creatorStr, vendorCsv);
			counter = fillLODFormat(counter, softwareNames, softwareIDs,
					descriptions, vendorNames, vendorIDs, lodFormat, LODConstants.FREEBASE);
//			if (counter > 2) break; // limit to test
		}
	}

	/**
	 * This method generates CSV information for software 
	 * @param softwareName
	 * @param softwareId
	 * @param fileFormat
	 *        To bind file format with vendor
	 * @return CSV information collected from LOD repository
	 */
	private static String generateFreebaseSoftwareDescriptionColumn(String softwareName, String softwareId, String fileFormat) {
		String res = ReportConstants.EMPTYSTRING;
		res = softwareName + ReportConstants.SEMICOLON;		
		res = setFreebaseLodSoftware(softwareName, softwareId, fileFormat, res);
		buildFreebaseVendorsList(softwareName, softwareId, fileFormat);
		res = res + ReportConstants.LINEEND;
		return res;
	}

	/**
	 * @param softwareName
	 * @param softwareId
	 * @param fileFormat
	 */
	private static void buildFreebaseVendorsList(String softwareName,
			String softwareId, String fileFormat) {
		List<String> fields = Arrays.asList(LODConstants.FB_SOFTWARE_DEVELOPER,
				LODConstants.FB_ID, LODConstants.FB_NAME, LODConstants.FB_GUID);
		List<List<String>> resultList = LODStatisticsFreebaseUtils.getFreebaseVendorsForSoftware(softwareId, fields);
		Iterator<List<String>> resultIter = resultList.iterator();
		while (resultIter.hasNext()) {
			List<String> subList = resultIter.next();
			Iterator<String> subIter = subList.iterator();
			buildFreebaseVendorsDescription(softwareName, fileFormat, fields, subIter);
		}
	}

	/**
	 * @param softwareName
	 * @param fileFormat
	 * @param fields
	 * @param subIter
	 */
	private static void buildFreebaseVendorsDescription(String softwareName,
			String fileFormat, List<String> fields, Iterator<String> subIter) {
		String tmpVendorName = ReportConstants.EMPTYSTRING;
		String tmpVendorId = ReportConstants.EMPTYSTRING;
		String tmpVendorGuid = ReportConstants.EMPTYSTRING;
		int index = 1;
		while (subIter.hasNext()) {
			String str = subIter.next();
			tmpVendorName = aggregateFreebaseVendors(fields, tmpVendorName, index, str);
			tmpVendorId = LODStatisticsFreebaseUtils.getFreebaseVendorId(fields, tmpVendorId, index, str);
			tmpVendorGuid = LODStatisticsFreebaseUtils.getFreebaseVendorGuid(fields, tmpVendorGuid, index, str);
			index++;
		}
		if (!vendorDescriptionCsv.contains(tmpVendorName)) {
			mapFileFormatToVendor.put(fileFormat, tmpVendorName);
			mapVendorNameToVendorId.put(tmpVendorName, tmpVendorId);
			vendorDescriptionCsv = vendorDescriptionCsv + 
				generateFreebaseVendorDescriptionColumn(tmpVendorId, tmpVendorName, tmpVendorGuid, fileFormat, softwareName);
		}
	}

	/**
	 * This method generates CSV information for software vendors
	 * @param vendorId
	 * @param vendorName
	 * @param vendorGuid
	 * @param fileFormat
	 * @param softwareName
	 * @return CSV information collected from LOD repository
	 */
	private static String generateFreebaseVendorDescriptionColumn(String vendorId,
			String vendorName, String vendorGuid, String fileFormat,
			String softwareName) {
		String res = ReportConstants.EMPTYSTRING;
		res = vendorName + ReportConstants.SEMICOLON;
		LODVendor lodVendor = new LODVendor();
		lodVendor.setRepository(LODConstants.FREEBASE);
		lodVendor.setOrganisationName(vendorName);
		res = LODStatisticsFreebaseUtils.setFreebaseLodVendorNumOfEmployees(vendorId, res, lodVendor);
		res = LODStatisticsFreebaseUtils.setFreebaseLodVendorBusinessStatus(vendorId, res, lodVendor);
		res = LODStatisticsFreebaseUtils.setFreebaseLodVendorCurrentFfma(vendorId, res, lodVendor);
		res = LODStatisticsFreebaseUtils.setFreebaseLodVendorStockIssues(vendorId, res, lodVendor);
		res = LODStatisticsFreebaseUtils.setFreebaseLodVendorRankedList(vendorId, res, lodVendor);
		res = LODStatisticsFreebaseUtils.setFreebaseLodVendorFoundationDate(vendorId, res, lodVendor);		
		LODStatisticsFreebaseUtils.setFreebaseLodVendorDescriptionAndId(vendorId, vendorGuid, lodVendor);
		LODStatisticsFreebaseUtils.setFreebaseLodVendorFileFormat(fileFormat, lodVendor);
		LODStatisticsFreebaseUtils.setFreebaseLodVendorSoftwareName(softwareName, lodVendor);
		lodVendorList.add(lodVendor);
		res = res + ReportConstants.SEMICOLON; // homepage
		res = res + ReportConstants.LINEEND; 
		return res;
	}

	/**
	 * @param softwareName
	 * @param softwareId
	 * @param fileFormat
	 * @param resStr
	 * @return
	 */
	private static String setFreebaseLodSoftware(String softwareName,
			String softwareId, String fileFormat, String resStr) {
		String res = resStr;
		LODSoftware lodSoftware = new LODSoftware();
		lodSoftware.setSoftwareName(softwareName);
		lodSoftware.setRepository(LODConstants.FREEBASE);
		res = LODStatisticsFreebaseUtils.setFreebaseLodSoftwareLicense(softwareId, res, lodSoftware);		
		res = LODStatisticsFreebaseUtils.setFreebaseLodSoftwareGenre(softwareId, res, lodSoftware);		
		res = LODStatisticsFreebaseUtils.setFreebaseLodSoftwareOs(softwareId, res, lodSoftware);
		res = LODStatisticsFreebaseUtils.setFreebaseLodSoftwareProtocol(softwareId, res, lodSoftware);
		res = LODStatisticsFreebaseUtils.setFreebaseLodSoftwareProgrammingLanguage(softwareId, res, lodSoftware);
		res = LODStatisticsFreebaseUtils.setFreebaseLodSoftwareVersion(softwareId, res, lodSoftware);		
		res = LODStatisticsFreebaseUtils.setFreebaseLodSoftwareReleaseDate(softwareId, res, lodSoftware);		
		// replace repository IDs like /m/123 by /en/123 due to get GUID number
//		if (softwareId == null || !softwareId.contains(LODConstants.FB_EN_ID)) {
//			softwareId = LODConstants.FB_EN_ID + softwareName.toLowerCase().replaceAll(" ", "_");
//		}		
		LODStatisticsFreebaseUtils.setFreebaseLodSoftwareDescriptionAndId(softwareId, lodSoftware);		
		LODStatisticsFreebaseUtils.setFreebaseLodSoftwareLatestRelease(softwareId, lodSoftware);
		LODStatisticsFreebaseUtils.setFreebaseLodSoftwareLatestVersion(softwareId, lodSoftware);
		LODStatisticsFreebaseUtils.setFreebaseLodSoftwareSwLicense(softwareId, lodSoftware);
		LODStatisticsFreebaseUtils.setFreebaseLodSoftwareRepGenre(softwareId, lodSoftware);		
		LODStatisticsFreebaseUtils.setFreebaseLodSoftwareFileFormat(fileFormat, lodSoftware);
		lodSoftwareList.add(lodSoftware);
		return res;
	}

	/**
	 * @param fields
	 * @param tmpVendorName
	 * @param index
	 * @param str
	 * @return
	 */
	private static String aggregateFreebaseVendors(List<String> fields,
			String tmpVendorName, int index, String str) {
		String tmpVendorNameRes = tmpVendorName;
		if (fields.get(index).equals(LODConstants.FB_NAME)) {
			tmpVendorNameRes = str;
			if (tmpVendorNameRes != null && !vendorListCsv.contains(tmpVendorNameRes)) {
				vendorListCsv = vendorListCsv + tmpVendorNameRes + ReportConstants.COMMASPACE;
			}
		}
		return tmpVendorNameRes;
	}

	/**
	 * @param vendorCsv
	 * @param vendorNames
	 * @param vendorIDs
	 */
	private static void getVendorIdsAndNames(String vendorCsv,
			List<String> vendorNames, List<String> vendorIDs) {
		if (vendorCsv != null && vendorCsv.length() > 0) {
			vendorNames.add(vendorCsv);
			if (mapVendorNameToVendorId != null && mapVendorNameToVendorId.size() > 0) {
				vendorIDs.add(mapVendorNameToVendorId.get(vendorCsv));
			}
		}
	}

	/**
	 * @param currentFileFormat
	 * @return
	 */
	private static String getCreator(String currentFileFormat) {
		List<String> creatorList = LODUtils.searchInFreebase(
				LODConstants.LOD_SOFTWARE_BY_FORMAT_PROPERTY_ID,
				currentFileFormat, LODConstants.FB_EXTENSION,
				LODConstants.FB_FORMAT_CREATOR, ReportConstants.EMPTYSTRING);

		String creatorStr = listToString(creatorList);
		creatorStr = creatorStr.replaceAll(ReportConstants.AMP, ReportConstants.AMPSHORT); // special case
		return creatorStr;
	}

	/**
	 * @param currentFileFormat
	 * @return
	 */
	private static String getGenre(String currentFileFormat) {
		List<String> genreList = LODUtils.searchInFreebase(
				LODConstants.LOD_SOFTWARE_BY_FORMAT_PROPERTY_ID,
				currentFileFormat, LODConstants.FB_EXTENSION,
				LODConstants.FB_GENRE, LODConstants.FB_NAME);
		return listToString(genreList);
	}

	/**
	 * @param currentFileFormat
	 * @return
	 */
	private static List<String> getGuidAndDescriptions(String currentFileFormat) {
		List<String> guidList = LODUtils.searchInFreebase(
				LODConstants.LOD_SOFTWARE_BY_FORMAT_PROPERTY_ID,
				currentFileFormat, LODConstants.FB_EXTENSION,
				LODConstants.FB_GUID, LODConstants.FB_ID);

		List<String> descriptions = new ArrayList<String>();
		String guidStr = ReportConstants.EMPTYSTRING;
		Iterator<String> guidIter = guidList.iterator();
		while (guidIter.hasNext()) {
			guidStr = guidIter.next();
			String descriptionStr = LODUtils.getFreebaseFormatDescription(guidStr);
			descriptions.add(descriptionStr);
			break;
		}
		return descriptions;
	}

	/**
	 * @param currentFileFormat
	 * @return
	 */
	private static List<String> getRepositoryIds(String currentFileFormat) {
		List<String> repositoryIDs = new ArrayList<String>();
		List<String> repositoryIdList = LODUtils.searchInFreebase(
				LODConstants.LOD_SOFTWARE_BY_FORMAT_PROPERTY_ID,
				currentFileFormat, LODConstants.FB_EXTENSION,
				LODConstants.FB_ID, LODConstants.FB_ID);
		Iterator<String> repositoryIdIter = repositoryIdList.iterator();
		while (repositoryIdIter.hasNext()) {
			repositoryIDs.add(repositoryIdIter.next());
		}
		return repositoryIDs;
	}

	/**
	 * @param currentFileFormat
	 * @return
	 */
	private static String getMimeType(String currentFileFormat) {
		List<String> mimeTypeList = LODUtils.searchInFreebase(
				LODConstants.LOD_SOFTWARE_BY_FORMAT_PROPERTY_ID,
				currentFileFormat, LODConstants.FB_EXTENSION,
				LODConstants.FB_MIME_TYPE, LODConstants.FB_ID);
		return listToString(mimeTypeList);
	}

	/**
	 * @param currentFileFormat
	 * @return
	 */
	private static String getName(String currentFileFormat) {
		List<String> nameList = LODUtils.searchInFreebase(
				LODConstants.LOD_SOFTWARE_BY_FORMAT_PROPERTY_ID,
				currentFileFormat, LODConstants.FB_EXTENSION,
				LODConstants.FB_NAME, ReportConstants.EMPTYSTRING);
		return listToString(nameList);
	}

	/**
	 * @param currentFileFormat
	 * @return
	 */
	private static String getCreationDate(String currentFileFormat) {
		List<String> creationDateList = LODUtils.searchInFreebase(
				LODConstants.LOD_SOFTWARE_BY_FORMAT_PROPERTY_ID,
				currentFileFormat, LODConstants.FB_EXTENSION,
				LODConstants.FB_FORMAT_CREATION_DATE, ReportConstants.EMPTYSTRING);
		return listToString(creationDateList);
	}

	/**
	 * @param currentFileFormat
	 * @param softwareCsv
	 * @param resultList
	 * @param creationDateStr
	 * @param nameStr
	 * @param mimeTypeStr
	 * @param repositoryIDs
	 * @param genreStr
	 * @param creatorStr
	 * @param vendorCsv
	 * @return
	 */
	private static LODFormat createLodFormat(String currentFileFormat,
			StringBuffer softwareCsv, List<List<String>> resultList,
			String creationDateStr, String nameStr, String mimeTypeStr,
			List<String> repositoryIDs, String genreStr, String creatorStr,
			String vendorCsv) {
		LODFormat lodFormat = new LODFormat();
		lodFormat.setFormatName(nameStr);
		lodFormat.setCurrentVersionReleaseDate(creationDateStr);
		lodFormat.setSoftwareCount(resultList.size());
		lodFormat.setSoftware(softwareCsv.toString());
		lodFormat.setMimeType(mimeTypeStr);
		lodFormat.setFormatGenre(genreStr);
		lodFormat.setFormatCreator(creatorStr);
		lodFormat.setFileExtensions(currentFileFormat);
		lodFormat.setVendors(vendorCsv);
		if (repositoryIDs.size() > 0) {
			lodFormat.setRepositoryId(repositoryIDs.toArray(new String[repositoryIDs.size()]));
		}
		return lodFormat;
	}

	/**
	 * @param softwareCsv
	 * @param fields
	 * @param resultIter
	 * @param softwareNames
	 * @param softwareIDs
	 * @param currentFileFormat
	 */
	private static void buildFreebaseSoftware(StringBuffer softwareCsv,
			List<String> fields, Iterator<List<String>> resultIter,
			List<String> softwareNames, List<String> softwareIDs, String currentFileFormat) {
		while (resultIter.hasNext()) {
			List<String> subList = resultIter.next();
			Iterator<String> subIter = subList.iterator();
			String tmpSoftware = ReportConstants.EMPTYSTRING;
			String tmpId = ReportConstants.EMPTYSTRING;
			int index = 1;
			while (subIter.hasNext()) {
				String str = subIter.next();
				if (fields.get(index).equals(LODConstants.FB_NAME)) {
					tmpSoftware = str;
					softwareCsv.append(str);
					softwareCsv.append(ReportConstants.COMMASPACE);
				}
				tmpId = LODStatisticsFreebaseUtils.getFreebaseVendorId(fields, tmpId, index, str);
				index++;
			}
			// add software name and ID to LODSoftware object
			softwareIDs.add(tmpId);
			softwareNames.add(tmpSoftware);
			generateFreebaseSoftwareDescriptionColumn(tmpSoftware, tmpId, currentFileFormat); // FB
		}
	}

	/**
	 * @param creationDateList
	 * @return
	 */
	private static String listToString(List<String> stringList) {
		StringBuffer resStr = new StringBuffer();
		resStr.append(ReportConstants.EMPTYSTRING);
		Iterator<String> stringIter = stringList.iterator();
		while (stringIter.hasNext()) {
			resStr.append(stringIter.next());
			resStr.append(ReportConstants.COMMASPACE);
		}
		return resStr.toString();
	}

	/**
	 * @param counter
	 * @param softwareNames
	 * @param softwareIDs
	 * @param descriptions
	 * @param vendorNames
	 * @param vendorIDs
	 * @param lodFormat
	 * @param repositoryName
	 * @return
	 */
	private static int fillLODFormat(int counter, List<String> softwareNames,
			List<String> softwareIDs, List<String> descriptions,
			List<String> vendorNames, List<String> vendorIDs,
			LODFormat lodFormat, String repositoryName) {
		if (descriptions != null && descriptions.size() > 0) {
			lodFormat.setDescription(descriptions.toArray(new String[descriptions.size()]));
		}
		return fillLODFormatLinks(counter, softwareNames, softwareIDs,
				vendorNames, vendorIDs, lodFormat, repositoryName);
	}
	
	/**
	 * @param resList
	 * @return
	 */
	public static String getStringFromList(List<String> resList) {
		StringBuffer buf = new StringBuffer();
		buf.append(ReportConstants.EMPTYSTRING);
		Iterator<String> resultIter = resList.iterator();
//		log.info("Format name list: " + resList.size());
		while (resultIter.hasNext()) {
			String resStr = resultIter.next();
//			log.info("Result found: " + resStr);
			buf.append(resStr);
			buf.append(ReportConstants.COMMASPACE);
		}
		return buf.toString();
	}
	
	/**
	 * This method evaluates file format information from DBPedia linked open data repository 
	 * to store results in database in LODFormat, LODSoftware, LODVendor and DipFormatId collections.
	 */
	public static void searchDBPediaFileFormatInformation() {
		int counter = 0; // limit to test
		Iterator<DipFormatId> fileFormatIter = dipFormatIdList.iterator();
		while (fileFormatIter.hasNext()) {
			DipFormatId dipFormatId = fileFormatIter.next();
			String currentFileFormat = dipFormatId.getExtension();
			String softwareCsv = ReportConstants.EMPTYSTRING;
			int softwareCount = 0;
			String vendorsCsv = ReportConstants.EMPTYSTRING;
			
			String nameStr = LODStatisticsDBPediaUtils.getDBPediaFormatName(currentFileFormat);
			String mimeTypeStr = LODStatisticsDBPediaUtils.getDBPediaFormatMimeType(currentFileFormat);
			String versionStr = LODStatisticsDBPediaUtils.getDBPediaFormatVersion(currentFileFormat);
			String releasedStr = LODStatisticsDBPediaUtils.getDBPediaFormatReleased(currentFileFormat);
			String standardStr = LODStatisticsDBPediaUtils.getDBPediaFormatStandard(currentFileFormat);
			String descriptionStr = LODStatisticsDBPediaUtils.getDBPediaFormatDescription(currentFileFormat);
			String repositoryIdStr = LODStatisticsDBPediaUtils.getDBPediaFormatRepositoryIds(currentFileFormat);
			List<String> vendorsList = LODStatisticsDBPediaUtils
					.searchInDBPedia(
							LODConstants.LOD_SOFTWARE_BY_FORMAT_PROPERTY_ID,
							ReportConstants.POINT + currentFileFormat, RiskConstants.EXTENSION,
							RiskConstants.OWNER, false);
			List<String> vendorNames = new ArrayList<String>();
			List<String> vendorIDs = new ArrayList<String>();
			if (vendorsList != null && vendorsList.size() > 0) {
				vendorsCsv = getStringFromList(vendorsList);
				Iterator<String> vendorsIter = vendorsList.iterator();
				while (vendorsIter.hasNext()) {
					String currentVendorsName = vendorsIter.next();
					if (currentVendorsName != null && currentVendorsName.length() > 0) {
						evaluateDBPediaVendors(currentFileFormat, vendorNames,
								vendorIDs, currentVendorsName);
					}
				}
			}
			List<String> softwareList = LODStatisticsDBPediaUtils.getDBPediaSoftwareList(currentFileFormat);			
			List<String> softwareIdList = LODStatisticsDBPediaUtils.getDBPediaSoftwareIds(currentFileFormat);			
			List<String> descriptionList = LODStatisticsDBPediaUtils.getDBPediaDescriptionsList(currentFileFormat);
			
			int swCounter = 0;
			List<String> softwareNames = new ArrayList<String>();
			List<String> softwareIDs = new ArrayList<String>();
			if (softwareList != null && softwareList.size() > 0) {
				softwareCsv = getStringFromList(softwareList);
				softwareCount = softwareList.size();
				Iterator<String> softwareIter = softwareList.iterator();
				while (softwareIter.hasNext()) {
					evaluateDBPediaSoftware(currentFileFormat, softwareIdList,
							descriptionList, swCounter, softwareNames,
							softwareIDs, softwareIter);
					log.info("*** CURRENT DBPEDIA SOFTWARE COUNT *** " + swCounter);
					swCounter++;
				}
			}
			LODFormat lodFormat = evaluateDBPediaFormat(currentFileFormat,
					softwareCsv, softwareCount, vendorsCsv, nameStr,
					mimeTypeStr, versionStr, releasedStr, standardStr);
			List<String> descriptions = LODStatisticsDBPediaUtils.getDBPediaFormatDescriptions(descriptionStr);
			List<String> repositoryIdList = LODStatisticsDBPediaUtils.getDBPediaFormatDescriptions(repositoryIdStr);
			lodFormat.setRepositoryId(repositoryIdList.toArray(new String[repositoryIdList.size()]));
			counter = fillLODFormat(counter, softwareNames, softwareIDs,
					descriptions, vendorNames, vendorIDs, lodFormat, LODConstants.DBPEDIA);
//			if (counter > 5) break; // limit to test
		}
	}

	/**
	 * @param currentFileFormat
	 * @param softwareCsv
	 * @param softwareCount
	 * @param vendorsCsv
	 * @param nameStr
	 * @param mimeTypeStr
	 * @param versionStr
	 * @param releasedStr
	 * @param standardStr
	 * @return
	 */
	private static LODFormat evaluateDBPediaFormat(String currentFileFormat,
			String softwareCsv, int softwareCount, String vendorsCsv,
			String nameStr, String mimeTypeStr, String versionStr,
			String releasedStr, String standardStr) {
		LODFormat lodFormat = new LODFormat();
		LODStatisticsDBPediaUtils.getDBPediaFormatName(nameStr, lodFormat);
		LODStatisticsDBPediaUtils.getDBPediaFormatVersion(versionStr, lodFormat);
		LODStatisticsDBPediaUtils.getDBPediaFormatSoftwareCount(softwareCsv, softwareCount, lodFormat);
		LODStatisticsDBPediaUtils.getDBPediaFormatMimeType(mimeTypeStr, lodFormat);
		LODStatisticsDBPediaUtils.getDBPediaFormatCurrentFileFormat(currentFileFormat, lodFormat);
		LODStatisticsDBPediaUtils.getDBPediaFormatReleased(releasedStr, lodFormat);
		LODStatisticsDBPediaUtils.getDBPediaFormatVendors(vendorsCsv, lodFormat);
		LODStatisticsDBPediaUtils.getDBPediaFormatStandards(standardStr, lodFormat);
		return lodFormat;
	}

	/**
	 * @param currentFileFormat
	 * @param vendorNames
	 * @param vendorIDs
	 * @param currentVendorsName
	 */
	private static void evaluateDBPediaVendors(String currentFileFormat,
			List<String> vendorNames, List<String> vendorIDs,
			String currentVendorsName) {
		String numOfEmployeesStr = LODStatisticsDBPediaUtils.getDBPediaVendorNumOfEmployees(currentVendorsName);
		String homepageStr = LODStatisticsDBPediaUtils.getDBPediaVendorHomepage(currentVendorsName);
		String locationStr = LODStatisticsDBPediaUtils.getDBPediaVendorLocation(currentVendorsName);
		String revenueStr = LODStatisticsDBPediaUtils.getDBPediaVendorRevenue(currentVendorsName);
		String vendorDescriptionStr = LODStatisticsDBPediaUtils.getDBPediaVendorDescriptions(currentVendorsName);
		LODVendor lodVendor = new LODVendor();
		lodVendor.setOrganisationName(currentVendorsName);
		try {
			if (numOfEmployeesStr != null && numOfEmployeesStr.length() > 0) {
				numOfEmployeesStr = numOfEmployeesStr.replaceAll(
						ReportConstants.COMMA, ReportConstants.EMPTYSTRING)
						.replaceAll(ReportConstants.SPACE, ReportConstants.EMPTYSTRING);
				lodVendor.setNumberOfEmployees(Integer.parseInt(numOfEmployeesStr));
			}
		} catch (Exception e) {
			log.info("number of employees not numeral value error: " + e);
		}
		LODStatisticsDBPediaUtils.getDBPediaLodVendorHomepage(homepageStr, lodVendor);
		LODStatisticsDBPediaUtils.getDBPediaLodVendorCountry(locationStr, lodVendor);
		LODStatisticsDBPediaUtils.getDBPediaLodVendorRevenue(revenueStr, lodVendor);
		lodVendor.setRepository(LODConstants.DBPEDIA);
		LODStatisticsDBPediaUtils.getDBPediaLodVendorNames(currentVendorsName, lodVendor);
		LODStatisticsDBPediaUtils.getDBPediaLodVendorDescriptions(vendorDescriptionStr, lodVendor);
		setDBPediaLodVendorFileFormatList(currentFileFormat, lodVendor);
		getDBPediaLodVendorNamesAndIds(vendorNames, vendorIDs,
				currentVendorsName, lodVendor);
	}

	/**
	 * @param currentFileFormat
	 * @param softwareIdList
	 * @param descriptionList
	 * @param swCounter
	 * @param softwareNames
	 * @param softwareIDs
	 * @param softwareIter
	 */
	private static void evaluateDBPediaSoftware(String currentFileFormat,
			List<String> softwareIdList, List<String> descriptionList,
			int swCounter, List<String> softwareNames,
			List<String> softwareIDs, Iterator<String> softwareIter) {
		String currentSoftwareName = softwareIter.next();
		String os = LODStatisticsDBPediaUtils.getDBPediaSoftwareOs(currentSoftwareName);
		String programmingLanguage = LODStatisticsDBPediaUtils.getDBPediaSoftwareProgrammingLanguage(currentSoftwareName);
		String version = LODStatisticsDBPediaUtils.getDBPediaSoftwareVersion(currentSoftwareName);
		String license = LODStatisticsDBPediaUtils.getDBPediaSoftwareLicense(currentSoftwareName);
		String homepage = LODStatisticsDBPediaUtils.getDBPediaSoftwareHomepage(currentSoftwareName);
		String softwareIdStr = softwareIdList.get(swCounter);
		String swDescriptionStr = descriptionList.get(swCounter);
		LODSoftware lodSoftware = new LODSoftware();
		LODStatisticsDBPediaUtils.getDBPediaLodSoftwareName(currentSoftwareName, lodSoftware);
		LODStatisticsDBPediaUtils.getDBPediaLodSoftwareVersion(version, lodSoftware);
		LODStatisticsDBPediaUtils.getDBPediaLodSoftwareOs(os, lodSoftware);
		LODStatisticsDBPediaUtils.getDBPediaLodSoftwareProgrammingLanguage(programmingLanguage, lodSoftware);
		LODStatisticsDBPediaUtils.getDBPediaLodSoftwareLicense(license, lodSoftware);
		LODStatisticsDBPediaUtils.getDBPediaLodSoftwareHomepage(homepage, lodSoftware);
		lodSoftware.setRepository(LODConstants.DBPEDIA);
		LODStatisticsDBPediaUtils.getDBPediaLodSoftwareSoftwareIds(softwareIdStr, lodSoftware);
		LODStatisticsDBPediaUtils.getDBPediaLodSoftwareDescriptions(swDescriptionStr, lodSoftware);
		LODStatisticsDBPediaUtils.getDBPediaLodSoftwareFileForamtList(currentFileFormat, lodSoftware); 
		lodSoftwareList.add(lodSoftware);
		LODStatisticsDBPediaUtils.getDBPediaSoftwareNamesAndIds(softwareNames, softwareIDs,
				currentSoftwareName, softwareIdStr);
	}

	/**
	 * @param vendorNames
	 * @param vendorIDs
	 * @param currentVendorsName
	 * @param lodVendor
	 */
	private static void getDBPediaLodVendorNamesAndIds(
			List<String> vendorNames, List<String> vendorIDs,
			String currentVendorsName, LODVendor lodVendor) {
		if (lodVendor.getOrganisationName() != null
				&& lodVendor.getOrganisationName().length() > 0
				&& lodVendor.getRepositoryId() != null
				&& lodVendor.getRepositoryId().contains(ReportConstants.HTTP)) {
			lodVendorList.add(lodVendor);
			vendorNames.add(currentVendorsName);
			vendorIDs.add(currentVendorsName);
		}
	}

	/**
	 * @param currentFileFormat
	 * @param lodVendor
	 */
	private static void setDBPediaLodVendorFileFormatList(
			String currentFileFormat, LODVendor lodVendor) {
		if (currentFileFormat != null && currentFileFormat.length() > 0 && !currentFileFormat.equals(ReportConstants.NULL)) {
			List<String> fileFormatList = new ArrayList<String>();
			fileFormatList.add(currentFileFormat);
			if (fileFormatList.size() > 0) {
				lodVendor.setFileFormat(fileFormatList.toArray(new String[fileFormatList.size()]));
			} 
		}
	}

	/**
	 * This method evaluates file format information from Pronom linked open data repository 
	 * to store results in database in LODFormat, LODSoftware, LODVendor and DipFormatId collections.
	 */
	public static void searchPronomFileFormatInformation() {
		int counter = 0; // limit to test
		List<LODPronomSoftware> softwareList = LODUtils.retrievePronomSoftwareInfo();
		Iterator<LODPronomSoftware> softwareListIter = softwareList.iterator();
		initPronomLodSoftware(softwareListIter);

		Iterator<DipFormatId> fileFormatIter = dipFormatIdList.iterator();
		while (fileFormatIter.hasNext()) {
			DipFormatId dipFormatId = fileFormatIter.next();
			String currentFileFormat = dipFormatId.getExtension();
			StringBuffer softwareCsv = new StringBuffer();
			softwareCsv.append(ReportConstants.EMPTYSTRING);
			StringBuffer vendorCsv = new StringBuffer();
			vendorCsv.append(ReportConstants.EMPTYSTRING);
			int softwareCount = 0;
			String nameStr = LODStatisticsPronomUtils.getStringFromPronomList(currentFileFormat, LODConstants.PRONOM_NAME);
//			String extStr = getStringFromPronomList(currentFileFormat, LODConstants.PRONOM_EXT);
			String verStr = LODStatisticsPronomUtils.getStringFromPronomList(currentFileFormat, LODConstants.PRONOM_VER);	
			List<String> exclusions = new ArrayList<String>();
			exclusions.add(LODConstants.EMPTY_PUID);
			String puidStr = LODStatisticsPronomUtils.getStringFromPronomListExt(currentFileFormat, LODConstants.PRONOM_PUID, exclusions);
			counter = initPronomFormats(counter, softwareList,
					currentFileFormat, softwareCsv, vendorCsv, softwareCount,
					nameStr, verStr, puidStr);
//			if (counter > 5) break; // limit to test
		}
	}

	/**
	 * @param counter
	 * @param softwareList
	 * @param currentFileFormat
	 * @param softwareCsv
	 * @param vendorCsv
	 * @param softwareCount
	 * @param nameStr
	 * @param verStr
	 * @param puidStr
	 * @return
	 */
	private static int initPronomFormats(int counter,
			List<LODPronomSoftware> softwareList, String currentFileFormat,
			StringBuffer softwareCsv, StringBuffer vendorCsv,
			int softwareCount, String nameStr, String verStr, String puidStr) {
		int counterRes = counter;
		int softwareCountRes = softwareCount;
		List<String> softwareNames = new ArrayList<String>();
		List<String> softwareIDs = new ArrayList<String>();
		List<String> vendorNames = new ArrayList<String>();
		List<String> vendorIDs = new ArrayList<String>();
		Iterator<LODPronomSoftware> resultIter = softwareList.iterator();
		log.info("software list size: " + softwareList.size());
		while (resultIter.hasNext()) {
			LODPronomSoftware software = resultIter.next();
			retrievePronomVendors(currentFileFormat, vendorCsv, vendorNames, vendorIDs, software);
			softwareCountRes = LODStatisticsPronomUtils.retrievePronomSoftwareIdsAndNames(
					currentFileFormat, softwareCsv, softwareCountRes,
					softwareNames, softwareIDs, software);
		}
		LODFormat lodFormat = LODStatisticsPronomUtils.setPronomLodFormat(currentFileFormat,
				softwareCsv, vendorCsv, softwareCountRes, nameStr, verStr,
				puidStr);
		counterRes = fillLODFormatLinks(counterRes, softwareNames, softwareIDs,
				vendorNames, vendorIDs, lodFormat, LODConstants.PRONOM);
		return counterRes;
	}

	/**
	 * @param softwareListIter
	 */
	private static void initPronomLodSoftware(
			Iterator<LODPronomSoftware> softwareListIter) {
		int swCounter = 0;
		while (softwareListIter.hasNext()) {
			LODPronomSoftware software = softwareListIter.next();
			LODSoftware lodSoftware = new LODSoftware();
			LODStatisticsPronomUtils.setPronomLodSoftware(software, lodSoftware);
			lodSoftwareList.add(lodSoftware);
			log.info("*** CURRENT PRONOM SOFTWARE COUNT *** " + swCounter);
			swCounter++;
		}
	}

	/**
	 * @param currentFileFormat
	 * @param vendorCsv
	 * @param vendorNames
	 * @param vendorIDs
	 * @param software
	 */
	private static void retrievePronomVendors(String currentFileFormat,
			StringBuffer vendorCsv, List<String> vendorNames,
			List<String> vendorIDs, LODPronomSoftware software) {
		if (software.getVendorsList() != null && software.getVendorsList().size() > 0) {
			log.info("Vendors: " + software.getVendorsList()); 
			Iterator<String> vendorIter = software.getVendorsList().iterator();
			int index = 0;
			while (vendorIter.hasNext()) {
				String currentVendor = vendorIter.next();
				if (!vendorDescriptionCsv.contains(currentVendor)) {
					vendorDescriptionCsv = vendorDescriptionCsv + 
							currentVendor
							+ ReportConstants.SEMICOLON
							+ ReportConstants.SEMICOLON
							+ ReportConstants.SEMICOLON
							+ ReportConstants.SEMICOLON
							+ ReportConstants.SEMICOLON
							+ ReportConstants.SEMICOLON
							+ ReportConstants.SEMICOLON
							+ ReportConstants.SEMICOLON
							+ ReportConstants.SEMICOLON + ReportConstants.LINEEND;
					vendorCsv.append(currentVendor);
					vendorCsv.append(ReportConstants.COMMASPACE);
					LODVendor lodVendor = new LODVendor();
					lodVendor.setOrganisationName(currentVendor);
					lodVendor.setRepositoryId(software.getVendorsIdList().get(index));
					lodVendor.setRepository(LODConstants.PRONOM);
					setDBPediaLodVendorFileFormatList(
							currentFileFormat, lodVendor);
					lodVendorList.add(lodVendor);
					vendorNames.add(currentVendor);
					vendorIDs.add(software.getVendorsIdList().get(index));
					log.info("*** CURRENT PRONOM VENDOR COUNT *** " + index);
					index++;
				}
			}
		}
	}

	/**
	 * @param counter
	 * @param softwareNames
	 * @param softwareIDs
	 * @param vendorNames
	 * @param vendorIDs
	 * @param lodFormat
	 * @param repositoryName
	 * @return
	 */
	private static int fillLODFormatLinks(int counter,
			List<String> softwareNames, List<String> softwareIDs,
			List<String> vendorNames, List<String> vendorIDs,
			LODFormat lodFormat, String repositoryName) {
		int counterRes = counter;
		lodFormat.setRepository(repositoryName);
		if (softwareNames != null && softwareNames.size() > 0) {
			lodFormat.setSoftwareName(softwareNames.toArray(new String[softwareNames.size()]));
		}
		if (softwareIDs != null && softwareIDs.size() > 0) {
			lodFormat.setSoftwareId(softwareIDs.toArray(new String[softwareIDs.size()]));
		}
		if (vendorNames != null && vendorNames.size() > 0) {
			lodFormat.setVendorName(vendorNames.toArray(new String[vendorNames.size()]));
		}
		if (vendorIDs != null && vendorIDs.size() > 0) {
			lodFormat.setVendorId(vendorIDs.toArray(new String[vendorIDs.size()]));
		}
		lodFormatList.add(lodFormat);
		log.info("*** CURRENT " + repositoryName + " FORMAT COUNT *** " + counterRes);
		counterRes++;
		return counterRes;
	}
	
	/**
	 * This method evaluates all file formats information from AIT to store it in 
	 * AitFileFormat collection
	 * @return AitFileFormat objects list
	 */
	public static List<FfmaDomainObject> searchAllFileFormatsInAIT() {
		List<LODFormat> tmpLodFormatList = new ArrayList<LODFormat>();
		List<FfmaDomainObject> FfmaDomainObjectsList = new ArrayList<FfmaDomainObject>();
		List<String> softwareNames = new ArrayList<String>();
		List<String> softwareIDs = new ArrayList<String>();
		List<String> vendorNames = new ArrayList<String>();
		List<String> vendorIDs = new ArrayList<String>();
		int formatsCount = 0;
		int softwareCount = 0;
		int vendorsCount = 0;
		List<String> formatsRows = LODStatisticsAitUtils.retrieveRows(LODConstants.CSV_FILE_FORMAT_HEADER);
		List<String> softwareRows = LODStatisticsAitUtils.retrieveRows(LODConstants.CSV_SOFTWARE_HEADER);
		List<String> vendorsRows = LODStatisticsAitUtils.retrieveRows(LODConstants.CSV_VENDOR_HEADER);
		retrieveAitSoftware(softwareNames, softwareIDs, softwareCount, formatsRows, softwareRows);
		retrieveAitVendors(vendorNames, vendorIDs, vendorsCount, formatsRows, vendorsRows);
		LODStatisticsAitUtils.retrieveAitFormats(tmpLodFormatList, formatsCount, formatsRows);
		List<AitFileFormat> aitFileFormatList = LODStatisticsAitUtils.createAitLodFormat(tmpLodFormatList, lodFormatList);		
		LODStatisticsAitUtils.insertFileFormatsForAit(FfmaDomainObjectsList, aitFileFormatList);
		log.info("Ait File formats count: " + FfmaDomainObjectsList.size());
		return FfmaDomainObjectsList;
	}

	/**
	 * @param softwareNames
	 * @param softwareIDs
	 * @param softwareCount
	 * @param formatsRows
	 * @param softwareRows
	 */
	private static void retrieveAitSoftware(List<String> softwareNames,
			List<String> softwareIDs, int softwareCount,
			List<String> formatsRows, List<String> softwareRows) {
		int softwareCountRes = softwareCount;
		if (softwareRows != null && softwareRows.size() > 0) {
			Iterator<String> softwareIter = softwareRows.iterator();
			while (softwareIter.hasNext()) {
				List<String> softwareStr = Arrays.asList(softwareIter.next().split(ReportConstants.SEMICOLON));
				if (softwareStr != null && softwareStr.size() > 0) {
					LODSoftware lodSoftware = LODStatisticsAitUtils.createLodSoftwareForAit(
							softwareNames, softwareIDs, formatsRows, softwareStr); 
					lodSoftwareList.add(lodSoftware);
					log.info("*** CURRENT AIT SOFTWARE COUNT *** " + softwareCountRes);
					softwareCountRes++;
				}
			}				
		}
	}

	/**
	 * @param vendorNames
	 * @param vendorIDs
	 * @param vendorsCount
	 * @param formatsRows
	 * @param vendorsRows
	 */
	private static void retrieveAitVendors(List<String> vendorNames,
			List<String> vendorIDs, int vendorsCount, List<String> formatsRows,
			List<String> vendorsRows) {
		int vendorsCountRes = vendorsCount;
		if (vendorsRows != null && vendorsRows.size() > 0) {
			Iterator<String> vendorsIter = vendorsRows.iterator();
			while (vendorsIter.hasNext()) {
				List<String> vendorsStr = Arrays.asList(vendorsIter.next().split(ReportConstants.SEMICOLON));
				if (vendorsStr != null && vendorsStr.size() > 0) {
					LODVendor lodVendor = new LODVendor();
					createAitLodVendor(vendorNames, vendorIDs, formatsRows,
							vendorsStr, lodVendor); 
					lodVendorList.add(lodVendor);
					log.info("*** CURRENT AIT VENDORS COUNT *** " + vendorsCountRes);
					vendorsCountRes++;
				}
			}				
		}
	}

	/**
	 * @param vendorNames
	 * @param vendorIDs
	 * @param formatsRows
	 * @param vendorsStr
	 * @param lodVendor
	 */
	private static void createAitLodVendor(List<String> vendorNames,
			List<String> vendorIDs, List<String> formatsRows,
			List<String> vendorsStr, LODVendor lodVendor) {
		LODStatisticsAitUtils.getAitVendorName(vendorNames, vendorIDs, vendorsStr, lodVendor);
		LODStatisticsAitUtils.getAitVendorNumOfEmployees(vendorsStr, lodVendor);
		String businessStatus = vendorsStr.get(LODConstants.VendorsCsvTypes.BusinessStatus.ordinal());
		LODStatisticsDBPediaUtils.getDBPediaLodVendorRevenue(businessStatus, lodVendor);
		LODStatisticsAitUtils.getAitVendorCurrentFfma(vendorsStr, lodVendor);
		LODStatisticsAitUtils.getAitVendorStockIssues(vendorsStr, lodVendor);
		LODStatisticsAitUtils.getAitVendorRankedList(vendorsStr, lodVendor);
		String country = vendorsStr.get(LODConstants.VendorsCsvTypes.Country.ordinal());
		LODStatisticsDBPediaUtils.getDBPediaLodVendorCountry(country, lodVendor);
		LODStatisticsAitUtils.getAitVendorFoundationDate(vendorsStr, lodVendor);
		String homepage = vendorsStr.get(LODConstants.VendorsCsvTypes.Homepage.ordinal());
		LODStatisticsDBPediaUtils.getDBPediaLodVendorHomepage(homepage, lodVendor);
		lodVendor.setRepository(LODConstants.LODRepositories.AIT.name());
		LODStatisticsAitUtils.getAitVendorFormatList(formatsRows, lodVendor);
	}

	/**
	 * This method checks the existing array and add new passed value to this array
	 * @param arr
	 * @param value
	 * @return extended array with new value
	 */
	public static String[] addValueToStringArray(String[] arr, String value) {
		List<String> existingValues = new ArrayList<String>();
		if (arr != null && arr.length > 0 && arr[0] != null) { 
			if (!arr[0].equals(ReportConstants.DIP_NULL) && !arr[0].equals(ReportConstants.SPACE)) {			
				existingValues.addAll(Arrays.asList(arr));
				List<String> emptyStrings = new ArrayList<String>();
				emptyStrings.add(ReportConstants.SPACE);
				existingValues.removeAll(emptyStrings);
			}
		}
		extractExistingValues(value, existingValues);
		return existingValues.toArray(new String[existingValues.size()]);
	}

	/**
	 * @param value
	 * @param existingValues
	 */
	private static void extractExistingValues(String value,
			List<String> existingValues) {
		if (existingValues != null && value != null && value.length() > 0 && !value.equals(ReportConstants.DIP_NULL)) {
			if (!existingValues.contains(value)) {
				try {
					existingValues.add(value);
				} catch (Exception e) {
					log.info("Unsupported operation exception: " + e);
				}
			}
		}
	}
	
	/**
	 * This method evaluates all file formats information from FREEBASE to store it in 
	 * FreebaseFileFormat collection
	 * @return FreebaseFileFormat objects list
	 */
	public static List<FfmaDomainObject> searchAllFileFormatsInFreebase() {
		List<FfmaDomainObject> FfmaDomainObjectsList = new ArrayList<FfmaDomainObject>();
		List<FreebaseFileFormat> freebaseFileFormatList = LODUtils.searchAllFreebaseFileFormats();
		Iterator<FreebaseFileFormat> resultIter = freebaseFileFormatList.iterator();
		while (resultIter.hasNext()) {
			FreebaseFileFormat fileFormatEntry = resultIter.next();
			log.info(ReportConstants.FILE_FORMAT_ENTRY + fileFormatEntry);
			FfmaDomainObjectsList.add((FfmaDomainObject) fileFormatEntry);
		}
		log.info("Freebase File formats count: " + FfmaDomainObjectsList.size());
		return FfmaDomainObjectsList;
	}

	/**
	 * This method evaluates all file formats information from Pronom to store it in 
	 * PronomFileFormat collection
	 * @return PronomFileFormat objects list
	 */
	public static List<FfmaDomainObject> searchAllFileFormatsInPronom() {
		List<FfmaDomainObject> FfmaDomainObjectsList = new ArrayList<FfmaDomainObject>();
		List<PronomFileFormat> pronomFileFormatList = LODUtils.searchAllPronomFileFormats();
		Iterator<PronomFileFormat> resultIter = pronomFileFormatList.iterator();
		while (resultIter.hasNext()) {
			PronomFileFormat fileFormatEntry = resultIter.next();
			log.info(ReportConstants.FILE_FORMAT_ENTRY + fileFormatEntry);
			FfmaDomainObjectsList.add((FfmaDomainObject) fileFormatEntry);
		}
		log.info("Pronom File formats count: " + FfmaDomainObjectsList.size());
		return FfmaDomainObjectsList;
	}

	/**
	 * This method evaluates all file formats information from DBPedia to store it in 
	 * DBPediaFileFormat collection
	 * @return DBPediaFileFormat objects list
	 */
	public static List<FfmaDomainObject> searchAllFileFormatsInDBPedia() {
		List<FfmaDomainObject> FfmaDomainObjectsList = new ArrayList<FfmaDomainObject>();
		List<DBPediaFileFormat> dbPediaFileFormatList = LODUtils.searchAllDBPediaFileFormats();
		Iterator<DBPediaFileFormat> resultIter = dbPediaFileFormatList.iterator();
		while (resultIter.hasNext()) {
			DBPediaFileFormat fileFormatEntry = resultIter.next();
			log.info(ReportConstants.FILE_FORMAT_ENTRY + fileFormatEntry);
			FfmaDomainObjectsList.add((FfmaDomainObject) fileFormatEntry);
		}
		log.info("DBPedia File formats count: " + FfmaDomainObjectsList.size());
		return FfmaDomainObjectsList;
	}

}
