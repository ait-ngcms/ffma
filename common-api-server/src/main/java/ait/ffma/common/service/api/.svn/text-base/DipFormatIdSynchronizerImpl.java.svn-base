package ait.ffma.common.service.api;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.CommonsHttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.common.params.ModifiableSolrParams;
import org.apache.solr.common.util.NamedList;

import ait.ffma.common.exception.FfmaFunctionalException;
import ait.ffma.common.exception.FfmaTechnicalRuntimeException;
import ait.ffma.domain.FfmaDomainObject;
import ait.ffma.domain.preservation.riskmanagement.DipFormatId;

public class DipFormatIdSynchronizerImpl implements DipFormatIdSynchronizer {

	Logger log = Logger.getLogger(getClass());

	private CommonsHttpSolrServer solrServer;

	private static final String DIP_ID = "dipId";
	private static final String EXT = "ext";
	private static final String DESCRIPTION = "description";
	private static final String REPOSITORY_ID = "repositoryId";
	private static final String REPOSITORY_NAME = "repositoryName";
	private static final String CLASS = "class";
	private static final String DESC_NAME = "descName";

	private static final String GENRE = "genre";
	private static final String EXTENSIONS = "extensions";
	private static final String FORMAT_NAME = "formatname";
	private static final String SOFTWARE = "software";
	private static final String MIMETYPE = "mimetype";

	private static final String EXTENSION = "extension";
	private static final String AVAILABILITY = "availability";

	/*
	 * (non-Javadoc)
	 * 
	 */
	public void setSolrServer(CommonsHttpSolrServer solrServer) {
		this.solrServer = solrServer;
	}

	/*
	 * (non-Javadoc)
	 * 
	 */
	@Override
	public void updateFFDoc(String id, FfmaDomainObject ffDoc)
			throws SolrServerException, FfmaFunctionalException {
		// commenceValidate(id, ffDoc);
		// SolrInputDocument solrInputDocument = prepareForImport(id, ffDoc);
		// indexRecord(solrInputDocument);
	}

	/*
	 * (non-Javadoc)
	 * 
	 */
	public void storeFFDoc(FfmaDomainObject ffDoc)
			throws SolrServerException, FfmaFunctionalException {
		DipFormatId dipFormatId = (DipFormatId) ffDoc;
		if (dipFormatId.getDescription() != null) {
			String[] texts = dipFormatId.getDescription();
			for (int i = 0; i < texts.length; i++) {
				SolrInputDocument solrInputDocument = createInputDocumentFromDipFormatIdExt(
						(DipFormatId) ffDoc, texts[i], i);
				indexRecord(solrInputDocument);
			}
		}

		// SolrInputDocument solrInputDocument =
		// createInputDocumentFromDipFormatId((DipFormatId) ffDoc);
		// indexRecord(solrInputDocument);
	}

	/*
	 * (non-Javadoc)
	 * 
	 */
	public FfmaDomainObject readFFDoc(String id) throws SolrServerException,
			FfmaFunctionalException {
		SolrInputDocument solrInputDocument = createInputDocumentById(id);
		DipFormatId dipFormatId = createDipFormatIdFromSolrInputDocument(solrInputDocument);
		return dipFormatId;
	}

