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
package org.eiichiro.jaguar;

import static org.eiichiro.jaguar.Jaguar.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;

import org.apache.commons.lang.ClassUtils;
import org.eiichiro.jaguar.inject.Default;
import org.eiichiro.jaguar.inject.Key;
import org.eiichiro.jaguar.inject.Name;
import org.eiichiro.jaguar.lifecycle.Activated;
import org.eiichiro.jaguar.lifecycle.Event;
import org.eiichiro.jaguar.scope.Application;
import org.eiichiro.jaguar.scope.Context;
import org.eiichiro.jaguar.scope.Prototype;
import org.eiichiro.jaguar.scope.Request;
import org.eiichiro.jaguar.scope.Scope;
import org.eiichiro.jaguar.scope.Session;
import org.eiichiro.jaguar.scope.Singleton;
import org.eiichiro.jaguar.scope.SingletonContext;
import org.eiichiro.jaguar.scope.Thread;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Collections2;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.MapConstraint;
import com.google.common.collect.MapConstraints;
import com.google.common.collect.Sets;

/**
 * {@code Container} is the core of Jaguar. {@code Container} deploys and 
 * manages Jaguar components.
 * 
 * @author <a href="mailto:mail@eiichiro.org">Eiichiro Uchiumi</a>
 */
@Builtin
@Singleton
@Name("org.eiichiro.jaguar.Container")
public class Container {

	private Logger logger = LoggerFactory.getLogger(getClass());
	
	private WeakHashMap<Object, Descriptor<?>> instances = new WeakHashMap<Object, Descriptor<?>>();
	
	private Set<Class<?>> installed = new HashSet<Class<?>>();
	
	private ListMultimap<Class<?>, Descriptor<?>> components = ArrayListMultimap.create();
	
	private ListMultimap<Annotation, Descriptor<?>> interceptors = ArrayListMultimap.create();
	
	private Map<Class<? extends Annotation>, Descriptor<? extends Context>> contexts 
			= MapConstraints.constrainedMap(
					new HashMap<Class<? extends Annotation>, Descriptor<? extends Context>>(), 
					new MapConstraint<Class<? extends Annotation>, Descriptor<? extends Context>>() {

		public void checkKeyValue(Class<? extends Annotation> scope,
				Descriptor<? extends Context> descriptor) {
			logger.info("Context [" + scope.getSimpleName() + " (" 
					+ descriptor.type().getName() + ")] started");
		}
		
	});
	
	private SingletonContext singleton;
	
	/**
	 * Initializes {@code Container} instance.
	 * Sets up built-in {@link Singleton}, {@link Application}, {@link Thread}, 
	 * {@link Session}, {@link Request} and {@link Prototype} contexts and makes 
	 * {@code Container} instance available in application. This method is 
	 * invoked by {@link Jaguar} in bootstrap.
	 */
	@SuppressWarnings("unchecked")
	public void initialize() {
		Class<? extends Context> context = Singleton.class.getAnnotation(Scope.class).value();
		install(context);
		contexts.put(Singleton.class, (Descriptor<? extends Context>) components.get(context).get(0));
		SingletonContext singleton = new SingletonContext();
		singleton.put((Descriptor<SingletonContext>) contexts.get(Singleton.class), singleton);
		instances.put(singleton, contexts.get(Singleton.class));
		this.singleton = singleton;
		install(Container.class);
		Descriptor<Container> container = (Descriptor<Container>) components.get(Container.class).get(0);
		singleton.put(container, this);
		instances.put(this, container);
		context = Application.class.getAnnotation(Scope.class).value();
		install(context);
		contexts.put(Application.class, (Descriptor<? extends Context>) components.get(context).get(0));
		context = Thread.class.getAnnotation(Scope.class).value();
		install(context);
		contexts.put(Thread.class, (Descriptor<? extends Context>) components.get(context).get(0));
		context = Session.class.getAnnotation(Scope.class).value();
		install(context);
		contexts.put(Session.class, (Descriptor<? extends Context>) components.get(context).get(0));
		context = Request.class.getAnnotation(Scope.class).value();
		install(context);
		contexts.put(Request.class, (Descriptor<? extends Context>) components.get(context).get(0));
		context = Prototype.class.getAnnotation(Scope.class).value();
		install(context);
		contexts.put(Prototype.class, (Descriptor<? extends Context>) components.get(context).get(0));
		logger.info("Container started");
	}
	
