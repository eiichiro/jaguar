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

import org.eiichiro.jaguar.Descriptor;

/**
 * Scoped component store.
 * Scope qualifier must have the corresponding {@code Context} implementation 
 * class that components are stored in.
 * 
 * @author <a href="mailto:mail@eiichiro.org">Eiichiro Uchiumi</a>
 */
public interface Context {

	/**
	 * Returns the component corresponding to the specified {@link Descriptor}.
	 * 
	 * @param <T> The component type.
	 * @param descriptor The descriptor corresponding to the component.
	 * @return The component corresponding to the specified {@link Descriptor}.
	 */
	public <T> T get(Descriptor<T> descriptor);
	
	/**
	 * Puts the specified component and descriptor pair.
	 * 
	 * @param <T> The component type.
	 * @param descriptor The descriptor corresponding to the component.
	 * @param component The component to be put.
	 */
	public <T> void put(Descriptor<T> descriptor, T component);
	
	/**
	 * Returns the component store this context manages.
	 * 
	 * @return The component store this context manages.
	 */
	public Object store();
	
}