	/**
	 * @param ffDoc
	 * @param solrInputDocument
	 * @throws FfmaFunctionalException
	 */
	private void updatedMetadataFields(FfmaDomainObject ffDoc,
			SolrInputDocument solrInputDocument)
			throws FfmaFunctionalException {

		DipFormatId dipFormatId = (DipFormatId) ffDoc;
		if (dipFormatId.getDipId() != null) {
			solrInputDocument.removeField(DIP_ID);
			String text = dipFormatId.getDipId();
			addTestToField(solrInputDocument, DIP_ID, text);
		}
		if (dipFormatId.getExtension() != null) {
			solrInputDocument.removeField(EXT);
			String text = dipFormatId.getExtension();
			addTestToField(solrInputDocument, EXT, text);
		}
		if (dipFormatId.getDescription() != null) {
			solrInputDocument.removeField(DESCRIPTION);
			String[] texts = dipFormatId.getDescription();
			for (int i = 0; i < texts.length; i++) {
				addTestToField(solrInputDocument, DESCRIPTION, texts[i]);
			}
		}

		// for (FieldDefEnum field : ((DipFormatId) ffDoc).getFieldsEnum()) {
		// for (FieldDefEnum field : ((FfmaFullDocImpl)
		// fullDoc).getFieldsEnum()) {
		// TODO: improve this to update only the DC fields and remove redundant
		// code
		// if(!field.evalName().startsWith("Dc"))
		// continue;

		// beanFieldName = convertToBeanFieldName(field.evalName());
		// beanField = getBeanField(FullBean.class, beanFieldName);
		// if (beanField == null)
		// beanField = getBeanField(BriefBean.class, beanFieldName);
		// if (beanField == null)
		// beanField = getBeanField(IdBean.class, beanFieldName);
		// if (beanField == null) {
		// // the field was not found
		// if (isNotIndexedField(field.evalName()))
		// continue;// this field is know to be in DB but not in the
		// // index
		// else
		// throw new FfmaTechnicalRuntimeException(
		// "Cannot find fulldoc field with the name: "
		// + beanFieldName);
		// }

		// EuropeanaField contains the mapping to solrAttribute names
		// euField = new EuropeanaField(beanField);

		// delete old values of the field
		// solrInputDocument.removeField( euField.getFieldNameString());
		// solrInputDocument.removeField(DIP_ID);

		// add the new values for the field
		// if (String.class.equals(field.evalType())) {
		// String text = fullDoc.getStringAttributeValue(field.evalName());
		// addTestToField(solrInputDocument, euField, text);
		// } else if (String[].class.equals(field.evalType())) {
		// // add all texts
		// String[] texts = fullDoc.getStringArrayAttributeValue(field
		// .evalName());
		// for (int i = 0; i < texts.length; i++) {
		// addTestToField(solrInputDocument, euField, texts[i]);
		// }
		// } else if (Boolean.class.equals(field.evalType())) {
		// // String (Boolean)fullDoc.getAttributeValue(field.evalName());
		// solrInputDocument.addField(euField.getFieldNameString(),
		// fullDoc.getAttributeValue(field.evalName()));
		// } else if (FfmaBriefDoc.class.equals(field.evalType())) {
		// // ignore it, the get brief doc must be migrated to the correct
		// // implementation
		// } else {
		// throw new FfmaFunctionalException(
		// "Convesion not supported for fieldType: "
		// + field.evalType() + " of field: "
		// + field.evalName());
		// }
		//
		// }
	}

	/**
	 * Assign DipFormatId fields to solr fields.
	 * 
	 * @param dipFormatId
	 * @return
	 * @throws FfmaFunctionalException
	 */
	// private SolrInputDocument createInputDocumentFromDipFormatId(DipFormatId
	// dipFormatId)
	// throws FfmaFunctionalException {
	// if (dipFormatId == null) {
	// throw new FfmaFunctionalException("No DipFormatId provided!");
	// }
	// SolrInputDocument solrInputDocument = new SolrInputDocument();
	//
	// try {
	// if (dipFormatId.getDipId() != null) {
	// solrInputDocument.removeField(DIP_ID);
	// String text = dipFormatId.getDipId();
	// addTestToField(solrInputDocument, DIP_ID, text);
	// }
	// if (dipFormatId.getExtension() != null) {
	// solrInputDocument.removeField(EXT);
	// String text = dipFormatId.getExtension();
	// addTestToField(solrInputDocument, EXT, text);
	// }
	// if (dipFormatId.getDescription() != null) {
	// solrInputDocument.removeField(DESCRIPTION);
	// String[] texts = dipFormatId.getDescription();
	// for (int i = 0; i < texts.length; i++) {
	// addTestToField(solrInputDocument, DESCRIPTION, texts[i]);
	// }
	// }
	// } catch (Exception e) {
	// throw new FfmaTechnicalRuntimeException(
	// "cannot store the given document: " + dipFormatId,
	// e);
	// }
	//
	// return solrInputDocument;
	// }

