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
package org.eiichiro.jaguar;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eiichiro.jaguar.aspect.Advice;
import org.eiichiro.jaguar.aspect.Pointcut;
import org.eiichiro.jaguar.aspect.Aspect;
import org.eiichiro.jaguar.deployment.Deployment;
import org.eiichiro.jaguar.inject.Binding;
import org.eiichiro.jaguar.inject.Provider;
import org.eiichiro.jaguar.inject.Inject;
import org.eiichiro.jaguar.lifecycle.Lifecycle;
import org.eiichiro.jaguar.scope.Scope;
import org.eiichiro.jaguar.validation.Constraint;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Multimap;

/**
 * {@code Descriptor} describes the provider's structure.
 * 
 * @author <a href="mailto:mail@eiichiro.org">Eiichiro Uchiumi</a>
 */
public class Descriptor<T> implements Serializable {

	private static final long serialVersionUID = -8983230721277727668L;

	private final Class<T> type;
	
	private final boolean provider;
	
	private final boolean aspect;
	
	private Set<Class<? extends Annotation>> deployments = new HashSet<Class<? extends Annotation>>();
	
	private Annotation scope;
	
	private Set<Annotation> bindings = new HashSet<Annotation>();
	
	private Set<Annotation> constraints = new HashSet<Annotation>();
	
	private Constructor<T> constructor;
	
	private Set<Field> injects = new HashSet<Field>();
	
	private Set<Method> joinpoints = new HashSet<Method>();
	
	private Multimap<Class<? extends Annotation>, Method> lifecycles = HashMultimap.create();
	
	private Set<Annotation> pointcuts = new HashSet<Annotation>();
	
	private Set<Field> validates = new HashSet<Field>();
	
	private ListMultimap<Class<? extends Annotation>, Method> advices = ArrayListMultimap.create();
	
	/**
	 * Constructs a new {@code Descriptor} with the specified provider class.
	 * NOTE: Joinpoints are ignored if the specified provider is a subclass of 
	 * {@link Provider} and advises are ignored if the specified provider is 
	 * not an aspect.
	 * 
	 * @param type The provider class.
	 * @throws ConfigurationException If the specified provider class is 
	 * annotated by multiple scope qualifiers or annotated with {@code @Inject} 
	 * on multiple constructors.
	 */
	@SuppressWarnings("unchecked")
	public Descriptor(Class<T> type) {
		Preconditions.checkArgument(type != null, "Parameter 'type' must not be [" + type + "]");
		this.type = type;
		provider = Provider.class.isAssignableFrom(type);
		aspect = type.isAnnotationPresent(Aspect.class);
		Function<Annotation[], Void> annotations = new Function<Annotation[], Void>() {
			
			public Void apply(Annotation[] annotations) {
				for (Annotation annotation : annotations) {
					Class<? extends Annotation> annotationType = annotation.annotationType();
					
					if (annotationType.isAnnotationPresent(Stereotype.class)) {
						apply(annotationType.getAnnotations());
					} else if (annotationType.isAnnotationPresent(Deployment.class)) {
						deployments.add(annotationType);
					} else if (annotationType.isAnnotationPresent(Binding.class)) {
						bindings.add(annotation);
					} else if (annotationType.isAnnotationPresent(Constraint.class)) {
						constraints.add(annotation);
					} else if (aspect && annotationType.isAnnotationPresent(Pointcut.class)) {
						pointcuts.add(annotation);
					} else if (annotationType.isAnnotationPresent(Scope.class)) {
						if (scope == null || scope.equals(annotation)) {
							scope = annotation;
						} else {
							throw new ConfigurationException(
									"Component must not be annotated with multiple scope qualifiers: ["
											+ scope + "] and [" + annotation
											+ "]");
						}
					}
				}
				
				return null;
			}
			
		};
		annotations.apply(type.getAnnotations());
		
		for (Constructor<?> constructor : type.getDeclaredConstructors()) {
			if (constructor.isAnnotationPresent(Inject.class)) {
				if (this.constructor == null) {
					this.constructor = (Constructor<T>) constructor;
				} else {
					throw new ConfigurationException(
							"Provider must not be annotated with @Inject on multiple constructors");
				}
			}
		}
		
		List<Field> fields = new ArrayList<Field>();
		
		for (Class<?> superclass = type; !superclass.equals(Object.class); 
				superclass = superclass.getSuperclass()) {
			for (Field field : superclass.getDeclaredFields()) {
				fields.add(field);
			}
		}
		
		for (Field field : fields) {
			if (field.isAnnotationPresent(Inject.class)) {
				injects.add(field);
			}
			
			for (Annotation annotation : field.getAnnotations()) {
				Class<? extends Annotation> annotationType = annotation.annotationType();
				
				if (annotationType.isAnnotationPresent(Constraint.class)) {
					validates.add(field);
				}
			}
		}
		
		List<Method> methods = new ArrayList<Method>();
		
		for (Class<?> superclass = type; !superclass.equals(Object.class); 
				superclass = superclass.getSuperclass()) {
			for (Method method : superclass.getDeclaredMethods()) {
				methods.add(method);
			}
		}
		
		for (Method method : methods) {
			for (Annotation annotation : method.getAnnotations()) {
				Class<? extends Annotation> annotationType = annotation.annotationType();
				
				if (!provider) {
					if (annotationType.isAnnotationPresent(Pointcut.class)) {
						joinpoints.add(method);
					}
				}
				
				if (aspect) {
					if (annotationType.isAnnotationPresent(Advice.class)) {
						advices.put(annotationType, method);
					}
				}
				
				if (annotationType.isAnnotationPresent(Lifecycle.class)) {
					lifecycles.put(annotationType, method);
				}
			}
		}
	}
	
