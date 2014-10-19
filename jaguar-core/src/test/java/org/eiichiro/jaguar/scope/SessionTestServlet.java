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

public class SessionTestServlet extends HttpServlet {

	private static final long serialVersionUID = 9179163318138786678L;

	@SuppressWarnings("unchecked")
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		install(SessionComponent.class);
		SessionComponent sessionComponent = component(SessionComponent.class);
		SessionComponent sessionObject2 = component(SessionComponent.class);
		assertSame(sessionComponent, sessionObject2);
		Map<Descriptor<?>, Object> components = (Map<Descriptor<?>, Object>) request.getSession().getAttribute(WebContext.COMPONENTS);
		assertNotNull(components);
		Object component = components.get(components.keySet().toArray()[0]);
		assertSame(sessionComponent, component);
	}
}
