package ait.ffma.service.preservation.riskmanagement.api.lod;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import ait.ffma.domain.FfmaDomainObject;
import ait.ffma.domain.preservation.riskmanagement.AitFileFormat;
import ait.ffma.domain.preservation.riskmanagement.LODFormat;
import ait.ffma.domain.preservation.riskmanagement.LODSoftware;
import ait.ffma.domain.preservation.riskmanagement.LODVendor;
import ait.ffma.service.preservation.riskmanagement.api.riskanalysis.risk.ReportConstants;

/**
 * This is a helper class for AIT LOD statistics calculations
 */
public final class LODStatisticsAitUtils {

	private static final String FILE_FORMAT_ENTRY = "File format entry: ";

	/**
	 * To prevent initialization
	 */
	private LODStatisticsAitUtils() {} 
	
	/**
	 * Logger
	 */
	private static Logger log = Logger.getLogger(LODStatisticsAitUtils.class.getName());
	
	/**
	 * This method retrieves manually aggregated data from AIT repository CSV file 
	 * @param header
	 *        The header as identification of particular part of the AIT repository file
	 * @return data rows list for particular header (format, software or vendors)
	 */
	public static List<String> retrieveRows(String header) {
		List<String> rowsList = new ArrayList<String>();
		try {
			InputStream in = LODStatisticsUtils.class.getResourceAsStream(LODConstants.AIT_REPOSIITORY_CSV);
			try {
				BufferedReader br = new BufferedReader(new InputStreamReader(in));
				try {
					String s;
					boolean read = false;
					boolean firstRow = true;
					while((s = br.readLine()) != null) {
						log.info(s);
						if (s.equals(ReportConstants.EMPTYSTRING)) {
							read = false;
						}
						if (s.equals(header.replaceAll(LODConstants.LINE_END, ReportConstants.EMPTYSTRING))) {
							read = true;
						}
						if (read) {
							if (!firstRow) {
								rowsList.add(s);
							}
							firstRow = false;
						}
					}
					br.close();
				} finally {
					br.close();
				}
				in.close();
			} finally {
                in.close();
			}
		} catch (IOException e) {
			log.info("Error reading AIT repository data: " + e);			
		}
		return rowsList;
	}
	
	/**
	 * This method parses passed format string according to passed type (e.g. 
	 * software or vendor) and returns a file formats list.
	 * @param formatsRows
	 *        The format row containing format information retrieved from CSV file
	 * @param type
	 *        The type of file format information like software or vendor
	 * @param filter
	 *        The value to filter resulting list
	 * @return file formats list
	 */
	public static List<String> retrieveAitFileFormats(List<String> formatsRows, String type, String filter) {
		List<String> fileFormatList = new ArrayList<String>();
		if (formatsRows != null && formatsRows.size() > 0) {
			Iterator<String> formatsExtIter = formatsRows.iterator();
			while (formatsExtIter.hasNext()) {
				List<String> formatsStr = Arrays.asList(formatsExtIter.next().split(ReportConstants.SEMICOLON));
				if (formatsStr != null && formatsStr.size() > 0) {
					retrieveAitFileExtensionsValue(type, filter, fileFormatList, formatsStr);
				}
			}
		}
		return fileFormatList;
	}

	/**
	 * @param type
	 * @param filter
	 * @param fileFormatList
	 * @param formatsStr
	 */
	public static void retrieveAitFileExtensionsValue(String type, String filter,
			List<String> fileFormatList, List<String> formatsStr) {
		String value = ReportConstants.EMPTYSTRING;
		if (type.equals(LODConstants.FormatCsvTypes.Software.name())) {
			value = formatsStr.get(LODConstants.FormatCsvTypes.Software.ordinal());
		}
		if (type.equals(LODConstants.FormatCsvTypes.Vendors.name())) {
			value = formatsStr.get(LODConstants.FormatCsvTypes.Vendors.ordinal());
		}
		if (value.contains(filter)) {
			String extensions = formatsStr.get(LODConstants.FormatCsvTypes.FileExtensions.ordinal());
			if (extensions != null && extensions.length() > 0) {
				fileFormatList.add(extensions);
			}
		}
	}

