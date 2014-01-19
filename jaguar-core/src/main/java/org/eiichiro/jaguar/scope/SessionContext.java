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
package org.eiichiro.jaguar.scope;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpSession;

import org.eiichiro.jaguar.Builtin;
import org.eiichiro.jaguar.Descriptor;
import org.eiichiro.jaguar.WebFilter;
import org.eiichiro.jaguar.inject.Name;
import org.eiichiro.jaguar.lifecycle.Destroyed;
import org.eiichiro.jaguar.lifecycle.Event;
import org.eiichiro.jaguar.lifecycle.Passivated;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Built-in {@link Context} implementation corresponding to the {@link Session} 
 * scope.
 * {@code SessionContext} stores a component in HTTP session. If the application 
 * is not running in Web environment, this context becomes equal to 
 * {@link ThreadContext}.
 * 
 * @author <a href="mailto:eiichiro@eiichiro.org">Eiichiro Uchiumi</a>
 */
@Builtin
@Singleton(eager=true)
@Name("org.eiichiro.jaguar.scope.SessionContext")
public class SessionContext extends ThreadContext implements WebContext<HttpSession> {

	private Logger logger = LoggerFactory.getLogger(getClass());
	
	/**
	 * Returns the component corresponding to the specified {@link Descriptor} 
	 * from the current {@code HttpSession}.
	 * If the application is not running in Web environment, the component is 
	 * got from {@code ThreadContext}'s component store.
	 * 
	 * @param <T> The component type.
	 * @param descriptor The descriptor corresponding to the component.
	 * @return The component corresponding to the specified {@link Descriptor}.
	 */
	@SuppressWarnings("unchecked")
	public <T> T get(Descriptor<T> descriptor) {
		HttpSession session = WebFilter.session();
		
		if (session == null) {
			return super.get(descriptor);
		}
		
		Map<Descriptor<?>, Object> components 
				= (Map<Descriptor<?>, Object>) session.getAttribute(COMPONENTS);
		
		if (components != null) {
			return (T) components.get(descriptor);
		}
		
		return null;
	}

	/**
	 * Puts the specified component and descriptor pair into the current 
	 * {@code HttpSession}.
	 * If the application is not running in Web environment, the component is 
	 * put into {@code ThreadContext}'s component store.
	 * 
	 * @param <T> The component type.
	 * @param descriptor The descriptor corresponding to the component.
	 * @param component The component to be put.
	 */
	@SuppressWarnings("unchecked")
	public <T> void put(Descriptor<T> descriptor, T component) {
		HttpSession session = WebFilter.session();
		
		if (session == null) {
			super.put(descriptor, component);
			return;
		}
		
		Map<Descriptor<?>, Object> components 
				= (Map<Descriptor<?>, Object>) session.getAttribute(COMPONENTS);
		
		if (components != null) {
			components.put(descriptor, component);
		}
	}
	
	/**
	 * Associates the current {@code HttpSession} with this instance.
	 * 
	 * @param context The current {@code HttpSession}.
	 */
	public void associate(HttpSession context) {
		try {
			context.setAttribute(COMPONENTS, new ConcurrentHashMap<Descriptor<?>, Object>());
		} catch (Exception e) {
			logger.debug("HTTP session is disabled or invalid in the current environment", e);
		}
	}

	/**
	 * Deassociates the current {@code HttpSession} from this instance.
	 * 
	 * @param context The current {@code HttpSession}.
	 */
	@SuppressWarnings("unchecked")
	public void deassociate(HttpSession context) {
		Map<Descriptor<?>, Object> components 
				= (Map<Descriptor<?>, Object>) context.getAttribute(COMPONENTS);
		
		if (components != null) {
			for (Object component : components.values()) {
				try {
					Event.of(Passivated.class).on(component).fire();
				} catch (Exception e) {}
			}
			
			context.removeAttribute(COMPONENTS);
			
			for (Object component : components.values()) {
				try {
					Event.of(Destroyed.class).on(component).fire();
				} catch (Exception e) {}
			}
		}
	}

	@Override
	public Object store() {
		HttpSession session = WebFilter.session();
		return (session == null) ? super.store() : session;
	}
	
}
