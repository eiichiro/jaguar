package org.eiichiro.jaguar.deployment;

import java.lang.annotation.Annotation;

import org.eiichiro.jaguar.deployment.Development;

@Development
public class DevelopmentDeployedComponent implements DeployedComponent {

	public Class<? extends Annotation> deployment() {
		return Development.class;
	}

}
