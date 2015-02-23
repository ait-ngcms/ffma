package ait.ffma.service.preservation.riskmanagement.api.lod;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import ait.ffma.common.exception.FfmaCommonException;
import ait.ffma.common.exception.ObjectNotFoundException;
import ait.ffma.common.exception.ObjectNotStoredException;
import ait.ffma.domain.FfmaDomainObject;
import ait.ffma.domain.preservation.riskmanagement.AitFileFormat;
import ait.ffma.domain.preservation.riskmanagement.DBPediaFileFormat;
import ait.ffma.domain.preservation.riskmanagement.DipFormatId;
import ait.ffma.domain.preservation.riskmanagement.DipSoftwareId;
import ait.ffma.domain.preservation.riskmanagement.DipVendorId;
import ait.ffma.domain.preservation.riskmanagement.FreebaseFileFormat;
import ait.ffma.domain.preservation.riskmanagement.LODFormat;
import ait.ffma.domain.preservation.riskmanagement.LODSoftware;
import ait.ffma.domain.preservation.riskmanagement.LODVendor;
import ait.ffma.domain.preservation.riskmanagement.PronomFileFormat;
import ait.ffma.service.preservation.riskmanagement.api.riskanalysis.risk.ReportConstants;
import ait.ffma.service.preservation.riskmanagement.api.riskanalysis.risk.RiskConstants;
import ait.ffma.service.preservation.riskmanagement.dao.PreservationRiskmanagementDao;

/**
 * This class provides research of data from LOD repositories and that ingestion in Mongo database.
 */
public class LODCreator {

	private static final String CANNOT_STORE_FFMA_FULL_DOC_OBJECT_LIST = "Cannot store FfmaFullDocObject list. ";
	/**
	 * The data access object for preservation risk management
	 */
	@Autowired
	private PreservationRiskmanagementDao preservationRiskmanagementDao;

	/**
	 * @param preservationRiskmanagementDao
	 */
	public void setPreservationRiskmanagementDao(PreservationRiskmanagementDao preservationRiskmanagementDao) {
		this.preservationRiskmanagementDao = preservationRiskmanagementDao;
	}

	/**
	 * A logger object.
	 */
	private Logger log = Logger.getLogger(getClass());

	/**
	 * This method checks if particular collection exist in Mongo database.
	 * @return
	 */
	public String checkLodData() {
		StringBuffer buf = new StringBuffer();
		buf.append(LODConstants.HTML_HEADER);
		buf.append("<title>Riskmanagement database collections report</title></head><body>");
		buf.append("<h2>Riskmanagement database collections report</h2>");
		buf.append(ReportConstants.TABLE_BORDER_1);
		buf.append(ReportConstants.TR);
		buf.append("<th>CollectionName</th>");
		buf.append("<th>ObjectsCount</th>");
		buf.append(ReportConstants.TREND);
		
		PronomFileFormat pronomFileFormat = new PronomFileFormat();
		DBPediaFileFormat dbpediaFileFormat = new DBPediaFileFormat();
		FreebaseFileFormat freebaseFileFormat = new FreebaseFileFormat();
		LODFormat lodFormat = new LODFormat();
		LODSoftware lodSoftware = new LODSoftware();
		LODVendor lodVendor = new LODVendor();
		DipFormatId dipFormatId = new DipFormatId();
		DipSoftwareId dipSoftwareId = new DipSoftwareId();
		DipVendorId dipVendorId = new DipVendorId();
		
		try {
			List<String> exclusionsList = new ArrayList<String>();
			exclusionsList.add(RiskConstants.DOMAIN_OBJECT_NAME);
			buf.append(checkCollection(pronomFileFormat));
			buf.append(checkCollection(dbpediaFileFormat));
			buf.append(checkCollection(freebaseFileFormat));
			buf.append(checkCollection(lodFormat));
			buf.append(checkCollection(lodSoftware));
			buf.append(checkCollection(lodVendor));
			buf.append(checkCollection(dipFormatId));
			buf.append(checkCollection(dipSoftwareId));
			buf.append(checkCollection(dipVendorId));
		} catch (Exception e) {
			log.info(ReportConstants.CAUGHT_OBJECT_NOT_RETRIEVED_EXCEPTION_ERROR + e.getMessage());
		}
		buf.append(ReportConstants.TABLEEND);
		buf.append(ReportConstants.BODYEND);
		buf.append(ReportConstants.HTMLEND);
        return buf.toString();
	}

	/**
	 * Check existence of a database collection for passed Ffma domain object
	 * @param obj
	 *        The Ffma domain object
	 * @return
	 */
	public String checkCollection(FfmaDomainObject obj) {
		String res = ReportConstants.EMPTYSTRING;
		try {
			List<String> exclusionsList = new ArrayList<String>();
			exclusionsList.add(RiskConstants.DOMAIN_OBJECT_NAME);
			List<? extends FfmaDomainObject> collectionList = preservationRiskmanagementDao
				.retrieveCollection(obj, exclusionsList);

			if (collectionList != null && collectionList.size() > 0) {
				String collectionName = ((FfmaDomainObject) obj).getClass().getSimpleName();
				log.info("collection name: " + collectionName + " size: " + collectionList.size());
				res = ReportConstants.TR + ReportConstants.TD + collectionName + ReportConstants.TDEND + ReportConstants.TD + collectionList.size() + ReportConstants.TDEND + ReportConstants.TREND;
			}
		} catch (Exception e) {
			log.info(ReportConstants.CAUGHT_OBJECT_NOT_RETRIEVED_EXCEPTION_ERROR + e.getMessage());
		}

		return res;
	}

	/**
	 * Creates LODFormat collection
	 * @throws FfmaCommonException 
	 */
	private String storeLodFormat() throws FfmaCommonException {
		String res = ReportConstants.EMPTYSTRING;
		List<LODFormat> lodFormatList = LODStatisticsUtils.getLodFormats();
		if (lodFormatList != null && lodFormatList.size() > 0) {
			Iterator<LODFormat> iterFormats = lodFormatList.iterator();
			while (iterFormats.hasNext()) {
				LODFormat currentLodFormat = iterFormats.next();
				LODFormat checkLodFormat = new LODFormat();
				checkLodFormat.setRepository(currentLodFormat.getRepository());
				checkLodFormat.setFormatName(currentLodFormat.getFormatName());
				try { // store LODFormat
					preservationRiskmanagementDao.updateObject(checkLodFormat, currentLodFormat);
				} catch (FfmaCommonException e) {
					log.debug(ReportConstants.ERROR_UPDATE_FREEBASSE_EXTENSIONS + e);
				}
			}
			res = res + LODFormat.class.getSimpleName() + ReportConstants.SEMICOLON;
		}
		return res;
	}

