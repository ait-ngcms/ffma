package ait.ffma.domain.preservation.riskmanagement;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import ait.ffma.domain.FieldDefEnum;
import ait.ffma.factory.ComponentNameConstants;

@SuppressWarnings("restriction")
@XmlRootElement
public class VideoFileDescription extends FileDescription {

	private static final long serialVersionUID = 2648429128507919537L;
	 
	@XmlTransient
	public enum FieldsEnum implements FieldDefEnum {
		VideoCodec                    { public Class<?> evalType() { return String.class; } },
		AudioCodec                    { public Class<?> evalType() { return String.class; } };

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
	public VideoFileDescription () {
		setFfmaObjectName(getClass().getSimpleName());
		setComponentName(ComponentNameConstants.COMPONENT_PRESERVATION_RISKMANAGEMENT);
	}
	
	/**
	 * @return the video codec
	 */
	@XmlElement
	public String getVideoCodec() {
		return getString(FieldsEnum.VideoCodec.name());
	}

	public void setVideoCodec(String videoCodec) {
		this.put(FieldsEnum.VideoCodec.name(), videoCodec);
	}

	/**
	 * @return the audio codec
	 */
	@XmlElement
	public String getAudioCodec() {
		return getString(FieldsEnum.AudioCodec.name());
	}

	public void setAudioCodec(String audioCodec) {
		this.put(FieldsEnum.AudioCodec.name(), audioCodec);
	}

	@Override
	public String toString() {
		return super.toString();
	}

}