	/**
	 * Returns assembled component instance of the specified component type.
	 * If the component is not found or duplicated, this method returns 
	 * <code>null</code>.
	 * 
	 * @param <T> The component type.
	 * @param component The component type.
	 * @return The component instance of the specified component type.
	 */
	public <T> T component(Class<T> component) {
		logger.debug("Component is requested with type: Type [" + component + "]");
		Preconditions.checkArgument(component != null, 
				"Parameter 'component' must not be [" + component + "]");
		List<Descriptor<?>> descriptors = new ArrayList<>();
		Class<?> deployment = deployment();
		
		for (Descriptor<?> descriptor : components.get(component)) {
			Set<Class<? extends Annotation>> deployments = descriptor.deployments();
			
			if (deployments.isEmpty() || deployment == null
					|| (deployment != null && deployments.contains(deployment))) {
				descriptors.add(descriptor);
			}
		}
		
		if (descriptors.isEmpty()) {
			logger.warn("No component returned: Current deployment ["
					+ ((deployment == null) ? deployment : deployment.getSimpleName())
					+ "]; Component type [" + component 
					+ "]; You must install an appropriate component on ahead "
					+ "invoking [public <T> void install(Class<T> component)]");
			return null;
		} else if (descriptors.size() > 1) {
			logger.warn("Component is duplicated: Component type [" + component
					+ "]; You must specify more concrete type");
			return null;
		}
		
		return instance(component(descriptors.get(0)));
	}
	
	/**
	 * Returns assembled component instance corresponding to the specified key 
	 * (Component type and binding annotations).
	 * <b>NOTE: This method is designed for an internal use.</b>
	 * If the component is not found or duplicated, this method returns 
	 * <code>null</code>. If a {@link Default} qualified component is included 
	 * in the candidate, this method returns it in preference.
	 * 
	 * @param <T> The component type.
	 * @param key The {@link Key} constructed from the injective field to get 
	 * the dependent component.
	 * @return The component instance corresponding to the specified key.
	 */
	public <T> T component(final Key<T> key) {
		logger.debug("Component is requested with key: Key [" + key + "]");
		Collection<Descriptor<?>> descriptors = new ArrayList<>();
		Class<?> deployment = deployment();
		
		for (Descriptor<?> descriptor : components.get(key.type())) {
			Set<Class<? extends Annotation>> deployments = descriptor.deployments();
			
			if (deployments.isEmpty() || deployment == null
					|| (deployment != null && deployments.contains(deployment))) {
				descriptors.add(descriptor);
			}
		}
		
		if (descriptors.isEmpty()) {
			logger.warn("No component returned: Current deployment [" 
					+ ((deployment == null) ? deployment : deployment.getSimpleName())
					+ "]; Component key [" + key 
					+ "]; You must install an appropriate component on ahead "
					+ "invoking [public <T> void install(Class<T> component)]");
			return null;
		}
		
		descriptors = Collections2.filter(descriptors, new Predicate<Descriptor<?>>() {

			public boolean apply(Descriptor<?> descriptor) {
				return Sets.difference(key.bindings(), descriptor.bindings()).size() == 0;
			}
			
		});
		
		if (descriptors.isEmpty()) {
			logger.warn("Component is missing: Component key [" + key 
					+ "]; You must specify binding annotations on the field correctly");
			return null;
		} else if (descriptors.size() == 1) {
			return instance(component(descriptors.toArray(new Descriptor<?>[1])[0]));
		} else {
			descriptors = Collections2.filter(descriptors, new Predicate<Descriptor<?>>() {

				public boolean apply(Descriptor<?> descriptor) {
					for (Annotation binding : descriptor.bindings()) {
						if (binding.annotationType().equals(Default.class)) {
							return true;
						}
					}
					
					return false;
				}
				
			});
			
			if (descriptors.size() == 1) {
				Descriptor<?> descriptor = descriptors.toArray(new Descriptor<?>[1])[0];
				logger.debug("@Default qualified component [" + descriptor + "] is returned in preference");
				return instance(component(descriptor));
			} else {
				logger.warn("Component is duplicated: Component key [" + key
						+ "]; You must specify more concrete type " 
						+ "or binding annotations strictly");
				return null;
			}
		}
	}
	
