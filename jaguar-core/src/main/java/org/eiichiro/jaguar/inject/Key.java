/*
 * Copyright (C) 2011-2012 Eiichiro Uchiumi. All Rights Reserved.
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
package org.eiichiro.jaguar.inject;

import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Set;

import org.eiichiro.jaguar.Assembler;

import com.google.common.base.Preconditions;

/**
 * Component type and binding annotations to lookup the component from the 
 * container.
 * {@code Key} is constructed by the {@link Assembler} internally from the 
 * {@code @Inject}-annotated constructor's parameters and field that the dependent 
 * component to be injected.
 * 
 * @author <a href="mailto:mail@eiichiro.org">Eiichiro Uchiumi</a>
 */
public class Key<T> {

	private final Class<T> type;
	
	private final Set<Annotation> bindings;
	
	/**
	 * Constructs a new {@code Key} instance from the specified type and binding 
	 * annotations.
	 * 
	 * @param type The class to qualify the component to be injected.
	 * @param bindings The binding annotations to qualify the component to be injected.
	 */
	public Key(Class<T> type, Set<Annotation> bindings) {
		Preconditions.checkNotNull(type, "Parameter 'type' must not be [" + type + "]");
		this.type = type;
		this.bindings = (bindings == null) ? new HashSet<Annotation>(0) : bindings;
	}
	
	/**
	 * Returns the class to qualify the component to be injected.
	 * 
	 * @return The class to qualify the component to be injected.
	 */
	public Class<T> type() {
		return type;
	}
	
	/**
	 * Returns the binding annotations to qualify the component to be injected.
	 * 
	 * @return The binding annotations to qualify the component to be injected.
	 */
	public Set<Annotation> bindings() {
		return bindings;
	}
	
	/** String representation. */
	@Override
	public String toString() {
		return "type: " + type + ", bindings: " + bindings;
	}
	
}
