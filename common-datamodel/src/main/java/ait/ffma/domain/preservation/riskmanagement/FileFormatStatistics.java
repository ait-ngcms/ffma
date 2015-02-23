package ait.ffma.domain.preservation.riskmanagement;

import java.util.Map;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import ait.ffma.domain.BaseFfmaDomainObject;
import ait.ffma.domain.FieldDefEnum;
import ait.ffma.factory.ComponentNameConstants;

/**
 * This class is a container for PRONOM file formats statistics. It comprises mapping between
 * field value e.g. extension and its count in collection.
 */
@SuppressWarnings({"restriction" })
@XmlRootElement
public class FileFormatStatistics extends BaseFfmaDomainObject {

	private static final long serialVersionUID = -8550899955989621586L;

	@XmlTransient
	public enum FieldsEnum implements FieldDefEnum {
		Extensions    { public Class<?> evalType() { return Map.class; } },
		Mimetypes     { public Class<?> evalType() { return Map.class; } },
		Repository    { public Class<?> evalType() { return String.class; } };

		public String evalName() { return this.name(); }
	}
	
	@XmlTransient
	public FieldDefEnum[] getFieldsEnum(){
		return FieldsEnum.values();
	}
	
	/**
	 * Constructor by component name
	 */
	public FileFormatStatistics () {
		setFfmaObjectName(FileFormatStatistics.class.getSimpleName());
		setComponentName(ComponentNameConstants.COMPONENT_PRESERVATION_RISKMANAGEMENT);
	}
	
	@SuppressWarnings("rawtypes")
	@XmlElement
	public Map getExtensions() {
		return (Map) get(FieldsEnum.Extensions.name());
	}

	@SuppressWarnings("rawtypes")
	public void setExtensions(Map value) {
		this.put(FieldsEnum.Extensions.name(), value);
	}
	
	@SuppressWarnings("rawtypes")
	@XmlElement
	public Map getMimetypes() {
		return (Map) get(FieldsEnum.Mimetypes.name());
	}

	@SuppressWarnings("rawtypes")
	public void setMimetypes(Map value) {
		this.put(FieldsEnum.Mimetypes.name(), value);
	}
	
  	@XmlElement
	public String getRepository() {
        return getString(FieldsEnum.Repository.name());
    }

	public void setRepository(String value) {
        put(FieldsEnum.Repository.name(), value);
    }

}
