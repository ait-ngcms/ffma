package ait.ffma.common.service.api;

import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.CommonsHttpSolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.common.params.ModifiableSolrParams;
import org.apache.solr.common.util.NamedList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import ait.ffma.common.exception.FfmaFunctionalException;
import ait.ffma.domain.FfmaDomainObject;
import ait.ffma.domain.preservation.riskmanagement.DipFormatId;

public interface DipFormatIdSynchronizer {

	@Autowired
	@Qualifier("solrUpdateServer")
	public abstract void setSolrServer(CommonsHttpSolrServer solrServer);

	/**
	 * Update DipFormatId document in solr.
	 * 
	 * @param id
	 * @param ffDoc
	 * @throws SolrServerException
	 * @throws FfmaFunctionalException
	 */
	public abstract void updateFFDoc(String id, FfmaDomainObject ffDoc)
			throws SolrServerException, FfmaFunctionalException;

	/**
	 * Store DipFormatId document in solr.
	 * 
	 * @param ffDoc
	 * @throws SolrServerException
	 * @throws FfmaFunctionalException
	 */
	public void storeFFDoc(FfmaDomainObject ffDoc)
			throws SolrServerException, FfmaFunctionalException;

	/**
	 * Read DipFormatId document from solr for given id.
	 * 
	 * @param id
	 * @return
	 * @throws SolrServerException
	 * @throws FfmaFunctionalException
	 */
	public FfmaDomainObject readFFDoc(String id) throws SolrServerException,
			FfmaFunctionalException;

	/**
	 * This method creates solr document by query.
	 * 
	 * @param query
	 * @return
	 * @throws FfmaFunctionalException
	 */
	public SolrInputDocument createInputDocumentByQuery(String query)
			throws FfmaFunctionalException;

	/**
	 * This method creates solr document by query using parameters.
	 * 
	 * @param params
	 * @return
	 * @throws FfmaFunctionalException
	 */
	public NamedList<Object> createInputDocumentByQuery(
			ModifiableSolrParams params)
	// public List<SolrInputDocument>
	// createInputDocumentByQuery(ModifiableSolrParams params)
			throws FfmaFunctionalException;

	/**
	 * @param dipFormatId
	 * @param softwareStr
	 * @param formatName
	 * @param mimeType
	 * @param availability
	 * @throws SolrServerException
	 * @throws FfmaFunctionalException
	 */
	public void storeDipFormatIdCollectionInSolr(DipFormatId dipFormatId,
			String softwareStr, String formatName, String mimeType,
			String availability) throws SolrServerException,
			FfmaFunctionalException;

}