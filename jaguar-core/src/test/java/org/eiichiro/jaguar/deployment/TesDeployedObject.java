package org.eiichiro.jaguar.deployment;

import java.lang.annotation.Annotation;

import org.eiichiro.jaguar.deployment.Testing;

// maven-surefire-plugin treats a class that the name starts with 'Test' as a 
// test case automatically, so changed the class name 'TestingDeployedObject' to 
// 'TesDeployedObject' to work around.
@Testing
public class TesDeployedObject implements DeployedObject {

	public Class<? extends Annotation> deployment() {
		return Testing.class;
	}

}