	/**
	 * @param desc
	 * @return
	 */
	private String getRepositoryName(String desc) {
		int beginIndex = desc.indexOf("<");
		int endIndex = desc.indexOf(">");
		String res = desc.substring(beginIndex + 1, endIndex);
		return res;
	}

	/**
	 * @param desc
	 * @return
	 */
	private String getRepositoryId(String desc) {
		int endIndex = desc.indexOf(">");
		int idBeginIndex = desc.indexOf("<", endIndex);
		int endIndexDBPediaId = desc.indexOf(">>", endIndex + 1);
		int idEndIndex = 0;
		if (endIndexDBPediaId > 0) {
			idEndIndex = endIndexDBPediaId + 1;
		} else {
			idEndIndex = desc.indexOf(">", endIndex + 1);
		}
		String res = desc.substring(idBeginIndex + 1, idEndIndex);
		return res;
	}

	/**
	 * @param desc
	 * @param repositoryId
	 * @return
	 */
	private String getDescription(String desc, String repositoryId) {
		int endIndex = desc.indexOf(">");
		int endIndexDBPediaId = desc.indexOf(">>", endIndex + 1);
		int idEndIndex = 0;
		if (endIndexDBPediaId > 0) {
			idEndIndex = endIndexDBPediaId + 1;
		} else {
			idEndIndex = desc.indexOf(">", endIndex + 1);
		}
		int lastBeginIndex = desc.indexOf("</" + repositoryId, endIndex + 1);
		String res = desc.substring(idEndIndex + 1, lastBeginIndex);
		return res;
	}

	/**
	 * Assign DipFormatId fields to solr fields.
	 * 
	 * @param dipFormatId
	 * @return
	 * @throws FfmaFunctionalException
	 */
	private SolrInputDocument createInputDocumentFromDipFormatIdExt(
			DipFormatId dipFormatId, String desc, int i)
			throws FfmaFunctionalException {
		if (dipFormatId == null) {
			throw new FfmaFunctionalException("No DipFormatId provided!");
		}
		SolrInputDocument solrInputDocument = new SolrInputDocument();

		try {
			if (dipFormatId.getDipId() != null) {
				solrInputDocument.removeField(DIP_ID);
				String text = dipFormatId.getDipId();
				addTestToField(solrInputDocument, DIP_ID, text);
			}
			if (dipFormatId.getExtension() != null) {
				solrInputDocument.removeField(EXT);
				String text = dipFormatId.getExtension();
				addTestToField(solrInputDocument, EXT, text);
			}
			if (dipFormatId.getDescription() != null) {
				solrInputDocument.removeField(DESCRIPTION);
				String repositoryName = getRepositoryName(desc);
				String repositoryId = getRepositoryId(desc);
				String description = getDescription(desc, repositoryId);
				addTestToField(solrInputDocument, REPOSITORY_NAME,
						repositoryName);
				addTestToField(solrInputDocument, REPOSITORY_ID, repositoryId);
				addTestToField(solrInputDocument, DESCRIPTION, description);
				addTestToField(solrInputDocument, DESC_NAME,
						dipFormatId.getDipId() + "/" + repositoryName + i);
			}
			if (dipFormatId != null) {
				solrInputDocument.removeField(CLASS);
				addTestToField(solrInputDocument, CLASS, "DipFormatId");
			}
		} catch (Exception e) {
			throw new FfmaTechnicalRuntimeException(
					"cannot store the given document: " + dipFormatId, e);
		}

		return solrInputDocument;
	}

