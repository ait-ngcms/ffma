package ait.ffma.domain;

/**
 * This interface defines the methods that are common for all Ffma domain objects
 * @author GordeaS <gsergiu79@gmx.at>
 *
 */
public interface FfmaDomainObject {

	/**
	 * Retrieve the identifier of the object stored in database
	 * 
	 * @return id
	 */
	public String getId();

	/**
	 * This method returns the logical name of the current domain object.
	 * By default, the simple classname will be used to as object name.  
	 * 
	 * @return
	 */
	public String getDomainObjectName();

	/**
	 * This method returns the name of the component to which the current domain
	 * object belongs
	 * 
	 * @return
	 */
	public String getComponentName();

	/**
	 * This method returns the list with the name of the attributes which hold
	 * the information related to the current domain object
	 * 
	 * @return
	 */
	public FieldDefEnum[] getFieldsEnum();

}