	/**
	 * @param tmpLodFormatList
	 * @param formatsCount
	 * @param formatsRows
	 */
	public static void retrieveAitFormats(List<LODFormat> tmpLodFormatList,
			int formatsCountRes, List<String> formatsRows) {
		int formatsCount = formatsCountRes;
		if (formatsRows != null && formatsRows.size() > 0) {
			Iterator<String> formatsIter = formatsRows.iterator();
			while (formatsIter.hasNext()) {
				List<String> formatsStr = Arrays.asList(formatsIter.next().split(ReportConstants.SEMICOLON));
				if (formatsStr != null && formatsStr.size() > 0) {
					LODFormat lodFormat = new LODFormat();
					getAitFormatName(formatsStr, lodFormat);
					getAitFormatReleaseDate(formatsStr, lodFormat);
					getAitFormatLicense(formatsStr, lodFormat);
					getAitFormatLimitations(formatsStr, lodFormat);
					getAitFormatHomepage(formatsStr, lodFormat);
					String mimeType = formatsStr.get(LODConstants.FormatCsvTypes.MimeType.ordinal());
					LODStatisticsDBPediaUtils.getDBPediaFormatMimeType(mimeType, lodFormat);
					getAitFormatGenre(formatsStr, lodFormat);
					getAitFormatCreator(formatsStr, lodFormat);
					getAitFormatOpenFormat(formatsStr, lodFormat);
					String standards = formatsStr.get(LODConstants.FormatCsvTypes.Standards.ordinal());
					LODStatisticsDBPediaUtils.getDBPediaFormatStandards(standards, lodFormat);
					String extensions = formatsStr.get(LODConstants.FormatCsvTypes.FileExtensions.ordinal());
					LODStatisticsDBPediaUtils.getDBPediaFormatCurrentFileFormat(extensions, lodFormat);
					String formatVersion = formatsStr.get(LODConstants.FormatCsvTypes.CurrentFormatVersion.ordinal());
					LODStatisticsDBPediaUtils.getDBPediaFormatVersion(formatVersion, lodFormat);
					lodFormat.setRepository(LODConstants.LODRepositories.AIT.name());
					getAitFormatSoftwareIdsAndNames(formatsStr, lodFormat);
					getAitFormatVendorIdsAndNames(formatsStr, lodFormat);
					getAitFormatFileExtensions(tmpLodFormatList, lodFormat);
					log.info("*** CURRENT AIT FORMATS COUNT *** " + formatsCount);
					formatsCount++;
				}
			}
		}
	}

	/**
	 * @param tmpLodFormatList
	 * @param lodFormat
	 */
	public static void getAitFormatFileExtensions(
			List<LODFormat> tmpLodFormatList, LODFormat lodFormat) {
		if (lodFormat.getFileExtensions() != null
				&& lodFormat.getFileExtensions().length() > 0
				&& lodFormat.getFileExtensions().contains(ReportConstants.COMMA)) {
			String[] extArray = lodFormat.getFileExtensions().replaceAll(LODConstants.DIP, ReportConstants.EMPTYSTRING).split(ReportConstants.COMMA);
			List<String> extList = Arrays.asList(extArray);
			Iterator<String> extIter = extList.iterator();
			while (extIter.hasNext()) {
				String extEntry = extIter.next();
				extEntry = extEntry.replaceAll(ReportConstants.SPACE, ReportConstants.EMPTYSTRING);
				LODFormat tmp = new LODFormat();
				getAitLodFormatName(lodFormat, tmp);
				getAitLodFormatRepositoryId(lodFormat, tmp);
				getAitLodFormatVersion(lodFormat, tmp);
				getAitLodFormatSoftwareName(lodFormat, tmp);
				getAitLodFormatSoftwareCount(lodFormat, tmp);
				getAitLodFormatSoftwareIds(lodFormat, tmp);
				getAitLodFormatVendorNames(lodFormat, tmp);
				getAitLodFormatVendorIds(lodFormat, tmp);
				tmp.setFileExtensions(extEntry);
				tmpLodFormatList.add(tmp);
			}
		} else {
			tmpLodFormatList.add(lodFormat);
		}
	}

	/**
	 * @param lodFormat
	 * @param tmp
	 */
	public static void getAitLodFormatVendorIds(LODFormat lodFormat,
			LODFormat tmp) {
		if (lodFormat.getVendorId() != null) {
			tmp.setVendorId(lodFormat.getVendorId());
		}
	}

