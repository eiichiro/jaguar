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

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.ServletContext;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.NotFoundException;

import org.eiichiro.jaguar.deployment.Deployment;
import org.eiichiro.jaguar.inject.Binding;
import org.eiichiro.jaguar.scope.Application;
import org.eiichiro.jaguar.scope.Scope;
import org.eiichiro.reverb.lang.ClassResolver;
import org.eiichiro.reverb.lang.ClassResolver.Matcher;
import org.eiichiro.reverb.lang.UncheckedException;
import org.eiichiro.reverb.system.Environment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;

/**
 * Built-in implementation of {@link Scanner} that scans components from runtime 
 * classpath.
 * 
 * @author <a href="mailto:mail@eiichiro.org">Eiichiro Uchiumi</a>
 */
public class ClasspathScanner implements Scanner {

	private static Logger logger = LoggerFactory.getLogger(ClasspathScanner.class);
	
	private List<URL> paths = new ArrayList<URL>();
	
	/**
	 * Constructs a new {@code ClasspathScanner} and detects the runtime 
	 * classpath.
	 * 
	 * The runtime classpath is detected according to the following order: 
	 * <ol>
	 * <li>
	 * If the application is running on URLClassLoader, {@code ClasspathScanner} 
	 * gets the classpath directly from its own search paths.
	 * </li>
	 * <li>
	 * If the application is running on Servlet container with the standard web 
	 * application layout, {@code ClasspathScanner} builds the classpath from 
	 * '/WEB-INF/classes' directory and '/WEB-INF/lib/*.jar' files.
	 * </li>
	 * <li>
	 * Neither 1 nor 2, {@code ClasspathScanner} builds the classpath from 
	 * 'java.class.path' system property.
	 * </li>
	 * </ol>
	 */
	@SuppressWarnings("unchecked")
	public ClasspathScanner() {
		Preconditions.checkState(Jaguar.running(), 
				"Cannot detect the runtime classpath until Jaguar starts");
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		
		if (classLoader instanceof URLClassLoader) {
			logger.debug("The application is running on URLClassLoader - "
					+ "directly getting the classpath from its own search paths");
			paths = Arrays.asList(((URLClassLoader) classLoader).getURLs());
			return;
		}
		
		Container container = Jaguar.component(Container.class);
		Object store = container.component(container.contexts().get(Application.class)).store();
		
		try {
			if (store instanceof ServletContext) {
				ServletContext context = (ServletContext) store;
				URL classes = context.getResource("/WEB-INF/classes/");
				
				if (classes != null) {
					logger.debug("The application is running on a Servlet container "
							+ "with the standard web application layout - "
							+ "building the classpath from '/WEB-INF/classes/' "
							+ "directory and '/WEB-INF/lib/*.jar' files");
					paths.add(classes);
					
					for (String jar : (Set<String>) context.getResourcePaths("/WEB-INF/lib/")) {
						if (jar.endsWith(".jar")) {
							paths.add(context.getResource(jar));
						}
					}
					
					return;
				}
			}
			
			logger.debug("The application is standalone or running on a embedded "
					+ "Servlet container - building the classpath from "
					+ "'java.class.path' system property");
			
			for (String path : Environment.getProperty("java.class.path").split(File.pathSeparator)) {
				paths.add(new File(path).toURI().toURL());
			}
			
		} catch (Exception e) {
			logger.error("Failed to detect the runtime classpath: " + e.getMessage(), e);
			throw new UncheckedException(e);
		}
	}
	
