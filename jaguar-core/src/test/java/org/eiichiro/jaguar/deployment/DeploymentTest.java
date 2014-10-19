package org.eiichiro.jaguar.deployment;

import static org.eiichiro.jaguar.Jaguar.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Test;

public class DeploymentTest {

	@Test
	public void test() {
		deployment(Deployment1.class);
		bootstrap();
		install(DevelopmentDeployedComponent.class);
		install(TesDeployedComponent.class);
		install(ProductionDeployedComponent.class);
		install(EmergencyDeployedComponent.class);
		install(Deployment1DeployedComponent.class);
		DeployedComponent component = component(DeployedComponent.class);
		assertThat(component.deployment(), is((Object) Deployment1.class));
		shutdown();
	}
	
}
