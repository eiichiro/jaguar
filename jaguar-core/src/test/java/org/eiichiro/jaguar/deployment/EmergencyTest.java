package org.eiichiro.jaguar.deployment;

import static org.eiichiro.jaguar.Jaguar.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.eiichiro.jaguar.deployment.Emergency;
import org.junit.Test;

public class EmergencyTest {

	@Test
	public void test() {
		deployment(Emergency.class);
		bootstrap();
		install(DevelopmentDeployedObject.class);
		install(TesDeployedObject.class);
		install(ProductionDeployedObject.class);
		install(EmergencyDeployedObject.class);
		install(Deployment1DeployedObject.class);
		DeployedObject component = component(DeployedObject.class);
		assertThat(component.deployment(), is((Object) Emergency.class));
		shutdown();
	}
	
}
