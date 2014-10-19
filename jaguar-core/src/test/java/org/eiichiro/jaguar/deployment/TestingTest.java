package org.eiichiro.jaguar.deployment;

import static org.eiichiro.jaguar.Jaguar.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.eiichiro.jaguar.deployment.Testing;
import org.junit.Test;

public class TestingTest {

	@Test
	public void test() {
		deployment(Testing.class);
		bootstrap();
		install(DevelopmentDeployedComponent.class);
		install(TesDeployedComponent.class);
		install(ProductionDeployedComponent.class);
		install(EmergencyDeployedComponent.class);
		install(Deployment1DeployedComponent.class);
		DeployedComponent component = component(DeployedComponent.class);
		assertThat(component.deployment(), is((Object) Testing.class));
		shutdown();
	}
	
}
