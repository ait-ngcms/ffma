package ait.ffma.service.preservation.riskmanagement.mappingregistry;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * This class is an internal risk mapping registry 
 */
public class BaseRegistry implements MappingRegistry {
	
	private Map<String, String> softwareVendorMap = new HashMap<String, String>();
	private Map<String, String> graphicsFileFormatsMap = new HashMap<String, String>();
	
	/**
	 * @return
	 */
	public Map<String, String> getSoftwareVendorMap() {
		return softwareVendorMap;
	}

	/**
	 * @param softwareVendorMap
	 */
	public void setSoftwareVendorMap(Map<String, String> softwareVendorMap) {
		this.softwareVendorMap = softwareVendorMap;
	}

	/**
	 * @return
	 */
	public Map<String, String> getGraphicsFileFormatsMap() {
		return graphicsFileFormatsMap;
	}

	/**
	 * @param graphicsFileFormatsMap
	 */
	public void setGraphicsFileFormatsMap(Map<String, String> graphicsFileFormatsMap) {
		this.graphicsFileFormatsMap = graphicsFileFormatsMap;
	}

	public BaseRegistry() {
	}

	/* (non-Javadoc)
	 * @see ait.ffma.service.preservation.riskmanagement.mappingregistry.MappingRegistry#getValue(ait.ffma.service.preservation.riskmanagement.mappingregistry.MappingRegistry.RegistryTypesEnum, java.lang.String)
	 */
	public String getValue(RegistryTypesEnum type, String key) {
		String res = null;
		switch (type) {
			case SoftwareVendor:
				res = softwareVendorMap.get(key);
				break;
			case GraphicsFileFormats:
				res = graphicsFileFormatsMap.get(key);
				break;
			default:
				res = "";			
		}
		return res;
	}
	
	/**
	 * This method evaluates key by passed value
	 * @param map
	 * @param value
	 * @return key
	 */
	private String evaluateKey(Map<String, String> map, String value) {
		String res = null;
		Iterator<Map.Entry<String,String>> iter = map.entrySet().iterator();
		while (iter.hasNext()) {
		    Map.Entry<String,String> entry = iter.next();
		    if (entry.getValue().equals(value)) {
		        res = entry.getKey();
		    }
		}
		return res;
	}

	/* (non-Javadoc)
	 * @see ait.ffma.service.preservation.riskmanagement.mappingregistry.MappingRegistry#getKey(ait.ffma.service.preservation.riskmanagement.mappingregistry.MappingRegistry.RegistryTypesEnum, java.lang.String)
	 */
	public String getKey(RegistryTypesEnum type, String value) {
		String res = null;
		switch (type) {
			case SoftwareVendor:
				res = evaluateKey(softwareVendorMap, value);
				break;
			case GraphicsFileFormats:
				res = evaluateKey(graphicsFileFormatsMap, value);
				break;
			default:
				res = "";			
		}
		return res;
	}

}
