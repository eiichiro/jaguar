package org.eiichiro.jaguar;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.testing.ServletTester;
import org.eiichiro.jaguar.ClasspathScanner;
import org.eiichiro.reverb.system.Environment;
import org.junit.Test;

public class ClasspathScannerTest {

	@Test
	public void testClasspathScanner() throws Exception {
		try {
			new ClasspathScanner();
			fail();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		}
		
		Jaguar.bootstrap();
		
		// Running on URLClassLoader.
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		
		if (!(loader instanceof URLClassLoader)) {
			fail();
		}
		
		URLClassLoader classLoader = (URLClassLoader) loader;
		List<URL> urls = Arrays.asList(classLoader.getURLs());
		Collection<URL> paths = (Collection<URL>) new ClasspathScanner().paths();
		assertThat(paths.size(), is(urls.size()));
		
		for (URL url : urls) {
			assertTrue(paths.contains(url));
		}
		
		// Standalone or running on a embedded Servlet container.
		urls = new ArrayList<>();
		
		for (String path : Environment.getProperty("java.class.path").split(
				File.pathSeparator)) {
			urls.add(new File(path).toURI().toURL());
		}
		
		Thread.currentThread().setContextClassLoader(new ClassLoader(classLoader) {});
		
		if (Thread.currentThread().getContextClassLoader() instanceof URLClassLoader) {
			fail();
		}
		
		paths = (Collection<URL>) new ClasspathScanner().paths();
		assertThat(paths.size(), is(urls.size()));
		
		for (URL url : urls) {
			assertTrue(paths.contains(url));
		}
		
		// Running on a Servlet container with the standard web application 
		// layout.
		Jaguar.shutdown();
		ServletTester tester = new ServletTester();
		tester.setContextPath("/jaguar");
		tester.addFilter(WebFilter.class, "/*", 0);
		tester.addServlet(DefaultServlet.class, "/");
		tester.addEventListener(new WebListener());
		ServletContextHandler context = tester.getContext();
		
		for (URL url : urls) {
			if (url.getPath().endsWith("jaguar-core/target/test-classes/")) {
				context.setResourceBase(url.getPath());
			}
		}
		
		tester.start();
		paths = (Collection<URL>) new ClasspathScanner().paths();
		assertThat(paths.size(), is(2));
		
		if (paths.toArray(new URL[0])[0].getPath().endsWith("jaguar-core/target/test-classes/WEB-INF/classes/")) {
			assertTrue(paths.toArray(new URL[0])[1].getPath().endsWith("jaguar-core/target/test-classes/WEB-INF/lib/jaguar-core-2.0-rc5.jar"));
		} else {
			assertTrue(paths.toArray(new URL[0])[0].getPath().endsWith("jaguar-core/target/test-classes/WEB-INF/lib/jaguar-core-2.0-rc5.jar"));
			assertTrue(paths.toArray(new URL[0])[1].getPath().endsWith("jaguar-core/target/test-classes/WEB-INF/classes/"));
		}
		
		tester.stop();
		Jaguar.shutdown();
		Thread.currentThread().setContextClassLoader(loader);
	}

	@Test
	public void testScan() {
		Jaguar.bootstrap();
		Collection<Class<?>> classes = new ClasspathScanner().scan();
		assertTrue(classes.contains(ClasspathScannerComponent1.class));
		assertTrue(classes.contains(ClasspathScannerComponent2.class));
		assertTrue(classes.contains(ClasspathScannerComponent3.class));
		assertTrue(classes.contains(ClasspathScannerComponent4.class));
		assertTrue(classes.contains(ClasspathScannerProvider.class));
		assertFalse(classes.contains(ClasspathScannerComponent5.class));
		Jaguar.shutdown();
	}

}