	/**
	 * Assign solr fields to DipFormatId fields.
	 * 
	 * @param solr
	 *            file format document
	 * @return
	 * @throws FfmaFunctionalException
	 */
	private DipFormatId createDipFormatIdFromSolrInputDocument(
			SolrInputDocument solrInputDocument)
			throws FfmaFunctionalException {
		if (solrInputDocument == null) {
			throw new FfmaFunctionalException(
					"No solr file format document provided!");
		}

		DipFormatId dipFormatId = new DipFormatId();
		try {
			if (solrInputDocument.getField(DIP_ID) != null) {
				dipFormatId.setDipId(solrInputDocument.getFieldValue(DIP_ID)
						.toString());
			}
			if (solrInputDocument.getField(EXT) != null) {
				dipFormatId.setExtension(solrInputDocument.getFieldValue(EXT)
						.toString());
			}
			if (solrInputDocument.getField(DESCRIPTION) != null) {
				String[] arr = new String[1];
				arr[0] = solrInputDocument.getFieldValue(DESCRIPTION)
						.toString();
				dipFormatId.setDescription(arr);
			}
			// if (solrInputDocument.getField(DESCRIPTION) != null) {
			// dipFormatId.setDescription(solrInputDocument.getFieldValues(
			// DESCRIPTION).toArray(
			// new String[solrInputDocument
			// .getFieldValues(DESCRIPTION).size()]));
			// }
		} catch (Exception e) {
			throw new FfmaTechnicalRuntimeException(
					"cannot read the given document: " + solrInputDocument, e);
		}

		return dipFormatId;
	}

	/**
	 * This method reads the old record from the Solr and creates a new
	 * InputDocument out of it.
	 * 
	 * @param europeanaId
	 * @return
	 * @throws EuropeanaQueryException
	 * @throws FfmaFunctionalException
	 */
	// private SolrInputDocument createInputDocumentWithOldValues(String id)
	// throws FfmaFunctionalException {
	// // EuropeanaId europeanaId) throws FfmaFunctionalException {
	// // TODO: remove duplication with the
	// // FfmaCommonDaoImpl.getMetadata(uri)
	// if (id == null || id.length() == 0) {
	// // if (europeanaId == null || europeanaId.getEuropeanaUri() == null)
	// // {
	// throw new FfmaFunctionalException("No DipFormatId provided!"); //
	// Expected
	// // uri
	// // query
	// // parameter
	// }
	//
	// SolrQuery solrQuery = new SolrQuery();
	// solrQuery.setQuery("dip_id:\"" + id
	// // solrQuery.setQuery("europeana_uri:\"" + europeanaId.getEuropeanaUri()
	// + "\"");
	//
	// QueryResponse response;
	// SolrInputDocument solrInputDocument = new SolrInputDocument();
	//
	// try {
	// response = beanQueryModelFactory.getSolrResponse(solrQuery);

	// if (response.getResults() == null
	// || response.getResults().isEmpty())
	// throw new FfmaFunctionalException(
	// "No document found for the id: " + id);
	//
	// SolrDocument oldRecord = response.getResults().get(0);

	// copy original fields from existing SOLR document
	// for (String field : oldRecord.getFieldNames()) {
	// solrInputDocument.addField(field,
	// oldRecord.getFieldValue(field));
	// }

	// } catch (EuropeanaQueryException e) {
	// throw new FfmaTechnicalRuntimeException(
	// "cannot read the given document with query: " + solrQuery,
	// e);
	// }

	// return solrInputDocument;
	// }

