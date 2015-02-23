package ait.ffma.domain.preservation.riskmanagement;

import java.util.Map;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import ait.ffma.domain.BaseFfmaDomainObject;
import ait.ffma.domain.FieldDefEnum;
import ait.ffma.factory.ComponentNameConstants;

@SuppressWarnings("restriction")
@XmlRootElement
public class PreservationDimension extends BaseFfmaDomainObject {

	private static final long serialVersionUID = -8829601523915961428L;
	 
	@XmlTransient
	public enum FieldsEnum implements FieldDefEnum {
		CollectionName                { public Class<?> evalType() { return String.class; } },
		Name                          { public Class<?> evalType() { return String.class; } },
		Content                       { public Class<?> evalType() { return Map.class; } };

		public String evalName() { return this.name(); }
	}
	
	@XmlTransient
	public FieldDefEnum[] getFieldsEnum(){
		return FieldsEnum.values();
	}
	
	/**
	 * Constructor by component name
	 * @param FfmaObjectName
	 */
	public PreservationDimension() {
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

	@XmlElement
	public String getName() {
		return getString(FieldsEnum.Name.name());
	}

	public void setName(String name) {
		this.put(FieldsEnum.Name.name(), name);
	}

	@Override
	public String toString() {
		return super.toString();
	}

	@SuppressWarnings("unchecked")
	@XmlElement
	public Map<String, String> getContent() {
		return (Map<String, String>) get(FieldsEnum.Content.name());
	}

	public void setContent(Map<String, String> content) {
		this.put(FieldsEnum.Content.name(), content);
	}

}
