package ait.ffma.factory;

import java.util.HashMap;
import java.util.Map;

import ait.ffma.common.exception.FfmaTechnicalRuntimeException;
import ait.ffma.domain.FfmaDomainObject;

/**
 * This class implements the functionality of the AbstractFactory pattern used for 
 * instantiation of the all Ffma domain objects. It instantiates the component 
 * factories on the fly and invoke their methods for instantiation of the required 
 * domain object. 
 * 
 * @see ComponentFactory
 *
 *  
 * @author GordeaS <gsergiu79@gmx.at>
 *
 */
public class FfmaAbstractFactoryImpl implements FfmaAbstractFactory {

	private static Map<String, ComponentFactory> componentFactoryMap = 
		new HashMap<String, ComponentFactory>();

	/*
	 * (non-Javadoc)
	 * @see ait.ffma.factory.FfmaAbstractFactory#createDomainObject(java.lang.String, java.lang.String)
	 */
	public FfmaDomainObject createDomainObject(String componentName,
			String domainObjectName) {

		return getComponentFactory(componentName).createDomainObject(
				domainObjectName);
	}

	protected ComponentFactory getComponentFactory(String componentName) {
		if (!getComponentFactoryMap().containsKey(componentName)) {
			getComponentFactoryMap().put(componentName,
					createComponentFactory(componentName));
		}

		return getComponentFactoryMap().get(componentName);
	}

	private ComponentFactory createComponentFactory(String componentName) {
		// String packageName = "ait.ffma.factory";
		String factoryClass = buildFactoryClassName(componentName);
		try {
			return (ComponentFactory) Class.forName(factoryClass).newInstance();
		} catch (InstantiationException e) {
			// TODO: think about using a proper checked exception
			throw new FfmaTechnicalRuntimeException(
					"Cannot instantiate factory for component: "
							+ componentName, e);
		} catch (IllegalAccessException e) {
			throw new FfmaTechnicalRuntimeException(
					"Cannot instantiate factory for component: "
							+ componentName, e);
		} catch (ClassNotFoundException e) {
			throw new FfmaTechnicalRuntimeException(
					"Cannot instantiate factory for component: "
							+ componentName, e);
		}
	}

	protected String buildFactoryClassName(String componentName) {
		String packageName = getClass().getPackage().getName();
		String[] names = componentName.split("-");

		StringBuilder builder = new StringBuilder();

		builder.append(packageName).append(".");

		for (String name : names) {
			if (name.length() > 0) {
				builder.append(name.substring(0, 1).toUpperCase());
				builder.append(name.substring(1));
			}
		}

		builder.append("Factory");

		return builder.toString();
	}

	public static Map<String, ComponentFactory> getComponentFactoryMap() {
		return componentFactoryMap;
	}

}