	/**
	 * Creates LODSoftware collection and also DipSoftwareId entries with links to 
	 * DipFormatId collection
	 * @throws FfmaCommonException 
	 */
	private String storeLodSoftware() throws FfmaCommonException {
		String res = ReportConstants.EMPTYSTRING;
		List<LODSoftware> lodSoftwareList = LODStatisticsUtils.getLodSoftware();
		if (lodSoftwareList != null && lodSoftwareList.size() > 0) {
			Iterator<LODSoftware> iterLodSw = lodSoftwareList.iterator();
			while (iterLodSw.hasNext()) {
				LODSoftware lodSoftware = iterLodSw.next();
				LODSoftware checkLodSoftware = new LODSoftware();
				checkLodSoftware.setRepository(lodSoftware.getRepository());
				checkLodSoftware.setRepositoryId(lodSoftware.getRepositoryId());
				try { // store LODSoftware
					preservationRiskmanagementDao.updateObject(checkLodSoftware, lodSoftware);
				} catch (FfmaCommonException e) {
					log.debug(ReportConstants.ERROR_UPDATE_FREEBASSE_EXTENSIONS + e);
				}
				createDipSoftwareIdObject(lodSoftware);
			}
			res = res + LODSoftware.class.getSimpleName() + ReportConstants.SEMICOLON;
			res = res + DipFormatId.class.getSimpleName() + ReportConstants.SEMICOLON;
			res = res + DipSoftwareId.class.getSimpleName() + ReportConstants.SEMICOLON;
		}
		return res;
	}

	/**
	 * @param lodSoftware
	 * @throws FfmaCommonException
	 */
	private void createDipSoftwareIdObject(LODSoftware lodSoftware)
			throws FfmaCommonException {

		DipSoftwareId dipSoftwareId = new DipSoftwareId();
		dipSoftwareId.setDipId(LODConstants.DIP + lodSoftware.getSoftwareName());
		dipSoftwareId.setName(lodSoftware.getSoftwareName());
		List<String> softwareIdList = new ArrayList<String>();
		softwareIdList.add(lodSoftware.getRepositoryId());
		setAitIdToSoftware(lodSoftware, softwareIdList, dipSoftwareId); 
		setFreebaseIdToSoftware(lodSoftware, softwareIdList,
				dipSoftwareId);
		setPronomIdToSoftware(lodSoftware, softwareIdList,
				dipSoftwareId);
		setDBPediaIdToSoftware(lodSoftware, softwareIdList,
				dipSoftwareId);
		List<String> dipFormatIdList = new ArrayList<String>();
		if (lodSoftware.getFileFormat() != null && lodSoftware.getFileFormat().length > 0) {
			List<String> listOfExtensions = normalizeExtensionsList(lodSoftware.getFileFormat());
			Iterator<String> ffIter = listOfExtensions.iterator();
			while (ffIter.hasNext()) {
				String ext = ffIter.next();
				computeDipIdForSoftware(dipFormatIdList, ext);
			}
		}
		dipSoftwareId.setDipFormatId(dipFormatIdList.toArray(new String[dipFormatIdList.size()]));
		List<String> descriptionsList = new ArrayList<String>();
		descriptionsList.add(lodSoftware.getDescription());
		dipSoftwareId.setDescription(descriptionsList.toArray(new String[descriptionsList.size()]));		
		addDipSoftwareIdLinkToExistingDipFormatIdObjects(lodSoftware,
				dipSoftwareId, softwareIdList);
	}

	/**
	 * @param dipFormatIdList
	 * @param ext
	 */
	private void computeDipIdForSoftware(List<String> dipFormatIdList,
			String ext) {
		if (ext.length() > 0 && ext.contains(ReportConstants.COMMA)) {
			String[] extArray = ext.split(ReportConstants.COMMA);
			List<String> extList = Arrays.asList(extArray);
			Iterator<String> extIter = extList.iterator();
			while (extIter.hasNext()) {
				String extEntry = extIter.next();
				dipFormatIdList.add(LODConstants.DIP + extEntry);
			}
		} else {
			dipFormatIdList.add(LODConstants.DIP + ext);
		}
	}

	/**
	 * @param lodSoftware
	 * @param dipSoftwareId
	 * @param softwareIdList
	 * @throws FfmaCommonException
	 */
	private void addDipSoftwareIdLinkToExistingDipFormatIdObjects(
			LODSoftware lodSoftware, DipSoftwareId dipSoftwareId,
			List<String> softwareIdList) throws FfmaCommonException {
		
		if (lodSoftware.getFileFormat() != null && lodSoftware.getFileFormat().length > 0) {
			List<String> listOfExtensions2 = normalizeExtensionsList(lodSoftware.getFileFormat());
			Iterator<String> ffIter2 = listOfExtensions2.iterator();
			while (ffIter2.hasNext()) {
				String ext2 = ffIter2.next();
				DipFormatId dipFormatId = new DipFormatId();
				dipFormatId.setDipId(LODConstants.DIP + ext2);
				List<String> exclusionsList = new ArrayList<String>();
				exclusionsList.add(RiskConstants.DOMAIN_OBJECT_NAME);
				List<? extends FfmaDomainObject> dipFormats = preservationRiskmanagementDao
					.retrieveCollection((FfmaDomainObject) dipFormatId, exclusionsList);
				Iterator<? extends FfmaDomainObject> iter = dipFormats.iterator();
				while (iter.hasNext()) {
					DipFormatId dipFormatIdEntry = (DipFormatId) iter.next();
					dipFormatIdEntry.setDipSoftwareId(LODStatisticsUtils
							.addValueToStringArray(
									dipFormatIdEntry.getDipSoftwareId(),
									dipSoftwareId.getDipId()));
					preservationRiskmanagementDao.updateObject(dipFormatId, dipFormatIdEntry);
				}				
				updateDipSoftwareIdCollectionWithNewEntry(lodSoftware,
						dipSoftwareId, softwareIdList, ext2,
						exclusionsList);
			}
		}
	}

