package org.eiichiro.jaguar.deployment;

import java.lang.annotation.Annotation;

public interface DeployedObject {

	public Class<? extends Annotation> deployment();
	
}
