package ait.ffma.common;

import ait.ffma.BaseFfmaConfiguration;

public abstract class CommonApiConfigurationImpl extends
		BaseFfmaConfiguration implements CommonApiConfiguration{

	public CommonApiConfigurationImpl() {
		super();
	}

	public String getComponentName() {
		return COMPONENT_NAME_COMMON;
	}

}