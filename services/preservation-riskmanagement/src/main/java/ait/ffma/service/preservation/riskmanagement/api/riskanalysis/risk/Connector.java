package ait.ffma.service.preservation.riskmanagement.api.riskanalysis.risk;

import java.util.List;

public interface Connector {

	/**
	 * This method returns base query for repository request.
	 * @return repository query
	 */
	public String getQuery();
	
	/**
	 * This method returns a risk property identifier.
	 * @return risk property name
	 */
	public String getPropertyIdentifier();
	
	/**
	 * This method returns search value. It is a request value for repository.
	 * @return search value
	 */
	public String getSearchValue();
	
	/**
	 * This method returns repository column names for particular request
	 * @return column names
	 */
	public List<String> getColumnNames();

	/**
	 * This method fills risk property with value retrieved from repository.
	 * @param value
	 */
	public void setValue(String value);
}