package ait.ffma.service.preservation.riskmanagement.mappingregistry;


public interface MappingRegistry {

	public enum RegistryTypesEnum {
		SoftwareVendor,
		GraphicsFileFormats;
	}
	
	/**
	 * This method returns value identified by passed key
	 * @param type
	 *        The risk property type
	 * @param key
	 *        The name of the mapping key
	 * @return stored value
	 */
	public String getValue(RegistryTypesEnum type, String key);

	/**
	 * This method returns key identified by passed value
	 * @param type
	 *        The risk property type
	 * @param value
	 *        The name of the mapped value
	 * @return stored key
	 */
	public String getKey(RegistryTypesEnum type, String value);
}