	/**
	 * Scans components from runtime classpath.
	 * {@code ClasspathScanner} detects the component by the following 
	 * conditions: 
	 * <ul>
	 * <li>Subclass of {@link Component}</li>
	 * <li>Qualified by {@link Stereotype}</li>
	 * <li>Qualified by {@link Deployment}</li>
	 * <li>Qualified by {@link Binding}</li>
	 * <li>Qualified by {@link Scope}</li>
	 * </ul>
	 * If any exception has occurred in scanning, {@code ClasspathScanner} 
	 * returns empty set.
	 * 
	 * @see org.eiichiro.jaguar.Scanner#scan()
	 */
	public Collection<Class<?>> scan() {
		try {
			ClassResolver<CtClass> resolver = new CtClassClassResolver(paths);
			logger.info("Scanning components from runtime classpath [" + resolver.paths() + "]");
			Set<CtClass> ctClasses = resolver.resolve(new Matcher<CtClass>() {
				
				public boolean matches(CtClass ctClass) {
					try {
						int modifiers = ctClass.getModifiers();
						
						if (ctClass.isInterface() || Modifier.isAbstract(modifiers)
								|| !Modifier.isPublic(modifiers)
								|| ctClass.hasAnnotation(Builtin.class)) {
							ctClass.detach();
							return false;
						}
						
						CtClass superclass = ctClass.getSuperclass();
						
						while (superclass != null) {
							if (superclass.getName().equals(Component.class.getName())) {
								return true;
							}
							
							superclass = superclass.getSuperclass();
						}
						
						for (Object object : ctClass.getAnnotations()) {
							Class<? extends Annotation> annotationType = ((Annotation) object).annotationType();
							
							if (annotationType.isAnnotationPresent(Stereotype.class) 
									|| annotationType.isAnnotationPresent(Deployment.class) 
									|| annotationType.isAnnotationPresent(Binding.class) 
									|| annotationType.isAnnotationPresent(Scope.class)) {
								return true;
							}
						}
						
					} catch (Exception e) {
//						e.printStackTrace();
					}
					
					ctClass.detach();
					return false;
				}
				
			});
			Set<Class<?>> classes = new TreeSet<Class<?>>(new Comparator<Class<?>>() {

				@Override
				public int compare(Class<?> o1, Class<?> o2) {
					return o1.getName().compareTo(o2.getName());
				}
				
			});
			
			for (CtClass ctClass : ctClasses) {
				try {
					classes.add(Class.forName(ctClass.getName(), true, Thread.currentThread().getContextClassLoader()));
				} catch (Exception e) {
					logger.error("Failed to scan component", e);
				}
			}
			
			logger.info("Components [" + classes.size() + "] scaned");
			return classes;
		} catch (Exception e) {
			logger.error("Failed to scan components: " + e.getMessage(), e);
			return Collections.emptySet();
		}
	}
	
	private class CtClassClassResolver extends ClassResolver<CtClass> {
		
		private ClassPool pool;
		
		private CtClassClassResolver(Iterable<URL> paths) throws NotFoundException {
			super(paths);
			ClassPool pool = ClassPool.getDefault();
			
			for (URL url : paths) {
				pool.appendClassPath(url.getPath());
			}
			
			this.pool = pool;
		}
		
		@Override
		protected CtClass load(String clazz, InputStream stream) {
			try {
				return pool.makeClass(stream);
			} catch (Exception e) {
				return null;
			}
		}

		@Override
		public Set<CtClass> resolveByAnnotation(final Class<? extends Annotation> annotation) 
				throws IOException {
			Matcher<CtClass> matcher = new Matcher<CtClass>() {
				
				@Override
				public boolean matches(CtClass ctClass) {
					try {
						for (Object object : ctClass.getAnnotations()) {
							Annotation a = (Annotation) object;
							
							if (a.annotationType().equals(annotation)) {
								return true;
							}
						}
						
					} catch (Exception e) {}
					
					ctClass.detach();
					return false;
				}
				
			};
			return resolve(matcher);
		}
		
		@Override
		public Set<CtClass> resolveByInterface(final Class<?> interfaceClass)
				throws IOException {
			Matcher<CtClass> matcher = new Matcher<CtClass>() {

				@Override
				public boolean matches(CtClass ctClass) {
					try {
						for (CtClass c : ctClass.getInterfaces()) {
							if (c.getName().equals(interfaceClass.getName())) {
								return true;
							}
						}
						
						CtClass superclass = ctClass.getSuperclass();
						
						if (superclass != null) {
							return matches(superclass);
						}
						
					} catch (Exception e) {}
					
					ctClass.detach();
					return false;
				}
				
			};
			return resolve(matcher);
		}

		@Override
		public Set<CtClass> resolveByName(final String name) throws IOException {
			Matcher<CtClass> matcher = new Matcher<CtClass>() {
				
				@Override
				public boolean matches(CtClass ctClass) {
					if (ctClass.getName().contains(name)) {
						return true;
					}
					
					ctClass.detach();
					return false;
				}
				
			};
			return resolve(matcher);
		}

		@Override
		public Set<CtClass> resolveBySuperclass(Class<?> superclass)
				throws IOException {
			Matcher<CtClass> matcher = new Matcher<CtClass>() {

				@Override
				public boolean matches(CtClass ctClass) {
					try {
						CtClass superclass = ctClass.getSuperclass();
						
						if (superclass != null) {
							if (superclass.getName().equals(ctClass.getName())) {
								return true;
							} else {
								return matches(superclass);
							}
						}
						
					} catch (Exception e) {
					}
					
					ctClass.detach();
					return false;
				}
				
			};
			return resolve(matcher);
		}
		
	}
	
	public Iterable<URL> paths() {
		return paths;
	}

}
