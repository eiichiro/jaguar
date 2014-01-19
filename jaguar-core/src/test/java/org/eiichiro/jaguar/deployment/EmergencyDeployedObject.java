package org.eiichiro.jaguar.deployment;

import java.lang.annotation.Annotation;

import org.eiichiro.jaguar.deployment.Emergency;

@Emergency
public class EmergencyDeployedObject implements DeployedObject {

	public Class<? extends Annotation> deployment() {
		return Emergency.class;
	}

}