	/**
	 * @param lodFormat
	 * @param tmp
	 */
	public static void getAitLodFormatVendorNames(LODFormat lodFormat,
			LODFormat tmp) {
		if (lodFormat.getVendorName() != null) {
			tmp.setVendorName(lodFormat.getVendorName());
		}
	}

	/**
	 * @param lodFormat
	 * @param tmp
	 */
	public static void getAitLodFormatSoftwareIds(LODFormat lodFormat,
			LODFormat tmp) {
		if (lodFormat.getSoftwareId() != null) {
			tmp.setSoftwareId(lodFormat.getSoftwareId());
		}
	}

	/**
	 * @param lodFormat
	 * @param tmp
	 */
	public static void getAitLodFormatSoftwareCount(LODFormat lodFormat,
			LODFormat tmp) {
		if (lodFormat.getSoftwareCount() != null) {
			tmp.setSoftwareCount(lodFormat.getSoftwareCount());
		}
	}

	/**
	 * @param lodFormat
	 * @param tmp
	 */
	public static void getAitLodFormatSoftwareName(LODFormat lodFormat,
			LODFormat tmp) {
		if (lodFormat.getSoftwareName() != null) {
			tmp.setSoftwareName(lodFormat.getSoftwareName());
		}
	}

	/**
	 * @param lodFormat
	 * @param tmp
	 */
	public static void getAitLodFormatVersion(LODFormat lodFormat,
			LODFormat tmp) {
		if (lodFormat.getCurrentFormatVersion() != null) {
			tmp.setCurrentFormatVersion(lodFormat.getCurrentFormatVersion());
		}
	}

	/**
	 * @param lodFormat
	 * @param tmp
	 */
	public static void getAitLodFormatRepositoryId(LODFormat lodFormat,
			LODFormat tmp) {
		if (lodFormat.getRepositoryId() != null) {
			tmp.setRepositoryId(lodFormat.getRepositoryId());
		}
	}

	/**
	 * @param lodFormat
	 * @param tmp
	 */
	public static void getAitLodFormatName(LODFormat lodFormat, LODFormat tmp) {
		if (lodFormat.getFormatName() != null) {
			tmp.setFormatName(lodFormat.getFormatName());
		}
	}

	/**
	 * @param formatsStr
	 * @param lodFormat
	 */
	public static void getAitFormatVendorIdsAndNames(List<String> formatsStr,
			LODFormat lodFormat) {
		String vendorNamesStr = formatsStr.get(LODConstants.FormatCsvTypes.Vendors.ordinal());
		if (vendorNamesStr != null && vendorNamesStr.length() > 0) {
			String[] vendorNamesArray = vendorNamesStr.split(ReportConstants.COMMA);
			if (vendorNamesArray != null && vendorNamesArray.length > 0) {
				lodFormat.setVendorName(vendorNamesArray);
				List<String> vendorNamesList = Arrays.asList(vendorNamesArray);
				List<String> vendorIdList = new ArrayList<String>();
				Iterator<String> vendorNamesIter = vendorNamesList.iterator();
				while (vendorNamesIter.hasNext()) {
					vendorIdList.add(LODConstants.AIT_ID + vendorNamesIter.next());
				}
				lodFormat.setVendorId(vendorIdList.toArray(new String[vendorIdList.size()]));
			}
		}
	}

	/**
	 * @param formatsStr
	 * @param lodFormat
	 */
	public static void getAitFormatSoftwareIdsAndNames(
			List<String> formatsStr, LODFormat lodFormat) {
		String softwareNamesStr = formatsStr.get(LODConstants.FormatCsvTypes.Software.ordinal());
		if (softwareNamesStr != null && softwareNamesStr.length() > 0) {
			String[] softwareNamesArray = softwareNamesStr.split(ReportConstants.COMMA);
			if (softwareNamesArray != null && softwareNamesArray.length > 0) {
				lodFormat.setSoftwareName(softwareNamesArray);
				lodFormat.setSoftwareCount(softwareNamesArray.length);
				List<String> swNamesList = Arrays.asList(softwareNamesArray);
				List<String> swIdList = new ArrayList<String>();
				Iterator<String> swNamesIter = swNamesList.iterator();
				while (swNamesIter.hasNext()) {
					swIdList.add(LODConstants.AIT_ID + swNamesIter.next());
				}
				lodFormat.setSoftwareId(swIdList.toArray(new String[swIdList.size()]));
			}
		}
	}

