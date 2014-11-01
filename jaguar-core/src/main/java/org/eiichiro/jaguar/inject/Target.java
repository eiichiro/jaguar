/*
 * Copyright (C) 2011-2014 Eiichiro Uchiumi. All Rights Reserved.
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
 * {@code Target} is constructed by the {@link Assembler} internally from the 
 * {@code @Inject}-annotated constructor's parameters and field that the dependent 
 * component to be injected.
 * 
 * @author <a href="mailto:mail@eiichiro.org">Eiichiro Uchiumi</a>
 */
public class Target {

	public static enum Kind {
		PARAMETER, 
		FIELD, 
		LOCAL_VARIABLE
	}
	
	private final Kind kind;
	
	private final Class<?> type;
	
	private final Set<Annotation> qualifiers;
	
	/**
	 * Constructs a new {@code Target} instance from the specified type and qualifiers.
	 * 
	 * @param type The class to qualify the component to be injected.
	 * @param qualifiers The qualifiers to qualify the component to be injected.
	 */
	public Target(Kind kind, Class<?> type, Set<Annotation> qualifiers) {
		Preconditions.checkNotNull(type, "Parameter 'kind' must not be [" + kind + "]");
		Preconditions.checkNotNull(type, "Parameter 'type' must not be [" + type + "]");
		this.kind = kind;
		this.type = type;
		this.qualifiers = (qualifiers == null) ? new HashSet<Annotation>(0) : qualifiers;
	}
	
	public Kind kind() {
		return kind;
	}
	
	/**
	 * Returns the class to qualify the component to be injected.
	 * 
	 * @return The class to qualify the component to be injected.
	 */
	public Class<?> type() {
		return type;
	}
	
	/**
	 * Returns the qualifiers to qualify the component to be injected.
	 * 
	 * @return The qualifiers to qualify the component to be injected.
	 */
	public Set<Annotation> qualifiers() {
		return qualifiers;
	}
	
	/** String representation. */
	@Override
	public String toString() {
		return "kind: " + kind + ", type: " + type + ", qualifiers: " + qualifiers;
	}
	
}
