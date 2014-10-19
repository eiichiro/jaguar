package org.eiichiro.jaguar.lifecycle;

import static org.eiichiro.jaguar.Jaguar.*;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

import org.eiichiro.jaguar.lifecycle.Event;
import org.junit.Test;

public class LifecycleTest {

	@Test
	public void test() {
		bootstrap();
		install(StartupComponent.class);
		StartupComponent startupComponent = component(StartupComponent.class);
		Event.of(Startup.class).on(startupComponent).fire();
		assertThat(startupComponent.started, is(true));
		shutdown();
	}
	
}