	/**
	 * @param formatsStr
	 * @param lodFormat
	 */
	public static void getAitFormatOpenFormat(List<String> formatsStr,
			LODFormat lodFormat) {
		String openFormat = formatsStr.get(LODConstants.FormatCsvTypes.OpenFormat.ordinal());
		if (openFormat != null && openFormat.length() > 0) {
			lodFormat.setOpenFormat(openFormat);
		}
	}

	/**
	 * @param formatsStr
	 * @param lodFormat
	 */
	public static void getAitFormatCreator(List<String> formatsStr,
			LODFormat lodFormat) {
		String formatCreator = formatsStr.get(LODConstants.FormatCsvTypes.FormatCreator.ordinal());
		if (formatCreator != null && formatCreator.length() > 0) {
			lodFormat.setFormatCreator(formatCreator);
		}
	}

	/**
	 * @param formatsStr
	 * @param lodFormat
	 */
	public static void getAitFormatGenre(List<String> formatsStr,
			LODFormat lodFormat) {
		String formatGenre = formatsStr.get(LODConstants.FormatCsvTypes.FormatGenre.ordinal());
		if (formatGenre != null && formatGenre.length() > 0) {
			lodFormat.setFormatGenre(formatGenre);
		}
	}

	/**
	 * @param formatsStr
	 * @param lodFormat
	 */
	public static void getAitFormatHomepage(List<String> formatsStr,
			LODFormat lodFormat) {
		String formatHomepage = formatsStr.get(LODConstants.FormatCsvTypes.FormatHomepage.ordinal());
		if (formatHomepage != null && formatHomepage.length() > 0) {
			lodFormat.setFormatHomepage(formatHomepage);
		}
	}

	/**
	 * @param formatsStr
	 * @param lodFormat
	 */
	public static void getAitFormatLimitations(List<String> formatsStr,
			LODFormat lodFormat) {
		String limitations = formatsStr.get(LODConstants.FormatCsvTypes.Limitations.ordinal());
		if (limitations != null && limitations.length() > 0) {
			lodFormat.setLimitations(limitations);
		}
	}

	/**
	 * @param formatsStr
	 * @param lodFormat
	 */
	public static void getAitFormatLicense(List<String> formatsStr,
			LODFormat lodFormat) {
		String formatLicense = formatsStr.get(LODConstants.FormatCsvTypes.FormatLicense.ordinal());
		if (formatLicense != null && formatLicense.length() > 0) {
			lodFormat.setFormatLicense(formatLicense);
		}
	}

	/**
	 * @param formatsStr
	 * @param lodFormat
	 */
	public static void getAitFormatReleaseDate(List<String> formatsStr,
			LODFormat lodFormat) {
		String currentVersionReleaseDate = formatsStr.get(LODConstants.FormatCsvTypes.CurrentVersionReleaseDate.ordinal());
		LODStatisticsDBPediaUtils.getDBPediaFormatReleased(currentVersionReleaseDate, lodFormat);
	}

	/**
	 * @param formatsStr
	 * @param lodFormat
	 */
	public static void getAitFormatName(List<String> formatsStr,
			LODFormat lodFormat) {
		String formatName = formatsStr.get(LODConstants.FormatCsvTypes.FormatName.ordinal());
		if (formatName != null && formatName.length() > 0) {
			lodFormat.setFormatName(formatName);
			String[] formatStrArray = new String[1];
			formatStrArray[0] = LODConstants.AIT_ID + formatName;
			lodFormat.setRepositoryId(formatStrArray);
		}
	}