	/**
	 * @param lodSoftware
	 * @param dipSoftwareId
	 * @param softwareIdList
	 * @param ext2
	 * @param exclusionsList
	 * @throws FfmaCommonException
	 */
	private void updateDipSoftwareIdCollectionWithNewEntry(
			LODSoftware lodSoftware, DipSoftwareId dipSoftwareId,
			List<String> softwareIdList, String ext2,
			List<String> exclusionsList) throws FfmaCommonException {

		DipSoftwareId checkDipSoftwareId = new DipSoftwareId();
		checkDipSoftwareId.setDipId(LODConstants.DIP + lodSoftware.getSoftwareName());
		checkDipSoftwareId.setName(lodSoftware.getSoftwareName());
		if (preservationRiskmanagementDao.existsInDb(checkDipSoftwareId)) {
			List<? extends FfmaDomainObject> dipSoftwareIdList = preservationRiskmanagementDao
				.retrieveCollection((FfmaDomainObject) checkDipSoftwareId, exclusionsList);
			Iterator<? extends FfmaDomainObject> iterSoftwareId = dipSoftwareIdList.iterator();
			while (iterSoftwareId.hasNext()) {
				DipSoftwareId dipSoftwareIdEntry = (DipSoftwareId) iterSoftwareId.next();
				setAitIdToSoftware(lodSoftware, softwareIdList, dipSoftwareIdEntry); 
				setFreebaseIdToSoftware(lodSoftware, softwareIdList, dipSoftwareIdEntry);
				setPronomIdToSoftware(lodSoftware, softwareIdList, dipSoftwareIdEntry);
				setDBPediaIdToSoftware(lodSoftware, softwareIdList, dipSoftwareIdEntry);
				String formatId = LODConstants.DIP + ext2;
				dipSoftwareIdEntry.setDipFormatId(LODStatisticsUtils
						.addValueToStringArray(
								dipSoftwareIdEntry.getDipFormatId(),
								formatId));
				dipSoftwareIdEntry.setDescription(LODStatisticsUtils
						.addValueToStringArray(
								dipSoftwareIdEntry.getDescription(),
								lodSoftware.getDescription()));
				preservationRiskmanagementDao.updateObject(checkDipSoftwareId, dipSoftwareIdEntry);
			}
		} else {
			try {
				preservationRiskmanagementDao.storeObject(dipSoftwareId);
			} catch (ObjectNotStoredException e) {
				log.info(e);
			} catch (ObjectNotFoundException e) {
				log.info(e);
			}
		}
	}

	/**
	 * @param lodSoftware
	 * @param softwareIdList
	 * @param dipSoftwareIdEntry
	 */
	private void setDBPediaIdToSoftware(LODSoftware lodSoftware,
			List<String> softwareIdList, DipSoftwareId dipSoftwareIdEntry) {
		if (lodSoftware.getRepository() != null && lodSoftware.getRepository().equals(LODConstants.LODRepositories.DBPedia.name())) {
			dipSoftwareIdEntry.setDBPediaId(softwareIdList.toArray(new String[softwareIdList.size()]));
		}
	}

	/**
	 * @param lodSoftware
	 * @param softwareIdList
	 * @param dipSoftwareIdEntry
	 */
	private void setPronomIdToSoftware(LODSoftware lodSoftware,
			List<String> softwareIdList, DipSoftwareId dipSoftwareIdEntry) {
		if (lodSoftware.getRepository() != null && lodSoftware.getRepository().equals(LODConstants.LODRepositories.Pronom.name())) {
			dipSoftwareIdEntry.setPronomId(softwareIdList.toArray(new String[softwareIdList.size()]));
		}
	}

	/**
	 * @param lodSoftware
	 * @param softwareIdList
	 * @param dipSoftwareIdEntry
	 */
	private void setFreebaseIdToSoftware(LODSoftware lodSoftware,
			List<String> softwareIdList, DipSoftwareId dipSoftwareIdEntry) {
		if (lodSoftware.getRepository() != null && lodSoftware.getRepository().equals(LODConstants.LODRepositories.Freebase.name())) {
			dipSoftwareIdEntry.setFreebaseId(softwareIdList.toArray(new String[softwareIdList.size()]));
		}
	}

	/**
	 * @param lodSoftware
	 * @param softwareIdList
	 * @param dipSoftwareIdEntry
	 */
	private void setAitIdToSoftware(LODSoftware lodSoftware,
			List<String> softwareIdList, DipSoftwareId dipSoftwareIdEntry) {
		if (lodSoftware.getRepository() != null && lodSoftware.getRepository().equals(LODConstants.LODRepositories.AIT.name())) {
			dipSoftwareIdEntry.setAitId(softwareIdList.toArray(new String[softwareIdList.size()]));
		}
	}
	
	/**
	 * This method normalizes file formats list - only one extension per list entry is possible.
	 * E.g. flv, flv1, flv2 is not possible
	 * @param inputArray
	 * @return normalized list
	 */
	private List<String> normalizeExtensionsList(String[] inputArray) {
		// 
		List<String> listOfExtensionsTmp = Arrays.asList(inputArray);
		List<String> listOfExtensions = new ArrayList<String>();
		Iterator<String> tmpIter = listOfExtensionsTmp.iterator();
		while (tmpIter.hasNext()) {
			String tmpStr = tmpIter.next();
			if (tmpStr.length() > 0 && tmpStr.contains(ReportConstants.COMMA)) {
				String[] extArray = tmpStr.split(ReportConstants.COMMA);
				List<String> extList = Arrays.asList(extArray);
				Iterator<String> extTmpIter = extList.iterator();
				while (extTmpIter.hasNext()) {
					String extEntry = extTmpIter.next();
					extEntry = extEntry.replaceAll(ReportConstants.SPACE, ReportConstants.EMPTYSTRING);
					listOfExtensions.add(extEntry);
				}
			} else {
				listOfExtensions.add(tmpStr);
			}
		}
		return listOfExtensions;
	}
	/**
	 * Creates LODVendor collection and also DipVendorId entries with links to 
	 * DipFormatId and DipSoftwareId collections
	 * @throws FfmaCommonException 
	 */
	private String storeLodVendor() throws FfmaCommonException {
		String res = ReportConstants.EMPTYSTRING;
		List<LODVendor> lodVendorList = LODStatisticsUtils.getLodVendors();
		if (lodVendorList != null && lodVendorList.size() > 0) {
			Iterator<LODVendor> iterVendors = lodVendorList.iterator();
			while (iterVendors.hasNext()) {
				LODVendor lodVendor = iterVendors.next();
				storeLodVendor(lodVendor);				
				// create DipVendorId object
				DipVendorId dipVendorId = new DipVendorId();
				dipVendorId.setDipId(LODConstants.DIP + lodVendor.getOrganisationName());
				dipVendorId.setName(lodVendor.getOrganisationName());
				List<String> vendorIdList = new ArrayList<String>();
				vendorIdList.add(lodVendor.getRepositoryId());
				setAitIdForVendor(lodVendor, vendorIdList, dipVendorId); 
				setFreebaseIdForVendor(lodVendor, vendorIdList, dipVendorId);
				setPronomIdForVendor(lodVendor, vendorIdList, dipVendorId);
				setDBPediaIdForVendor(lodVendor, vendorIdList, dipVendorId);
				List<String> dipFormatIdList = new ArrayList<String>();
				createDipIdForVendor(lodVendor, dipFormatIdList);
				dipVendorId.setDipFormatId(dipFormatIdList.toArray(new String[dipFormatIdList.size()]));
				List<String> dipSoftwareIdList = new ArrayList<String>();
				dipSoftwareIdList.add(LODConstants.DIP + lodVendor.getSoftwareName());
				dipVendorId.setDipSoftwareId(dipSoftwareIdList.toArray(new String[dipSoftwareIdList.size()]));
				List<String> descriptionsList = new ArrayList<String>();
				descriptionsList.add(lodVendor.getDescription());
				dipVendorId.setDescription(descriptionsList.toArray(new String[descriptionsList.size()]));		
				addDipVendorIdLinkToExistingDipFormatIdObjects(lodVendor,
						dipVendorId, vendorIdList);
			}
			res = res + LODVendor.class.getSimpleName() + ReportConstants.SEMICOLON;
			res = res + DipVendorId.class.getSimpleName() + ReportConstants.SEMICOLON;
		}
		return res;
	}

