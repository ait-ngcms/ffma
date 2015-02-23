package ait.ffma.domain.preservation.riskmanagement;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import ait.ffma.domain.BaseFfmaDomainObject;
import ait.ffma.domain.FieldDefEnum;
import ait.ffma.factory.ComponentNameConstants;

/**
 * This class is a container for LOD software information retrieved from LOD repositories.
 */
@SuppressWarnings({"restriction" })
@XmlRootElement
public class LODSoftware extends BaseFfmaDomainObject {

	private static final long serialVersionUID = -2564477681567712577L;

	@XmlTransient
	public enum FieldsEnum implements FieldDefEnum {
		Index                   { public Class<?> evalType() { return Integer.class; } },
		Repository              { public Class<?> evalType() { return String.class; } },
		Timestamp               { public Class<?> evalType() { return String.class; } },
		SoftwareName            { public Class<?> evalType() { return String.class; } },
		SoftwareLicense         { public Class<?> evalType() { return String.class; } },
		SoftwareHomepage        { public Class<?> evalType() { return String.class; } },
		Genre                   { public Class<?> evalType() { return String.class; } },
		OperatingSystem         { public Class<?> evalType() { return String.class; } },
		Protocols               { public Class<?> evalType() { return String.class; } },
		ProgrammingLanguage     { public Class<?> evalType() { return String.class; } },
		SoftwareLatestVersion   { public Class<?> evalType() { return String.class; } },
		SoftwareReleaseDate     { public Class<?> evalType() { return String.class; } },
		RepositoryId            { public Class<?> evalType() { return String.class; } },
		FileFormat              { public Class<?> evalType() { return String[].class; } },
		Description             { public Class<?> evalType() { return String.class; } };

		public String evalName() { return this.name(); }
	}
	
	@XmlTransient
	public FieldDefEnum[] getFieldsEnum(){
		return FieldsEnum.values();
	}
	
	/**
	 * Constructor by component name
	 */
	public LODSoftware() {
		setFfmaObjectName(LODSoftware.class.getSimpleName());
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
	public String getSoftwareName() {
        return getString(FieldsEnum.SoftwareName.name());
    }

	public void setSoftwareName(String value) {
        put(FieldsEnum.SoftwareName.name(), value);
    }

  	@XmlElement
	public String getSoftwareLicense() {
        return getString(FieldsEnum.SoftwareLicense.name());
    }

	public void setSoftwareLicense(String value) {
        put(FieldsEnum.SoftwareLicense.name(), value);
    }

  	@XmlElement
	public String getSoftwareHomepage() {
        return getString(FieldsEnum.SoftwareHomepage.name());
    }

	public void setSoftwareHomepage(String value) {
        put(FieldsEnum.SoftwareHomepage.name(), value);
    }

  	@XmlElement
	public String getGenre() {
        return getString(FieldsEnum.Genre.name());
    }

	public void setGenre(String value) {
        put(FieldsEnum.Genre.name(), value);
    }

  	@XmlElement
	public String getOperatingSystem() {
        return getString(FieldsEnum.OperatingSystem.name());
    }

	public void setOperatingSystem(String value) {
        put(FieldsEnum.OperatingSystem.name(), value);
    }

  	@XmlElement
	public String getProtocols() {
        return getString(FieldsEnum.Protocols.name());
    }

	public void setProtocols(String value) {
        put(FieldsEnum.Protocols.name(), value);
    }

  	@XmlElement
	public String getProgrammingLanguage() {
        return getString(FieldsEnum.ProgrammingLanguage.name());
    }

	public void setProgrammingLanguage(String value) {
        put(FieldsEnum.ProgrammingLanguage.name(), value);
    }

  	@XmlElement
	public String getSoftwareLatestVersion() {
        return getString(FieldsEnum.SoftwareLatestVersion.name());
    }

	public void setSoftwareLatestVersion(String value) {
        put(FieldsEnum.SoftwareLatestVersion.name(), value);
    }

  	@XmlElement
	public String getSoftwareReleaseDate() {
        return getString(FieldsEnum.SoftwareReleaseDate.name());
    }

	public void setSoftwareReleaseDate(String value) {
        put(FieldsEnum.SoftwareReleaseDate.name(), value);
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
	public String getDescription() {
        return getString(FieldsEnum.Description.name());
    }

	public void setDescription(String value) {
        put(FieldsEnum.Description.name(), value);
    }
	
}
