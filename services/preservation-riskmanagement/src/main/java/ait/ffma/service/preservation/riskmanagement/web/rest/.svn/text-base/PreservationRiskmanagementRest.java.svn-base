package ait.ffma.service.preservation.riskmanagement.web.rest;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import javax.activation.MimetypesFileTypeMap;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.commons.lang.time.DateUtils;
import org.openimaj.image.ImageUtilities;
import org.openimaj.image.MBFImage;
import org.springframework.beans.factory.annotation.Autowired;

import ait.ffma.common.exception.FfmaCommonException;
import ait.ffma.common.web.rest.BaseFfmaRest;
import ait.ffma.service.preservation.riskmanagement.PreservationRiskmanagementConfiguration;
import ait.ffma.service.preservation.riskmanagement.api.PreservationRiskmanagementService;
import ait.ffma.service.preservation.riskmanagement.api.fingerdetection.FingerDetector;
import ait.ffma.service.preservation.riskmanagement.api.lod.LODConstants;
import ait.ffma.service.preservation.riskmanagement.api.lod.LODConstants.LODRepositories;
import ait.ffma.service.preservation.riskmanagement.api.riskanalysis.risk.ReportConstants;
import ait.ffma.service.preservation.riskmanagement.api.riskanalysis.risk.RiskConstants;

@Path("/rest")
@Produces(MediaType.TEXT_PLAIN)
public class PreservationRiskmanagementRest extends BaseFfmaRest {

	private static final String TRUE = "true";
	public static final String TYPE = "type";
	public static final String NAME = "name";
	public static final String ID = "id";
	public static final String EXT = "ext";
	public static final String COUNT = "count";
	public static final String CONFIG = "config";
	public static final String CLASSIFICATION = "classification";
	public static final String CONFIGNAME = "configName";
	public static final String CLASSIFICATIONNAME = "classificationName";
	public static final String NULLSTR = "null";
	public static final String DIV = "/";
	public static final String COLLECTION = "collection";

	@Autowired
	private PreservationRiskmanagementService preservationRiskmanagementService;
	
	/**
	 * @return
	 */
	public PreservationRiskmanagementService getPreservationRiskmanagementService() {
		return preservationRiskmanagementService;
	}

	@Autowired
	private PreservationRiskmanagementConfiguration configuration;
	
	/**
	 * @param preservationRiskmanagementService
	 */
	public void setPreservationRiskmanagementService(PreservationRiskmanagementService preservationRiskmanagementService) {
		this.preservationRiskmanagementService = preservationRiskmanagementService;
	}

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
		
	@Override
	public String getComponentName() {
		return getConfiguration().getComponentName();
	}
	
	@GET
	@Path("/component")
	@Produces(MediaType.TEXT_HTML)
	public String displayComponentName() {
		return getComponentName();
	}
		
	/**
	 * @param classification
	 * @return
	 */
	private String checkClassificationParameter(String classification) {
		String classificationStr = classification;
		if (classification != null && classification.length() > 0 && !classification.contains(DIV)) {
			classificationStr = DIV + classification;
		}
		return classificationStr;
	}

	/**
	 * @param configPar
	 * @return
	 */
	private String checkConfigParameter(String configPar) {
		String config = configPar;
		if (config != null && config.length() > 0 && config.equals(NULLSTR)) {
			config = null;
		}
		return config;
	}

	/**
	 * @param id
	 * @return
	 */
	private int checkCollectionIdParameter(String id) {
		int collectionId = -1;
		if (id != null && id.length() > 0) {
			collectionId = Integer.valueOf(id);
		}
		return collectionId;
	}
	
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
	@GET
	@Path("/loddataanalysis/checkdataexist")
	@Produces( MediaType.TEXT_HTML )
	public String checkLodData() {
		return preservationRiskmanagementService.checkLodData();
	}
	
