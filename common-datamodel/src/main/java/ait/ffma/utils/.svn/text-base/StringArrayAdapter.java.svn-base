package ait.ffma.utils;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@SuppressWarnings("restriction")
@XmlRootElement 
@XmlAccessorType(XmlAccessType.FIELD) 
public class StringArrayAdapter {

	StringArrayAdapter() {
		_stringArray = null;
	}

	public StringArrayAdapter(String[] stringArray) {
		_stringArray = stringArray;
	}

	@XmlElement 
	private String[] _stringArray;
	
	public String [] getStringArray() {
			return _stringArray;
	}
}
