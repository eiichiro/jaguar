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

import javax.servlet.http.HttpServletRequest;

import org.eiichiro.jaguar.Builtin;
import org.eiichiro.jaguar.Descriptor;
import org.eiichiro.jaguar.WebFilter;
import org.eiichiro.jaguar.inject.Name;
import org.eiichiro.jaguar.lifecycle.Destroyed;
import org.eiichiro.jaguar.lifecycle.Event;
import org.eiichiro.jaguar.lifecycle.Passivated;

/**
 * Built-in {@link Context} implementation corresponding to the {@link Request} 
 * scope.
 * {@code RequestContext} stores a component in HTTP request. If the application 
 * is not running in Web environment, this context becomes equal to 
 * {@link ThreadContext}.
 * 
 * @author <a href="mailto:eiichiro@eiichiro.org">Eiichiro Uchiumi</a>
 */
@Builtin
@Singleton(eager=true)
@Name("org.eiichiro.jaguar.scope.RequestContext")
public class RequestContext extends ThreadContext implements WebContext<HttpServletRequest> {

	/**
	 * Returns the component corresponding to the specified {@link Descriptor} 
	 * from the current {@code HttpServletRequest}.
	 * If the application is not running in Web environment, the component is 
	 * got from {@code ThreadContext}'s component store.
	 * 
	 * @param <T> The component type.
	 * @param descriptor The descriptor corresponding to the component.
	 * @return The component corresponding to the specified {@link Descriptor}.
	 */
	@SuppressWarnings("unchecked")
	public <T> T get(Descriptor<T> descriptor) {
		HttpServletRequest request = WebFilter.request();
		
		if (request == null) {
			return super.get(descriptor);
		}
		
		Map<Descriptor<?>, Object> components 
				= (Map<Descriptor<?>, Object>) request.getAttribute(COMPONENTS);
		return (T) components.get(descriptor);
	}

	/**
	 * Puts the specified component and descriptor pair into the current 
	 * {@code HttpServletRequest}.
	 * If the application is not running in Web environment, the component is 
	 * put into {@code ThreadContext}'s component store.
	 * 
	 * @param <T> The component type.
	 * @param descriptor The descriptor corresponding to the component.
	 * @param component The component to be put.
	 */
	@SuppressWarnings("unchecked")
	public <T> void put(Descriptor<T> descriptor, T component) {
		HttpServletRequest request = WebFilter.request();
		
		if (request == null) {
			super.put(descriptor, component);
			return;
		}
		
		Map<Descriptor<?>, Object> components 
				= (Map<Descriptor<?>, Object>) request.getAttribute(COMPONENTS);
		components.put(descriptor, component);
	}
	
	/**
	 * Associates the current {@code HttpServletRequest} with this instance.
	 * 
	 * @param context The current {@code HttpServletRequest}.
	 */
	public void associate(HttpServletRequest context) {
		context.setAttribute(COMPONENTS, new ConcurrentHashMap<Descriptor<?>, Object>());
	}
	
	/**
	 * Deassociates the current {@code HttpServletRequest} from this instance.
	 * 
	 * @param context The current {@code HttpServletRequest}.
	 */
	@SuppressWarnings("unchecked")
	public void deassociate(HttpServletRequest context) {
		Map<Descriptor<?>, Object> components 
				= (Map<Descriptor<?>, Object>) context.getAttribute(COMPONENTS);
		
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

	@Override
	public Object store() {
		HttpServletRequest request = WebFilter.request();
		return (request == null) ? super.store() : request;
	}
	
}