	/**
	 * @param lodVendor
	 * @param dipFormatIdList
	 */
	private void createDipIdForVendor(LODVendor lodVendor,
			List<String> dipFormatIdList) {
		if (lodVendor.getFileFormat() != null && lodVendor.getFileFormat().length > 0) {
			List<String> listOfExtensions = normalizeExtensionsList(lodVendor.getFileFormat());
			Iterator<String> ffIter = listOfExtensions.iterator();
			while (ffIter.hasNext()) {
				String ext = ffIter.next();
				addExtensionsForVendor(dipFormatIdList, ext);
			}
		}
	}

	/**
	 * @param dipFormatIdList
	 * @param ext
	 */
	private void addExtensionsForVendor(List<String> dipFormatIdList, String ext) {
		if (ext.length() > 0 && ext.contains(ReportConstants.COMMA)) {
			String[] extArray = ext.split(ReportConstants.COMMA);
			List<String> extList = Arrays.asList(extArray);
			Iterator<String> extIter = extList.iterator();
			while (extIter.hasNext()) {
				String extEntry = extIter.next();
				extEntry = extEntry.replaceAll(ReportConstants.SPACE, ReportConstants.EMPTYSTRING);
				dipFormatIdList.add(LODConstants.DIP + extEntry);
			}
		} else {
			dipFormatIdList.add(LODConstants.DIP + ext);
		}
	}

	/**
	 * @param lodVendor
	 */
	private void storeLodVendor(LODVendor lodVendor) {
		LODVendor checkLodVendor = new LODVendor();
		checkLodVendor.setRepository(lodVendor.getRepository());
		checkLodVendor.setOrganisationName(lodVendor.getOrganisationName());
		try { 
			preservationRiskmanagementDao.updateObject(checkLodVendor, lodVendor);
		} catch (FfmaCommonException e) {
			log.debug(ReportConstants.ERROR_UPDATE_FREEBASSE_EXTENSIONS + e);
		}
	}

	/**
	 * @param lodVendor
	 * @param dipVendorId
	 * @param vendorIdList
	 * @throws FfmaCommonException
	 */
	private void addDipVendorIdLinkToExistingDipFormatIdObjects(
			LODVendor lodVendor, DipVendorId dipVendorId,
			List<String> vendorIdList) throws FfmaCommonException {

		if (lodVendor.getFileFormat() != null && lodVendor.getFileFormat().length > 0) {
			List<String> listOfExtensions2 = normalizeExtensionsList(lodVendor.getFileFormat());
			Iterator<String> ffIter2 = listOfExtensions2.iterator();
			while (ffIter2.hasNext()) {
				String ext2 = ffIter2.next();
				DipFormatId dipFormatId = new DipFormatId();
				dipFormatId.setDipId(LODConstants.DIP + ext2);
				List<String> exclusionsList = new ArrayList<String>();
				exclusionsList.add(RiskConstants.DOMAIN_OBJECT_NAME);
				updateVendorLinks(dipVendorId, dipFormatId, exclusionsList);				
				addDipVendorIdLinkToExistingDipSoftwareIdObjects(
						lodVendor, dipVendorId, exclusionsList);				
				updateDipVendorIdCollectionWithNewEntry(lodVendor,
						dipVendorId, vendorIdList, ext2, exclusionsList);
			}
		}
	}

	/**
	 * @param dipVendorId
	 * @param dipFormatId
	 * @param exclusionsList
	 * @throws FfmaCommonException
	 */
	private void updateVendorLinks(DipVendorId dipVendorId,
			DipFormatId dipFormatId, List<String> exclusionsList)
			throws FfmaCommonException {
		List<? extends FfmaDomainObject> dipFormats = preservationRiskmanagementDao
			.retrieveCollection((FfmaDomainObject) dipFormatId, exclusionsList);
		Iterator<? extends FfmaDomainObject> iter = dipFormats.iterator();
		while (iter.hasNext()) {
			DipFormatId dipFormatIdEntry = (DipFormatId) iter.next();
			dipFormatIdEntry.setDipVendorId(LODStatisticsUtils
					.addValueToStringArray(
							dipFormatIdEntry.getDipVendorId(),
							dipVendorId.getDipId()));
			preservationRiskmanagementDao.updateObject(dipFormatId, dipFormatIdEntry);
		}
	}

	/**
	 * @param lodVendor
	 * @param dipVendorId
	 * @param exclusionsList
	 * @throws FfmaCommonException
	 */
	private void addDipVendorIdLinkToExistingDipSoftwareIdObjects(
			LODVendor lodVendor, DipVendorId dipVendorId,
			List<String> exclusionsList) throws FfmaCommonException {

		DipSoftwareId dipSoftwareId = new DipSoftwareId();
		dipSoftwareId.setDipId(LODConstants.DIP + lodVendor.getSoftwareName());
		List<? extends FfmaDomainObject> dipSoftwareList = preservationRiskmanagementDao
			.retrieveCollection((FfmaDomainObject) dipSoftwareId, exclusionsList);
		Iterator<? extends FfmaDomainObject> iterSw = dipSoftwareList.iterator();
		while (iterSw.hasNext()) {
			DipSoftwareId dipSoftwareIdEntry = (DipSoftwareId) iterSw.next();
			dipSoftwareIdEntry.setDipVendorId(LODStatisticsUtils
					.addValueToStringArray(
							dipSoftwareIdEntry.getDipVendorId(),
							dipVendorId.getDipId()));
			preservationRiskmanagementDao.updateObject(dipSoftwareId, dipSoftwareIdEntry);
		}
	}

	/**
	 * @param lodVendor
	 * @param dipVendorId
	 * @param vendorIdList
	 * @param ext2
	 * @param exclusionsList
	 * @throws FfmaCommonException
	 */
	private void updateDipVendorIdCollectionWithNewEntry(LODVendor lodVendor,
			DipVendorId dipVendorId, List<String> vendorIdList, String ext2,
			List<String> exclusionsList) throws FfmaCommonException {

		DipVendorId checkDipVendorId = new DipVendorId();
		checkDipVendorId.setDipId(LODConstants.DIP + lodVendor.getOrganisationName());
		checkDipVendorId.setName(lodVendor.getOrganisationName());
		if (preservationRiskmanagementDao.existsInDb(checkDipVendorId)) {
			List<? extends FfmaDomainObject> dipVendorIdList = preservationRiskmanagementDao
				.retrieveCollection((FfmaDomainObject) checkDipVendorId, exclusionsList);
			Iterator<? extends FfmaDomainObject> iterVendorId = dipVendorIdList.iterator();
			while (iterVendorId.hasNext()) {
				DipVendorId dipVendorIdEntry = (DipVendorId) iterVendorId.next();
				setIdsForVendor(lodVendor, vendorIdList, ext2,
						checkDipVendorId, dipVendorIdEntry);
			}
		} else {
			storeDipVendorId(dipVendorId);
		}
	}

