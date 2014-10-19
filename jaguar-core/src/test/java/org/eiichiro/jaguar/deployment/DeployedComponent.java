package org.eiichiro.jaguar.deployment;

import java.lang.annotation.Annotation;

public interface DeployedComponent {

	public Class<? extends Annotation> deployment();
	
}
