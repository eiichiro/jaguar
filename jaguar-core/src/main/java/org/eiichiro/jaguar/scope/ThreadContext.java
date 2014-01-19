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

import java.util.HashMap;
import java.util.Map;

import org.eiichiro.jaguar.Builtin;
import org.eiichiro.jaguar.Descriptor;
import org.eiichiro.jaguar.inject.Name;

/**
 * Built-in {@link Context} implementation corresponding to the 
 * {@link Thread} scope.
 * 
 * @author <a href="mailto:eiichiro@eiichiro.org">Eiichiro Uchiumi</a>
 */
@Builtin
@Singleton(eager=true)
@Name("org.eiichiro.jaguar.scope.ThreadContext")
public class ThreadContext implements Context {

	private ThreadLocal<Map<Descriptor<?>, Object>> store = new ThreadLocal<Map<Descriptor<?>,Object>>() {
		
		protected Map<Descriptor<?>, Object> initialValue() {
			return new HashMap<Descriptor<?>, Object>();
		}
		
	};
	
	/**
	 * Returns the component corresponding to the specified {@link Descriptor} 
	 * from the {@code ThreadLocal} in current thread.
	 * 
	 * @param <T> The component type.
	 * @param descriptor The descriptor corresponding to the component.
	 * @return The component corresponding to the specified {@link Descriptor}.
	 */
	@SuppressWarnings("unchecked")
	public <T> T get(Descriptor<T> descriptor) {
		return (T) store.get().get(descriptor);
	}

	/**
	 * Puts the specified component and descriptor pair into the 
	 * {@code ThreadLocal} in current thread.
	 * 
	 * @param <T> The component type.
	 * @param descriptor The descriptor corresponding to the component.
	 * @param component The component to be put.
	 */
	public <T> void put(Descriptor<T> descriptor, T component) {
		store.get().put(descriptor, component);
	}

	@Override
	public Object store() {
		return store;
	}

}