	/**
	 * @param lodVendor
	 * @param vendorIdList
	 * @param ext2
	 * @param checkDipVendorId
	 * @param dipVendorIdEntry
	 * @throws FfmaCommonException
	 */
	private void setIdsForVendor(LODVendor lodVendor,
			List<String> vendorIdList, String ext2,
			DipVendorId checkDipVendorId, DipVendorId dipVendorIdEntry)
			throws FfmaCommonException {
		setAitIdForVendor(lodVendor, vendorIdList, dipVendorIdEntry); 
		setFreebaseIdForVendor(lodVendor, vendorIdList, dipVendorIdEntry);
		setPronomIdForVendor(lodVendor, vendorIdList, dipVendorIdEntry);
		setDBPediaIdForVendor(lodVendor, vendorIdList, dipVendorIdEntry);
		String formatId = LODConstants.DIP + ext2;
		String softwareId = LODConstants.DIP + lodVendor.getSoftwareName();
		dipVendorIdEntry.setDipFormatId(LODStatisticsUtils
				.addValueToStringArray(
						dipVendorIdEntry.getDipFormatId(),
						formatId));
		dipVendorIdEntry.setDipSoftwareId(LODStatisticsUtils
				.addValueToStringArray(
						dipVendorIdEntry.getDipSoftwareId(),
						softwareId));
		dipVendorIdEntry.setDescription(LODStatisticsUtils
				.addValueToStringArray(
						dipVendorIdEntry.getDescription(),
						lodVendor.getDescription()));
		preservationRiskmanagementDao.updateObject(checkDipVendorId, dipVendorIdEntry);
	}

	/**
	 * @param lodVendor
	 * @param vendorIdList
	 * @param dipVendorIdEntry
	 */
	private void setDBPediaIdForVendor(LODVendor lodVendor,
			List<String> vendorIdList, DipVendorId dipVendorIdEntry) {
		if (lodVendor.getRepository() != null && lodVendor.getRepository().equals(LODConstants.LODRepositories.DBPedia.name())) {
			dipVendorIdEntry.setDBPediaId(vendorIdList.toArray(new String[vendorIdList.size()]));
		}
	}

	/**
	 * @param lodVendor
	 * @param vendorIdList
	 * @param dipVendorIdEntry
	 */
	private void setPronomIdForVendor(LODVendor lodVendor,
			List<String> vendorIdList, DipVendorId dipVendorIdEntry) {
		if (lodVendor.getRepository() != null && lodVendor.getRepository().equals(LODConstants.LODRepositories.Pronom.name())) {
			dipVendorIdEntry.setPronomId(vendorIdList.toArray(new String[vendorIdList.size()]));
		}
	}

	/**
	 * @param lodVendor
	 * @param vendorIdList
	 * @param dipVendorIdEntry
	 */
	private void setFreebaseIdForVendor(LODVendor lodVendor,
			List<String> vendorIdList, DipVendorId dipVendorIdEntry) {
		if (lodVendor.getRepository() != null && lodVendor.getRepository().equals(LODConstants.LODRepositories.Freebase.name())) {
			dipVendorIdEntry.setFreebaseId(vendorIdList.toArray(new String[vendorIdList.size()]));
		}
	}

	/**
	 * @param lodVendor
	 * @param vendorIdList
	 * @param dipVendorIdEntry
	 */
	private void setAitIdForVendor(LODVendor lodVendor,
			List<String> vendorIdList, DipVendorId dipVendorIdEntry) {
		if (lodVendor.getRepository() != null && lodVendor.getRepository().equals(LODConstants.LODRepositories.AIT.name())) {
			dipVendorIdEntry.setAitId(vendorIdList.toArray(new String[vendorIdList.size()]));
		}
	}

	/**
	 * @param dipVendorId
	 */
	private void storeDipVendorId(DipVendorId dipVendorId) {
		try {
			preservationRiskmanagementDao.storeObject(dipVendorId);
		} catch (ObjectNotStoredException e) {
			log.info(e);
		} catch (ObjectNotFoundException e) {
			log.info(e);
		}
	}

	/**
	 *  Retrieve repository IDs and descriptions from PronomFileFormat database collection
	 */
	private void addRepositoryIdAndDescriptionToPronomLodFormats() {
		List<LODFormat> lodFormatsList = LODStatisticsUtils.getLodFormats();
		Iterator<? extends FfmaDomainObject> iter = lodFormatsList.iterator();
		while (iter.hasNext()) {
			LODFormat lodFormat = (LODFormat) iter.next();
			if (lodFormat.getRepository().equals(LODConstants.LODRepositories.Pronom.name())) {
				PronomFileFormat pronomFileFormat = new PronomFileFormat();
				pronomFileFormat.setFfmaObjectName(PronomFileFormat.class.getSimpleName());
				pronomFileFormat.setExtension(lodFormat.getFileExtensions());
				List<String> repositoryIDs = new ArrayList<String>();
				List<String> descriptions = new ArrayList<String>();
				List<String> exclusionsList = new ArrayList<String>();
				exclusionsList.add(RiskConstants.DOMAIN_OBJECT_NAME);
				addRepositoryIdsToPronom(pronomFileFormat, repositoryIDs,
						descriptions, exclusionsList);
				setRepositoryIdsForFormat(lodFormat, repositoryIDs);
				setDescriptionsForFormat(lodFormat, descriptions);
			}
		}
		List<LODFormat> lodFormatsListtmp = LODStatisticsUtils.getLodFormats();
        log.info(lodFormatsListtmp);
	}

	/**
	 * @param pronomFileFormat
	 * @param repositoryIDs
	 * @param descriptions
	 * @param exclusionsList
	 */
	private void addRepositoryIdsToPronom(PronomFileFormat pronomFileFormat,
			List<String> repositoryIDs, List<String> descriptions,
			List<String> exclusionsList) {
		List<? extends FfmaDomainObject> fileFormats = preservationRiskmanagementDao
			.retrieveCollection((FfmaDomainObject) pronomFileFormat, exclusionsList);
		Iterator<? extends FfmaDomainObject> fileFormatsIter = fileFormats.iterator();
		while (fileFormatsIter.hasNext()) {
			PronomFileFormat fileFormat = (PronomFileFormat) fileFormatsIter.next();
			addPuidsToPronom(repositoryIDs, fileFormat);
			addXpuidToPronom(repositoryIDs, fileFormat);
			addDescriptionsToPronom(descriptions, fileFormat);
		}
	}

