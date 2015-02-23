package ait.ffma.domain.preservation.riskmanagement;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import ait.ffma.domain.BaseFfmaDomainObject;
import ait.ffma.domain.FieldDefEnum;
import ait.ffma.factory.ComponentNameConstants;

/**
 * This class is a container for DBPedia file formats information like extension, version, mimetype, puid and xpuid.
 */
@SuppressWarnings({"restriction" })
@XmlRootElement
public class DBPediaFileFormat extends BaseFfmaDomainObject {

	private static final long serialVersionUID = -6644382431472098558L;

	@XmlTransient
	public enum FieldsEnum implements FieldDefEnum {
		Index           { public Class<?> evalType() { return Integer.class; } },
		Name            { public Class<?> evalType() { return String.class; } },
		Extension       { public Class<?> evalType() { return String.class; } },
		Version         { public Class<?> evalType() { return String.class; } },
		Mimetype        { public Class<?> evalType() { return String.class; } },
		Puid            { public Class<?> evalType() { return String.class; } },
		Xpuid           { public Class<?> evalType() { return String.class; } },
		Description     { public Class<?> evalType() { return String.class; } };

		public String evalName() { return this.name(); }
	}
	
	@XmlTransient
	public FieldDefEnum[] getFieldsEnum(){
		return FieldsEnum.values();
	}
	
	/**
	 * Constructor by component name
	 */
	public DBPediaFileFormat () {
		setFfmaObjectName(DBPediaFileFormat.class.getSimpleName());
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
	public String getName() {
        return getString(FieldsEnum.Name.name());
    }

	public void setName(String value) {
        put(FieldsEnum.Name.name(), value);
    }

	@XmlElement
	public String getExtension() {
        return getString(FieldsEnum.Extension.name());
    }

	public void setExtension(String value) {
        put(FieldsEnum.Extension.name(), value);
    }
	
  	@XmlElement
	public String getVersion() {
        return getString(FieldsEnum.Version.name());
    }

	public void setVersion(String value) {
        put(FieldsEnum.Version.name(), value);
    }

  	@XmlElement
	public String getMimetype() {
        return getString(FieldsEnum.Mimetype.name());
    }

	public void setMimetype(String value) {
        put(FieldsEnum.Mimetype.name(), value);
    }

  	@XmlElement
	public String getPuid() {
        return getString(FieldsEnum.Puid.name());
    }

	public void setPuid(String value) {
        put(FieldsEnum.Puid.name(), value);
    }

  	@XmlElement
	public String getXpuid() {
        return getString(FieldsEnum.Xpuid.name());
    }

	public void setXpuid(String value) {
        put(FieldsEnum.Xpuid.name(), value);
    }

  	@XmlElement
	public String getDescription() {
        return getString(FieldsEnum.Description.name());
    }

	public void setDescription(String value) {
        put(FieldsEnum.Description.name(), value);
    }

}
