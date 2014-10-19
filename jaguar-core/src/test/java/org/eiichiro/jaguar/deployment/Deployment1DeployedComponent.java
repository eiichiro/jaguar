package org.eiichiro.jaguar.deployment;

import java.lang.annotation.Annotation;

@Deployment1
public class Deployment1DeployedComponent implements DeployedComponent {

	public Class<? extends Annotation> deployment() {
		return Deployment1.class;
	}

}
