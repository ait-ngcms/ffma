package ait.ffma.service.preservation.riskmanagement.api.lod;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import ait.ffma.domain.FfmaDomainObject;
import ait.ffma.domain.preservation.riskmanagement.DipFormatId;
import ait.ffma.domain.preservation.riskmanagement.FreebaseFileFormat;
import ait.ffma.domain.preservation.riskmanagement.LODSoftware;
import ait.ffma.domain.preservation.riskmanagement.LODVendor;
import ait.ffma.service.preservation.riskmanagement.api.riskanalysis.risk.ReportConstants;

/**
 * This is a helper class for Freebase LOD statistics calculations
 */
public final class LODStatisticsFreebaseUtils {

	/**
	 * To prevent initialization 
	 */
	private LODStatisticsFreebaseUtils() {} 

	/**
	 * Logger
	 */
	private static Logger log = Logger.getLogger(LODStatisticsFreebaseUtils.class.getName());


	/**
	 * This method retrieves extension list from FREEBASE
	 * @param fileFormatsList
	 * @return
	 */
	public static List<DipFormatId> getFreebaseDipFileFormatsList(List<? extends FfmaDomainObject> fileFormatsList) {
		List<DipFormatId> res = new ArrayList<DipFormatId>();
		
		Iterator<? extends FfmaDomainObject> iter = fileFormatsList.iterator();
		while (iter.hasNext()) {
			FreebaseFileFormat freebaseFileFormatObject = (FreebaseFileFormat) iter.next();
			log.info("retrieved freebaseFileFormat object: " + freebaseFileFormatObject);
			if (freebaseFileFormatObject != null
					&& freebaseFileFormatObject.getExtension() != null
					&& !LODUtils.checkDipFormatIdsForExtension(res,
							freebaseFileFormatObject.getExtension())) {
				String currentFileFormat = freebaseFileFormatObject.getExtension();
				DipFormatId dipFormatId = new DipFormatId();
				dipFormatId.setDipId(LODConstants.DIP + currentFileFormat);
				dipFormatId.setExtension(currentFileFormat);
				List<String> idList = new ArrayList<String>();
				String puid = freebaseFileFormatObject.getPuid();
				if (puid != null && puid.length() > 0) {
					idList.add(puid);
					dipFormatId.setFreebaseId(idList.toArray(new String[idList.size()]));
				}
				String description = freebaseFileFormatObject.getDescription();
				LODUtils.addDescriptionList(dipFormatId, puid, description,
						LODConstants.FREEBASE_BEGIN, LODConstants.FREEBASE_END);
				res.add(dipFormatId);
			}
		}
		return res;
	}

	/**
	 * @param softwareName
	 * @param lodVendor
	 */
	public static void setFreebaseLodVendorSoftwareName(String softwareName,
			LODVendor lodVendor) {
		if (softwareName != null && softwareName.length() > 0 && !softwareName.equals(ReportConstants.NULL)) {
			lodVendor.setSoftwareName(softwareName);
		}
	}

	/**
	 * @param fileFormat
	 * @param lodVendor
	 */
	public static void setFreebaseLodVendorFileFormat(String fileFormat,
			LODVendor lodVendor) {
		if (fileFormat != null && fileFormat.length() > 0 && !fileFormat.equals(ReportConstants.NULL)) {
			List<String> fileFormatList = new ArrayList<String>();
			fileFormatList.add(fileFormat);
			if (fileFormatList.size() > 0) {
				lodVendor.setFileFormat(fileFormatList.toArray(new String[fileFormatList.size()]));
			} 
//			lodVendor.setFileFormat(fileFormat);
		}
	}

	/**
	 * @param vendorId
	 * @param vendorGuid
	 * @param lodVendor
	 */
	public static void setFreebaseLodVendorDescriptionAndId(String vendorId,
			String vendorGuid, LODVendor lodVendor) {
		List<String> descriptions = new ArrayList<String>();
		descriptions.add(LODUtils.getFreebaseFormatDescription(vendorGuid));

		if (descriptions.size() > 0) {
			lodVendor.setDescription(descriptions.get(0));
		}
		lodVendor.setRepositoryId(vendorId);
	}

