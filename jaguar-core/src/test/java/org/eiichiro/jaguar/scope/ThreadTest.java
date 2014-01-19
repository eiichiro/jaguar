package org.eiichiro.jaguar.scope;

import static org.eiichiro.jaguar.Jaguar.*;
import static org.junit.Assert.*;

import org.junit.Test;

public class ThreadTest {

	@Test
	public void test() throws InterruptedException {
		bootstrap();
		install(ThreadObject.class);
		ThreadObject threadObject = component(ThreadObject.class);
		final ThreadObject threadObject2 = component(ThreadObject.class);
		assertSame(threadObject, threadObject2);
		java.lang.Thread thread = new java.lang.Thread(new Runnable() {
			
			public void run() {
				ThreadObject threadObject3 = component(ThreadObject.class);
				assertNotSame(threadObject2, threadObject3);
			}
			
		});
		thread.start();
		thread.join();
		shutdown();
	}
	
}