	/**
	 * This method reads the record from the Solr by given DipFormatId and
	 * creates a new InputDocument out of it.
	 * 
	 * @param DipFormatId
	 *            identifier
	 * @return
	 * @throws EuropeanaQueryException
	 * @throws FfmaFunctionalException
	 */
	private SolrInputDocument createInputDocumentById(String id)
			throws FfmaFunctionalException {

		SolrInputDocument solrInputDocument = new SolrInputDocument();
		if (id == null || id.length() == 0) {
			throw new FfmaFunctionalException("No DipFormatId provided!");
		}

		SolrQuery solrQuery = new SolrQuery();
		solrQuery.setQuery(DIP_ID + ":\"" + id + "\"");

		QueryResponse response = null;
		try {
			response = solrServer.query(solrQuery);
			if (response.getResults() == null
					|| response.getResults().isEmpty()) {
				throw new FfmaFunctionalException(
						"No document found for the id: " + id);
			}
			SolrDocument solrDocument = response.getResults().get(0);
			for (String field : solrDocument.getFieldNames()) {
				solrInputDocument.addField(field,
						solrDocument.getFieldValue(field));
			}
		} catch (SolrServerException e) {
			log.error("The solr server can not reply", e);
			return null;
		} catch (Exception e) {
			log.error("Problem retrieving data from solr: " + e);
			return null;
		}
		return solrInputDocument;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * eu.europeana.core.database.incoming.FFSynchronizer#createInputDocumentByQuery
	 * (org.apache.solr.common.params.ModifiableSolrParams)
	 */
	@SuppressWarnings("unchecked")
	public NamedList<Object> createInputDocumentByQuery(
			ModifiableSolrParams params)
	// public List<SolrInputDocument>
	// createInputDocumentByQuery(ModifiableSolrParams params)
			throws FfmaFunctionalException {

		// List<SolrInputDocument> solrList = new
		// ArrayList<SolrInputDocument>();
		NamedList<Object> solrList = null;
		if (params == null) {
			throw new FfmaFunctionalException("No DipFormatId provided!");
		}

		QueryResponse response = null;
		try {
			response = solrServer.query(params);
			if (response.getResults() == null
					|| response.getResults().isEmpty()) {
				throw new FfmaFunctionalException(
						"No document found for the id: " + params);
			}
			solrList = (NamedList<Object>) response.getResponse().get(
					"moreLikeThis");
			// NamedList<Object> moreLikeThis = (NamedList<Object>)
			// response.getResponse().get("moreLikeThis");
			// SolrDocumentList solrDocumentList = response.getResults();
			// Iterator<SolrDocument> iter = solrDocumentList.iterator();
			// while (iter.hasNext()) {
			// SolrDocument solrDocument = iter.next();
			// SolrInputDocument solrInputDocument = new SolrInputDocument();
			// for (String field : solrDocument.getFieldNames()) {
			// solrInputDocument.addField(field,
			// solrDocument.getFieldValue(field));
			// }
			// solrList.add(solrInputDocument);
			// }
		} catch (SolrServerException e) {
			log.error("The solr server can not reply", e);
			return null;
		} catch (Exception e) {
			log.error("Problem retrieving data from solr: " + e);
			return null;
		}
		return solrList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 */
	public SolrInputDocument createInputDocumentByQuery(String query)
			throws FfmaFunctionalException {

		SolrInputDocument solrInputDocument = new SolrInputDocument();
		if (query == null || query.length() == 0) {
			throw new FfmaFunctionalException("No DipFormatId provided!");
		}

		SolrQuery solrQuery = new SolrQuery();
		solrQuery.setQuery(query);

		QueryResponse response = null;
		try {
			response = solrServer.query(solrQuery);
			if (response.getResults() == null
					|| response.getResults().isEmpty()) {
				throw new FfmaFunctionalException(
						"No document found for the id: " + query);
			}
			SolrDocument solrDocument = response.getResults().get(0);
			for (String field : solrDocument.getFieldNames()) {
				solrInputDocument.addField(field,
						solrDocument.getFieldValue(field));
			}
		} catch (SolrServerException e) {
			log.error("The solr server can not reply", e);
			return null;
		} catch (Exception e) {
			log.error("Problem retrieving data from solr: " + e);
			return null;
		}
		return solrInputDocument;
	}

	// private Field getBeanField(Class<?> beanClass, String beanFieldName) {
	// // get the field of bean class
	// try {
	// return beanClass.getDeclaredField(beanFieldName);
	// } catch (NoSuchFieldException e) {
	// return null;
	// } catch (SecurityException e) {
	// return null;
	// }
	// }

	/**
	 * @param solrInputDocument
	 * @param fieldName
	 * @param text
	 */
	private void addTestToField(SolrInputDocument solrInputDocument,
			String fieldName, String text) {
		text = truncateText(text);
		solrInputDocument.addField(fieldName, text);
	}

	// see ESEIMmporterImpl#indexRecordList
	private void indexRecord(SolrInputDocument solrInputDocument)
			throws FfmaFunctionalException {

		try {
			solrServer.add(solrInputDocument);
//			solrServer.commit();
		} catch (SolrServerException e) {
			throw new FfmaFunctionalException(
					"unable to index this document! :" + solrInputDocument,
					e.fillInStackTrace());

		} catch (IOException e) {
			throw new FfmaFunctionalException(
					"unable to index this document! :" + solrInputDocument,
					e.fillInStackTrace());
		}
		// solrServer.commit(); // It is better to use the autocommit from solr
		// recordList.clear();
	}

	private String truncateText(String text) {
		if (text.length() > 10000)
			return text.substring(0, 9999);

		return text;
	}

	// void resetHash(SolrInputDocument document, String id) {
	// final String hash = IngestionUtils.makeRecordHash(id);
	// document.removeField(FIELD_HASH);
	// document.addField(FIELD_HASH, hash);
	// document.removeField(FIELD_HASH_FIRST_FOUR);
	// document.addField(FIELD_HASH_FIRST_FOUR,
	// StringUtils.substring(hash, 0, 4));
	// }
	//
	// void resetCompletenessRank(SolrInputDocument document, String id) {
	// document.removeField(FIELD_RECORD_COMPLETENESS);
	// document.addField(FIELD_RECORD_COMPLETENESS,
	// RecordCompletenessRanking.rankRecordCompleteness(document));
	// }

	public void storeDipFormatIdCollectionInSolr(DipFormatId dipFormatId,
			String softwareStr, String formatName, String mimeType,
			String availability) throws SolrServerException,
			FfmaFunctionalException {
		// Iterator<FfmaDomainObject> iter = dipFormatIdList.iterator();
		// while (iter.hasNext()) {
		// DipFormatId currentFormat = (DipFormatId) iter.next();
		// SolrInputDocument solrInputDocument =
		// createInputDocumentFromDipFormatId(currentFormat);
		// indexRecord(solrInputDocument);
		// }
		SolrInputDocument solrInputDocument = createInputDocumentFromDipFormatId(
				dipFormatId, softwareStr, formatName, mimeType, availability);
		indexRecord(solrInputDocument);
		try {
			solrServer.commit();
		} catch (IOException e) {
			log.info("solr commit error: " + e);
		} 
	}

	/**
	 * @param lodFormat
	 * @return
	 * @throws FfmaFunctionalException
	 */
	private SolrInputDocument createInputDocumentFromDipFormatId(
			DipFormatId dipFormatId, String softwareStr, String formatName,
			String mimeType, String availability)
			throws FfmaFunctionalException {
		if (dipFormatId == null) {
			throw new FfmaFunctionalException("No dipFormatId provided!");
		}
		SolrInputDocument solrInputDocument = new SolrInputDocument();

		try {
			if (dipFormatId.getDescription() != null) {
				String descriptionsStr = "";
				String[] texts = dipFormatId.getDescription();
				for (int i = 0; i < texts.length; i++) {
					if (!texts[i].equals(" ") && !texts[i].contains("This is an outline")) {
						String description = texts[i];
						String repositoryId = getRepositoryId(description);
						String descriptionPure = getDescription(description,
								repositoryId);
						if (descriptionsStr.equals("")) {
							descriptionsStr = descriptionPure;
						} else {
							descriptionsStr = descriptionsStr+ ", " + descriptionPure;
						}
					}
				}
				addTestToField(solrInputDocument, DESCRIPTION, descriptionsStr);
			}
			if (softwareStr != null) {
				addTestToField(solrInputDocument, SOFTWARE, softwareStr);
			}
			if (formatName != null) {
				addTestToField(solrInputDocument, FORMAT_NAME, formatName);
			}
			if (mimeType != null) {
				addTestToField(solrInputDocument, MIMETYPE, mimeType);
			}
			if (availability != null) {
				addTestToField(solrInputDocument, AVAILABILITY, availability);
			}
			if (dipFormatId.getExtension() != null) {
				String text = dipFormatId.getExtension();
				addTestToField(solrInputDocument, EXTENSION, text);
			}
			if (dipFormatId != null) {
				addTestToField(solrInputDocument, CLASS, "DipFormatId");
				addTestToField(solrInputDocument, DESC_NAME, dipFormatId.getDipId());
			}
		} catch (Exception e) {
			throw new FfmaTechnicalRuntimeException(
					"cannot store the given document: " + dipFormatId, e);
		}

		return solrInputDocument;
	}

}
