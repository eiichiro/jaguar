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

import org.eiichiro.jaguar.Builtin;
import org.eiichiro.jaguar.Descriptor;
import org.eiichiro.jaguar.inject.Name;

/**
 * Built-in pseudo-{@link Context} implementation corresponding to the 
 * {@link Prototype} scope.
 * {@code PrototypeContext} does not hold any component in the instance, so 
 * {@code #get(Descriptor)} method always returns <code>null</code> and 
 * {@code #put(Descriptor, Object)} method does no-op.
 * 
 * @author <a href="mailto:eiichiro@eiichiro.org">Eiichiro Uchiumi</a>
 */
@Builtin
@Singleton(eager=true)
@Name("org.eiichiro.jaguar.scope.PrototypeContext")
public class PrototypeContext implements Context {

	/**
	 * Always returns <code>null</code>.
	 * 
	 * @param <T> The component type.
	 * @param descriptor The descriptor corresponding to the component.
	 * @return <code>null</code>.
	 */
	public <T> T get(Descriptor<T> descriptor) {
		return null;
	}

	/**
	 * no-op.
	 * 
	 * @param <T> The component type.
	 * @param descriptor The descriptor corresponding to the component.
	 * @param component The component to be put.
	 */
	public <T> void put(Descriptor<T> descriptor, T component) {}

	@Override
	public Object store() {
		// TODO Auto-generated method stub
		return null;
	}

}