	/**
	 * @param vendorId
	 * @param res
	 * @param lodVendor
	 * @return
	 */
	public static String setFreebaseLodVendorFoundationDate(String vendorId,
			String resStr, LODVendor lodVendor) {
		String res = resStr;
		res = res + ReportConstants.SEMICOLON; 
		List<String> foundationDateFields = Arrays.asList(LODConstants.FB_VENDOR_FOUNDATION_DATE, ReportConstants.EMPTYSTRING);
		String foundationDate = retrieveSimpleObjectFromRepository(foundationDateFields,
				LODConstants.LOD_VENDOR_FOUNDATION_DATE_PROPERTY_ID,
				vendorId);
		res = res + foundationDate + ReportConstants.SEMICOLON;
		lodVendor.setFoundationDate(foundationDate);
		return res;
	}

	/**
	 * @param vendorId
	 * @param res
	 * @param lodVendor
	 * @return
	 */
	public static String setFreebaseLodVendorRankedList(String vendorId,
			String resStr, LODVendor lodVendor) {
		String res = resStr;
		List<String> rankedListFields = Arrays.asList(
				LODConstants.FB_VENDOR_RANKED_LIST, LODConstants.FB_LIST,
				LODConstants.FB_RANK, LODConstants.FB_YEAR);
		String rankedList = retrieveComplexObjectFromRepositoryInRow(rankedListFields,
				LODConstants.LOD_VENDOR_RANKED_LIST_PROPERTY_ID,
				vendorId);
		res = res + rankedList + ReportConstants.SEMICOLON;
		lodVendor.setRankedList(rankedList);
		return res;
	}

	/**
	 * @param vendorId
	 * @param res
	 * @param lodVendor
	 * @return
	 */
	public static String setFreebaseLodVendorStockIssues(String vendorId,
			String resStr, LODVendor lodVendor) {
		String res = resStr;
		List<String> stockIssuesFields = Arrays.asList(
				LODConstants.FB_VENDOR_STOCK_ISSUES, LODConstants.FB_NAME);
		String stockIssues = retrieveComplexObjectFromRepositoryInRow(stockIssuesFields,
				LODConstants.LOD_VENDOR_STOCK_ISSUES_PROPERTY_ID,
				vendorId);
		res = res + stockIssues + ReportConstants.SEMICOLON;
		lodVendor.setStockIssues(stockIssues);
		return res;
	}

	/**
	 * @param vendorId
	 * @param res
	 * @param lodVendor
	 * @return
	 */
	public static String setFreebaseLodVendorCurrentFfma(String vendorId,
			String resStr, LODVendor lodVendor) {
		String res = resStr;
		List<String> currentAsssetsFields = Arrays.asList(
				LODConstants.FB_VENDOR_CURRENT_FFMA, LODConstants.FB_AMOUNT,
				LODConstants.FB_CURRENCY, LODConstants.FB_VALID_DATE);
		String currentFfma = retrieveComplexObjectFromRepositoryInRow(currentAsssetsFields,
				LODConstants.LOD_VENDOR_CURRENT_FFMA_PROPERTY_ID,
				vendorId);
		res = res + currentFfma + ReportConstants.SEMICOLON;
		lodVendor.setCurrentFfma(currentFfma);
		return res;
	}

	/**
	 * @param vendorId
	 * @param res
	 * @param lodVendor
	 * @return
	 */
	public static String setFreebaseLodVendorBusinessStatus(String vendorId,
			String resStr, LODVendor lodVendor) {
		String res = resStr;
		List<String> businessStatusFields = Arrays.asList(
				LODConstants.FB_VENDOR_REVENUE, LODConstants.FB_AMOUNT,
				LODConstants.FB_CURRENCY, LODConstants.FB_VALID_DATE);
		String businessStatus = retrieveComplexObjectFromRepositoryInRow(businessStatusFields,
				LODConstants.LOD_VENDOR_BUSINESS_STATUS_PROPERTY_ID,
				vendorId);
		res = res + businessStatus + ReportConstants.SEMICOLON;
		lodVendor.setBusinessStatus(businessStatus);
		return res;
	}

