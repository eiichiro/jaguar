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

import org.eiichiro.jaguar.Builtin;
import org.eiichiro.jaguar.Descriptor;
import org.eiichiro.jaguar.inject.Name;
import org.eiichiro.jaguar.lifecycle.Destroyed;
import org.eiichiro.jaguar.lifecycle.Event;
import org.eiichiro.jaguar.lifecycle.Passivated;

/**
 * Built-in {@link Context} implementation corresponding to the 
 * {@link Singleton} scope.
 * {@code SingletonContext} stores a component in static global store. So the 
 * components stored in this context are sustained in per-{@code ClassLoader}.
 * 
 * @author <a href="mailto:eiichiro@eiichiro.org">Eiichiro Uchiumi</a>
 */
@Builtin
@Singleton
@Name("org.eiichiro.jaguar.scope.SingletonContext")
public class SingletonContext implements Context {

	private static Map<Descriptor<?>, Object> store 
			= new ConcurrentHashMap<Descriptor<?>, Object>();
	
	/**
	 * Returns the component corresponding to the specified {@link Descriptor} 
	 * from the static global store.
	 * 
	 * @param <T> The component type.
	 * @param descriptor The descriptor corresponding to the component.
	 * @return The component corresponding to the specified {@link Descriptor}.
	 */
	@SuppressWarnings("unchecked")
	public <T> T get(Descriptor<T> descriptor) {
		return (T) store.get(descriptor);
	}

	/**
	 * Puts the specified component and descriptor pair into the static global 
	 * store.
	 * 
	 * @param <T> The component type.
	 * @param descriptor The descriptor corresponding to the component.
	 * @param component The component to be put.
	 */
	public <T> void put(Descriptor<T> descriptor, T component) {
		store.put(descriptor, component);
	}
	
	/**
	 * Clears component store.
	 * Raises {@link Passivated} event then {@link Destroyed} event on the 
	 * components.
	 */
	public void clear() {
		for (Object component : store.values()) {
			try {
				Event.of(Passivated.class).on(component).fire();
			} catch (Exception e) {}
		}
		
		for (Object component : store.values()) {
			try {
				Event.of(Destroyed.class).on(component).fire();
			} catch (Exception e) {}
		}
		
		store.clear();
	}

	@Override
	public Object store() {
		return store;
	}

}
