package ait.ffma.domain.preservation.riskmanagement;

import java.util.Iterator;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import ait.ffma.domain.BaseFfmaDomainObject;
import ait.ffma.domain.ClassDefEnum;
import ait.ffma.domain.FieldDefEnum;
import ait.ffma.factory.ComponentNameConstants;

@SuppressWarnings("restriction")
@XmlRootElement
public class PreservationDimensions extends BaseFfmaDomainObject {

	private static final long serialVersionUID = -8829601523915961428L;
	 
	@XmlTransient
	public enum FieldsEnum implements FieldDefEnum, ClassDefEnum {
		CollectionId                  { public Class<?> evalType() { return Long.class; } },
		CollectionName                { public Class<?> evalType() { return String.class; } },
		PreservationDimensionList     { public Class<?> evalType() { return List.class; } };

		public String evalName() { return this.name(); }		

		/**
		 * This method returns the class of the list by passed enumeration field
		 * @return the class of the list
		 */
		public Class<?> evalListType() { 
			return PreservationDimension.class;
		}	
	}
	
	@XmlTransient
	public FieldDefEnum[] getFieldsEnum(){
		return FieldsEnum.values();
	}
	
	/**
	 * Constructor by component name
	 * @param FfmaObjectName
	 */
	public PreservationDimensions() {
		setFfmaObjectName(getClass().getSimpleName());
		setComponentName(ComponentNameConstants.COMPONENT_PRESERVATION_RISKMANAGEMENT);
	}
	
	/**
	 * @return the collectionId
	 */
	@XmlElement
	public Long getCollectionId() {
		return Long.valueOf((String) get(FieldsEnum.CollectionId.name()));
	}

	public void setCollectionId(String collectionId) {
		this.put(FieldsEnum.CollectionId.name(), collectionId);
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

	@SuppressWarnings("unchecked")
	@XmlElement
	public List<PreservationDimension> getPreservationDimensionList() {
		return (List<PreservationDimension>)(get(FieldsEnum.PreservationDimensionList.name()));
	}

	public void setPreservationDimensionsList(List<PreservationDimension> preservationDimensionList) {
		this.put(FieldsEnum.PreservationDimensionList.name(), preservationDimensionList);
	}

	@Override
	public String toString() {
		return super.toString();
	}

	/**
	 * This method returns preservation dimension object by passed name
	 * @param preservationDimensionName
	 *        The name of preservation dimension
	 * @return preservation dimension object
	 */
	public PreservationDimension getPreservationDimension(String preservationDimensionName) {
		PreservationDimension res = null;
		List<PreservationDimension> preservationDimensionList = getPreservationDimensionList();
		Iterator<PreservationDimension> iterPreservationDimension = preservationDimensionList.iterator();
		while (iterPreservationDimension.hasNext()) {
			PreservationDimension preservationDimension = iterPreservationDimension.next();
			if (preservationDimension.getName().equals(preservationDimensionName)) {
				res = preservationDimension;
				break;
			}
		}
		return res;
	}
}
