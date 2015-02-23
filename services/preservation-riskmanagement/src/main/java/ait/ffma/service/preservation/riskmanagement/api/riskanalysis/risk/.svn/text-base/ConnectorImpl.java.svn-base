package ait.ffma.service.preservation.riskmanagement.api.riskanalysis.risk;

import java.util.ArrayList;
import java.util.List;

import ait.ffma.service.preservation.riskmanagement.api.riskanalysis.risk.riskmodel.RiskProperty;

public class ConnectorImpl implements Connector {

	private String value;
	private String searchValue;
    private List<String> columnNames = new ArrayList<String>();
	private RiskProperty riskProperty;
	
	public ConnectorImpl(RiskProperty riskProperty) {
		this.riskProperty = riskProperty;
	}

	/**
	 * @return the riskProperty
	 */
	public RiskProperty getRiskProperty() {
		return riskProperty;
	}

	public List<String> getColumnNames() {
		return columnNames;
	}

	public void setColumnNames(List<String> columnNames) {
		this.columnNames = columnNames;
	}

	public String getSearchValue() {
		return searchValue;
	}

	public void setSearchValue(String searchValue) {
		this.searchValue = searchValue;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getQuery() {
		return riskProperty.getQuery();
	}

	public void setQuery(String query) {
		riskProperty.setQuery(query);
	}

	public String getPropertyIdentifier() {
		return riskProperty.getId();
	}

}
