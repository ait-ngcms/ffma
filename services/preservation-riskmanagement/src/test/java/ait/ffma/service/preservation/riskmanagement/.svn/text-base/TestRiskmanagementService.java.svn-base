package ait.ffma.service.preservation.riskmanagement;

import static org.junit.Assert.assertNotNull;

import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.common.params.ModifiableSolrParams;
import org.apache.solr.common.util.NamedList;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import ait.ffma.common.exception.FfmaCommonException;
import ait.ffma.common.exception.client.FfmaClientException;
import ait.ffma.domain.FfmaDomainObject;
import ait.ffma.domain.preservation.riskmanagement.DipFormatId;
import ait.ffma.domain.preservation.riskmanagement.DipSoftwareId;
import ait.ffma.service.preservation.riskmanagement.api.PreservationRiskmanagementServiceImpl;
import ait.ffma.service.preservation.riskmanagement.api.lod.LODConstants;
import ait.ffma.service.preservation.riskmanagement.api.measurement.Cluster;
import ait.ffma.service.preservation.riskmanagement.api.measurement.DbscanObject;
import ait.ffma.service.preservation.riskmanagement.api.measurement.DbscanObject.pointType;
import ait.ffma.service.preservation.riskmanagement.api.measurement.Neighbor;
import ait.ffma.service.preservation.riskmanagement.api.measurement.Neighborhood;
import ait.ffma.service.preservation.riskmanagement.api.measurement.Node;
import ait.ffma.service.preservation.riskmanagement.api.riskanalysis.risk.ReportConstants;
import ait.ffma.service.preservation.riskmanagement.api.riskanalysis.risk.RiskConstants.RiskReportTypesEnum;

@ContextConfiguration(locations = { "/ffma-preservation-riskmanagement-application-context.xml",
//		"/core-application-context.xml", 
		"/ffma-common-api-server-application-context.xml"})
@RunWith(SpringJUnit4ClassRunner.class)
public class TestRiskmanagementService {

	private final static String PDF = "pdf";
	private final static String JPG = "jpg";
	
	private static final String DIP_ID = "dipId";
	private static final String EXT = "ext";
	private static final String DESCRIPTION = "description";
	
	private static double EPS_VALUE = 0.15;
	private static int MIN_POINTS = 3;

	/**
	 * File format list
	 */
	private static ArrayList<String> ffList = new ArrayList<String>();

	@Autowired
	PreservationRiskmanagementServiceImpl preservationRiskmanagementService;
		
	@BeforeClass 
	public static void initialize() throws Exception {
		System.setProperty("europeana.properties", "../../europeana.properties");
		
		ffList.add("pdf");
		ffList.add("jpg");
		ffList.add("tif");
		ffList.add("png");
		ffList.add("gif");
		ffList.add("bmp");
	}

	/**
	 * Logger
	 */
	private Logger log = Logger.getLogger(getClass().toString());
		
	@Test
	public void fileFormatSolrFunctionality() throws MalformedURLException, URISyntaxException {	
		DipFormatId dipFormatId = preservationRiskmanagementService.retrieveDipFormatIdObj(PDF);
		assertNotNull("The resulting value must be a test value", dipFormatId);
		try {
			preservationRiskmanagementService.storeFFDoc(dipFormatId);
			dipFormatId = preservationRiskmanagementService.readFFDoc(LODConstants.DIP + PDF);
		} catch (FfmaCommonException e) {
			log.info("File format solr update error: " + e);
		}
		assertNotNull("The resulting value must be a test value", dipFormatId);
		
		dipFormatId = preservationRiskmanagementService.retrieveDipFormatIdObj(JPG);
		assertNotNull("The resulting value must be a test value", dipFormatId);
		try {
			preservationRiskmanagementService.storeFFDoc(dipFormatId);
			dipFormatId = preservationRiskmanagementService.readFFDoc(LODConstants.DIP + JPG);
		} catch (FfmaCommonException e) {
			log.info("File format solr update error: " + e);
		}
		assertNotNull("The resulting value must be a test value", dipFormatId);
	}
	
	@Test
	public void storeFileFormatSetInSolr() throws MalformedURLException, URISyntaxException {	
		Iterator<String> iter = ffList.iterator();
		while (iter.hasNext()) {
			String currentFormat = iter.next();
			DipFormatId dipFormatId = preservationRiskmanagementService.retrieveDipFormatIdObj(currentFormat);
			assertNotNull("The resulting value must be a test value", dipFormatId);
			try {
				preservationRiskmanagementService.storeFFDoc(dipFormatId);
				dipFormatId = preservationRiskmanagementService.readFFDoc(LODConstants.DIP + currentFormat);
			} catch (FfmaCommonException e) {
				log.info("File format solr update error: " + e);
			}
			assertNotNull("The resulting value must be a test value", dipFormatId);
		}
	}
	