	/**
	 * @param vendorNames
	 * @param vendorIDs
	 * @param formatsRows
	 * @param vendorsStr
	 * @param lodVendor
	 */
//	public static void createAitLodVendor(List<String> vendorNames,
//			List<String> vendorIDs, List<String> formatsRows,
//			List<String> vendorsStr, LODVendor lodVendor) {
//		getAitVendorName(vendorNames, vendorIDs, vendorsStr, lodVendor);
//		getAitVendorNumOfEmployees(vendorsStr, lodVendor);
//		String businessStatus = vendorsStr.get(LODConstants.VendorsCsvTypes.BusinessStatus.ordinal());
//		LODStatisticsDBPediaUtils.getDBPediaLodVendorRevenue(businessStatus, lodVendor);
//		getAitVendorCurrentFfma(vendorsStr, lodVendor);
//		getAitVendorStockIssues(vendorsStr, lodVendor);
//		getAitVendorRankedList(vendorsStr, lodVendor);
//		String country = vendorsStr.get(LODConstants.VendorsCsvTypes.Country.ordinal());
//		LODStatisticsDBPediaUtils.getDBPediaLodVendorCountry(country, lodVendor);
//		getAitVendorFoundationDate(vendorsStr, lodVendor);
//		String homepage = vendorsStr.get(LODConstants.VendorsCsvTypes.Homepage.ordinal());
//		LODStatisticsDBPediaUtils.getDBPediaLodVendorHomepage(homepage, lodVendor);
//		lodVendor.setRepository(LODConstants.LODRepositories.AIT.name());
//		getAitVendorFormatList(formatsRows, lodVendor);
//	}

	/**
	 * @param formatsRows
	 * @param lodVendor
	 */
	public static void getAitVendorFormatList(List<String> formatsRows,
			LODVendor lodVendor) {
		List<String> fileFormatList;
		fileFormatList = retrieveAitFileFormats(formatsRows,
				LODConstants.FormatCsvTypes.Vendors.name(),
				lodVendor.getOrganisationName());
		if (fileFormatList.size() > 0) {
			lodVendor.setFileFormat(fileFormatList.toArray(new String[fileFormatList.size()]));
		}
	}

	/**
	 * @param vendorsStr
	 * @param lodVendor
	 */
	public static void getAitVendorFoundationDate(List<String> vendorsStr,
			LODVendor lodVendor) {
		String foundationDate = vendorsStr.get(LODConstants.VendorsCsvTypes.FoundationDate.ordinal());
		if (foundationDate != null && foundationDate.length() > 0) {
			lodVendor.setFoundationDate(foundationDate);
		}
	}

	/**
	 * @param vendorsStr
	 * @param lodVendor
	 */
	public static void getAitVendorRankedList(List<String> vendorsStr,
			LODVendor lodVendor) {
		String rankedList = vendorsStr.get(LODConstants.VendorsCsvTypes.RankedList.ordinal());
		if (rankedList != null && rankedList.length() > 0) {
			lodVendor.setRankedList(rankedList);
		}
	}

	/**
	 * @param vendorsStr
	 * @param lodVendor
	 */
	public static void getAitVendorStockIssues(List<String> vendorsStr,
			LODVendor lodVendor) {
		String stockIssues = vendorsStr.get(LODConstants.VendorsCsvTypes.StockIssues.ordinal());
		if (stockIssues != null && stockIssues.length() > 0) {
			lodVendor.setStockIssues(stockIssues);
		}
	}

	/**
	 * @param vendorsStr
	 * @param lodVendor
	 */
	public static void getAitVendorCurrentFfma(List<String> vendorsStr,
			LODVendor lodVendor) {
		String currentFfma = vendorsStr.get(LODConstants.VendorsCsvTypes.CurrentFfma.ordinal());
		if (currentFfma != null && currentFfma.length() > 0) {
			lodVendor.setCurrentFfma(currentFfma);
		}
	}

	/**
	 * @param vendorsStr
	 * @param lodVendor
	 */
	public static void getAitVendorNumOfEmployees(List<String> vendorsStr,
			LODVendor lodVendor) {
		String numberOfEmployees = vendorsStr.get(LODConstants.VendorsCsvTypes.NumberOfEmployees.ordinal());
		if (numberOfEmployees != null && numberOfEmployees.length() > 0) {
			lodVendor.setNumberOfEmployees(Integer.parseInt(numberOfEmployees));
		}
	}