	/**
	 * @param descriptions
	 * @param fileFormat
	 */
	private void addDescriptionsToPronom(List<String> descriptions,
			PronomFileFormat fileFormat) {
		if (fileFormat.getDescription() != null && fileFormat.getDescription().length() > 0) {
			descriptions.add(fileFormat.getDescription());
		}
	}

	/**
	 * @param repositoryIDs
	 * @param fileFormat
	 */
	private void addXpuidToPronom(List<String> repositoryIDs,
			PronomFileFormat fileFormat) {
		if (fileFormat.getXpuid() != null && fileFormat.getXpuid().length() > 0) {
			repositoryIDs.add(fileFormat.getXpuid());
		}
	}

	/**
	 * @param repositoryIDs
	 * @param fileFormat
	 */
	private void addPuidsToPronom(List<String> repositoryIDs,
			PronomFileFormat fileFormat) {
		if (fileFormat.getPuid() != null && fileFormat.getPuid().length() > 0) {
			if (!fileFormat.getPuid().equals(LODConstants.EMPTY_PUID)) {
				repositoryIDs.add(fileFormat.getPuid());
			}
		}
	}

	/**
	 * @param lodFormat
	 * @param descriptions
	 */
	private void setDescriptionsForFormat(LODFormat lodFormat,
			List<String> descriptions) {
		if (descriptions.size() > 0) {
			lodFormat.setDescription(descriptions.toArray(new String[descriptions.size()]));
		}
	}

	/**
	 * @param lodFormat
	 * @param repositoryIDs
	 */
	private void setRepositoryIdsForFormat(LODFormat lodFormat,
			List<String> repositoryIDs) {
		if (repositoryIDs.size() > 0) {
			lodFormat.setRepositoryId(repositoryIDs.toArray(new String[repositoryIDs.size()]));
		}
	}

	/**
	 * In this method fill AitFileFormat, FreebaseFileFormat, DBPediaFileFormat and PronomFileFormat collections
	 * then fill LODFormat, LODSoftware, LODVendor collections
	 * @param type
	 * @param overwriteRepositoryFormats
	 * @param overwriteLodData
	 * @param preservationRiskmanagementDao
	 * @return
	 */
	public String storeAllExtensions(String type,
			boolean overwriteRepositoryFormats, boolean overwriteLodData) {
		String res = ReportConstants.EMPTYSTRING;
		LODUtils.initCalculationModel();
		res = overwriteFreebaseRepositoryFormats(type, overwriteRepositoryFormats, res);
		res = overwritePronomRepositoryFormats(type, overwriteRepositoryFormats, res);
		res = overwriteDBPediaRepositoryFormats(type, overwriteRepositoryFormats, res);
		res = overwriteAitRepositoryFormats(type, overwriteRepositoryFormats, res);

		// fill DipFormatId, DipSoftwareId and DipVendorId collections
		AitFileFormat aitFileFormat = new AitFileFormat();
		PronomFileFormat pronomFileFormat = new PronomFileFormat();
		DBPediaFileFormat dbpediaFileFormat = new DBPediaFileFormat();
		FreebaseFileFormat freebaseFileFormat = new FreebaseFileFormat();
		try {
			List<String> exclusionsList = new ArrayList<String>();
			exclusionsList.add(RiskConstants.DOMAIN_OBJECT_NAME);
			List<? extends FfmaDomainObject> aitFileFormats = retrieveAitFormats(type, aitFileFormat, exclusionsList);
			List<? extends FfmaDomainObject> pronomFileFormats = retrievePronomFormats(type, pronomFileFormat, exclusionsList);
			List<? extends FfmaDomainObject> dbpediaFileFormats = retrieveDBPediaFormats(type, dbpediaFileFormat, exclusionsList);
			List<? extends FfmaDomainObject> freebaseFileFormats = retrieveFreebaseFormats(type, freebaseFileFormat, exclusionsList);
			LODStatisticsUtils.initFileFormatListFromAllRepositories(
					aitFileFormats, pronomFileFormats, dbpediaFileFormats, freebaseFileFormats);
			storeDipFormatIdInDatabase(exclusionsList);			
			overwriteLodDataInDb(type, overwriteLodData, exclusionsList);
			res = res + storeLodFormat();
			res = res + storeLodSoftware();
			res = res + storeLodVendor();
		} catch (ObjectNotStoredException e) {
			log.info(ReportConstants.CAUGHT_OBJECT_NOT_RETRIEVED_EXCEPTION_ERROR + e);
		} catch (ObjectNotFoundException e) {
			log.info(ReportConstants.CAUGHT_OBJECT_NOT_RETRIEVED_EXCEPTION_ERROR + e);
		} catch (FfmaCommonException e) {
			log.info(ReportConstants.CAUGHT_OBJECT_NOT_RETRIEVED_EXCEPTION_ERROR + e);
		}
		return res;
	}

	/**
	 * @param type
	 * @param overwriteRepositoryFormats
	 * @param res
	 * @return
	 */
	public String overwriteAitRepositoryFormats(String type,
			boolean overwriteRepositoryFormats, String res) {
		String resStr = res;
		if (overwriteRepositoryFormats
				&& type.equals(LODConstants.LODRepositories.AIT.name())
				|| type.equals(LODConstants.LODRepositories.All.name())) {
			List<FfmaDomainObject> aitFormatList = LODStatisticsUtils.searchAllFileFormatsInAIT();
			try {
				/* remove stored in database AIT file formats */
				preservationRiskmanagementDao.removeCollectionByName(AitFileFormat.class.getSimpleName());
				/* store new file formats */
				preservationRiskmanagementDao.storeObjectsList(aitFormatList);
				resStr = res + AitFileFormat.class.getSimpleName() + ReportConstants.SEMICOLON;
			} catch (Exception e) {
				log.info(CANNOT_STORE_FFMA_FULL_DOC_OBJECT_LIST + e);
			} finally {
				aitFormatList.clear();
			}
		}
		return resStr;
	}

	/**
	 * @param type
	 * @param overwriteRepositoryFormats
	 * @param res
	 * @return
	 */
	public String overwriteDBPediaRepositoryFormats(String type,
			boolean overwriteRepositoryFormats, String res) {
		String resStr = res;
		if (overwriteRepositoryFormats
				&& type.equals(LODConstants.LODRepositories.DBPedia.name())
				|| type.equals(LODConstants.LODRepositories.All.name())) {
			List<FfmaDomainObject> dbPediaFormatList = LODStatisticsUtils.searchAllFileFormatsInDBPedia();
			try {
				/* remove stored in database DBPedia file formats */
				preservationRiskmanagementDao.removeCollectionByName(DBPediaFileFormat.class.getSimpleName());
				/* store new file formats */
				preservationRiskmanagementDao.storeObjectsList(dbPediaFormatList);
				resStr = res + DBPediaFileFormat.class.getSimpleName() + ReportConstants.SEMICOLON;
			} catch (Exception e) {
				log.info(CANNOT_STORE_FFMA_FULL_DOC_OBJECT_LIST + e);
			} finally {
				dbPediaFormatList.clear();
			}
		}
		return resStr;
	}

