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

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sf.cglib.core.DefaultNamingPolicy;
import net.sf.cglib.core.NamingPolicy;
import net.sf.cglib.proxy.Enhancer;

import org.eiichiro.jaguar.aspect.Interceptor;
import org.eiichiro.jaguar.inject.Binding;
import org.eiichiro.jaguar.inject.Injectee;
import org.eiichiro.jaguar.lifecycle.Constructed;
import org.eiichiro.jaguar.lifecycle.Event;
import org.eiichiro.jaguar.validation.Constraint;
import org.eiichiro.jaguar.validation.Validator;
import org.eiichiro.jaguar.validation.ViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * {@code Assembler} assembles a component instance according to the descriptor.
 * 
 * @author <a href="mailto:mail@eiichiro.org">Eiichiro Uchiumi</a>
 */
public class Assembler<T> {

	private static final NamingPolicy NAMING_POLICY = new DefaultNamingPolicy() {
		
		@Override
		protected String getTag() {
			return "ByJaguar";
		}
		
	};
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	private final Container container;
	
	private final Descriptor<T> descriptor;
	
	private T instance;
	
	/**
	 * Constructs a new {@code Assembler} instance with the specified container 
	 * and descriptor.
	 * 
	 * @param container The {@link Container} dependent components are required.
	 * @param descriptor The component descriptor.
	 */
	public Assembler(Container container, Descriptor<T> descriptor) {
		this.container = container;
		this.descriptor = descriptor;
	}
	
	/**
	 * Constructs a new {@code Assembler} instance with the specified container, 
	 * descriptor and component instance.
	 * 
	 * @param container The {@link Container} dependent components are required.
	 * @param descriptor The component descriptor.
	 * @param instance The component instance to be assembled.
	 */
	public Assembler(Container container, Descriptor<T> descriptor, T instance) {
		this.container = container;
		this.descriptor = descriptor;
		this.instance = instance;
	}
	