	/**
	 * @param vendorNames
	 * @param vendorIDs
	 * @param vendorsStr
	 * @param lodVendor
	 */
	public static void getAitVendorName(List<String> vendorNames,
			List<String> vendorIDs, List<String> vendorsStr, LODVendor lodVendor) {
		String organisationName = vendorsStr.get(LODConstants.VendorsCsvTypes.OrganisationName.ordinal());
		if (organisationName != null && organisationName.length() > 0) {
			lodVendor.setOrganisationName(organisationName);
			lodVendor.setRepositoryId(LODConstants.AIT_ID + organisationName);
			vendorNames.add(organisationName);
			vendorIDs.add(LODConstants.AIT_ID + organisationName);
		}
	}

	/**
	 * @param softwareNames
	 * @param softwareIDs
	 * @param formatsRows
	 * @param softwareStr
	 * @return
	 */
	public static LODSoftware createLodSoftwareForAit(
			List<String> softwareNames, List<String> softwareIDs,
			List<String> formatsRows, List<String> softwareStr) {
		LODSoftware lodSoftware = new LODSoftware();
		getAitSoftwareName(softwareNames, softwareIDs, softwareStr, lodSoftware);
		String softwareLicense = softwareStr.get(LODConstants.SoftwareCsvTypes.SoftwareLicense.ordinal());
		LODStatisticsDBPediaUtils.getDBPediaLodSoftwareLicense(softwareLicense, lodSoftware);
		String softwareHomepage = softwareStr.get(LODConstants.SoftwareCsvTypes.SoftwareHomepage.ordinal());
		LODStatisticsDBPediaUtils.getDBPediaLodSoftwareHomepage(softwareHomepage, lodSoftware);
		getAitSoftwareGenre(softwareStr, lodSoftware);
		String operatingSystem = softwareStr.get(LODConstants.SoftwareCsvTypes.OperatingSystem.ordinal());
		LODStatisticsDBPediaUtils.getDBPediaLodSoftwareOs(operatingSystem, lodSoftware);
		getAitSoftwareProtocols(softwareStr, lodSoftware);
		String programmingLanguage = softwareStr.get(LODConstants.SoftwareCsvTypes.ProgrammingLanguage.ordinal());
		LODStatisticsDBPediaUtils.getDBPediaLodSoftwareProgrammingLanguage(programmingLanguage, lodSoftware);
		String softwareVersion = softwareStr.get(LODConstants.SoftwareCsvTypes.SoftwareLatestVersion.ordinal());
		LODStatisticsDBPediaUtils.getDBPediaLodSoftwareVersion(softwareVersion, lodSoftware);
		getAitSoftwareReleaseDate(softwareStr, lodSoftware);
		lodSoftware.setRepository(LODConstants.LODRepositories.AIT.name());
		getAitSoftwareFileFormatList(formatsRows, lodSoftware);
		return lodSoftware;
	}

	/**
	 * @param formatsRows
	 * @param lodSoftware
	 */
	public static void getAitSoftwareFileFormatList(List<String> formatsRows,
			LODSoftware lodSoftware) {
		List<String> fileFormatList;
		fileFormatList = retrieveAitFileFormats(formatsRows,
				LODConstants.FormatCsvTypes.Software.name(),
				lodSoftware.getSoftwareName());
		if (fileFormatList.size() > 0) {
			lodSoftware.setFileFormat(fileFormatList.toArray(new String[fileFormatList.size()]));
		}
	}

	/**
	 * @param softwareStr
	 * @param lodSoftware
	 */
	public static void getAitSoftwareReleaseDate(List<String> softwareStr,
			LODSoftware lodSoftware) {
		String softwareReleaseDate = softwareStr.get(LODConstants.SoftwareCsvTypes.SoftwareReleaseDate.ordinal());
		if (softwareReleaseDate != null && softwareReleaseDate.length() > 0) {
			lodSoftware.setSoftwareReleaseDate(softwareReleaseDate);
		}
	}

	/**
	 * @param softwareStr
	 * @param lodSoftware
	 */
	public static void getAitSoftwareProtocols(List<String> softwareStr,
			LODSoftware lodSoftware) {
		String protocols = softwareStr.get(LODConstants.SoftwareCsvTypes.Protocols.ordinal());
		if (protocols != null && protocols.length() > 0) {
			lodSoftware.setProtocols(protocols);
		}
	}