	/**
	 * @param type
	 * @param overwriteRepositoryFormats
	 * @param res
	 * @return
	 */
	public String overwritePronomRepositoryFormats(String type,
			boolean overwriteRepositoryFormats, String res) {
		String resStr = res;
		if (overwriteRepositoryFormats
				&& type.equals(LODConstants.LODRepositories.Pronom.name())
				|| type.equals(LODConstants.LODRepositories.All.name())) {
			List<FfmaDomainObject> pronomFormatList = LODStatisticsUtils.searchAllFileFormatsInPronom();
			try {
				/* remove stored in database Pronom file formats */
				preservationRiskmanagementDao.removeCollectionByName(PronomFileFormat.class.getSimpleName());
				/* store new file formats */
				preservationRiskmanagementDao.storeObjectsList(pronomFormatList);
				resStr = res + PronomFileFormat.class.getSimpleName() + ReportConstants.SEMICOLON;
			} catch (Exception e) {
				log.info(CANNOT_STORE_FFMA_FULL_DOC_OBJECT_LIST + e);
			} finally {
				pronomFormatList.clear();
			}
		}
		return resStr;
	}

	/**
	 * @param type
	 * @param overwriteRepositoryFormats
	 * @param res
	 * @return
	 */
	public String overwriteFreebaseRepositoryFormats(String type,
			boolean overwriteRepositoryFormats, String res) {
		String resStr = res;
		if (overwriteRepositoryFormats
				&& type.equals(LODConstants.LODRepositories.Freebase.name())
				|| type.equals(LODConstants.LODRepositories.All.name())) {
			List<FfmaDomainObject> freebaseFormatList = LODStatisticsUtils.searchAllFileFormatsInFreebase();
			try {
				/* remove stored in database Freebase file formats */
				preservationRiskmanagementDao.removeCollectionByName(FreebaseFileFormat.class.getSimpleName());
				/* store new file formats */
				preservationRiskmanagementDao.storeObjectsList(freebaseFormatList);
				resStr = res + FreebaseFileFormat.class.getSimpleName() + ReportConstants.SEMICOLON;
			} catch (Exception e) {
				log.info(CANNOT_STORE_FFMA_FULL_DOC_OBJECT_LIST + e);
			} finally {
				freebaseFormatList.clear();
			}
		}
		return resStr;
	}

	/**
	 * @param type
	 * @param freebaseFileFormat
	 * @param exclusionsList
	 * @return
	 */
	public List<? extends FfmaDomainObject> retrieveFreebaseFormats(
			String type, FreebaseFileFormat freebaseFileFormat,
			List<String> exclusionsList) {
		List<? extends FfmaDomainObject> freebaseFileFormats = null;
		if (type.equals(LODConstants.LODRepositories.Freebase.name()) || type.equals(LODConstants.LODRepositories.All.name())) {
			freebaseFileFormats = preservationRiskmanagementDao
				.retrieveCollection((FfmaDomainObject) freebaseFileFormat, exclusionsList);
		}
		return freebaseFileFormats;
	}

	/**
	 * @param type
	 * @param dbpediaFileFormat
	 * @param exclusionsList
	 * @return
	 */
	public List<? extends FfmaDomainObject> retrieveDBPediaFormats(
			String type, DBPediaFileFormat dbpediaFileFormat,
			List<String> exclusionsList) {
		List<? extends FfmaDomainObject> dbpediaFileFormats = null;
		if (type.equals(LODConstants.LODRepositories.DBPedia.name()) || type.equals(LODConstants.LODRepositories.All.name())) {
			dbpediaFileFormats = preservationRiskmanagementDao
				.retrieveCollection((FfmaDomainObject) dbpediaFileFormat, exclusionsList);
		}
		return dbpediaFileFormats;
	}

	/**
	 * @param type
	 * @param pronomFileFormat
	 * @param exclusionsList
	 * @return
	 */
	public List<? extends FfmaDomainObject> retrievePronomFormats(
			String type, PronomFileFormat pronomFileFormat,
			List<String> exclusionsList) {
		List<? extends FfmaDomainObject> pronomFileFormats = null;
		if (type.equals(LODConstants.LODRepositories.Pronom.name()) || type.equals(LODConstants.LODRepositories.All.name())) {
			pronomFileFormats = preservationRiskmanagementDao
				.retrieveCollection((FfmaDomainObject) pronomFileFormat, exclusionsList);
		}
		return pronomFileFormats;
	}

	/**
	 * @param type
	 * @param aitFileFormat
	 * @param exclusionsList
	 * @return
	 */
	public List<? extends FfmaDomainObject> retrieveAitFormats(String type,
			AitFileFormat aitFileFormat, List<String> exclusionsList) {
		List<? extends FfmaDomainObject> aitFileFormats = null;
		if (type.equals(LODConstants.LODRepositories.AIT.name()) || type.equals(LODConstants.LODRepositories.All.name())) {
			aitFileFormats = preservationRiskmanagementDao
				.retrieveCollection((FfmaDomainObject) aitFileFormat, exclusionsList);
		}
		return aitFileFormats;
	}

	/**
	 * @param type
	 * @param overwriteLodData
	 * @param exclusionsList
	 */
	private void overwriteLodDataInDb(String type, boolean overwriteLodData,
			List<String> exclusionsList) {
		if (overwriteLodData) {
			LODStatisticsUtils.clearCache();
			overwriteAitData(type);
			overwriteFreebaseData(type);
			overwriteDBPediaData(type);
			overwritePronomData(type);
		} else {
			LODFormat lodFormat = new LODFormat();
			List<? extends FfmaDomainObject> lodFormatList = preservationRiskmanagementDao
				.retrieveCollection((FfmaDomainObject) lodFormat, exclusionsList);
			LODStatisticsUtils.setLodFormats(lodFormatList);
			LODSoftware lodSoftware = new LODSoftware();
			List<? extends FfmaDomainObject> lodSoftwareList = preservationRiskmanagementDao
				.retrieveCollection((FfmaDomainObject) lodSoftware, exclusionsList);
			LODStatisticsUtils.setLodSoftware(lodSoftwareList);
			LODVendor lodVendor = new LODVendor();
			List<? extends FfmaDomainObject> lodVendorList = preservationRiskmanagementDao
				.retrieveCollection((FfmaDomainObject) lodVendor, exclusionsList);
			LODStatisticsUtils.setLodVendors(lodVendorList);
		}
	}

