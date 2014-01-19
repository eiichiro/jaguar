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
package org.eiichiro.jaguar.interceptor;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eiichiro.jaguar.Assembler;
import org.eiichiro.jaguar.ConfigurationException;
import org.eiichiro.jaguar.Container;
import org.eiichiro.jaguar.Descriptor;
import org.eiichiro.reverb.lang.UncheckedException;
import org.eiichiro.reverb.reflection.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Function;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Lists;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

/**
 * <a href="http://cglib.sourceforge.net/">cglib</a> {@code MethodInterceptor} 
 * implementation to intercept the method invocation and invoke interceptor 
 * components.
 * {@code Dispatcher} is constructed by {@link Assembler} internally and 
 * attached to the dynamically-generated subclass of the component if a 
 * joinpoint method is declared on the component class.
 * 
 * @see Assembler
 * @author <a href="mailto:eiichiro@eiichiro.org">Eiichiro Uchiumi</a>
 */
public class Dispatcher implements MethodInterceptor {

	private Logger logger = LoggerFactory.getLogger(getClass());
	
	private Container container;
	
	private Descriptor<?> descriptor;

	/**
	 * Constructs a new {@code Dispatcher} with the specified {@link Container} 
	 * and intercepted component {@link Descriptor}.
	 * 
	 * @param container
	 * @param descriptor
	 */
	public Dispatcher(Container container, Descriptor<?> descriptor) {
		this.container = container;
		this.descriptor = descriptor;
	}
	
	/**
	 * Intercepts the component method invocation and invokes the bound 
	 * interceptor components.
	 * 
	 * @param object The component (Dynamically-generated subclass) instance 
	 * invoked.
	 * @param method The component method invoked.
	 * @param args The component method invocation arguments.
	 * @param proxy {@code MethodProxy} provided by cglib and used to invoke 
	 * superclass method from dynamically-generated subclass.
	 */
	public Object intercept(final Object object, Method method, 
			final Object[] args, final MethodProxy proxy) throws Throwable {
		if (!descriptor.joinpoints().contains(method)) {
			return proxy.invokeSuper(object, args);
		}
		
		Map<Descriptor<?>, Object> interceptors = new HashMap<Descriptor<?>, Object>();
		List<Descriptor<?>> descriptors = new ArrayList<Descriptor<?>>();
		ListMultimap<Descriptor<?>, Method> before = ArrayListMultimap.create();
		ListMultimap<Descriptor<?>, Method> around = ArrayListMultimap.create();
		ListMultimap<Descriptor<?>, Method> after = ArrayListMultimap.create();
		ListMultimap<Descriptor<?>, Method> throwing = ArrayListMultimap.create();
		
		for (Annotation annotation : method.getAnnotations()) {
			if (annotation.annotationType().isAnnotationPresent(Intercept.class)) {
				for (Descriptor<?> descriptor : container.interceptors().get(annotation)) {
					interceptors.put(descriptor, container.component(descriptor));
					descriptors.add(descriptor);
					before.putAll(descriptor, descriptor.advices().get(Before.class));
					around.putAll(descriptor, descriptor.advices().get(Around.class));
					after.putAll(descriptor, descriptor.advices().get(After.class));
					throwing.putAll(descriptor, descriptor.advices().get(Throwing.class));
				}
			}
		}
		
		new Invoke(logger, descriptors, before, interceptors, "before").apply(args);
		MethodInvocation<?> invocation = new MethodInvocation<Object>(method, object, args) {
			
			@Override
			public Object proceed() throws Throwable {
				return proxy.invokeSuper(object, args);
			}
			
		};
		
		List<Method> methods = new ArrayList<Method>();
		Map<Method, Descriptor<?>> descs = new HashMap<Method, Descriptor<?>>();
		
		for (Descriptor<?> descriptor : descriptors) {
			if (around.containsKey(descriptor)) {
				for (Method m : around.get(descriptor)) {
					if (!m.isAccessible()) {
						m.setAccessible(true);
					}
					
					methods.add(m);
					descs.put(m, descriptor);
				}
			}
		}
		
		for (Method m : Lists.reverse(methods)) {
			if (m.getParameterTypes().length == 1 
					&& m.getParameterTypes()[0].isAssignableFrom(MethodInvocation.class)) {
				invocation = new MethodInvocation<Object>(m, 
						interceptors.get(descs.get(m)), new Object[] {invocation});
			} else {
				throw new ConfigurationException("Around advice [" + m
						+ "] must receive one argument as " 
						+ "org.eiichiro.reverb.reflection.Invocation<R> or " 
						+ "org.eiichiro.reverb.reflection.MethodInvocation<R>");
			}
		}
		
		Object result = null;
		
		try {
			result = invocation.proceed();
		} catch (Throwable t) {
			if (around.isEmpty()) {
				logger.error("Failed to invoke component method [" + method + "]", t);
			} else {
				logger.error("Failed to invoke around advice", t);
			}
			
			if (throwing.isEmpty()) {
				throw t;
			}
			
			new Invoke(logger, descriptors, throwing, interceptors, "throwing").apply(new Object[] {t});
		}
		
		new Invoke(logger, descriptors, after, interceptors, "after").apply(new Object[] {result});
		return result;
	}
	
	private static class Invoke implements Function<Object[], Object> {
		
		final Logger logger;
		
		final List<Descriptor<?>> descriptors;
		
		final ListMultimap<Descriptor<?>, Method> advices;
		
		final Map<Descriptor<?>, Object> interceptors;
		
		final String type;
		
		Invoke(Logger logger, List<Descriptor<?>> descriptors,
				ListMultimap<Descriptor<?>, Method> advices,
				Map<Descriptor<?>, Object> interceptors, String type) {
			this.logger = logger;
			this.descriptors = descriptors;
			this.advices = advices;
			this.interceptors = interceptors;
			this.type = type;
		}

		public Object apply(Object[] args) {
			for (Descriptor<?> descriptor : descriptors) {
				if (advices.containsKey(descriptor)) {
					for (Method method : advices.get(descriptor)) {
						if (!method.isAccessible()) {
							method.setAccessible(true);
						}
						
						try {
							if (method.getParameterTypes().length == 0) {
								method.invoke(interceptors.get(descriptor));
							} else {
								method.invoke(interceptors.get(descriptor), args);
							}
							
						} catch (InvocationTargetException e) {
							logger.error("Exception from " + type + " advice [" + method + "]", e.getTargetException());
							throw new UncheckedException((Exception) e.getTargetException());
						} catch (Exception e) {
							logger.error("Cannot invoke " + type + " advice [" + method + "]", e);
							throw new ConfigurationException("Cannot invoke " + type + " advice [" + method + "]", e);
						}
					}
				}
			}
			
			return null;
		}
		
	}
	
}