	/**
	 * @param vendorId
	 * @param res
	 * @param lodVendor
	 * @return
	 */
	public static String setFreebaseLodVendorNumOfEmployees(String vendorId,
			String resStr, LODVendor lodVendor) {
		String res = resStr;
		List<String> numOfEmployeesFields = Arrays.asList(
				LODConstants.FB_VENDOR_NUM_OF_EMPLOYEES, LODConstants.FB_NUMBER, LODConstants.FB_YEAR);
		String numOfEmployees = retrieveComplexObjectFromRepositoryInRow(numOfEmployeesFields,
				LODConstants.LOD_VENDOR_NUM_OF_EMPLOYEES_PROPERTY_ID,
				vendorId);
		res = res + numOfEmployees + ReportConstants.SEMICOLON;
		try {
			if (numOfEmployees != null && numOfEmployees.length() > 0) {
				lodVendor.setNumberOfEmployees(Integer.parseInt(numOfEmployees));
			}
		} catch (Exception e) {
			log.info("number of employees not numeral value error: " + e);
		}
		return res;
	}
	
	/**
	 * This method retrieves encapsulated information from complex LOD object.
	 * @param fields
	 *        The field names in JSON object
	 * @param propertyId
	 * @param repositoryId
	 * @return CSV information for one JSON object
	 */
	public static String retrieveComplexObjectFromRepositoryInRow(List<String> fields, String propertyId, String repositoryId) {
		StringBuffer buf = new StringBuffer();
		buf.append(ReportConstants.EMPTYSTRING);
		LODUtils.initCalculationModel();
		List<List<String>> resultList = LODUtils.searchInFreebaseExt(
				propertyId,
				repositoryId, LODConstants.FB_ID,
				fields);
		Iterator<List<String>> resultIter = resultList.iterator();
		while (resultIter.hasNext()) {
			List<String> subList = resultIter.next();
			Iterator<String> subIter = subList.iterator();
			int index = 1;
			while (subIter.hasNext()) {
				String str = subIter.next();
				buf.append(fields.get(index));
				buf.append(ReportConstants.DBLPOINTSPACE);
				buf.append(str);
				buf.append(ReportConstants.COMMASPACE);
				index++;
			}
			buf.append(ReportConstants.ROUTE);
		}
		return buf.toString();
	}		

	/**
	 * @param fields
	 * @param tmpVendorGuid
	 * @param index
	 * @param str
	 * @return
	 */
	public static String getFreebaseVendorGuid(List<String> fields,
			String tmpVendorGuid, int index, String str) {
		String tmpVendorGuidRes = tmpVendorGuid;
		if (fields.get(index).equals(LODConstants.FB_GUID)) {
			tmpVendorGuidRes = str;
		}
		return tmpVendorGuidRes;
	}

	/**
	 * @param fields
	 * @param tmpVendorId
	 * @param index
	 * @param str
	 * @return
	 */
	public static String getFreebaseVendorId(List<String> fields,
			String tmpVendorId, int index, String str) {
		String tmpVendorIdRes = tmpVendorId;
		if (fields.get(index).equals(LODConstants.FB_ID)) {
			tmpVendorIdRes = str;
		}
		return tmpVendorIdRes;
	}

	/**
	 * @param softwareId
	 * @param fields
	 * @return
	 */
	public static List<List<String>> getFreebaseVendorsForSoftware(
			String softwareId, List<String> fields) {
		List<List<String>> resultList = LODUtils.searchInFreebaseExt(
				LODConstants.LOD_VENDOR_BY_SOFTWARE_PROPERTY_ID,
				softwareId, LODConstants.FB_ID,
				fields);
		return resultList;
	}

	/**
	 * @param fileFormat
	 * @param lodSoftware
	 */
	public static void setFreebaseLodSoftwareFileFormat(String fileFormat,
			LODSoftware lodSoftware) {
		if (fileFormat != null && fileFormat.length() > 0 && !fileFormat.equals(ReportConstants.NULL)) {
			List<String> fileFormatList = new ArrayList<String>();
			fileFormatList.add(fileFormat);
			if (fileFormatList.size() > 0) {
				lodSoftware.setFileFormat(fileFormatList.toArray(new String[fileFormatList.size()]));
			} 
//			lodSoftware.setFileFormat(fileFormat);
		}
	}

