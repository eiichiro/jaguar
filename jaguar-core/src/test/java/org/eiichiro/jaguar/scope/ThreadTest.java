package org.eiichiro.jaguar.scope;

import static org.eiichiro.jaguar.Jaguar.*;
import static org.junit.Assert.*;

import org.junit.Test;

public class ThreadTest {

	@Test
	public void test() throws InterruptedException {
		bootstrap();
		install(ThreadComponent.class);
		ThreadComponent threadComponent = component(ThreadComponent.class);
		final ThreadComponent threadObject2 = component(ThreadComponent.class);
		assertSame(threadComponent, threadObject2);
		java.lang.Thread thread = new java.lang.Thread(new Runnable() {
			
			public void run() {
				ThreadComponent threadObject3 = component(ThreadComponent.class);
				assertNotSame(threadObject2, threadObject3);
			}
			
		});
		thread.start();
		thread.join();
		shutdown();
	}
	
}