	/**
	 * @param softwareStr
	 * @param lodSoftware
	 */
	public static void getAitSoftwareGenre(List<String> softwareStr,
			LODSoftware lodSoftware) {
		String genre = softwareStr.get(LODConstants.SoftwareCsvTypes.Genre.ordinal());
		if (genre != null && genre.length() > 0) {
			lodSoftware.setGenre(genre);
		}
	}

	/**
	 * @param softwareNames
	 * @param softwareIDs
	 * @param softwareStr
	 * @param lodSoftware
	 */
	public static void getAitSoftwareName(List<String> softwareNames,
			List<String> softwareIDs, List<String> softwareStr,
			LODSoftware lodSoftware) {
		String softwareName = softwareStr.get(LODConstants.SoftwareCsvTypes.SoftwareName.ordinal());
		if (softwareName != null && softwareName.length() > 0) {
			lodSoftware.setSoftwareName(softwareName);
			lodSoftware.setRepositoryId(LODConstants.AIT_ID + softwareName);
			softwareNames.add(softwareName);
			softwareIDs.add(LODConstants.AIT_ID + softwareName);
		}
	}

	/**
	 * @param tmpLodFormatList
	 * @return
	 */
	public static List<AitFileFormat> createAitLodFormat(
			List<LODFormat> tmpLodFormatList, List<LODFormat> lodFormatList) {
		List<AitFileFormat> aitFileFormatList = new ArrayList<AitFileFormat>();
		Iterator<LODFormat> iter = tmpLodFormatList.iterator();
		while (iter.hasNext()) {
			AitFileFormat aitFileFormat = new AitFileFormat();
			LODFormat lodFormat = iter.next();
			setAitFileFormatName(aitFileFormat, lodFormat);
			setAitFileFormatExtension(aitFileFormat, lodFormat);
			setAitFileFormatVersion(aitFileFormat, lodFormat);
			setAitFileFormatId(aitFileFormat, lodFormat);
			aitFileFormatList.add(aitFileFormat);
			lodFormatList.add(lodFormat);
		}
		return aitFileFormatList;
	}

	/**
	 * @param aitFileFormat
	 * @param lodFormat
	 */
	private static void setAitFileFormatId(AitFileFormat aitFileFormat,
			LODFormat lodFormat) {
		if (lodFormat.getRepositoryId() != null && lodFormat.getRepositoryId()[0] != null 
				&& lodFormat.getRepositoryId()[0].length() > 0) {
			aitFileFormat.setPuid(lodFormat.getRepositoryId()[0]);
		}
	}

	/**
	 * @param aitFileFormat
	 * @param lodFormat
	 */
	private static void setAitFileFormatVersion(AitFileFormat aitFileFormat,
			LODFormat lodFormat) {
		if (lodFormat.getCurrentFormatVersion() != null && lodFormat.getCurrentFormatVersion().length() > 0) {
			aitFileFormat.setVersion(lodFormat.getCurrentFormatVersion());
		}
	}

	/**
	 * @param aitFileFormat
	 * @param lodFormat
	 */
	private static void setAitFileFormatExtension(AitFileFormat aitFileFormat,
			LODFormat lodFormat) {
		if (lodFormat.getFileExtensions() != null && lodFormat.getFileExtensions().length() > 0) {
			aitFileFormat.setExtension(lodFormat.getFileExtensions());
		}
	}

	/**
	 * @param aitFileFormat
	 * @param lodFormat
	 */
	private static void setAitFileFormatName(AitFileFormat aitFileFormat,
			LODFormat lodFormat) {
		if (lodFormat.getFormatName() != null && lodFormat.getFormatName().length() > 0) {
			aitFileFormat.setName(lodFormat.getFormatName());
		}
	}

	/**
	 * @param FfmaDomainObjectsList
	 * @param aitFileFormatList
	 */
	public static void insertFileFormatsForAit(
			List<FfmaDomainObject> FfmaDomainObjectsList,
			List<AitFileFormat> aitFileFormatList) {
		Iterator<AitFileFormat> resultIter = aitFileFormatList.iterator();
		while (resultIter.hasNext()) {
			AitFileFormat fileFormatEntry = resultIter.next();
			log.info(FILE_FORMAT_ENTRY + fileFormatEntry);
			FfmaDomainObjectsList.add((FfmaDomainObject) fileFormatEntry);
		}
	}
	
}