	/**
	 * Returns assembled component instance corresponding to the specified 
	 * descriptor.
	 * <b>NOTE: This method is designed for an internal use.</b>
	 * 
	 * @param <T> The component type.
	 * @param descriptor The {@link Descriptor}.
	 * @return The assembled component instance corresponding to the specified 
	 * descriptor.
	 */
	public <T> T component(Descriptor<T> descriptor) {
		logger.debug("Component is requested with descriptor: Descriptor [" + descriptor + "]");
		Set<Class<? extends Annotation>> deployments = descriptor.deployments();
		Class<?> deployment = deployment();
		
		if (deployment != null && !deployments.isEmpty() && !deployments.contains(deployment)) {
			logger.warn("No component returned: Current deployment [" + deployment.getSimpleName() 
					+ "]; Component descriptor [" + descriptor 
					+ "]; You must install an appropriate component on ahead "
					+ "invoking [public <T> void install(Class<T> component)]");
			return null;
		}
		
		Function<Descriptor<?>, Context> context = new Function<Descriptor<?>, Context>() {

			@SuppressWarnings("unchecked")
			public Context apply(Descriptor<?> descriptor) {
				Annotation scope = descriptor.scope();
				Class<? extends Annotation> s = (scope == null) ? Prototype.class : scope.annotationType();
				
				if (s.equals(Singleton.class)) {
					return singleton;
				}
				
				Descriptor<? extends Context> desc = contexts.get(s);
				
				if (desc == null) {
					Class<? extends Context> ctx = s.getAnnotation(Scope.class).value();
					install(ctx);
					desc = (Descriptor<? extends Context>) components.get(ctx).get(0);
					contexts.put(s, desc);
				}
				
				return instance(component(desc));
			}
			
		};
		Context c = context.apply(descriptor);
		
		synchronized (c) {
			T component = c.get(descriptor);
			
			if (component == null) {
				component = new Assembler<T>(this, descriptor).assemble();
				
				if (component != null) {
					Event.of(Activated.class).on(component).fire();
					c.put(descriptor, component);
				}
			}
			
			return component;
		}
	}
	
	@SuppressWarnings("unchecked")
	private <T> T instance(Object component) {
		return (component instanceof Component) ? ((Component<T>) component).instance() : (T) component;
	}
	
	/**
	 * Assembles the specified component instance by injecting the dependent 
	 * components into it.
	 * The assembled component instance is <b>not</b> managed by the container.
	 * 
	 * @param <T> The component type.
	 * @param component The component instance that the dependent components to 
	 * be injected.
	 * @return The assembled component instance.
	 */
	@SuppressWarnings("unchecked")
	public <T> T assemble(T component) {
		Preconditions.checkArgument(component != null, 
				"Parameter 'component' must not be [" + component + "]");
		Descriptor<T> descriptor = new Descriptor<T>((Class<T>) component.getClass());
		return new Assembler<T>(this, descriptor, component).assemble();
	}
	