	/**
	 * @param softwareId
	 * @param lodSoftware
	 */
	public static void setFreebaseLodSoftwareRepGenre(String softwareId,
			LODSoftware lodSoftware) {
		if (lodSoftware.getGenre().length() == 0) {
			List<String> genreList2 = LODUtils.searchInFreebase(
					LODConstants.LOD_SOFTWARE_BY_REPOSITORY_ID,
					softwareId, LODConstants.FB_ID,
					LODConstants.FB_GENRE, ReportConstants.EMPTYSTRING);
			StringBuffer genreStr = new StringBuffer();
			genreStr.append(ReportConstants.EMPTYSTRING);
			Iterator<String> genreIter2 = genreList2.iterator();
			while (genreIter2.hasNext()) {
				genreStr.append(genreIter2.next());
				genreStr.append(ReportConstants.COMMASPACE);
			}
			lodSoftware.setGenre(genreStr.toString());
		}
	}

	/**
	 * @param softwareId
	 * @param lodSoftware
	 */
	public static void setFreebaseLodSoftwareSwLicense(String softwareId,
			LODSoftware lodSoftware) {
		if (lodSoftware.getSoftwareLicense().length() == 0) {
			List<String> licenseList2 = LODUtils.searchInFreebase(
					LODConstants.LOD_SOFTWARE_BY_REPOSITORY_ID,
					softwareId, LODConstants.FB_ID,
					LODConstants.FB_LICENSE, ReportConstants.EMPTYSTRING);
			StringBuffer licenseStr = new StringBuffer();
			licenseStr.append(ReportConstants.EMPTYSTRING);
			Iterator<String> licenseIter2 = licenseList2.iterator();
			while (licenseIter2.hasNext()) {
				licenseStr.append(licenseIter2.next());
				licenseStr.append(ReportConstants.COMMASPACE);
			}
			lodSoftware.setSoftwareLicense(licenseStr.toString());
		}
	}

	/**
	 * @param softwareId
	 * @param lodSoftware
	 */
	public static void setFreebaseLodSoftwareLatestVersion(String softwareId,
			LODSoftware lodSoftware) {
		if (lodSoftware.getSoftwareLatestVersion().length() == 0) {
			List<String> latestVersionList2 = LODUtils.searchInFreebase(
					LODConstants.LOD_SOFTWARE_BY_REPOSITORY_ID,
					softwareId, LODConstants.FB_ID,
					LODConstants.FB_LATEST_VERSION, ReportConstants.EMPTYSTRING);
			StringBuffer latestVersionStr = new StringBuffer();
			latestVersionStr.append(ReportConstants.EMPTYSTRING);
			Iterator<String> latestVersionIter2 = latestVersionList2.iterator();
			while (latestVersionIter2.hasNext()) {
				latestVersionStr.append(latestVersionIter2.next());
				latestVersionStr.append(ReportConstants.COMMASPACE);
			}
			lodSoftware.setSoftwareLatestVersion(latestVersionStr.toString());
		}
	}

	/**
	 * @param softwareId
	 * @param lodSoftware
	 */
	public static void setFreebaseLodSoftwareLatestRelease(String softwareId,
			LODSoftware lodSoftware) {
		if (lodSoftware.getSoftwareReleaseDate().length() == 0) {
			List<String> latestReleaseDateList2 = LODUtils.searchInFreebase(
					LODConstants.LOD_SOFTWARE_BY_REPOSITORY_ID,
					softwareId, LODConstants.FB_ID,
					LODConstants.FB_LATEST_RELEASE_DATE, ReportConstants.EMPTYSTRING);
			StringBuffer latestReleaseDateStr = new StringBuffer();
			latestReleaseDateStr.append(ReportConstants.EMPTYSTRING);
			Iterator<String> latestReleaseDateIter2 = latestReleaseDateList2.iterator();
			while (latestReleaseDateIter2.hasNext()) {
				latestReleaseDateStr.append(latestReleaseDateIter2.next());
				latestReleaseDateStr.append(ReportConstants.COMMASPACE);
			}
			lodSoftware.setSoftwareReleaseDate(latestReleaseDateStr.toString());
		}
	}

