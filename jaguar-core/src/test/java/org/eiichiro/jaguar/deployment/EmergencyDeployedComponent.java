package org.eiichiro.jaguar.deployment;

import java.lang.annotation.Annotation;

import org.eiichiro.jaguar.deployment.Emergency;

@Emergency
public class EmergencyDeployedComponent implements DeployedComponent {

	public Class<? extends Annotation> deployment() {
		return Emergency.class;
	}

}
