package ait.ffma.domain.preservation.riskmanagement;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import ait.ffma.domain.BaseFfmaDomainObject;
import ait.ffma.domain.FieldDefEnum;
import ait.ffma.factory.ComponentNameConstants;

/**
 * This class is a container for LOD format information retrieved from LOD repositories.
 */
@SuppressWarnings({"restriction" })
@XmlRootElement
public class LODFormat extends BaseFfmaDomainObject {

	private static final long serialVersionUID = -8919702531027568996L;

	@XmlTransient
	public enum FieldsEnum implements FieldDefEnum {
		Index                      { public Class<?> evalType() { return Integer.class; } },
		Repository                 { public Class<?> evalType() { return String.class; } },
		Timestamp                  { public Class<?> evalType() { return String.class; } },
		FormatName                 { public Class<?> evalType() { return String.class; } },
		CurrentVersionReleaseDate  { public Class<?> evalType() { return String.class; } },
		SoftwareCount              { public Class<?> evalType() { return Integer.class; } },
		Software                   { public Class<?> evalType() { return String.class; } },
		CurrentFormatVersion       { public Class<?> evalType() { return String.class; } },
		FormatLicense              { public Class<?> evalType() { return String.class; } },
		Limitations                { public Class<?> evalType() { return String.class; } },
		Puid                       { public Class<?> evalType() { return String.class; } },
		FormatHomepage             { public Class<?> evalType() { return String.class; } },
		MimeType                   { public Class<?> evalType() { return String.class; } },
		FormatGenre                { public Class<?> evalType() { return String.class; } },
		FormatCreator              { public Class<?> evalType() { return String.class; } },
		OpenFormat                 { public Class<?> evalType() { return String.class; } },
		FileExtensions             { public Class<?> evalType() { return String.class; } },
		Vendors                    { public Class<?> evalType() { return String.class; } },
		Standards                  { public Class<?> evalType() { return String.class; } },
		RepositoryId               { public Class<?> evalType() { return String[].class; } },
		Description                { public Class<?> evalType() { return String[].class; } },
		SoftwareName               { public Class<?> evalType() { return String[].class; } },
		SoftwareId                 { public Class<?> evalType() { return String[].class; } },
		VendorName                 { public Class<?> evalType() { return String[].class; } },
		VendorId                   { public Class<?> evalType() { return String[].class; } };

		public String evalName() { return this.name(); }
	}
	
	@XmlTransient
	public FieldDefEnum[] getFieldsEnum(){
		return FieldsEnum.values();
	}
	
