package ait.ffma.domain.preservation.riskmanagement;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import ait.ffma.domain.BaseFfmaDomainObject;
import ait.ffma.domain.FieldDefEnum;
import ait.ffma.factory.ComponentNameConstants;

/**
 * This class contains mapping between software, linked open data repositories IDs for software and file formats.
 */
@SuppressWarnings({"restriction" })
@XmlRootElement
public class DipSoftwareId extends BaseFfmaDomainObject {

	private static final long serialVersionUID = -6223651276044726688L;

	@XmlTransient
	public enum FieldsEnum implements FieldDefEnum {
		DipId         { public Class<?> evalType() { return String.class; } },
		Name          { public Class<?> evalType() { return String.class; } },
		AitId         { public Class<?> evalType() { return String[].class; } },
		PronomId      { public Class<?> evalType() { return String[].class; } },
		FreebaseId    { public Class<?> evalType() { return String[].class; } },
		DBPediaId     { public Class<?> evalType() { return String[].class; } },
		Description   { public Class<?> evalType() { return String[].class; } }, // summarized description from all LOD repositories
		DipFormatId   { public Class<?> evalType() { return String[].class; } },
		DipVendorId   { public Class<?> evalType() { return String[].class; } };

		public String evalName() { return this.name(); }
	}
	
	@XmlTransient
	public FieldDefEnum[] getFieldsEnum(){
		return FieldsEnum.values();
	}
	
	/**
	 * Constructor by component name
	 */
	public DipSoftwareId() {
		setFfmaObjectName(DipSoftwareId.class.getSimpleName());
		setComponentName(ComponentNameConstants.COMPONENT_PRESERVATION_RISKMANAGEMENT);
	}
	
  	@XmlElement
	public String getDipId() {
        return getString(FieldsEnum.DipId.name());
    }

	public void setDipId(String value) {
        put(FieldsEnum.DipId.name(), value);
    }

	@XmlElement
	public String getName() {
        return getString(FieldsEnum.Name.name());
    }

	public void setName(String value) {
        put(FieldsEnum.Name.name(), value);
    }
	
	@XmlElement
	public String[] getDipFormatId() {
        return toStringArray(get(FieldsEnum.DipFormatId.name()));
	}
	
	public void setDipFormatId(String[] value) {
        put(FieldsEnum.DipFormatId.name(), value);
	}
	
	@XmlElement
	public String[] getAitId() {
        return toStringArray(get(FieldsEnum.AitId.name()));
	}
	
	public void setAitId(String[] value) {
        put(FieldsEnum.AitId.name(), value);
	}
	
	@XmlElement
	public String[] getPronomId() {
        return toStringArray(get(FieldsEnum.PronomId.name()));
	}
	
	public void setPronomId(String[] value) {
        put(FieldsEnum.PronomId.name(), value);
	}
	
	@XmlElement
	public String[] getFreebaseId() {
        return toStringArray(get(FieldsEnum.FreebaseId.name()));
    }

	public void setFreebaseId(String[] value) {
        put(FieldsEnum.FreebaseId.name(), value);
    }
	
	@XmlElement
	public String[] getDBPediaId() {
        return toStringArray(get(FieldsEnum.DBPediaId.name()));
    }

	public void setDBPediaId(String[] value) {
        put(FieldsEnum.DBPediaId.name(), value);
    }
	
	@XmlElement
	public String[] getDescription() {
        return toStringArray(get(FieldsEnum.Description.name()));
    }

	public void setDescription(String[] value) {
        put(FieldsEnum.Description.name(), value);
    }
	
	@XmlElement
	public String[] getDipVendorId() {
        return toStringArray(get(FieldsEnum.DipVendorId.name()));
	}
	
	public void setDipVendorId(String[] value) {
        put(FieldsEnum.DipVendorId.name(), value);
	}
	
}