	/**
	 * @param type
	 */
	private void overwritePronomData(String type) {
		if (type.equals(LODConstants.LODRepositories.Pronom.name()) || type.equals(LODConstants.LODRepositories.All.name())) {
			LODStatisticsUtils.searchPronomFileFormatInformation();
			addRepositoryIdAndDescriptionToPronomLodFormats();
		}
	}

	/**
	 * @param type
	 */
	private void overwriteDBPediaData(String type) {
		if (type.equals(LODConstants.LODRepositories.DBPedia.name()) || type.equals(LODConstants.LODRepositories.All.name())) {
			LODStatisticsUtils.searchDBPediaFileFormatInformation();
		}
	}

	/**
	 * @param type
	 */
	private void overwriteFreebaseData(String type) {
		if (type.equals(LODConstants.LODRepositories.Freebase.name()) || type.equals(LODConstants.LODRepositories.All.name())) {
			LODStatisticsUtils.searchFreebaseFileFormatInformation();
		}
	}

	/**
	 * @param type
	 */
	private void overwriteAitData(String type) {
		if (type.equals(LODConstants.LODRepositories.AIT.name()) || type.equals(LODConstants.LODRepositories.All.name())) {
			LODStatisticsUtils.searchAllFileFormatsInAIT();
		}
	}

	/**
	 * @param exclusionsList
	 * @throws ObjectNotStoredException
	 * @throws ObjectNotFoundException
	 * @throws FfmaCommonException
	 */
	private void storeDipFormatIdInDatabase(List<String> exclusionsList)
			throws ObjectNotStoredException, ObjectNotFoundException,
			FfmaCommonException {

		Iterator<? extends FfmaDomainObject> iterFormatId = LODStatisticsUtils.getDipFormatIdList().iterator();
		while (iterFormatId.hasNext()) {
			DipFormatId dipFormatId = (DipFormatId) iterFormatId.next();
			DipFormatId checkDipFormatId = new DipFormatId();
			checkDipFormatId.setDipId(dipFormatId.getDipId());
			if (!preservationRiskmanagementDao.existsInDb(checkDipFormatId)) {
				preservationRiskmanagementDao.storeObject(dipFormatId);
			} else {
				updateDipFormatId(exclusionsList, dipFormatId, checkDipFormatId);
			}
		}
	}

	/**
	 * @param exclusionsList
	 * @param dipFormatId
	 * @param checkDipFormatId
	 * @throws FfmaCommonException
	 */
	private void updateDipFormatId(List<String> exclusionsList,
			DipFormatId dipFormatId, DipFormatId checkDipFormatId)
			throws FfmaCommonException {
		List<? extends FfmaDomainObject> dipFormatIdList = preservationRiskmanagementDao
			.retrieveCollection((FfmaDomainObject) checkDipFormatId, exclusionsList);
		Iterator<? extends FfmaDomainObject> iterFormatIdExist = dipFormatIdList.iterator();
		while (iterFormatIdExist.hasNext()) {
			DipFormatId dipFormatIdEntry = (DipFormatId) iterFormatIdExist.next();
			addAitIdToDipFormat(dipFormatId, dipFormatIdEntry); 
			addFreebaseIdToDipFormat(dipFormatId, dipFormatIdEntry); 
			addDBPediaIdToDipFormat(dipFormatId, dipFormatIdEntry); 
			addPronomIdToDipFormat(dipFormatId, dipFormatIdEntry); 
			addDescriptionToDipFormat(dipFormatId, dipFormatIdEntry);
			preservationRiskmanagementDao.updateObject(checkDipFormatId, dipFormatIdEntry);
		}
	}

	/**
	 * @param dipFormatId
	 * @param dipFormatIdEntry
	 */
	private void addAitIdToDipFormat(DipFormatId dipFormatId,
			DipFormatId dipFormatIdEntry) {
		if (dipFormatId.getAitId() != null && dipFormatId.getAitId().length > 0) { 
			if (dipFormatId.getAitId()[0] != null && !dipFormatId.getAitId()[0].equals(ReportConstants.DIP_NULL) 
					&& !dipFormatId.getAitId()[0].equals(ReportConstants.SPACE)) {
				dipFormatIdEntry.setAitId(dipFormatId.getAitId());
			}
		}
	}

	/**
	 * @param dipFormatId
	 * @param dipFormatIdEntry
	 */
	private void addFreebaseIdToDipFormat(DipFormatId dipFormatId,
			DipFormatId dipFormatIdEntry) {
		if (dipFormatId.getFreebaseId() != null && dipFormatId.getFreebaseId().length > 0) {
			if (dipFormatId.getFreebaseId()[0] != null && !dipFormatId.getFreebaseId()[0].equals(ReportConstants.DIP_NULL)
				&& !dipFormatId.getFreebaseId()[0].equals(ReportConstants.SPACE)) {
				dipFormatIdEntry.setFreebaseId(dipFormatId.getFreebaseId());
			}
		}
	}

	/**
	 * @param dipFormatId
	 * @param dipFormatIdEntry
	 */
	private void addDBPediaIdToDipFormat(DipFormatId dipFormatId,
			DipFormatId dipFormatIdEntry) {
		if (dipFormatId.getDBPediaId() != null && dipFormatId.getDBPediaId().length > 0) { 
			if (dipFormatId.getDBPediaId()[0] != null && !dipFormatId.getDBPediaId()[0].equals(ReportConstants.DIP_NULL) 
				&& !dipFormatId.getDBPediaId()[0].equals(ReportConstants.SPACE)) {
				dipFormatIdEntry.setDBPediaId(dipFormatId.getDBPediaId());
			}
		}
	}

	/**
	 * @param dipFormatId
	 * @param dipFormatIdEntry
	 */
	private void addPronomIdToDipFormat(DipFormatId dipFormatId,
			DipFormatId dipFormatIdEntry) {
		if (dipFormatId.getPronomId() != null && dipFormatId.getPronomId().length > 0) { 
			if (dipFormatId.getPronomId()[0] != null && !dipFormatId.getPronomId()[0].equals(ReportConstants.DIP_NULL) 
				&& !dipFormatId.getPronomId()[0].equals(ReportConstants.SPACE)) {
				dipFormatIdEntry.setPronomId(dipFormatId.getPronomId());
			}
		}
	}

	/**
	 * @param dipFormatId
	 * @param dipFormatIdEntry
	 */
	private void addDescriptionToDipFormat(DipFormatId dipFormatId,
			DipFormatId dipFormatIdEntry) {
		if (dipFormatId.getDescription() != null && dipFormatId.getDescription().length > 0) {
			List<String> descList = Arrays.asList(dipFormatId.getDescription());
			Iterator<String> descIter = descList.iterator();
			while (descIter.hasNext()) {
				String description = descIter.next();
				dipFormatIdEntry.setDescription(LODStatisticsUtils
						.addValueToStringArray(
								dipFormatIdEntry.getDescription(),
								description));
			}
		}
	}
	
}