	/**
	 * This method checks if giving LOD repository type is supported.
	 * @param type
	 * @return true if repository type is supported
	 */
	public boolean isSupportedRepositoryType(String type) {
		boolean res = false;
		if (type != null && type.length() > 0) {
			for (LODRepositories repository : LODConstants.LODRepositories.values()) {
		        if (repository.name().equals(type)) {
		            return true;
		        }
			}
		}
		return res;
	}
	
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
	@GET
	@Path("/loddataanalysis/storeallextensions/{type}/{updateformats}/{updateloddata}")
	@Produces({ MediaType.TEXT_HTML })
	public String storeAllExtensions(@PathParam(TYPE) String type,
			@PathParam("updateformats") String updateformats,
			@PathParam("updateloddata") String updateloddata) {
		boolean overwriteRepositoryFormats = false;
		boolean overwriteLodData = false;
		String res = ReportConstants.EMPTYSTRING;
		if (isSupportedRepositoryType(type)) {
			if (updateformats != null && updateformats.length() > 0 && updateformats.equals(TRUE)) {
				overwriteRepositoryFormats = true;
			}
			if (updateloddata != null && updateloddata.length() > 0 && updateloddata.equals(TRUE)) {
				overwriteLodData = true;
			}
			res = preservationRiskmanagementService.storeAllExtensions(type, overwriteRepositoryFormats, overwriteLodData);
		} 
		return res;
	}
	
	/**
	 * This method retrieves associated software for particular file format extension	
	 * @param ext
	 *        The file format extension
	 * @return
	 */
	@GET
	@Path("/loddataanalysis/software/{ext}")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public String retrieveSoftware(@PathParam(EXT) String ext) {
		return preservationRiskmanagementService.retrieveSoftware(ext);
	}
	
	/**
	 * This method retrieves associated software for particular file format extension	
	 * @param ext
	 *        The file format extension
	 * @return the list of matching software in HTML format
	 */
	@GET
	@Path("/loddataanalysis/software/html")
	@Produces({ MediaType.TEXT_HTML })
	public String retrieveSoftwareHtml(@QueryParam(NAME) String name) {
		String report = preservationRiskmanagementService.retrieveSoftware(name);
		return preservationRiskmanagementService.createHtmlForLODSoftware(report, name);
	}
	
	/**
	 * This method retrieves associated vendor for particular file format extension	
	 * @param ext
	 *        The file format extension
	 * @return
	 */
	@GET
	@Path("/loddataanalysis/vendor/{ext}")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public String retrieveVendor(@PathParam(EXT) String ext) {
		return preservationRiskmanagementService.retrieveVendor(ext);
	}
	
	/**
	 * This method retrieves associated vendor for particular file format extension	
	 * @param ext
	 *        The file format extension
	 * @return the list of matching vendors in HTML format
	 */
	@GET
	@Path("/loddataanalysis/vendor/html")
	@Produces({ MediaType.TEXT_HTML })
	public String retrieveVendorHtml(@QueryParam(NAME) String name) {
		String report = preservationRiskmanagementService.retrieveVendor(name);
		return preservationRiskmanagementService.createHtmlForLODVendor(report, name);
	}
	
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
	@GET
	@Path("/loddataanalysis/csv/{type}/{ext}")
	@Produces( MediaType.TEXT_PLAIN )
	public String retrievePreservationStatistic(@PathParam(TYPE) String type,
			@PathParam(EXT) String ext) {
		return preservationRiskmanagementService
				.retrievePreservationStatistic(type, ext);
	}

	/**
	 * This method retrieves DipFormatId object for particular file format extension	
	 * @param ext
	 *        The file format extension
	 * @return the DipFormatId object in HTML format
	 */
	@GET
	@Path("/loddataanalysis/dipformatid/html")
	@Produces({ MediaType.TEXT_HTML })
	public String retrieveDipFormatId(@QueryParam(NAME) String name) {
		return preservationRiskmanagementService.retrieveDipFormatId(name);
	}
	
	/**
	 * This method removes spaces between collection names and converts collection names to the 
	 * enumeration types.
	 * @param configName
	 * @param intern 
	 *        If this parameter is true - add "Enum" extension for conversion to the internal view
	 * @return enriched configurations
	 */
	private String enrichConfigurations(String configStr, boolean intern) {
		String config = configStr.replaceAll(ReportConstants.SPACE, ReportConstants.EMPTYSTRING);
		StringBuffer buf = new StringBuffer();
		buf.append(ReportConstants.EMPTYSTRING);
		List<String> configList = null;
		if (config != null && config.length() > 0) {
			String[] configArray = config.split(ReportConstants.COMMA);
			configList = Arrays.asList(configArray);
			Iterator<String> configIter = configList.iterator();
			while (configIter.hasNext()) {
				String ext = ReportConstants.EMPTYSTRING;
				if (intern) {
					ext = "Enum";
				}
				buf.append(configIter.next());
				buf.append(ext);
				buf.append(ReportConstants.COMMA);
			}
		}
		return buf.toString();
	}
	