	@Test
	public void readFileFormatSetFromSolr() throws MalformedURLException, URISyntaxException {	
//		ArrayList<String> descList = new ArrayList<String>();
		Iterator<String> iter = ffList.iterator();
		while (iter.hasNext()) {
			String currentFormat = iter.next();
			DipFormatId dipFormatId = null;
			try {
				dipFormatId = preservationRiskmanagementService.readFFDoc(LODConstants.DIP + currentFormat);
				for (int i = 0; i < dipFormatId.getDescription().length; i++) {
					log.info("Description for file format " + currentFormat + ": " + dipFormatId.getDescription()[i]);
				}
			} catch (FfmaCommonException e) {
				log.info("File format solr update error: " + e);
			}
			assertNotNull("The resulting value must be a test value", dipFormatId);
		}
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void querySolrMoreLikeThis() throws MalformedURLException, URISyntaxException {	
		List<SolrInputDocument> solrList = null;
		try {
		    ModifiableSolrParams params = new ModifiableSolrParams();
		    params.set("q", "class:DipFormatId");
		    params.set("mlt", "true");
		    params.set("mlt.count", "10");
		    params.set("mlt.fl", "description");
		    params.set("mlt.mindf", "1");
		    params.set("mlt.mintf", "1");
		    params.set("fl", "descName,score,ext");
		    params.set("rows", "100");
		
//			solrList = preservationRiskmanagementService.querySolrParams(params);
			NamedList<Object> solrDocumentList = preservationRiskmanagementService.querySolrParams(params);
			log.info("solrDocumentList size: " + solrDocumentList.size());
			for (int n = 0; n < solrDocumentList.size(); n++) {
				String imageName = (String) solrDocumentList.getName(n);
				List<SolrDocument> docs = (List<SolrDocument>) solrDocumentList.getVal(n);
				log.info("description source: " + imageName);
				for (SolrDocument doc : docs) {
					log.info("score: " + doc.get("score") + ", descName: " + doc.get("descName") + ", ext: " + doc.get("ext"));
				}
			}
/*			log.info("solrList size: " + solrList.size());
			log.info("solrList: " + solrList);
			Iterator<SolrInputDocument> iter = solrList.iterator();
			while (iter.hasNext()) {
				SolrInputDocument solrInputDocument = iter.next();
//				try {
					if (solrInputDocument.getField(DIP_ID) != null) {
						String dipId = solrInputDocument.getFieldValue(DIP_ID).toString();
						log.info(DIP_ID + ": " + dipId);
					}
//					if (solrInputDocument.getField(EXT) != null) {
//						dipFormatId.setExtension(solrInputDocument.getFieldValue(EXT).toString());
//					}
//					if (solrInputDocument.getField(DESCRIPTION) != null) {
//						dipFormatId.setDescription(solrInputDocument.getFieldValues(
//								DESCRIPTION).toArray(
//								new String[solrInputDocument
//										.getFieldValues(DESCRIPTION).size()]));
//					}
//					} catch (Exception e) {
//						throw new FfmaTechnicalRuntimeException(
//								"cannot read the given document: " + solrInputDocument,
//								e);
//					}
//				for (int i = 0; i < dipFormatId.getDescription().length; i++) {
//					log.info("Description for file format " + currentFormat + ": " + dipFormatId.getDescription()[i]);
				}*/
			} catch (FfmaCommonException e) {
				log.info("File format solr update error: " + e);
			}
			assertNotNull("The resulting value must be a test value", solrList);
//		}
	}

	@Test
	public void storeLodFormatCollectionInSolr() throws MalformedURLException, URISyntaxException {	
		List<FfmaDomainObject> lodFormatList = null;
		try {
			lodFormatList = preservationRiskmanagementService.retrieveLodFormatCollection();
			log.info("lod format list size: " + lodFormatList.size());
			preservationRiskmanagementService.storeLODFormatsInSolr(lodFormatList);
		} catch (FfmaCommonException e) {
			log.info("retrieve LODFormat collection error: " + e);
		}
		assertNotNull("The resulting value must be a test value", lodFormatList);
//		try {
//			preservationRiskmanagementService.storeLODFormatsInSolr(lodFormatList);
//			dipFormatId = preservationRiskmanagementService.readFFDoc(LODConstants.DIP + currentFormat);
//		} catch (FfmaCommonException e) {
//			log.info("File format solr update error: " + e);
//		}
//		assertNotNull("The resulting value must be a test value", dipFormatId);
//		Iterator<String> iter = ffList.iterator();
//		while (iter.hasNext()) {
//			String currentFormat = iter.next();
//		}
	}

	@Test
	public void storeDipFormatIdCollectionInSolr() throws MalformedURLException, URISyntaxException {	
		List<FfmaDomainObject> dipFormatIdList = null;
		try {
			dipFormatIdList = preservationRiskmanagementService.retrieveDipFormatIdCollection();
			log.info("DipFormatId list size: " + dipFormatIdList.size());
			Iterator<FfmaDomainObject> iter = dipFormatIdList.iterator();
			while (iter.hasNext()) {
				DipFormatId dipFormatId = (DipFormatId) iter.next();
				preservationRiskmanagementService.storeDipFormatIdCollectionInSolr(dipFormatId);
			}
		} catch (FfmaCommonException e) {
			log.info("retrieve LODFormat collection error: " + e);
		}
		assertNotNull("The resulting value must be a test value", dipFormatIdList);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void queryDipFormatIdSolrMoreLikeThis() throws MalformedURLException, URISyntaxException {	
		List<SolrInputDocument> solrList = null;
		try {
		    ModifiableSolrParams params = new ModifiableSolrParams();
		    params.set("q", "class:DipFormatId");
//		    params.set("q", "availability:Pronom");
		    params.set("mlt", "true");
		    params.set("mlt.count", "10");
		    params.set("mlt.fl", "description");
		    params.set("mlt.mindf", "1");
		    params.set("mlt.mintf", "1");
//		    params.set("fl", "extension,mimetype,formatname,score");
//		    params.set("fl", "descName,score,extension,formatname"); //works
		    params.set("fl", "descName,score,availability,extension,mimetype,formatname,software"); //works
//		    params.set("fl", "score");
		    params.set("rows", "100");
		
			NamedList<Object> solrDocumentList = preservationRiskmanagementService.queryDipFormatIdSolrParams(params);
			log.info("solrDocumentList size: " + solrDocumentList.size());
			String res = "";
			List<DbscanObject> dbscanList = new ArrayList<DbscanObject>();
			for (int n = 0; n < solrDocumentList.size(); n++) {
				int neighborPointsCount = 0;
				String imageName = (String) solrDocumentList.getName(n);
				List<SolrDocument> docs = (List<SolrDocument>) solrDocumentList.getVal(n);
				res = res + "description source: " + imageName + "\n";
//				res = "description source: " + imageName + "\n";
				for (SolrDocument doc : docs) {
					res = res + "score: " + doc.get("score") + 
					", availability: " + doc.get("availability") + 
					", neighbor: " + doc.get("descName") + 
					", extension: " + doc.get("extension") + 
					", mimetype: " + doc.get("mimetype") + 
					", formatname: " + doc.get("formatname") + 
					", software: " + doc.get("software") + "\n";
					DbscanObject dbscan = new DbscanObject();
					dbscan.setSourceId(imageName);
					dbscan.setScore((Float) doc.get("score"));
					dbscan.setAvailability((String) doc.get("availability"));
					dbscan.setNeighborId((String) doc.get("descName"));
					dbscan.setExtension((String) doc.get("extension"));
					dbscan.setMimetype((String) doc.get("mimetype"));
					dbscan.setFormatname((String) doc.get("formatname"));
					dbscan.setSoftware((String) doc.get("software"));
					if (!dbscan.getSourceId().equals(dbscan.getNeighborId())) {
						dbscanList.add(dbscan);
						if (dbscan.getScore() > EPS_VALUE) {
							neighborPointsCount++;
						}
					}
				}
				Iterator<DbscanObject> iter = dbscanList.iterator();
				while (iter.hasNext()) {
					DbscanObject point = iter.next();
					if (point.getSourceId().equals(imageName)) {
						point.setNeighborPointsCount(neighborPointsCount);
					}
//					point.setVisited(true);
				}
			}
			log.info(res);
			String resDbscan = dbscan(dbscanList);
			log.info(resDbscan);
		} catch (FfmaCommonException e) {
			log.info("File format solr update error: " + e);
		}
		assertNotNull("The resulting value must be a test value", solrList);
	}

	/**
	 * @param dbscanList
	 * @return
	 */
	private String dbscan(List<DbscanObject> dbscanList) {
		/*
		 * DBSCAN(D, eps, MinPts)
		   C = 0
		   for each unvisited point P in dataset D
		      mark P as visited
		      NeighborPts = regionQuery(P, eps)
		      if sizeof(NeighborPts) < MinPts
		         mark P as NOISE
		      else
		         C = next cluster
		         expandCluster(P, NeighborPts, C, eps, MinPts)
		          
			expandCluster(P, NeighborPts, C, eps, MinPts)
			   add P to cluster C
			   for each point P' in NeighborPts 
			      if P' is not visited
			         mark P' as visited
			         NeighborPts' = regionQuery(P', eps)
			         if sizeof(NeighborPts') >= MinPts
			            NeighborPts = NeighborPts joined with NeighborPts'
			      if P' is not yet member of any cluster
			         add P' to cluster C
			          
			regionQuery(P, eps)
			   return all points within P's eps-neighborhood   
			   
			where D - number of dimensions
			      eps - distance determined by k-distance graph (k >= D + 1)
			      minpts - minimal count of points to build a cluster
		 */
		String res = "";
		int d = dbscanList.size();
		double eps = EPS_VALUE;
		int minPts = 3;
		int c = 0; // cluster number
		Iterator<DbscanObject> iter = dbscanList.iterator();
		while (iter.hasNext()) {
			DbscanObject point = iter.next();
//			point.setVisited(true);
			int neighborPointsCount = point.getNeighborPointsCount();
			if (neighborPointsCount < minPts) {
				point.setType(pointType.NOISY);
			} else {
				c++;
//				point.setClusterNumber(c);
				expandCluster(point, neighborPointsCount, c, eps, minPts, dbscanList);
			}
		}
		
		// print results
		Map<Integer, String> clusterMap = new HashMap<Integer, String>();
		Iterator<DbscanObject> iterRes = dbscanList.iterator();
		while (iterRes.hasNext()) {
			DbscanObject point = iterRes.next();
			if (point.getClusterNumber() > 0) {
				log.info("cluster number: " + point.getClusterNumber() + 
						", extensions: " + point.getExtension() +
						", source: " + point.getSourceId());
				clusterMap.put(point.getClusterNumber(), point.getSourceId());
			}
		}
		return res;
	}

	/**
	 * @param point
	 * @param neighborPointsCount
	 * @param c
	 * @param eps
	 * @param minPts
	 * @param dbscanList
	 */
	private void expandCluster(DbscanObject point, int neighborPointsCount,
			int c, double eps, int minPts, List<DbscanObject> dbscanList) {
		point.setClusterNumber(c);
		Iterator<DbscanObject> iter = dbscanList.iterator();
		while (iter.hasNext()) {
			DbscanObject neighborPoint = iter.next();
			if (neighborPoint.getSourceId().equals(point.getSourceId())) {
//				if (neighborPoint.getSourceId().equals(point.getNeighborId())) {
				if (!neighborPoint.isVisited()) {
					neighborPoint.setVisited(true);
					int nnPointsCount = neighborPoint.getNeighborPointsCount();
					if (nnPointsCount >= minPts) {
					   // join neighbors
//						Iterator<DbscanObject> iterNN = dbscanList.iterator();
//						while (iterNN.hasNext()) {
//							DbscanObject nnPoint = iterNN.next();
//							if (nnPoint.getSourceId().equals(neighborPoint.getNeighborId())) {
								if (neighborPoint.getClusterNumber() <= 0) {					
									neighborPoint.setClusterNumber(c);
								}
//							}
//						}
					}
//					if (neighborPoint.getClusterNumber() <= 0) {					
//						neighborPoint.setClusterNumber(c);
//					}
				}
			}
		}
	}

	@SuppressWarnings("unchecked")
	@Test
	public void queryDipFormatIdSolrMoreLikeThisBoost() throws MalformedURLException, URISyntaxException {	
		List<FfmaDomainObject> dipFormatIdList = null;
		List<String> extensionsList = new ArrayList<String>();
		Map<String, Neighborhood> neighborhoodMap = new HashMap<String, Neighborhood>();
		try {
			try {
				dipFormatIdList = preservationRiskmanagementService.retrieveDipFormatIdCollection();
			} catch (FfmaCommonException e1) {
				log.info("retrieve DipFormatId collection error: " + e1);
			}
			log.info("DipFormatId list size: " + dipFormatIdList.size());
			Iterator<FfmaDomainObject> iter = dipFormatIdList.iterator();
		    FileWriter writer = new FileWriter("c:\\tmp\\ff" + MIN_POINTS + ".csv");
			while (iter.hasNext()) {
				DipFormatId dipFormatId = (DipFormatId) iter.next();
				String ext = dipFormatId.getExtension();
				log.info("current extension: " + ext);
				extensionsList.add(ext);
				try {
				if (ext != null && ext.length() > 2) {
	//				http://localhost:8989/solr/dipformatid/select?q=extension:jls&mlt=true&mlt.boost=true&mlt.count=15&qf=description^0.5,software^0.1,mimetype^0.2,formatname^0.2&mlt.fl=description,software,mimetype,formatname&mlt.mindf=1&mlt.mintf=1&fl=description,descName,score,availability,extension,mimetype,formatname,software&rows=100
				    ModifiableSolrParams params = new ModifiableSolrParams();
				    params.set("q", "extension:" + ext);
				    params.set("mlt", "true");
				    params.set("mlt.boost", "true");
				    params.set("mlt.count", "15");
				    params.set("qf", "description^0.5,software^0.1,mimetype^0.2,formatname^0.2");
				    params.set("mlt.fl", "description,software,mimetype,formatname");
				    params.set("mlt.mindf", "1");
				    params.set("mlt.mintf", "1");
				    params.set("fl", "description,descName,score,availability,extension,mimetype,formatname,software"); 
				    params.set("rows", "100");
				
					if (ext.equals("jpg")) {
						int ss = 0;
					}
					NamedList<Object> solrDocumentList = preservationRiskmanagementService.queryDipFormatIdSolrParams(params);
					if (solrDocumentList != null) {
						log.info("solrDocumentList size: " + solrDocumentList.size());
						String res = "";
						for (int n = 0; n < solrDocumentList.size(); n++) {
							String imageName = (String) solrDocumentList.getName(n);
							List<SolrDocument> docs = (List<SolrDocument>) solrDocumentList.getVal(n);
							res = res + "description source: " + imageName + "\n";
							Neighborhood neighborhood = new Neighborhood();
//							String pointName = "";
							int count = 0;
							float currentScore = 0.0f;
							float maxScore = 0.0f;
							for (SolrDocument doc : docs) {
								Neighbor neighbor = new Neighbor();
								float currentScoreTmp = (Float) doc.get("score");
								currentScore = Math.round(currentScoreTmp*1000)/1000.0f;
								if (count == 2) {
									float maxScoreTmp = (Float) doc.get("score");
									maxScore = Math.round(maxScoreTmp*1000)/1000.0f;
								}
								if (maxScore == 0.0f || (currentScore <= maxScore && currentScore >= maxScore/2)) {
									String extension = (String) doc.get("extension");
//									if (count == 0) {
//										pointName = extension;
//									}
								    writer.append(extension);
								    neighbor.setExtension(extension);
								    writer.append(';');
								    String scoreStr = Float.toString(currentScore);
								    writer.append(scoreStr);
								    neighbor.setScore(currentScore);
								    writer.append(';');
									res = res + "score: " + doc.get("score") + 
									", availability: " + doc.get("availability") + 
									", neighbor: " + doc.get("descName") + 
									", extension: " + doc.get("extension") + 
									", mimetype: " + doc.get("mimetype") + 
									", formatname: " + doc.get("formatname") + 
									", software: " + doc.get("software") + "\n";
								    neighborhood.getNeighborhoodList().add(neighbor);
								}
								count++;
							}
						    writer.append('\n');
//						    neighborhoodMap.put(pointName, neighborhood);
						    neighborhoodMap.put(ext, neighborhood);
						}
						log.info(res);
					}
				}
				} catch (FfmaCommonException e) {
					log.info("retrieve LODFormat collection error: " + e);
				}
			}
		    writer.flush();
		    writer.close();
		} catch (IOException ioe) {
			log.info("write to csv error: " + ioe);
//		} catch (FfmaCommonException e) {
//			log.info("retrieve LODFormat collection error: " + e);
		}
		assertNotNull("The resulting value must be a test value", dipFormatIdList);
		
		for (Map.Entry<String, Neighborhood> entry : neighborhoodMap.entrySet()) {
			log.info("Cluster node name: " + entry.getKey() + ", node values size: " + entry.getValue().getNeighborhoodList().size());
		}
//		String res = dbscan(neighborhoodMap, extensionsList);
//		String res = dbscanExt(neighborhoodMap, extensionsList);
		int deep = 2;
		String res = dbscanExtDeep(neighborhoodMap, extensionsList, deep);
		log.info(res);
	}

	/**
	 * This method retrieves file formats count for each software, calculates software count with the same file formats
	 * count and writes this dependency to the CSV file.
	 * @throws MalformedURLException
	 * @throws URISyntaxException
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void queryDipSoftwareId() throws MalformedURLException, URISyntaxException {	
		List<FfmaDomainObject> dipSoftwareIdList = null;
		Map<Integer, Integer> softwareToFileFormatsCountsMap = new HashMap<Integer, Integer>();
		try {
			try {
				dipSoftwareIdList = preservationRiskmanagementService.retrieveDipSoftwareIdCollection();
			} catch (FfmaCommonException e1) {
				log.info("retrieve DipSoftwareId collection error: " + e1);
			}
			log.info("DipSoftwareId list size: " + dipSoftwareIdList.size());
			Iterator<FfmaDomainObject> iter = dipSoftwareIdList.iterator();
		    FileWriter writer = new FileWriter("c:\\tmp\\softwareVsFileFormats.csv");
			while (iter.hasNext()) {
				DipSoftwareId dipSoftwareId = (DipSoftwareId) iter.next();
				int fileFormatsCount = dipSoftwareId.getDipFormatId().length;
				if (fileFormatsCount > 140) {
					int iii = 0;
				}
				log.info("current fileFormatsCount: " + fileFormatsCount + ", software name: " + dipSoftwareId.getName());
				if (softwareToFileFormatsCountsMap.containsKey(fileFormatsCount)) {
					int currentSoftwareCount = softwareToFileFormatsCountsMap.get(fileFormatsCount);
//					if (currentSoftwareCount > 1410) {
//						int iii = 0;
//					}
					int resSoftwareCount = currentSoftwareCount + 1;
					softwareToFileFormatsCountsMap.put(fileFormatsCount, resSoftwareCount);
				} else {
					softwareToFileFormatsCountsMap.put(fileFormatsCount, 1);
				}
			}
		    writer.append("File Formats #; Software #\n");
			for (Map.Entry<Integer, Integer> entry : softwareToFileFormatsCountsMap.entrySet()) {
			    writer.append(Integer.toString(entry.getKey()));
			    writer.append(';');
			    writer.append(Integer.toString(entry.getValue()));
			    writer.append('\n');
	        	log.info("Key : " + entry.getKey() + " Value : " + entry.getValue());
	        }
		    writer.flush();
		    writer.close();
		} catch (IOException ioe) {
			log.info("write to csv error: " + ioe);
		}
		assertNotNull("The resulting value must be a test value", dipSoftwareIdList);
	}

	/**
	 * @param neighborhoodMap
	 * @param extensionsList
	 * @return
	 */
	private String dbscan(Map<String, Neighborhood> neighborhoodMap, List<String> extensionsList) {
		/*
		 * DBSCAN(D, eps, MinPts)
		   C = 0
		   for each unvisited point P in dataset D
		      mark P as visited
		      NeighborPts = regionQuery(P, eps)
		      if sizeof(NeighborPts) < MinPts
		         mark P as NOISE
		      else
		         C = next cluster
		         expandCluster(P, NeighborPts, C, eps, MinPts)
		          
			expandCluster(P, NeighborPts, C, eps, MinPts)
			   add P to cluster C
			   for each point P' in NeighborPts 
			      if P' is not visited
			         mark P' as visited
			         NeighborPts' = regionQuery(P', eps)
		             NeighborPts = NeighborPts joined with NeighborPts'
			      if P' is not yet member of any cluster
			         add P' to cluster C
			          
			regionQuery(P, eps)
			   return all points within P's eps-neighborhood   
			   
			where D - number of dimensions
			      eps - distance determined by k-distance graph (k >= D + 1)
			      minpts - minimal count of points to build a cluster
		 */
		
		// evaluate extension frequency
		Map<String, Integer> freqMap = new HashMap<String, Integer>();
		Iterator<String> extIter = extensionsList.iterator();
		while (extIter.hasNext()) {
			String currentExt = extIter.next();
			int freqCounter = 0;
			for (Map.Entry<String, Neighborhood> entry : neighborhoodMap.entrySet()) {
//				String currentExt = entry.getKey();
				Neighborhood neighborhood = entry.getValue();
				List<Neighbor> neighborList = neighborhood.getNeighborhoodList();
				Iterator<Neighbor> iterN = neighborList.iterator();
				while (iterN.hasNext()) {
					Neighbor neighbor = iterN.next();
					if (neighbor.getExtension().equals(currentExt)) {
						freqCounter++;
					}
				}
			}
//			int currentFreq = freqMap.get(currentExt);
//			freqMap.put(currentExt, currentFreq + freqCounter);
			freqMap.put(currentExt, freqCounter);
		}

		// run dbscan
		String res = "";
//		int d = neighborhoodMap.size();
		double eps = EPS_VALUE;
		int minPts = MIN_POINTS;
		List<String> visitedNodes = new ArrayList<String>();
		List<String> noisyNodes = new ArrayList<String>();
		List<Cluster> clusterList = new ArrayList<Cluster>();
		
		for (Map.Entry<String, Neighborhood> entry : neighborhoodMap.entrySet()) {
			String nodeName = entry.getKey();
			Neighborhood neighborhood = entry.getValue();
			log.info("point: " + nodeName);
			if (!visitedNodes.contains(nodeName)) {
				visitedNodes.add(nodeName);
//				int neighborPointsCount = neighborhood.getNeighborhoodList().size();
				int neighborPointsCount = 0;
				Iterator<Neighbor> iter = neighborhood.getNeighborhoodList().iterator();
				while (iter.hasNext()) {
					Neighbor neighbor = iter.next();
					if (!visitedNodes.contains(neighbor.getExtension())) {
						neighborPointsCount++;
					}
				}
				if (neighborPointsCount < minPts) {
					noisyNodes.add(nodeName);
				} else {
					Cluster cluster = new Cluster();
					expandCluster(nodeName, neighborhood, cluster, eps, minPts, neighborhoodMap, visitedNodes, clusterList, freqMap);
					clusterList.add(cluster);
				}
			}
		}
		
		// print results
	    try {
			FileWriter writer = new FileWriter("c:\\tmp\\cluster" + MIN_POINTS + ".csv");
			Iterator<Cluster> iterRes = clusterList.iterator();
			while (iterRes.hasNext()) {
				Cluster cluster = iterRes.next();
				if (cluster.getNodeList().size() >= MIN_POINTS) {
					res = res + "cluster name: " + cluster.getName() + "\n";
					writer.append(cluster.getName());
					writer.append(';');
					writer.append(';');
					String centroidName = "";
					int clusterSize = 0;
					int maxFrequency = 0;
					Iterator<Node> nodeIter = cluster.getNodeList().iterator();
					while (nodeIter.hasNext()) {
						Node node = nodeIter.next();
						res = res + "node name: " + node.getName() + ", frequency: " + node.getFrequency() + "\n";
						clusterSize = clusterSize + node.getFrequency();
						if (maxFrequency < node.getFrequency()) {
							maxFrequency = node.getFrequency();
							centroidName = node.getName();
						}
						writer.append(';');
						writer.append(node.getName());
						writer.append(';');
						writer.append(Integer.toString(node.getFrequency()));
						writer.append(';');
					}
					writer.append("\n");
					cluster.setCentroidName(centroidName);
					cluster.setSize(clusterSize);
				}
			}
			// print plot
			writer.append("\n");
			writer.append("Cluster Plot\n");
			writer.append("\n");
			Iterator<Cluster> iterPlot = clusterList.iterator();
			while (iterPlot.hasNext()) {
				Cluster cluster = iterPlot.next();
				if (cluster.getNodeList().size() >= MIN_POINTS) {
					writer.append(cluster.getCentroidName());
					writer.append(';');
	//				writer.append(Integer.toString(cluster.getSize()));
					writer.append(Integer.toString(cluster.getNodeList().size()));
					writer.append("\n");
				}
			}
			// print sample cluster node
			writer.append("\n");
			writer.append("Cluster node\n");
			writer.append("\n");
			Iterator<Cluster> iterClusterNode = clusterList.iterator();
			while (iterClusterNode.hasNext()) {
				Cluster cluster = iterClusterNode.next();
				if (cluster.getName().equals("dpx")) {
					Iterator<Node> nodeIter = cluster.getNodeList().iterator();
					while (nodeIter.hasNext()) {
						Node node = nodeIter.next();
						writer.append(node.getName());
						writer.append(';');
						writer.append(Integer.toString(node.getFrequency()));
						writer.append("\n");
					}
				}
			}
			writer.flush();
			writer.close();
		} catch (IOException e) {
			log.info("writing csv cluster error: " + e);
		}
		return res;
	}

	/**
	 * @param nodeName
	 * @param neighborhood
	 * @param cluster
	 * @param eps
	 * @param minPts
	 * @param neighborhoodMap
	 * @param visitedNodes
	 * @param clusterList
	 */
	private void expandCluster(String nodeName, Neighborhood neighborhood,
			Cluster cluster, double eps, int minPts,
			Map<String, Neighborhood> neighborhoodMap,
			List<String> visitedNodes,
			List<Cluster> clusterList, Map<String, Integer> freqMap) {
		cluster.setName(nodeName);
		
		Iterator<Neighbor> iter = neighborhood.getNeighborhoodList().iterator();
		while (iter.hasNext()) {
			Neighbor neighbor = iter.next();
			if (!visitedNodes.contains(neighbor.getExtension())) {
				visitedNodes.add(neighbor.getExtension());
			}
//			int neighborPointsCount = neighborhood.getNeighborhoodList().size();
			int neighborPointsCount = 0;
			Iterator<Neighbor> iterTmp = neighborhood.getNeighborhoodList().iterator();
			while (iterTmp.hasNext()) {
				Neighbor neighborTmp = iterTmp.next();
				if (!visitedNodes.contains(neighborTmp.getExtension())) {
					neighborPointsCount++;
				}
			}
			if (neighborPointsCount >= minPts) {
				// check if neighbor is a member of any cluster - if not add to the current cluster
				boolean isMemberOfCluster = false;
				Iterator<Cluster> iterCluster = clusterList.iterator();
				while (iterCluster.hasNext()) {
					Cluster clusterObj = iterCluster.next();
					Iterator<Node> iterNode = clusterObj.getNodeList().iterator();
					while (iterNode.hasNext()) {
						Node nodeObj = iterNode.next();
						if (nodeObj.getName().equals(neighbor.getExtension())) {
							isMemberOfCluster = true;
							break;
						}
					}
				}
				if (!isMemberOfCluster) {
					Node newNode = new Node();
					newNode.setName(neighbor.getExtension());
					int frequency = 0;
					if (freqMap.get(neighbor.getExtension()) != null) {
						frequency = freqMap.get(neighbor.getExtension());
					}
					newNode.setFrequency(frequency);
					cluster.getNodeList().add(newNode);
				}
			}
		}
	}

	/**
	 * @param neighborhoodMap
	 * @param extensionsList
	 * @return
	 */
	private String dbscanExt(Map<String, Neighborhood> neighborhoodMap, List<String> extensionsList) {
		/*
		 * DBSCAN(D, eps, MinPts)
		   C = 0
		   for each unvisited point P in dataset D
		      mark P as visited
		      NeighborPts = regionQuery(P, eps)
		      if sizeof(NeighborPts) < MinPts
		         mark P as NOISE
		      else
		         C = next cluster
		         expandCluster(P, NeighborPts, C, eps, MinPts)
		          
			expandCluster(P, NeighborPts, C, eps, MinPts)
			   add P to cluster C
			   for each point P' in NeighborPts 
			      if P' is not visited
			         mark P' as visited
			         NeighborPts' = regionQuery(P', eps)
		             NeighborPts = NeighborPts joined with NeighborPts'
			      if P' is not yet member of any cluster
			         add P' to cluster C
			          
			regionQuery(P, eps)
			   return all points within P's eps-neighborhood   
			   
			where D - number of dimensions
			      eps - distance determined by k-distance graph (k >= D + 1)
			      minpts - minimal count of points to build a cluster
		 */
		
		// evaluate extension frequency
		Map<String, Integer> freqMap = new HashMap<String, Integer>();
		Iterator<String> extIter = extensionsList.iterator();
		while (extIter.hasNext()) {
			String currentExt = extIter.next();
			int freqCounter = 0;
			for (Map.Entry<String, Neighborhood> entry : neighborhoodMap.entrySet()) {
//				String currentExt = entry.getKey();
				Neighborhood neighborhood = entry.getValue();
				List<Neighbor> neighborList = neighborhood.getNeighborhoodList();
				Iterator<Neighbor> iterN = neighborList.iterator();
				while (iterN.hasNext()) {
					Neighbor neighbor = iterN.next();
					if (neighbor.getExtension().equals(currentExt)) {
						freqCounter++;
					}
				}
			}
//			int currentFreq = freqMap.get(currentExt);
//			freqMap.put(currentExt, currentFreq + freqCounter);
			freqMap.put(currentExt, freqCounter);
		}

		// run dbscan
		String res = "";
//		int d = neighborhoodMap.size();
		double eps = EPS_VALUE;
		int minPts = MIN_POINTS;
		List<String> visitedNodes = new ArrayList<String>();
		List<String> noisyNodes = new ArrayList<String>();
		List<Cluster> clusterList = new ArrayList<Cluster>();
		
		for (Map.Entry<String, Neighborhood> entry : neighborhoodMap.entrySet()) {
			String nodeName = entry.getKey();
			Neighborhood neighborhood = entry.getValue();
			log.info("point: " + nodeName);
			if (!visitedNodes.contains(nodeName)) {
				visitedNodes.add(nodeName);
//				int neighborPointsCount = neighborhood.getNeighborhoodList().size();
				int neighborPointsCount = 0;
				Iterator<Neighbor> iter = neighborhood.getNeighborhoodList().iterator();
				while (iter.hasNext()) {
					Neighbor neighbor = iter.next();
					if (!visitedNodes.contains(neighbor.getExtension())) {
						neighborPointsCount++;
					}
				}
				if (neighborPointsCount < minPts) {
					noisyNodes.add(nodeName);
				} else {
					Cluster cluster = new Cluster();
					cluster.setName(nodeName);
					expandClusterExt(nodeName, neighborhood, cluster, eps, minPts, neighborhoodMap, visitedNodes, clusterList, freqMap);
					clusterList.add(cluster);
				}
			}
		}
		
		// print results
	    try {
			FileWriter writer = new FileWriter("c:\\tmp\\clusterExt" + MIN_POINTS + ".csv");
			Iterator<Cluster> iterRes = clusterList.iterator();
			while (iterRes.hasNext()) {
				Cluster cluster = iterRes.next();
				res = res + "cluster name: " + cluster.getName() + "\n";
				writer.append(cluster.getName());
				writer.append(';');
				writer.append(';');
				String centroidName = "";
				int clusterSize = 0;
				int maxFrequency = 0;
				Iterator<Node> nodeIter = cluster.getNodeList().iterator();
				while (nodeIter.hasNext()) {
					Node node = nodeIter.next();
					res = res + "node name: " + node.getName() + ", frequency: " + node.getFrequency() + "\n";
					clusterSize = clusterSize + node.getFrequency();
					if (maxFrequency < node.getFrequency()) {
						maxFrequency = node.getFrequency();
						centroidName = node.getName();
					}
					writer.append(';');
					writer.append(node.getName());
					writer.append(';');
					writer.append(Integer.toString(node.getFrequency()));
					writer.append(';');
				}
				writer.append("\n");
				cluster.setCentroidName(centroidName);
				cluster.setSize(clusterSize);
			}
			// print plot
			writer.append("\n");
			writer.append("Cluster Plot\n");
			writer.append("\n");
			Iterator<Cluster> iterPlot = clusterList.iterator();
			while (iterPlot.hasNext()) {
				Cluster cluster = iterPlot.next();
				writer.append(cluster.getCentroidName());
				writer.append(';');
//				writer.append(Integer.toString(cluster.getSize()));
				writer.append(Integer.toString(cluster.getNodeList().size()));
				writer.append("\n");
			}
			// print sample cluster node
			writer.append("\n");
			writer.append("Cluster node\n");
			writer.append("\n");
			Iterator<Cluster> iterClusterNode = clusterList.iterator();
			while (iterClusterNode.hasNext()) {
				Cluster cluster = iterClusterNode.next();
				if (cluster.getName().equals("dpx")) {
					Iterator<Node> nodeIter = cluster.getNodeList().iterator();
					while (nodeIter.hasNext()) {
						Node node = nodeIter.next();
						writer.append(node.getName());
						writer.append(';');
						writer.append(Integer.toString(node.getFrequency()));
						writer.append("\n");
					}
				}
			}
			writer.flush();
			writer.close();
		} catch (IOException e) {
			log.info("writing csv cluster error: " + e);
		}
		return res;
	}

	/**
	 * @param nodeName
	 * @param neighborhood
	 * @param cluster
	 * @param eps
	 * @param minPts
	 * @param neighborhoodMap
	 * @param visitedNodes
	 * @param clusterList
	 */
	private void expandClusterExt(String nodeName, Neighborhood neighborhood,
			Cluster cluster, double eps, int minPts,
			Map<String, Neighborhood> neighborhoodMap,
			List<String> visitedNodes,
			List<Cluster> clusterList, Map<String, Integer> freqMap) {		
		// for each point P' in NeighborPts
		Iterator<Neighbor> iter = neighborhood.getNeighborhoodList().iterator();
		while (iter.hasNext()) {
			Neighbor neighbor = iter.next();
			// if P' is not visited
			if (!visitedNodes.contains(neighbor.getExtension())) {
				// mark P' as visited
				visitedNodes.add(neighbor.getExtension());
				// NeighborPts' = regionQuery(P', eps)
				Neighborhood subNeighborhood = neighborhoodMap.get(neighbor.getExtension());
				// if size of (NeighborPts') >= MinPts
				if (subNeighborhood != null && subNeighborhood.getNeighborhoodList() != null) {
//					int neighborPointsCount = subNeighborhood.getNeighborhoodList().size();
					int neighborPointsCount = 0;
					Iterator<Neighbor> iterTmp = subNeighborhood.getNeighborhoodList().iterator();
					while (iterTmp.hasNext()) {
						Neighbor neighborTmp = iterTmp.next();
						if (!visitedNodes.contains(neighborTmp.getExtension())) {
							neighborPointsCount++;
						}
					}
					if (neighborPointsCount >= minPts) {
						// NeighborPts = NeighborPts joined with NeighborPts'
						expandClusterExt("", subNeighborhood, cluster, eps, minPts,
								neighborhoodMap, visitedNodes, clusterList, freqMap);
					}
				}
//			}
			// check if neighbor is a member of any cluster - if not add to the current cluster
			boolean isMemberOfCluster = false;
			Iterator<Cluster> iterCluster = clusterList.iterator();
			while (iterCluster.hasNext()) {
				Cluster clusterObj = iterCluster.next();
				Iterator<Node> iterNode = clusterObj.getNodeList().iterator();
				while (iterNode.hasNext()) {
					Node nodeObj = iterNode.next();
					if (nodeObj.getName().equals(neighbor.getExtension())) {
						isMemberOfCluster = true;
						break;
					}
				}
			}
			if (!isMemberOfCluster) {
				// add P' to cluster C
				Node newNode = new Node();
				newNode.setName(neighbor.getExtension());
				int frequency = 0;
				if (freqMap.get(neighbor.getExtension()) != null) {
					frequency = freqMap.get(neighbor.getExtension());
				}
				newNode.setFrequency(frequency);
				cluster.getNodeList().add(newNode);
			}
			}
		}
	}

	/**
	 * @param neighborhoodMap
	 * @param extensionsList
	 * @return
	 */
	private String dbscanExtDeep(Map<String, Neighborhood> neighborhoodMap, List<String> extensionsList, int deep) {
		// evaluate extension frequency
		Map<String, Integer> freqMap = new HashMap<String, Integer>();
		/*Iterator<String> extIter = extensionsList.iterator();
		while (extIter.hasNext()) {
			String currentExt = extIter.next();
			int freqCounter = 0;
			for (Map.Entry<String, Neighborhood> entry : neighborhoodMap.entrySet()) {
//				String currentExt = entry.getKey();
				Neighborhood neighborhood = entry.getValue();
				List<Neighbor> neighborList = neighborhood.getNeighborhoodList();
				Iterator<Neighbor> iterN = neighborList.iterator();
				while (iterN.hasNext()) {
					Neighbor neighbor = iterN.next();
					if (neighbor.getExtension().equals(currentExt)) {
						freqCounter++;
					}
				}
			}
//			int currentFreq = freqMap.get(currentExt);
//			freqMap.put(currentExt, currentFreq + freqCounter);
			freqMap.put(currentExt, freqCounter);
		}*/

		// run dbscan
		String res = "";
//		int d = neighborhoodMap.size();
		double eps = EPS_VALUE;
		int minPts = MIN_POINTS;
//		List<String> visitedNodes = new ArrayList<String>();
		List<String> noisyNodes = new ArrayList<String>();
		List<Cluster> clusterList = new ArrayList<Cluster>();
		
		for (Map.Entry<String, Neighborhood> entry : neighborhoodMap.entrySet()) {
			String nodeName = entry.getKey();
//			if (nodeName.equals("fh4")) {
			if (nodeName.equals("jpg")) {
				int ss = 0;
			}
			Neighborhood neighborhood = entry.getValue();
//			if (neighborhood.getNeighborhoodList() != null && neighborhood.getNeighborhoodList().size() > 0) {
			log.info("point: " + nodeName);
//			if (!visitedNodes.contains(nodeName)) {
//				visitedNodes.add(nodeName);
				int neighborPointsCount = 0;
				Iterator<Neighbor> iter = neighborhood.getNeighborhoodList().iterator();
				while (iter.hasNext()) {
					Neighbor neighbor = iter.next();
//					if (!visitedNodes.contains(neighbor.getExtension())) {
						neighborPointsCount++;
//					}
				}
				if (neighborPointsCount < minPts) {
					noisyNodes.add(nodeName);
				} else {
					Cluster cluster = new Cluster();
					cluster.setName(nodeName);
//					expandClusterExtDeep(nodeName, neighborhood, cluster, eps, minPts, neighborhoodMap, visitedNodes, clusterList, freqMap, deep);
					expandClusterExtDeep(nodeName, neighborhood, cluster, eps, minPts, neighborhoodMap, null, clusterList, freqMap, deep);
					if (cluster.getNodeList().size() >= MIN_POINTS) {
						clusterList.add(cluster);
					} else {
						noisyNodes.add(nodeName);
					}
				}
//			}
		}
		
		// print results
	    try {
	    	// evaluate centroids
			Iterator<Cluster> iterResC = clusterList.iterator();
			while (iterResC.hasNext()) {
				Cluster cluster = iterResC.next();
				String centroidName = "";
				int clusterSize = 0;
				int maxFrequency = 0;
				Iterator<Node> nodeIter = cluster.getNodeList().iterator();
				while (nodeIter.hasNext()) {
					Node node = nodeIter.next();
					clusterSize++;
//					clusterSize = clusterSize + node.getFrequency();
					if (maxFrequency < node.getFrequency()) {
						maxFrequency = node.getFrequency();
						centroidName = node.getName();
					}
				}
				cluster.setCentroidName(centroidName);
				cluster.setSize(clusterSize);
			}

			// dump clusters
			FileWriter writer = new FileWriter("c:\\tmp\\clusterExt" + MIN_POINTS + ".csv");
			Iterator<Cluster> iterRes = clusterList.iterator();
			while (iterRes.hasNext()) {
				Cluster cluster = iterRes.next();
				res = res + "cluster name: " + cluster.getName() + "\n";
//				writer.append(cluster.getName());
				writer.append(cluster.getCentroidName());
				writer.append(';');
				writer.append(';');
//				String centroidName = "";
//				int clusterSize = 0;
//				int maxFrequency = 0;
				Iterator<Node> nodeIter = cluster.getNodeList().iterator();
				while (nodeIter.hasNext()) {
					Node node = nodeIter.next();
					res = res + "node name: " + node.getName() + ", frequency: " + node.getFrequency() + "\n";
//					clusterSize = clusterSize + node.getFrequency();
//					if (maxFrequency < node.getFrequency()) {
//						maxFrequency = node.getFrequency();
//						centroidName = node.getName();
//					}
					writer.append(';');
					writer.append(node.getName());
					writer.append(';');
					writer.append(Integer.toString(node.getFrequency()));
					writer.append(';');
				}
				writer.append("\n");
//				cluster.setCentroidName(centroidName);
//				cluster.setSize(clusterSize);
			}
			// print plot
			writer.append("\n");
			writer.append("Cluster Plot\n");
			writer.append("\n");
			Iterator<Cluster> iterPlot = clusterList.iterator();
			while (iterPlot.hasNext()) {
				Cluster cluster = iterPlot.next();
				writer.append(cluster.getCentroidName());
				writer.append(';');
//				writer.append(Integer.toString(cluster.getSize()));
				writer.append(Integer.toString(cluster.getNodeList().size()));
				writer.append("\n");
			}
			// print sample cluster node
			writer.append("\n");
			writer.append("Cluster node\n");
			writer.append("\n");
			Iterator<Cluster> iterClusterNode = clusterList.iterator();
			while (iterClusterNode.hasNext()) {
				Cluster cluster = iterClusterNode.next();
				if (cluster.getCentroidName().equals("jpg")) {
//					if (cluster.getName().equals("jpg")) {
					Iterator<Node> nodeIter = cluster.getNodeList().iterator();
					while (nodeIter.hasNext()) {
						Node node = nodeIter.next();
						writer.append(node.getName());
						writer.append(';');
						writer.append(Integer.toString(node.getFrequency()));
						writer.append("\n");
					}
				}
			}
			writer.flush();
			writer.close();
		} catch (IOException e) {
			log.info("writing csv cluster error: " + e);
		}
		return res;
	}

	/**
	 * @param nodeName
	 * @param neighborhood
	 * @param cluster
	 * @param eps
	 * @param minPts
	 * @param neighborhoodMap
	 * @param visitedNodes
	 * @param clusterList
	 */
	private void expandClusterExtDeep(String nodeName, Neighborhood neighborhood,
			Cluster cluster, double eps, int minPts,
			Map<String, Neighborhood> neighborhoodMap,
			List<String> visitedNodes,
			List<Cluster> clusterList, Map<String, Integer> freqMap, int deep) {		
		if (deep > 0) {
			deep--;
			// for each point P' in NeighborPts
			Iterator<Neighbor> iter = neighborhood.getNeighborhoodList().iterator();
			while (iter.hasNext()) {
				Neighbor neighbor = iter.next();
				if (freqMap.get(neighbor.getExtension()) != null) {
					int currentFreq = freqMap.get(neighbor.getExtension());
					freqMap.put(neighbor.getExtension(), currentFreq + 1);
				} else {
					freqMap.put(neighbor.getExtension(), 1);
				}
				// if P' is not visited
//				if (!visitedNodes.contains(neighbor.getExtension())) {
					// mark P' as visited
//					visitedNodes.add(neighbor.getExtension());
					// NeighborPts' = regionQuery(P', eps)
					Neighborhood subNeighborhood = neighborhoodMap.get(neighbor.getExtension());
					// if size of (NeighborPts') >= MinPts
					if (subNeighborhood != null && subNeighborhood.getNeighborhoodList() != null) {
						int neighborPointsCount = 0;
						Iterator<Neighbor> iterTmp = subNeighborhood.getNeighborhoodList().iterator();
						while (iterTmp.hasNext()) {
							Neighbor neighborTmp = iterTmp.next();
//							if (!visitedNodes.contains(neighborTmp.getExtension())) {
								neighborPointsCount++;
//							}
						}
						if (neighborPointsCount >= minPts) {
							// NeighborPts = NeighborPts joined with NeighborPts'
							if (deep > 0) {
							expandClusterExtDeep("", subNeighborhood, cluster, eps, minPts,
									neighborhoodMap, visitedNodes, clusterList, freqMap, deep);
							}
						}
					}
	//			}
				// check if neighbor is a member of any cluster - if not add to the current cluster
				boolean isMemberOfCluster = false;
				Iterator<Cluster> iterCluster = clusterList.iterator();
				while (iterCluster.hasNext()) {
					Cluster clusterObj = iterCluster.next();
					Iterator<Node> iterNode = clusterObj.getNodeList().iterator();
					while (iterNode.hasNext()) {
						Node nodeObj = iterNode.next();
						if (nodeObj.getName().equals(neighbor.getExtension())) {
							isMemberOfCluster = true;
							break;
						}
					}
				}
				if (!isMemberOfCluster) {
					// add P' to cluster C
					Node newNode = new Node();
					newNode.setName(neighbor.getExtension());
					int frequency = 0;
					if (freqMap.get(neighbor.getExtension()) != null) {
						frequency = freqMap.get(neighbor.getExtension());
					}
					if (newNode.getName().equals("jpg")) {
						int fr = frequency;
					}
					newNode.setFrequency(frequency);
					// check new cluster for the node with the same name
					Iterator<Node> iterNode = cluster.getNodeList().iterator();
					while (iterNode.hasNext()) {
						Node nodeObj = iterNode.next();
						if (nodeObj.getName().equals(neighbor.getExtension())) {
							isMemberOfCluster = true;
							nodeObj.setFrequency(frequency);
							break;
						}
					}
					if (!isMemberOfCluster) {
						cluster.getNodeList().add(newNode);
					}
				}
//				}
			}
		}
	}

}
