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


public class RequestTest {

	@Test
	public void test() throws Exception {
		// Outside Web container -> the same behavior as @Thread.
		bootstrap();
		install(RequestComponent.class);
		RequestComponent requestComponent = component(RequestComponent.class);
		final RequestComponent requestObject2 = component(RequestComponent.class);
		assertSame(requestComponent, requestObject2);
		java.lang.Thread thread = new java.lang.Thread(new Runnable() {
			
			public void run() {
				RequestComponent requestObject3 = component(RequestComponent.class);
				assertNotSame(requestObject2, requestObject3);
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
		tester.addServlet(RequestTestServlet.class, "/test");
		tester.addEventListener(new WebListener());
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
