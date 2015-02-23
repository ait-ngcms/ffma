package ait.ffma.service.preservation.riskmanagement.api.lod;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import ait.ffma.domain.preservation.riskmanagement.LODFormat;
import ait.ffma.domain.preservation.riskmanagement.LODSoftware;
import ait.ffma.service.preservation.riskmanagement.api.lod.lodmodel.LODPronomSoftware;
import ait.ffma.service.preservation.riskmanagement.api.riskanalysis.risk.ReportConstants;

/**
 * This is a helper class for LOD statistics calculations
 */
public final class LODStatisticsPronomUtils {

	/**
	 * To prevent initialization
	 */
	private LODStatisticsPronomUtils() {} 
	
	/**
	 * Help method
	 * @param currentFileFormat
	 * @param currentField
	 * @return
	 */
	public static String getStringFromPronomList(String currentFileFormat, String currentField) {
		StringBuffer buf = new StringBuffer();
		buf.append(ReportConstants.EMPTYSTRING);
		List<String> infoList = LODUtils.searchPronomInfoForFileFormat(currentFileFormat, currentField);
	
		if (infoList != null) {
			Iterator<String> resultIter = infoList.iterator();
			while (resultIter.hasNext()) {
				buf.append(resultIter.next());
				buf.append(ReportConstants.COMMASPACE);
			}
		}
	//	log.info("res list: " + buf.toString());
		return buf.toString();
	}

	/**
	 * Help method to read particular fields from Pronom with exclusions option.
	 * @param currentFileFormat
	 * @param currentField
	 * @param exclusions
	 *        Fields that should not be included in resulting list
	 * @return values list
	 */
	public static String getStringFromPronomListExt(String currentFileFormat, String currentField, List<String> exclusions) {
		StringBuffer buf = new StringBuffer();
		buf.append(ReportConstants.EMPTYSTRING);
		List<String> infoList = LODUtils.searchPronomInfoForFileFormat(currentFileFormat, currentField);
	
		if (infoList != null) {
			Iterator<String> resultIter = infoList.iterator();
			while (resultIter.hasNext()) {
				String value = resultIter.next();
				if (!exclusions.contains(value)) {
					buf.append(value);
					buf.append(ReportConstants.COMMASPACE);
				}
			}
		}
	//	log.info("res list: " + buf.toString());
		return buf.toString();
	}

	/**
	 * @param currentFileFormat
	 * @param softwareCsv
	 * @param softwareCount
	 * @param softwareNames
	 * @param softwareIDs
	 * @param software
	 * @return
	 */
	public static int retrievePronomSoftwareIdsAndNames(
			String currentFileFormat, StringBuffer softwareCsv,
			int softwareCountRes, List<String> softwareNames,
			List<String> softwareIDs, LODPronomSoftware software) {
		int softwareCount = softwareCountRes;
		if (software.getFileFormatMap() != null && software.getFileFormatMap().size() > 0) {
//					log.info("Software file formats: " + software.getFileFormatMap()); 
			for (Entry<String, String> entry : software.getFileFormatMap().entrySet()) {
				if (entry.getKey().contains(currentFileFormat)) {
					softwareCsv.append(entry.getValue());
					softwareCsv.append(ReportConstants.COMMASPACE);
					if (software.getName() != null && software.getName().length() > 0) {
						softwareNames.add(software.getName());
					}
					if (software.getIdentifier() != null && software.getIdentifier().length() > 0) {
						softwareIDs.add(software.getIdentifier());
					}
					softwareCount++;
				}
			}
		}
		return softwareCount;
	}

	/**
	 * @param currentFileFormat
	 * @param softwareCsv
	 * @param vendorCsv
	 * @param softwareCount
	 * @param nameStr
	 * @param verStr
	 * @param puidStr
	 * @return
	 */
	public static LODFormat setPronomLodFormat(String currentFileFormat,
			StringBuffer softwareCsv, StringBuffer vendorCsv,
			int softwareCount, String nameStr, String verStr, String puidStr) {
		LODFormat lodFormat = new LODFormat();
		lodFormat.setFormatName(nameStr);
		lodFormat.setSoftwareCount(softwareCount);
		lodFormat.setSoftware(softwareCsv.toString());
		lodFormat.setPuid(puidStr);
		lodFormat.setVendors(vendorCsv.toString());
		lodFormat.setFileExtensions(currentFileFormat);
		lodFormat.setCurrentFormatVersion(verStr);
		return lodFormat;
	}

	/**
	 * @param software
	 * @param lodSoftware
	 */
	public static void setPronomLodSoftware(LODPronomSoftware software,
			LODSoftware lodSoftware) {
		lodSoftware.setSoftwareName(software.getName());
		lodSoftware.setSoftwareLatestVersion(software.getVersion());
		lodSoftware.setSoftwareReleaseDate(software.getReleaseDate());
		lodSoftware.setDescription(software.getDescription());
		lodSoftware.setRepositoryId(software.getIdentifier());
		lodSoftware.setRepository(LODConstants.PRONOM);
		Map<String, String> fileFormatMap = software.getFileFormatMap();
		if (fileFormatMap != null && fileFormatMap.size() > 0) { 
			Set<String> fileFormatsKeySet = fileFormatMap.keySet();
			String[] fileFormatsArray = fileFormatsKeySet.toArray(new String[fileFormatsKeySet.size()]);
			lodSoftware.setFileFormat(fileFormatsArray);
		}
	}

}
