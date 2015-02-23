package ait.ffma.domain.preservation.riskmanagement;

import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import ait.ffma.domain.BaseFfmaDomainObject;
import ait.ffma.domain.FieldDefEnum;
import ait.ffma.factory.ComponentNameConstants;

@SuppressWarnings("restriction")
@XmlRootElement
public class FileDescription extends BaseFfmaDomainObject {

	private static final long serialVersionUID = 2648429128507919536L;
	 
	@XmlTransient
	public enum FieldsEnum implements FieldDefEnum {
		Creator                       { public Class<?> evalType() { return String.class; } },
		CreationDate                  { public Class<?> evalType() { return String.class; } },
		EuropeanaId                   { public Class<?> evalType() { return String.class; } },
		CollectionName                { public Class<?> evalType() { return String.class; } },
		FileName                      { public Class<?> evalType() { return String.class; } },
		Format                        { public Class<?> evalType() { return String.class; } };

		public String evalName() { return this.name(); }
	}
	
	/**
	 * This mapping contains property key value pairs.
	 */
	private Map<String, String> propertyMap = new HashMap<String, String>();
	
	@XmlTransient
	public FieldDefEnum[] getFieldsEnum(){
		return FieldsEnum.values();
	}
	
	/**
	 * Constructor by component name
	 * @param FfmajectName
	 */
	public FileDescription () {
		setFfmaObjectName(getClass().getSimpleName());
		setComponentName(ComponentNameConstants.COMPONENT_PRESERVATION_RISKMANAGEMENT);
	}
	
	/**
	 * @return the collectionName
	 */
	@XmlElement
	public String getCollectionName() {
		return getString(FieldsEnum.CollectionName.name());
	}

	public void setCollectionName(String collectionName) {
		this.put(FieldsEnum.CollectionName.name(), collectionName);
	}

	/**
	 * @return the europeanaId
	 */
	@XmlElement
	public String getEuropeanaId() {
		return getString(FieldsEnum.EuropeanaId.name());
	}

	public void setEuropeanaId(String europeanaId) {
		this.put(FieldsEnum.EuropeanaId.name(), europeanaId);
	}

	/**
	 * @return the creator
	 */
	@XmlElement
	public String getCreator() {
		return getString(FieldsEnum.Creator.name());
	}

	public void setCreator(String creator) {
		this.put(FieldsEnum.Creator.name(), creator);
	}

	/**
	 * @return the creation date
	 */
	@XmlElement
	public String getCreationDate() {
		return getString(FieldsEnum.CreationDate.name());
	}

	public void setCreationDate(String creationDate) {
		this.put(FieldsEnum.CreationDate.name(), creationDate);
	}

	/**
	 * @return the file name
	 */
	@XmlElement
	public String getFileName() {
		return getString(FieldsEnum.FileName.name());
	}

	public void setFileName(String fileName) {
		this.put(FieldsEnum.FileName.name(), fileName);
	}

	/**
	 * @return the format
	 */
	@XmlElement
	public String getFormat() {
		return getString(FieldsEnum.Format.name());
	}

	public void setFormat(String format) {
		this.put(FieldsEnum.Format.name(), format);
	}

	/**
	 * @return the propertyMap
	 */
	public Map<String, String> getPropertyMap() {
		return propertyMap;
	}

	/**
	 * @param propertyMap the propertyMap to set
	 */
	public void setPropertyMap(Map<String, String> propertyMap) {
		this.propertyMap = propertyMap;
	}

	@Override
	public String toString() {
		return super.toString();
	}

}
