/*
 * Copyright (C) 2011 Eiichiro Uchiumi. All Rights Reserved.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 *     
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.eiichiro.jaguar;

import static org.eiichiro.jaguar.Jaguar.*;

import java.util.Collection;
import java.util.HashSet;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.eiichiro.jaguar.deployment.Production;
import org.eiichiro.jaguar.deployment.Deployment;
import org.eiichiro.jaguar.inject.Inject;
import org.eiichiro.jaguar.scope.Application;
import org.eiichiro.jaguar.scope.Context;
import org.eiichiro.jaguar.scope.Request;
import org.eiichiro.jaguar.scope.Session;
import org.eiichiro.jaguar.scope.WebContext;

/**
 * A listener to capture Web-related events.
 * You need to setup this listener and {@link WebFilter} in the <code>web.xml</code> 
 * as below if you use Jaguar in Web environment: 
 * <pre>
 * &lt;listener&gt;
 *     &lt;listener-class&gt;org.eiichiro.jaguar.WebListener&lt;/listener-class&gt;
 *     &lt;!-- 
 *      * or org.eiichiro.jaguar.WebListener extension class overriding 
 *      * {@code WebListener#install()} method to install components on 
 *      * the application bootstrap.
 *      --&gt;
 * &lt;/listener&gt;
 * </pre>
 * and specify the {@link Deployment} the application is running on like this (If no 
 * deployment has been specified, the application is running on {@link Production} 
 * deployment): 
 * <pre>
 * &lt;context-param&gt;
 *     &lt;param-name&gt;org.eiichiro.jaguar.deployment&lt;/param-name&gt;
 *     &lt;!-- Fully qualified deployment qualifier class name. --&gt;
 *     &lt;param-value&gt;org.eiichiro.jaguar.deployment.Testing&lt;/param-value&gt;
 * &lt;/context-param&gt;
 * </pre>
 * 
 * @author <a href="mailto:eiichiro@eiichiro.org">Eiichiro Uchiumi</a>
 */
public class WebListener implements ServletRequestListener,
		HttpSessionListener, ServletContextListener {

	private static final String DEPLOYMENT = "org.eiichiro.jaguar.deployment";
	
	@Inject private Container container;
	
	/**
	 * Bootstraps the application in Web environment on the specified deployment 
	 * (if specified in the web.xml) and associates the current {@code ServletContext} 
	 * with {@link Application} context.
	 * 
	 * @param sce The event the {@code ServletContext} is initialized.
	 */
	@SuppressWarnings("unchecked")
	public void contextInitialized(ServletContextEvent sce) {
		synchronized (Jaguar.class) {
			if (!running()) {
				bootstrap();
			}
		}
		
		assemble(this);
		WebContext<ServletContext> context = (WebContext<ServletContext>) container.component(container.contexts().get(Application.class));
		context.associate(sce.getServletContext());
		Class<?> deployment = deployment(sce.getServletContext());
		
		if (deployment != null) {
			Jaguar.deployment(deployment);
		}
		
		install(components(sce.getServletContext()));
	}
	
	/**
	 * Returns the deployment qualifier class specified at web.xml.
	 * If no deployment qualifier class is specified, this method returns 
	 * <code>null</code>.
	 * 
	 * @param context {@code ServletContext} of the current application.
	 * @return The deployment qualifier class specified at web.xml.
	 */
	protected Class<?> deployment(ServletContext context) {
		String clazz = context.getInitParameter(DEPLOYMENT);
		Class<?> deployment = null;
		
		if (clazz != null) {
			try {
				deployment = Class.forName(clazz);
			} catch (ClassNotFoundException e) {
				throw new IllegalArgumentException(
						"Web bootstrap failed: Deployment qualifier specified at web.xml ["
								+ clazz + "] cannot be loaded", e);
			}
		}
		
		return deployment;
	}
	
	/**
	 * Declare {@code WebListener} subclass and override this method to specify 
	 * components to be install in bulk after the application bootstrap (Of course, 
	 * you can also install components from anywhere on the fly after the bootstrap).
	 * This method is invoked in {@code #contextInitialized(ServletContextEvent)}, 
	 * right after the application bootstrap. Use {@code Jaguar#install(Collection)} 
	 * to install components.
	 * 
	 * @param context {@code ServletContext} of the current application.
	 * @see Jaguar#install(Collection)
	 */
	protected Collection<Class<?>> components(ServletContext context) {
		return new HashSet<>(0);
	}

	/**
	 * Shuts down the application in Web environment and deassociates the 
	 * current {@code ServletContext} from {@link Application} context.
	 * 
	 * @param sce The event the {@code ServletContext} is destroyed.
	 */
	@SuppressWarnings("unchecked")
	public void contextDestroyed(ServletContextEvent sce) {
		WebContext<ServletContext> context = (WebContext<ServletContext>) container.component(container.contexts().get(Application.class));
		context.deassociate(sce.getServletContext());
		
		synchronized (Jaguar.class) {
			if (running()) {
				shutdown();
			}
		}
	}

	/**
	 * Associates the current {@code HttpSessionEvent} with {@link Session} 
	 * context.
	 * 
	 * @param se The event the {@code HttpSession} is created.
	 */
	@SuppressWarnings("unchecked")
	public void sessionCreated(HttpSessionEvent se) {
		Descriptor<? extends Context> descriptor = container.contexts().get(Session.class);
		
		if (descriptor != null) {
			WebContext<HttpSession> context = (WebContext<HttpSession>) container.component(descriptor);
			context.associate(se.getSession());
		}
	}

	/**
	 * Deassociates the current {@code HttpSessionEvent} from {@link Session} 
	 * context.
	 * 
	 * @param se The event the {@code HttpSession} is destroyed.
	 */
	@SuppressWarnings("unchecked")
	public void sessionDestroyed(HttpSessionEvent se) {
		WebContext<HttpSession> context = (WebContext<HttpSession>) container.component(container.contexts().get(Session.class));
		context.deassociate(se.getSession());
	}

	/**
	 * Associates the current {@code ServletRequest} with {@code Request} 
	 * context.
	 * 
	 * @param sre The event the {@code ServletRequest} is initialized.
	 */
	@SuppressWarnings("unchecked")
	public void requestInitialized(ServletRequestEvent sre) {
		WebContext<HttpServletRequest> context = (WebContext<HttpServletRequest>) container.component(container.contexts().get(Request.class));
		context.associate((HttpServletRequest) sre.getServletRequest());
	}
	
	/**
	 * Deassociates the current {@code ServletRequest} from {@code Request} 
	 * context.
	 * 
	 * @param sre The event the {@code ServletRequest} is destroyed.
	 */
	@SuppressWarnings("unchecked")
	public void requestDestroyed(ServletRequestEvent sre) {
		WebContext<HttpServletRequest> context = (WebContext<HttpServletRequest>) container.component(container.contexts().get(Request.class));
		context.deassociate((HttpServletRequest) sre.getServletRequest());
	}

}