	@GET
	@Path("/loddataanalysis/html/riskscorereport")
	@Produces({ MediaType.TEXT_HTML })
	public String createRiskScoreReport(
			@QueryParam(NAME) String name,
			@QueryParam(CONFIGNAME) String configName,
			@QueryParam(CLASSIFICATIONNAME) String classificationName) {
		if (name != null && name.length() > 0) {
			name = name.toLowerCase();
			// should be handled in the future - formats with different spelling and the same meaning
			if (name.equals("tiff")) {
				name = "tif";
			}
			if (name.equals("jpeg")) {
				name = "jpg";
			}

		}
		return getRiskReportHtmlByNameAsId(name,
				enrichConfigurations(configName, true), classificationName, RiskConstants.RiskReportTypesEnum.RiskScore);
	}
	
	/** 
	 * This method analyzes given document image (scan) in order to detect possible unintended placed fingers.
	 * @param img
	 *        The digital document image
	 * @return the path to the image with flagged possible finger regions
	 */
	@GET
	@Path("/fingerdetection")
	@Produces("image/png")
	public Response checkFingers(@QueryParam("img") String img) {
		if (img != null && img.length() > 0) {
		    File f = new File("fingers.png");
	
			try {
				FingerDetector fd = new FingerDetector();
				MBFImage image = fd.analyse(img);
				if (image != null) {
					ImageUtilities.write(image, f);
				} else {
			        throw new WebApplicationException(404);
			    }
			} catch (MalformedURLException e) {
				System.out.println("document path error: " + e.getMessage());
			} catch (IOException e) {
				System.out.println("document path error: " + e.getMessage());
			}
	
		    if (!f.exists()) {
		        throw new WebApplicationException(404);
		    }
		      
		    return returnFile(new File("fingers.png"));
		} else {
	        return Response.status(Status.NOT_FOUND).build();
		}
	}
	
	/**
	 * This method accesses test images by nr parameter. These images have JPG format.
	 * @param nr
	 * @return
	 */
	@GET
	@Path("/fingerfile")
	@Produces("image/png")
	public Response getFingerImage(@QueryParam("nr") String nr) {
		if (nr != null && nr.length() > 0) {
		    File repositoryFile = new File("finger" + nr + ".jpg");
		    return returnFile(repositoryFile);
		} else {
	        return Response.status(Status.NOT_FOUND).build();
		}
	}
	
	/**
	 * This method allows to access test images changing image number directly in path.
	 * @param nr
	 * @return
	 */
	@GET
	@Path("/finger{nr}.jpg")
	@Produces("image/jpg")
	public Response getFingerImageNr(@PathParam("nr") String nr) {
		try {
		    File repositoryFile = new File("finger" + nr + ".jpg");
			return returnFile(repositoryFile);
	    } catch (Exception e) {
	        return Response.status(Status.NOT_FOUND).build();
	    }

	}
	
	/**
	 * This method returns an image file to the web service
	 * @param file
	 * @return
	 */
	public static Response returnFile(File file) {
	    if (!file.exists()) {
	        return Response.status(Status.NOT_FOUND).build();
	    }

	    try {
	        Date fileDate = new Date(file.lastModified());
	        return Response.ok(new FileInputStream(file)).lastModified(fileDate).build();
	    } catch (FileNotFoundException e) {
	        return Response.status(Status.NOT_FOUND).build();
	    }
	}

	/**
	 * This method computes risk report in HTML format applying
	 * passed parameter and file format extension name. 
	 * @param name
	 * @param config
	 * @param classification
	 * @param type
	 * @return risk report
	 */
	private String getRiskReportHtmlByNameAsId(String name, String configName,
			String classificationName, RiskConstants.RiskReportTypesEnum type) {
		String configNameStr = configName;
		if (configName != null && configName.length() > 0 && configName.equals(NULLSTR)) {
			configNameStr = null;
		}
		return preservationRiskmanagementService
			.computeRiskReportHtml(name, configNameStr,
					classificationName, type);
	}
	
	
}

