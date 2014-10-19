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

public class RequestTestServlet extends HttpServlet {

	private static final long serialVersionUID = 6545631280993215474L;

	@SuppressWarnings("unchecked")
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		install(RequestComponent.class);
		RequestComponent requestComponent = component(RequestComponent.class);
		RequestComponent requestObject2 = component(RequestComponent.class);
		assertSame(requestComponent, requestObject2);
		Map<Descriptor<?>, Object> components = (Map<Descriptor<?>, Object>) request.getAttribute(WebContext.COMPONENTS);
		assertNotNull(components);
		Object component = components.get(components.keySet().toArray()[0]);
		assertSame(requestComponent, component);
	}
}