	/**
	 * Constructor by component name
	 */
	public LODFormat() {
		setFfmaObjectName(LODFormat.class.getSimpleName());
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
	public String getFormatName() {
        return getString(FieldsEnum.FormatName.name());
    }

	public void setFormatName(String value) {
        put(FieldsEnum.FormatName.name(), value);
    }

  	@XmlElement
	public String getCurrentVersionReleaseDate() {
        return getString(FieldsEnum.CurrentVersionReleaseDate.name());
    }

	public void setCurrentVersionReleaseDate(String value) {
        put(FieldsEnum.CurrentVersionReleaseDate.name(), value);
    }

  	@XmlElement
	public Integer getSoftwareCount() {
  		Object res = get(FieldsEnum.SoftwareCount.name());
  		if (res != null) {
	  		if (res.getClass().equals(Integer.class)) {
	  			return (Integer) res;
	  		} else {
	  			return Integer.valueOf((String) get(FieldsEnum.SoftwareCount.name()));
	  		}
  		} else {
  			return null;
  		}
    }

	public void setSoftwareCount(Integer value) {
		this.put(FieldsEnum.SoftwareCount.name(), value);
    }

  	@XmlElement
	public String getSoftware() {
        return getString(FieldsEnum.Software.name());
    }

	public void setSoftware(String value) {
        put(FieldsEnum.Software.name(), value);
    }

  	@XmlElement
	public String getCurrentFormatVersion() {
        return getString(FieldsEnum.CurrentFormatVersion.name());
    }

	public void setCurrentFormatVersion(String value) {
        put(FieldsEnum.CurrentFormatVersion.name(), value);
    }

  	@XmlElement
	public String getFormatLicense() {
        return getString(FieldsEnum.FormatLicense.name());
    }

	public void setFormatLicense(String value) {
        put(FieldsEnum.FormatLicense.name(), value);
    }

  	@XmlElement
	public String getLimitations() {
        return getString(FieldsEnum.Limitations.name());
    }

	public void setLimitations(String value) {
        put(FieldsEnum.Limitations.name(), value);
    }

  	@XmlElement
	public String getPuid() {
        return getString(FieldsEnum.Puid.name());
    }

	public void setPuid(String value) {
        put(FieldsEnum.Puid.name(), value);
    }

  	@XmlElement
	public String getFormatHomepage() {
        return getString(FieldsEnum.FormatHomepage.name());
    }

	public void setFormatHomepage(String value) {
        put(FieldsEnum.FormatHomepage.name(), value);
    }

  	@XmlElement
	public String getMimeType() {
        return getString(FieldsEnum.MimeType.name());
    }

	public void setMimeType(String value) {
        put(FieldsEnum.MimeType.name(), value);
    }

  	@XmlElement
	public String getFormatGenre() {
        return getString(FieldsEnum.FormatGenre.name());
    }

	public void setFormatGenre(String value) {
        put(FieldsEnum.FormatGenre.name(), value);
    }

  	@XmlElement
	public String getFormatCreator() {
        return getString(FieldsEnum.FormatCreator.name());
    }

	public void setFormatCreator(String value) {
        put(FieldsEnum.FormatCreator.name(), value);
    }

  	@XmlElement
	public String getOpenFormat() {
        return getString(FieldsEnum.OpenFormat.name());
    }

	public void setOpenFormat(String value) {
        put(FieldsEnum.OpenFormat.name(), value);
    }

  	@XmlElement
	public String getFileExtensions() {
        return getString(FieldsEnum.FileExtensions.name());
    }

	public void setFileExtensions(String value) {
        put(FieldsEnum.FileExtensions.name(), value);
    }

  	@XmlElement
	public String getVendors() {
        return getString(FieldsEnum.Vendors.name());
    }

	public void setVendors(String value) {
        put(FieldsEnum.Vendors.name(), value);
    }

  	@XmlElement
	public String getStandards() {
        return getString(FieldsEnum.Standards.name());
    }

	public void setStandards(String value) {
        put(FieldsEnum.Standards.name(), value);
    }

	@XmlElement
	public String[] getRepositoryId() {
        return toStringArray(get(FieldsEnum.RepositoryId.name()));
    }

	public void setRepositoryId(String[] value) {
        put(FieldsEnum.RepositoryId.name(), value);
    }
	
	@XmlElement
	public String[] getDescription() {
        return toStringArray(get(FieldsEnum.Description.name()));
    }

	public void setDescription(String[] value) {
        put(FieldsEnum.Description.name(), value);
    }
	
	@XmlElement
	public String[] getSoftwareName() {
        return toStringArray(get(FieldsEnum.SoftwareName.name()));
    }

	public void setSoftwareName(String[] value) {
        put(FieldsEnum.SoftwareName.name(), value);
    }
	
	@XmlElement
	public String[] getSoftwareId() {
        return toStringArray(get(FieldsEnum.SoftwareId.name()));
    }

	public void setSoftwareId(String[] value) {
        put(FieldsEnum.SoftwareId.name(), value);
    }
	
	@XmlElement
	public String[] getVendorName() {
        return toStringArray(get(FieldsEnum.VendorName.name()));
    }

	public void setVendorName(String[] value) {
        put(FieldsEnum.VendorName.name(), value);
    }
	
	@XmlElement
	public String[] getVendorId() {
        return toStringArray(get(FieldsEnum.VendorId.name()));
    }

	public void setVendorId(String[] value) {
        put(FieldsEnum.VendorId.name(), value);
    }
	
}
