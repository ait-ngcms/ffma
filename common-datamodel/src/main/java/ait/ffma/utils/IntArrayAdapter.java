package ait.ffma.utils;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@SuppressWarnings("restriction")
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class IntArrayAdapter {

	IntArrayAdapter() {
		_intArray = null;
	}

	public IntArrayAdapter(int[] intArray) {
		_intArray = intArray;
	}

	@XmlElement
	private int[] _intArray;

	public int[] getIntArray() {
		return _intArray;
	}
}
