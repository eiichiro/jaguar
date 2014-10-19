package org.eiichiro.jaguar.scope;

import static org.eiichiro.jaguar.Jaguar.*;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.testing.HttpTester;
import org.eclipse.jetty.testing.ServletTester;
import org.eiichiro.jaguar.WebFilter;
import org.eiichiro.jaguar.WebListener;
import org.junit.Test;


public class SessionTest {

	@Test
	public void test() throws Exception {
		// Outside Web container -> the same behavior as @Thread.
		bootstrap();
		install(SessionComponent.class);
		SessionComponent sessionComponent = component(SessionComponent.class);
		final SessionComponent sessionObject2 = component(SessionComponent.class);
		assertSame(sessionComponent, sessionObject2);
		java.lang.Thread thread = new java.lang.Thread(new Runnable() {
			
			public void run() {
				SessionComponent sessionObject3 = component(SessionComponent.class);
				assertNotSame(sessionObject2, sessionObject3);
			}
			
		});
		thread.start();
		thread.join();
		shutdown();
		
		// Inside Web container.
		ServletTester tester = new ServletTester();
		tester.setContextPath("/jaguar");
		tester.addFilter(WebFilter.class, "/*", 0);
		tester.addServlet(DefaultServlet.class, "/");
		tester.addServlet(SessionTestServlet.class, "/test");
		WebListener listener = new WebListener();
		tester.addEventListener(listener);
		tester.getContext().getSessionHandler().addEventListener(listener);
		tester.start();
		HttpTester request = new HttpTester();
		request.setURI("/jaguar/test");
		request.setMethod("GET");
		request.setHeader("Host", "");
		HttpTester response = new HttpTester();
		response.parse(tester.getResponses(request.generate()));
		assertThat(response.getStatus(), is(200));
		tester.stop();
	}
	
}