	/**
	 * Returns the provider class.
	 * 
	 * @return The provider class.
	 */
	public Class<T> type() {
		return type;
	}

	/**
	 * Returns if the provider is an aspect.
	 * 
	 * @return If the provider is an aspect.
	 */
	public boolean aspect() {
		return aspect;
	}
	
	/**
	 * Returns the deployment qualifiers the provider is deployed.
	 * 
	 * @return The deployment qualifiers the provider is deployed.
	 */
	public Set<Class<? extends Annotation>> deployments() {
		return deployments;
	}
	
	/**
	 * Returns the scope the provider is sustained.
	 * 
	 * @return The scope the provider is sustained.
	 */
	public Annotation scope() {
		return scope;
	}
	
	/**
	 * Returns the binding annotations the provider is qualified.
	 * 
	 * @return The binding annotations the provider is qualified.
	 */
	public Set<Annotation> bindings() {
		return bindings;
	}
	
	/**
	 * Returns {@link Constraint} on this class.
	 * 
	 * @return {@link Constraint} on this class.
	 */
	public Set<Annotation> constraints() {
		return constraints;
	}
	
	/**
	 * Returns the constructor for the dependency injection.
	 * 
	 * @return The constructor for the dependency injection.
	 */
	public Constructor<T> constructor() {
		return constructor;
	}
	
	/**
	 * Returns injectable fields.
	 * 
	 * @return Injectable fields.
	 */
	public Set<Field> injects() {
		return injects;
	}

	/**
	 * Returns interceptable methods.
	 * 
	 * @return Interceptable methods.
	 */
	public Set<Method> joinpoints() {
		return joinpoints;
	}

	/**
	 * Returns {@link Lifecycle} class and corresponding callback methods.
	 * 
	 * @return {@link Lifecycle} class and corresponding callback methods.
	 */
	public Multimap<Class<? extends Annotation>, Method> lifecycles() {
		return lifecycles;
	}

	/**
	 * Returns {@link Constraint} class and corresponding fields to be validated.
	 * 
	 * @return {@link Constraint} class and corresponding fields to be validated.
	 */
	public Set<Field> validates() {
		return validates;
	}
	
	/**
	 * Returns the pointcut annotations the provider is qualified.
	 * 
	 * @return The pointcut annotations the provider is qualified.
	 */
	public Set<Annotation> pointcuts() {
		return pointcuts;
	}
	
	/**
	 * Returns {@link Advice} class and corresponding methods to be executed in 
	 * aspect.
	 * 
	 * @return {@link Advice} class and corresponding methods to be executed in 
	 * aspect.
	 */
	public ListMultimap<Class<? extends Annotation>, Method> advices() {
		return advices;
	}
	
	/** String representation. */
	@Override
	public String toString() {
		return "type: " + type + ", deployments: " + deployments + ", scope: " + scope
				+ ", bindings: " + bindings + ", constraints: " + constraints + ", constructor: " + constructor
				+ ", injects: " + injects + ((!provider) ? ", joinpoints: " + joinpoints : "") 
				+ ", lifecycles: " + lifecycles + ", validates: " + validates 
				+ ((aspect) ? ", pointcuts: " + pointcuts : "")
				+ ((aspect) ? ", advices: " + advices : "");
	}
	
}
