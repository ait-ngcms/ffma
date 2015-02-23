package ait.ffma.domain.preservation.riskmanagement;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import ait.ffma.domain.BaseFfmaDomainObject;
import ait.ffma.domain.FieldDefEnum;
import ait.ffma.factory.ComponentNameConstants;

/**
 * This class is a container for LOD vendor information retrieved from LOD repositories.
 */
@SuppressWarnings({"restriction" })
@XmlRootElement
public class LODVendor extends BaseFfmaDomainObject {

	private static final long serialVersionUID = 1744725638422473188L;

	@XmlTransient
	public enum FieldsEnum implements FieldDefEnum {
		Index              { public Class<?> evalType() { return Integer.class; } },
		Repository         { public Class<?> evalType() { return String.class; } },
		Timestamp          { public Class<?> evalType() { return String.class; } },
		OrganisationName   { public Class<?> evalType() { return String.class; } },
		NumberOfEmployees  { public Class<?> evalType() { return Integer.class; } },
		BusinessStatus     { public Class<?> evalType() { return String.class; } },
		CurrentFfma      { public Class<?> evalType() { return String.class; } },
		StockIssues        { public Class<?> evalType() { return String.class; } },
		RankedList         { public Class<?> evalType() { return String.class; } },
		Country            { public Class<?> evalType() { return String.class; } },
		FoundationDate     { public Class<?> evalType() { return String.class; } },
		Homepage           { public Class<?> evalType() { return String.class; } },
		RepositoryId       { public Class<?> evalType() { return String.class; } },
		FileFormat         { public Class<?> evalType() { return String[].class; } },
		SoftwareName       { public Class<?> evalType() { return String.class; } },
		Description        { public Class<?> evalType() { return String.class; } };

		public String evalName() { return this.name(); }
	}
	
	@XmlTransient
	public FieldDefEnum[] getFieldsEnum(){
		return FieldsEnum.values();
	}
	
	/**
	 * Constructor by component name
	 */
	public LODVendor() {
		setFfmaObjectName(LODVendor.class.getSimpleName());
		setComponentName(ComponentNameConstants.COMPONENT_PRESERVATION_RISKMANAGEMENT);
	}
	
	@XmlElement
    public Integer getIndex() {
		return Integer.valueOf((String) get(FieldsEnum.Index.name()));
	}

	public void setIndex(Integer index) {
		this.put(FieldsEnum.Index.name(), index);
	}

  	@XmlElement
	public String getRepository() {
        return getString(FieldsEnum.Repository.name());
    }

	public void setRepository(String value) {
        put(FieldsEnum.Repository.name(), value);
    }

  	@XmlElement
	public String getTimestamp() {
        return getString(FieldsEnum.Timestamp.name());
    }

	public void setTimestamp(String value) {
        put(FieldsEnum.Timestamp.name(), value);
    }

  	@XmlElement
	public String getOrganisationName() {
        return getString(FieldsEnum.OrganisationName.name());
    }

	public void setOrganisationName(String value) {
        put(FieldsEnum.OrganisationName.name(), value);
    }

	@XmlElement
    public Integer getNumberOfEmployees() {
  		Object res = get(FieldsEnum.NumberOfEmployees.name());
  		if (res != null) {
	  		if (res.getClass().equals(Integer.class)) {
	  			return (Integer) res;
	  		} else {
	  			return Integer.valueOf((String) get(FieldsEnum.NumberOfEmployees.name()));
	  		}
  		} else {
  			return null;
  		}
	}

	public void setNumberOfEmployees(Integer value) {
		this.put(FieldsEnum.NumberOfEmployees.name(), value);
	}

  	@XmlElement
	public String getBusinessStatus() {
        return getString(FieldsEnum.BusinessStatus.name());
    }

	public void setBusinessStatus(String value) {
        put(FieldsEnum.BusinessStatus.name(), value);
    }

  	@XmlElement
	public String getCurrentFfma() {
        return getString(FieldsEnum.CurrentFfma.name());
    }

	public void setCurrentFfma(String value) {
        put(FieldsEnum.CurrentFfma.name(), value);
    }

  	@XmlElement
	public String getStockIssues() {
        return getString(FieldsEnum.StockIssues.name());
    }

	public void setStockIssues(String value) {
        put(FieldsEnum.StockIssues.name(), value);
    }

  	@XmlElement
	public String getRankedList() {
        return getString(FieldsEnum.RankedList.name());
    }

	public void setRankedList(String value) {
        put(FieldsEnum.RankedList.name(), value);
    }

  	@XmlElement
	public String getCountry() {
        return getString(FieldsEnum.Country.name());
    }

	public void setCountry(String value) {
        put(FieldsEnum.Country.name(), value);
    }

  	@XmlElement
	public String getFoundationDate() {
        return getString(FieldsEnum.FoundationDate.name());
    }

	public void setFoundationDate(String value) {
        put(FieldsEnum.FoundationDate.name(), value);
    }

  	@XmlElement
	public String getHomepage() {
        return getString(FieldsEnum.Homepage.name());
    }

	public void setHomepage(String value) {
        put(FieldsEnum.Homepage.name(), value);
    }

  	@XmlElement
	public String getRepositoryId() {
        return getString(FieldsEnum.RepositoryId.name());
    }

	public void setRepositoryId(String value) {
        put(FieldsEnum.RepositoryId.name(), value);
    }
	
//  	@XmlElement
//	public String getFileFormat() {
//        return getString(FieldsEnum.FileFormat.name());
//    }
//
//	public void setFileFormat(String value) {
//        put(FieldsEnum.FileFormat.name(), value);
//    }
	
	@XmlElement
	public String[] getFileFormat() {
        return toStringArray(get(FieldsEnum.FileFormat.name()));
    }

	public void setFileFormat(String[] value) {
        put(FieldsEnum.FileFormat.name(), value);
    }
	
  	@XmlElement
	public String getSoftwareName() {
        return getString(FieldsEnum.SoftwareName.name());
    }

	public void setSoftwareName(String value) {
        put(FieldsEnum.SoftwareName.name(), value);
    }

  	@XmlElement
	public String getDescription() {
        return getString(FieldsEnum.Description.name());
    }

	public void setDescription(String value) {
        put(FieldsEnum.Description.name(), value);
    }
}
