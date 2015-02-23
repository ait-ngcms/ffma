package ait.ffma.factory;

import ait.ffma.domain.FfmaDomainObject;

/**
 * This interface defines the methods used for the cross-component instantiation of
 * domain objects.  
 * 
 * @author GordeaS <gsergiu79@gmx.at>
 *
 */
public interface FfmaAbstractFactory {
	
	/**
	 * This method is used for the instantiation of the given domain objects from the given component 
	 *  
	 * @see ComponentNameConstants
	 * @param componentName - the name of component.
	 * @param domainObjectName
	 * @return
	 */
	public FfmaDomainObject createDomainObject(String componentName, String domainObjectName);
	

}
