package org.eiichiro.jaguar.scope;

import static org.eiichiro.jaguar.Jaguar.*;
import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eiichiro.jaguar.Descriptor;
import org.eiichiro.jaguar.scope.WebContext;

public class ApplicationTestServlet extends HttpServlet {

	private static final long serialVersionUID = 1559369261080351507L;

	@SuppressWarnings("unchecked")
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		install(ApplicationObject.class);
		ApplicationObject applicationObject = component(ApplicationObject.class);
		ApplicationObject applicationObject2 = component(ApplicationObject.class);
		assertSame(applicationObject, applicationObject2);
		Map<Descriptor<?>, Object> components = (Map<Descriptor<?>, Object>) request.getSession().getServletContext().getAttribute(WebContext.COMPONENTS);
		assertNotNull(components);
		Object component = components.get(components.keySet().toArray()[0]);
		assertSame(applicationObject, component);
	}
}
