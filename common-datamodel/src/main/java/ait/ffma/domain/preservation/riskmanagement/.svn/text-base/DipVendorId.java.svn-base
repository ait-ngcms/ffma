package ait.ffma.domain.preservation.riskmanagement;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import ait.ffma.domain.BaseFfmaDomainObject;
import ait.ffma.domain.FieldDefEnum;
import ait.ffma.factory.ComponentNameConstants;

/**
 * This class contains mapping between vendors, linked open data repositories IDs for 
 * vendors and file formats und software.
 */
@SuppressWarnings({"restriction" })
@XmlRootElement
public class DipVendorId extends BaseFfmaDomainObject {

	private static final long serialVersionUID = 2584981008401214206L;

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
		DipSoftwareId { public Class<?> evalType() { return String[].class; } };

		public String evalName() { return this.name(); }
	}
	
	@XmlTransient
	public FieldDefEnum[] getFieldsEnum(){
		return FieldsEnum.values();
	}
	
	/**
	 * Constructor by component name
	 */
	public DipVendorId() {
		setFfmaObjectName(DipVendorId.class.getSimpleName());
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
	public String[] getDipSoftwareId() {
        return toStringArray(get(FieldsEnum.DipSoftwareId.name()));
	}
	
	public void setDipSoftwareId(String[] value) {
        put(FieldsEnum.DipSoftwareId.name(), value);
	}
	
}