	/**
	 * Installs the specified component into the container.
	 * The component must not be an interface and abstract class.
	 * 
	 * @param component The component instance of the specified component class.
	 */
	@SuppressWarnings("unchecked")
	public synchronized <T> void install(Class<T> component) {
		Preconditions.checkArgument(component != null, 
				"Parameter 'component' must not be [" + component + "]");
		
		if (installed(component)) {
			logger.debug("Installation ignored: Component [" + component + "] has already been installed");
			return;
		}
		
		int modifiers = component.getModifiers();
		
		if (component.isInterface() || Modifier.isAbstract(modifiers) || !Modifier.isPublic(modifiers)) {
			logger.warn("Interface, abstract and non-public class cannot be installed: " 
					+ "Component [" + component + "]");
			return;
		}
		
		try {
			Descriptor<T> descriptor = new Descriptor<T>(component);
			List<Class<?>> classes = Collections.emptyList();
			
			if (Component.class.isAssignableFrom(component)) {
				Type type = component.getGenericSuperclass();
				
				for (Class<?> superclass = component.getSuperclass(); 
						!superclass.equals(Component.class); superclass = superclass.getSuperclass()) {
					if (type instanceof ParameterizedType) {
						type = ((Class<?>) ((ParameterizedType) type).getRawType()).getGenericSuperclass();
					} else {
						type = ((Class<?>) type).getGenericSuperclass();
					}
				}
				
				Type t = ((ParameterizedType) type).getActualTypeArguments()[0];
				Class<?> clazz = (Class<?>) ((t instanceof ParameterizedType) ? ((ParameterizedType) t).getRawType() : t);
				classes = (List<Class<?>>) ClassUtils.getAllInterfaces(clazz);
				classes.addAll(ClassUtils.getAllSuperclasses(clazz));
				classes.add(clazz);
			} else {
				classes = (List<Class<?>>) ClassUtils.getAllInterfaces(component);
				classes.addAll(ClassUtils.getAllSuperclasses(component));
				classes.add(component);
			}
			
			classes.remove(Object.class);
			ListMultimap<Class<?>, Descriptor<?>> components = ArrayListMultimap.create();
			
			for (Class<?> type : classes) {
				components.put(type, descriptor);
			}
			
			ListMultimap<Annotation, Descriptor<?>> interceptors = ArrayListMultimap.create();
			
			if (descriptor.interceptor()) {
				for (Annotation intercept : descriptor.intercepts()) {
					interceptors.put(intercept, descriptor);
				}
			}
			
			Annotation scope = descriptor.scope();
			Class<? extends Annotation> s = (scope == null) ? Prototype.class : scope.annotationType();
			
			if (s.equals(Singleton.class) && ((Singleton) descriptor.scope()).eager()) {
				component(descriptor);
			}
			
			installed.add(component);
			this.components.putAll(components);
			this.interceptors.putAll(interceptors);
			logger.info("Component has been installed: Component descriptor [" + descriptor + "]");
		} catch (Exception e) {
			logger.warn("Component cannot be installed: Component type [" + component + "]", e);
			return;
		}
	}
	
	/**
	 * Indicates the specified component installed or not in the container.
	 * 
	 * @param component The component class to be examined.
	 * @return <code>true</code> if the specified component installed in the 
	 * container.
	 */
	public boolean installed(Class<?> component) {
		Preconditions.checkArgument(component != null, 
				"Parameter 'component' must not be [" + component + "]");
		return installed.contains(component);
	}
	
	/**
	 * Disposes container-managed objects.
	 * This method is invoked by {@link Jaguar} in shutdown.
	 */
	public void dispose() {
		singleton.clear();
		components.clear();
		interceptors.clear();
		contexts.clear();
		instances.clear();
	}
	
	/**
	 * Returns scope annotation-{@link Descriptor} of {@link Context} map the 
	 * container manages.
	 * 
	 * @return The scope annotation-{@link Descriptor} of {@link Context} map 
	 * the container manages.
	 */
	public Map<Class<? extends Annotation>, Descriptor<? extends Context>> contexts() {
		return contexts;
	}
	
	/**
	 * Returns component instance-{@link Descriptor} map the container manages.
	 * 
	 * @return The component instance-{@link Descriptor} map the container 
	 * manages.
	 */
	public WeakHashMap<Object, Descriptor<?>> instances() {
		return instances;
	}
	
	/**
	 * Returns component type-{@link Descriptor} map the container manages.
	 * 
	 * @return The component type-{@link Descriptor} map the container manages.
	 */
	public ListMultimap<Class<?>, Descriptor<?>> components() {
		return components;
	}
	
	/**
	 * Returns intercept annotation-{@link Descriptor} map the container 
	 * manages.
	 * 
	 * @return The intercept annotation-{@link Descriptor} map the container 
	 * manages.
	 */
	public ListMultimap<Annotation, Descriptor<?>> interceptors() {
		return interceptors;
	}
	
}
