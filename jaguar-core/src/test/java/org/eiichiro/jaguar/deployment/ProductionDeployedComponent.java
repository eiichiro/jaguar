package org.eiichiro.jaguar.deployment;

import java.lang.annotation.Annotation;

import org.eiichiro.jaguar.deployment.Production;

@Production
public class ProductionDeployedComponent implements DeployedComponent {

	public Class<? extends Annotation> deployment() {
		return Production.class;
	}

}