	/**
	 * Assembles a component instance according to the descriptor.
	 * {@code Assembler} assembles a component instance by the following 
	 * instructions: 
	 * <ol>
	 * <li>Instance construction with the constructor dependency injection. 
	 * If the component instance has not been specified, assembler constructs 
	 * the instance first.  
	 * If the component type has an injectable constructor, assembler instantiates 
	 * the component type {@code Descriptor#type()} returns with the dependent 
	 * components getting from the {@code Container} with the constructor's 
	 * parameter type and binding annotations. Otherwise, assembler constructs 
	 * the instance by invoking {@code Class#newInstance()} method on the class 
	 * {@code Descriptor#type()} returns. 
	 * When a joinpoint method is declared on the component class, assembler 
	 * generates the subclass dynamically with <a href="http://cglib.sourceforge.net/">cglib</a> 
	 * before the instantiation and instantiates it attaching {@link Interceptor} 
	 * as the method invocation handler to intercept the method invocations. 
	 * After the instance construction, assembler fires the {@link Constructed} 
	 * event on the instance. If any exception has occurred, assembler returns 
	 * <code>null</code> immediately.</li>
	 * <li>Field dependency injection. Assembler gets the dependent component 
	 * from the {@code Container} corresponding to the {@link Injectee} constructed from the 
	 * injectable field and populates it to the assembling instance. If any 
	 * exception has occurred, assembler skips the failed population.
	 * </li>
	 * <li>Validation. After the dependencies are resolved, assembler validates 
	 * every constrained field and the component instance by invoking 
	 * {@code Validator#valid(Annotation, Object)}. If the method returns 
	 * <code>false</code>, assembler throws {@link ViolationException}. If all 
	 * constrained fields are validated, the assembled instance is returned.</li>
	 * </ol>
	 * 
	 * @return Provider instance assembled.
	 */
	@SuppressWarnings("unchecked")
	public T assemble() {
		if (instance == null) {
			Constructor<T> constructor = descriptor.constructor();
			
			try {
				if (descriptor.joinpoints().isEmpty()) {
					if (constructor == null) {
						instance = descriptor.type().newInstance();
					} else {
						instance = constructor.newInstance(
								parameters(constructor).toArray());
					}
					
				} else {
					Enhancer enhancer = new Enhancer();
					enhancer.setSuperclass(descriptor.type());
					enhancer.setCallback(new Interceptor(container, descriptor));
					enhancer.setNamingPolicy(NAMING_POLICY);
					
					if (constructor == null) {
						instance = (T) enhancer.create();
					} else {
						instance = (T) enhancer.create(
								constructor.getParameterTypes(),
								parameters(constructor).toArray());
					}
				}
				
			} catch (Exception e) {
				logger.warn("Component cannot be instantiated: Descriptor [" + descriptor + "]", e);
				return null;
			}
			
			container.instances().put(instance, descriptor);
			Event.of(Constructed.class).on(instance).fire();
		}
		
		for (Field field : descriptor.injects()) {
			if (!field.isAccessible()) {
				field.setAccessible(true);
			}
			
			Set<Annotation> qualifiers = new HashSet<Annotation>();
			
			for (Annotation annotation : field.getAnnotations()) {
				Class<? extends Annotation> type = annotation.annotationType();
				
				if (type.isAnnotationPresent(Qualifier.class) || type.isAnnotationPresent(Binding.class)) {
					qualifiers.add(annotation);
				}
			}
			
			Object component = container.component(new Injectee(Injectee.Kind.FIELD, field.getType(), qualifiers));
			
			try {
				field.set(instance, component);
			} catch (Exception e) {
				logger.warn("Dependent component cannot be injected into [" + field + "]", e);
			}
		}
		
		for (Field field : descriptor.validates()) {
			for (Annotation annotation : field.getAnnotations()) {
				Class<? extends Annotation> annotationType = annotation.annotationType();
				
				if (annotationType.isAnnotationPresent(Constraint.class)) {
					Constraint constraint = annotationType.getAnnotation(Constraint.class);
					Class<? extends Validator<Annotation>> validator = (Class<? extends Validator<Annotation>>) constraint.value();
					Validator<Annotation> v = container.component(validator);
					
					if (v == null) {
						container.install(validator);
						v = container.component(validator);
					}
					
					if (!field.isAccessible()) {
						field.setAccessible(true);
					}
					
					Object value = null;
					
					try {
						value = field.get(instance);
					} catch (Exception e) {
						logger.warn("Component field [" + field + "] cannot be validated", e);
					}
					
					boolean valid = false;
					
					try {
						valid = v.validate(annotation, value);
					} catch (ClassCastException e) {
						throw new ConfigurationException(
								"The other way around about Constraint and Validator: Validator ["
										+ validator + "] must validate ["
										+ annotationType + "]", e);
					}
					
					if (valid) {
						logger.debug("Field [" + field + "]'s value [" + value + "] is valid for ["
								+ annotation + "] constraint");
					} else {
						logger.error("Field [" + field + "]'s value [" + value + "] is invalid for ["
								+ annotation + "] constraint");
						throw new ViolationException(field, annotation, value);
					}
				}
			}
		}
		
		for (Annotation annotation : descriptor.constraints()) {
			Class<? extends Annotation> annotationType = annotation.annotationType();
			Constraint constraint = annotationType.getAnnotation(Constraint.class);
			Class<? extends Validator<Annotation>> validator = (Class<? extends Validator<Annotation>>) constraint.value();
			Validator<Annotation> v = container.component(validator);
			
			if (v == null) {
				container.install(validator);
				v = container.component(validator);
			}
			
			boolean valid = false;
			
			try {
				valid = v.validate(annotation, instance);
			} catch (ClassCastException e) {
				throw new ConfigurationException(
						"The other way around about Constraint and Validator: Validator ["
								+ validator + "] must validate ["
								+ annotationType + "]", e);
			}
			
			if (valid) {
				logger.debug("Class [" + descriptor.type() + "]'s value [" 
						+ instance + "] is valid for [" + annotation + "] constraint");
			} else {
				logger.error("Class [" + descriptor.type() + "]'s value [" 
						+ instance + "] is invalid for [" + annotation + "] constraint");
				throw new ViolationException(descriptor.type(), annotation, instance);
			}
		}
		
		logger.debug("Component has been assembled: Descriptor [" + descriptor + "]");
		return instance;
	}
	
	private List<Object> parameters(Constructor<T> constructor) {
		List<Object> parameters = new ArrayList<Object>();
		Class<?>[] parameterTypes = constructor.getParameterTypes();
		Annotation[][] annotations = constructor.getParameterAnnotations();
		
		for (int i = 0; i < parameterTypes.length; i++) {
			Set<Annotation> qualifiers = new HashSet<Annotation>();
			
			for (Annotation annotation : annotations[i]) {
				Class<? extends Annotation> type = annotation.annotationType();
				
				if (type.isAnnotationPresent(Qualifier.class) || type.isAnnotationPresent(Binding.class)) {
					qualifiers.add(annotation);
				}
			}
			
			parameters.add(container.component(new Injectee(Injectee.Kind.PARAMETER, parameterTypes[i], qualifiers)));
		}
		
		return parameters;
	}
	
}
