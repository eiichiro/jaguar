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
package org.eiichiro.jaguar.lifecycle;

import static org.eiichiro.jaguar.Jaguar.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;

import org.eiichiro.jaguar.Container;
import org.eiichiro.jaguar.Descriptor;
import org.eiichiro.jaguar.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Lifecycle event that fires on a component.
 * If lifecycle event fires, the component methods listening to the lifecycle 
 * (methods annotated with the lifecycle) are called back.
 * 
 * @author <a href="mailto:eiichiro@eiichiro.org">Eiichiro Uchiumi</a>
 */
public class Event<L extends Annotation> {

	private Logger logger = LoggerFactory.getLogger(getClass());
	
	protected final Class<L> lifecycle;
	
	protected Object[] args;
	
	protected Object component;
	
	@Inject private Container container;
	
	/**
	 * Constructs a new {@code Event} of the specified lifecycle.
	 * 
	 * @param lifecycle Lifecycle this event fires.
	 */
	protected Event(Class<L> lifecycle) {
		if (lifecycle != null && lifecycle.isAnnotationPresent(Lifecycle.class)) {
			this.lifecycle = (Class<L>) lifecycle;
			assemble(this);
		} else {
			throw new IllegalArgumentException(
					"Type parameter [L] of [Event<L>] must be a lifecycle annotation type, "
							+ "not be [Event<" + lifecycle + ">]");
		}
	}
	
	/**
	 * Creates a new {@code Event} of the specified lifecycle.
	 * 
	 * @param lifecycle Lifecycle this event fires.
	 * @return {@code Event} instance.
	 */
	public static <L extends Annotation> Event<L> of(Class<L> lifecycle) {
		return new Event<L>(lifecycle);
	}
	
	/**
	 * Sets event arguments.
	 * 
	 * @param args The event arguments.
	 * @return This {@code Event} instance.
	 */
	public Event<L> with(Object... args) {
		this.args = args;
		return this;
	}
	
	/**
	 * Sets component on which this event fires.
	 * 
	 * @param component The component on which this event fires.
	 * @return This {@code Event} instance.
	 */
	public Event<L> on(Object component) {
		this.component = component;
		return this;
	}
	
	/**
	 * Calls back the methods listening to the lifecycle on the specified 
	 * components.
	 * If an exception has occurred, this method catches it and raises 
	 * {@link Failed} event on the component.
	 */
	public final void fire() {
		Descriptor<?> descriptor = container.instances().get(component);
		Collection<Method> methods = descriptor.lifecycles().get(lifecycle);
		logger.debug("Event [" + lifecycle.getSimpleName()
				+ "] fires on component [" + descriptor + "]");
		
		if (methods.isEmpty() && lifecycle.equals(Failed.class)) {
			throw ((LifecycleException) args[0]);
		}
		
		for (Method method : methods) {
			if (!method.isAccessible()) {
				method.setAccessible(true);
			}
			
			try {
				if (method.getParameterTypes().length == 0) {
					method.invoke(component);
				} else {
					method.invoke(component, args);
				}
				
				continue;
			} catch (InvocationTargetException e) {
				logger.warn("Exception from [" + lifecycle.getSimpleName()
						+ " (" + method + ")] event listener on component ["
						+ descriptor + "]", e.getTargetException());
				if (lifecycle.equals(Failed.class)) {
					throw new LifecycleException(e.getTargetException(), lifecycle, component);
				} else {
					Event.of(Failed.class).with(new LifecycleException(e.getTargetException(), lifecycle, component)).on(component).fire();
				}
				
			} catch (Exception e) {
				logger.warn("Cannot fire [" + lifecycle.getSimpleName()
						+ " (" + method + ")] event on component ["
						+ descriptor + "]", e);
			}
		}
	}
	
}