	/**
	 * @param softwareId
	 * @param lodSoftware
	 */
	public static void setFreebaseLodSoftwareDescriptionAndId(
			String softwareId, LODSoftware lodSoftware) {
		List<String> guidList = LODUtils.searchInFreebase(
				LODConstants.LOD_SOFTWARE_BY_REPOSITORY_ID,
				softwareId, LODConstants.FB_ID,
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

		if (descriptions.size() > 0) {
			lodSoftware.setDescription(descriptions.get(0));
		}
		lodSoftware.setRepositoryId(softwareId);
	}

	/**
	 * @param softwareId
	 * @param res
	 * @param lodSoftware
	 * @return
	 */
	public static String setFreebaseLodSoftwareReleaseDate(String softwareId,
			String resStr, LODSoftware lodSoftware) {
		String res = resStr;
		List<String> latestReleaseDateList = LODUtils.searchInFreebase(
				LODConstants.LOD_SOFTWARE_LATEST_RELEASE_DATE_PROPERTY_ID,
				softwareId, LODConstants.FB_ID,
				LODConstants.FB_SOFTWARE_LATEST_RELEASE_DATE, ReportConstants.EMPTYSTRING);

		StringBuffer latestReleaseDateStr = new StringBuffer();
		latestReleaseDateStr.append(ReportConstants.EMPTYSTRING);
		Iterator<String> latestReleaseDateIter = latestReleaseDateList.iterator();
		while (latestReleaseDateIter.hasNext()) {
			latestReleaseDateStr.append(latestReleaseDateIter.next());
			latestReleaseDateStr.append(ReportConstants.COMMASPACE);
		}
		res = res + latestReleaseDateStr.toString() + ReportConstants.SEMICOLON;
		lodSoftware.setSoftwareReleaseDate(latestReleaseDateStr.toString());
		return res;
	}

	/**
	 * @param softwareId
	 * @param res
	 * @param lodSoftware
	 * @return
	 */
	public static String setFreebaseLodSoftwareVersion(String softwareId,
			String resStr, LODSoftware lodSoftware) {
		String res = resStr;
		List<String> versionList = LODUtils.searchInFreebase(
				LODConstants.LOD_SOFTWARE_LATEST_VERSION_PROPERTY_ID,
				softwareId, LODConstants.FB_ID,
				LODConstants.FB_SOFTWARE_LATEST_VERSION, ReportConstants.EMPTYSTRING);

		StringBuffer versionStr = new StringBuffer();
		versionStr.append(ReportConstants.EMPTYSTRING);
		Iterator<String> versionIter = versionList.iterator();
		while (versionIter.hasNext()) {
			versionStr.append(versionIter.next());
			versionStr.append(ReportConstants.COMMASPACE);
		}
		res = res + versionStr.toString() + ReportConstants.SEMICOLON;
		lodSoftware.setSoftwareLatestVersion(versionStr.toString());
		return res;
	}

	/**
	 * @param softwareId
	 * @param res
	 * @param lodSoftware
	 * @return
	 */
	public static String setFreebaseLodSoftwareProgrammingLanguage(
			String softwareId, String resStr, LODSoftware lodSoftware) {
		String res = resStr;
		List<String> programmingLanguageFields = Arrays.asList(LODConstants.FB_SOFTWARE_PROGRAMMING_LANGUAGE, ReportConstants.EMPTYSTRING);
		String swProgrammingLanguage = retrieveSimpleObjectFromRepository(programmingLanguageFields,
				LODConstants.LOD_SOFTWARE_PROGRAMMING_LANGUAGE_PROPERTY_ID,
				softwareId);
		res = res + swProgrammingLanguage + ReportConstants.SEMICOLON;
		lodSoftware.setProgrammingLanguage(swProgrammingLanguage);
		return res;
	}

	/**
	 * @param softwareId
	 * @param res
	 * @param lodSoftware
	 * @return
	 */
	public static String setFreebaseLodSoftwareProtocol(String softwareId,
			String resStr, LODSoftware lodSoftware) {
		String res = resStr;
		List<String> protocolFields = Arrays.asList(LODConstants.FB_SOFTWARE_PROTOCOL, ReportConstants.EMPTYSTRING);
		String swProtocol = retrieveSimpleObjectFromRepository(protocolFields,
				LODConstants.LOD_SOFTWARE_PROTOCOL_PROPERTY_ID,
				softwareId);
		res = res + swProtocol + ReportConstants.SEMICOLON;
		lodSoftware.setProtocols(swProtocol);
		return res;
	}

	/**
	 * @param softwareId
	 * @param res
	 * @param lodSoftware
	 * @return
	 */
	public static String setFreebaseLodSoftwareOs(String softwareId,
			String resStr, LODSoftware lodSoftware) {
		String res = resStr;
		List<String> compatibleOsFields = Arrays.asList(LODConstants.FB_SOFTWARE_COMPATIBLE_OS, ReportConstants.EMPTYSTRING);
		String swOs = retrieveSimpleObjectFromRepository(compatibleOsFields,
				LODConstants.LOD_SOFTWARE_COMPATIBLE_OS_PROPERTY_ID,
				softwareId);
		res = res + swOs + ReportConstants.SEMICOLON;
		lodSoftware.setOperatingSystem(swOs);
		return res;
	}

	/**
	 * @param softwareId
	 * @param res
	 * @param lodSoftware
	 * @return
	 */
	public static String setFreebaseLodSoftwareGenre(String softwareId,
			String resStr, LODSoftware lodSoftware) {
		String res = resStr;
		res = res + ReportConstants.SEMICOLON;
		List<String> genreFields = Arrays.asList(LODConstants.FB_SOFTWARE_GENRE, ReportConstants.EMPTYSTRING);
		String swGenre = retrieveSimpleObjectFromRepository(genreFields,
				LODConstants.LOD_SOFTWARE_GENRE_PROPERTY_ID,
				softwareId);
		res = res + swGenre + ReportConstants.SEMICOLON;
		lodSoftware.setGenre(swGenre);
		return res;
	}

	/**
	 * @param softwareId
	 * @param res
	 * @param lodSoftware
	 * @return
	 */
	public static String setFreebaseLodSoftwareLicense(String softwareId,
			String resStr, LODSoftware lodSoftware) {
		String res = resStr;
		List<String> licenseList = LODUtils.searchInFreebase(
				LODConstants.LOD_SOFTWARE_LICENSE_PROPERTY_ID,
				softwareId, LODConstants.FB_ID,
				LODConstants.FB_SOFTWARE_LICENSE, LODConstants.FB_NAME);

		StringBuffer licenseStr = new StringBuffer();
		licenseStr.append(ReportConstants.EMPTYSTRING);
		Iterator<String> licenseIter = licenseList.iterator();
		while (licenseIter.hasNext()) {
			licenseStr.append(licenseIter.next());
			licenseStr.append(ReportConstants.COMMASPACE);
		}
		res = res + licenseStr.toString() + ReportConstants.SEMICOLON;
		lodSoftware.setSoftwareLicense(licenseStr.toString());
		return res;
	}
	
	/**
	 * This method retrieves information (single list) from LOD object.
	 * @param fields
	 *        The field names in JSON object
	 * @param propertyId
	 * @param repositoryId
	 * @return CSV information for one JSON object
	 */
	public static String retrieveSimpleObjectFromRepository(List<String> fields, String propertyId, String repositoryId) {
		List<String> strList = LODUtils.searchInFreebase(
				propertyId, repositoryId, LODConstants.FB_ID,
				fields.get(0), fields.get(1));

		StringBuffer buf = new StringBuffer();
		buf.append(ReportConstants.EMPTYSTRING);
		Iterator<String> strIter = strList.iterator();
		while (strIter.hasNext()) {
			buf.append(strIter.next());
			buf.append(ReportConstants.COMMASPACE);		
		}
		return buf.toString();
	}

}
