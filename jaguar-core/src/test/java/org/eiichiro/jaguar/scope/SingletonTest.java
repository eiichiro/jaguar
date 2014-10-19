package org.eiichiro.jaguar.scope;

import static org.eiichiro.jaguar.Jaguar.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.eiichiro.jaguar.Container;
import org.eiichiro.jaguar.Descriptor;
import org.eiichiro.jaguar.scope.Context;
import org.eiichiro.jaguar.scope.Singleton;
import org.junit.Test;

public class SingletonTest {

	@Test
	public void test() throws InterruptedException {
		bootstrap();
		install(SingletonComponent.class);
		SingletonComponent singletonComponent = component(SingletonComponent.class);
		final SingletonComponent singletonObject2 = component(SingletonComponent.class);
		assertSame(singletonComponent, singletonObject2);
		java.lang.Thread thread = new java.lang.Thread(new Runnable() {
			
			public void run() {
				SingletonComponent singletonObject3 = component(SingletonComponent.class);
				assertSame(singletonObject2, singletonObject3);
			}
			
		});
		thread.start();
		thread.join();
		shutdown();
	}
	
	@Test
	public void testEager() {
		bootstrap();
		install(EagerSingletonComponent.class);
		Container container = component(Container.class);
		Descriptor<?> descriptor = (Descriptor<?>) container.components().get(EagerSingletonComponent.class).toArray()[0];
		Context context = container.component(container.contexts().get(Singleton.class));
		Object object = context.get(descriptor);
		assertNotNull(object);
		assertThat(object, instanceOf(EagerSingletonComponent.class));
		shutdown();
	}

}
