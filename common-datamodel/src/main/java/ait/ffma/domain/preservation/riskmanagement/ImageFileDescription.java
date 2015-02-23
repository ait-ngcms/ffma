package ait.ffma.domain.preservation.riskmanagement;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import ait.ffma.domain.FieldDefEnum;
import ait.ffma.factory.ComponentNameConstants;

@SuppressWarnings("restriction")
@XmlRootElement
public class ImageFileDescription extends FileDescription {

	private static final long serialVersionUID = 2648429128507919537L;
	 
	@XmlTransient
	public enum FieldsEnum implements FieldDefEnum {
		Compression                    { public Class<?> evalType() { return String.class; } };

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
	public ImageFileDescription () {
		setFfmaObjectName(getClass().getSimpleName());
		setComponentName(ComponentNameConstants.COMPONENT_PRESERVATION_RISKMANAGEMENT);
	}
	
	/**
	 * @return the comression
	 */
	@XmlElement
	public String getCompression() {
		return getString(FieldsEnum.Compression.name());
	}

	public void setCompression(String compression) {
		this.put(FieldsEnum.Compression.name(), compression);
	}

	@Override
	public String toString() {
		return super.toString();
	}

}
