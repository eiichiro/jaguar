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

import static org.eiichiro.jaguar.Version.*;

import java.util.Collection;

import org.eiichiro.jaguar.deployment.Deployment;
import org.eiichiro.reverb.diagnostics.Dump;
import org.eiichiro.reverb.lang.UncheckedException;
import org.eiichiro.reverb.system.Environment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;

/**
 * The entry point to the Jaguar.
 * 
 * @author <a href="mailto:eiichiro@eiichiro.org">Eiichiro Uchiumi</a>
 */
public final class Jaguar {

	private static Logger logger = LoggerFactory.getLogger(Jaguar.class);
	
	private static Class<?> deployment;
	
	private static Container container;
	
	static {
		Logger logger = LoggerFactory.getLogger(Version.class);
		logger.info("Jaguar " + MAJOR + "." + MINER + BUILD);
//		logger.info("Copyright (C) 2011 Eiichiro Uchiumi. All Rights Reserved.");
	}
	
	private Jaguar() {}
	
	/**
	 * Sets the deployment qualifier.
	 * The deployment qualifier must be specified before Jaguar starts.
	 * 
	 * @param deployment The deployment qualifier components are deployed.
	 */
	public static synchronized void deployment(Class<?> deployment) {
		Preconditions.checkArgument(deployment != null, 
				"Parameter 'deployment' must not be [" + deployment + "]");
		Preconditions.checkArgument(deployment.getAnnotation(Deployment.class) != null,
				"'deployment' [" + deployment + "] must be annotated with @Deployment");
		Jaguar.deployment = deployment;
		logger.info("Deployment has been set to [" + deployment.getSimpleName() + "]");
	}
	
	/**
	 * Returns the deployment qualifier.
	 * 
	 * @return The deployment qualifier.
	 */
	public static Class<?> deployment() {
		return deployment;
	}
	
	/** Bootstraps Jaguar. */
	public static synchronized void bootstrap() {
		if (container != null) {
			logger.debug("Jaguar has already started");
			return;
		}
		
		logger.info("Starting Jaguar on Java Runtime Environment [" + Environment.getProperty("java.version") + "]");
		
		try {
			container = new Container();
			container.initialize();
		} catch (Exception e) {
			container = null;
			throw new UncheckedException(e);
		}
	}
	
	/** Shuts down Jaguar. */
	public static synchronized void shutdown() {
		if (container == null) {
			logger.debug("Jaguar has already stopped");
			return;
		}
		
		try {
			container.dispose();
		} finally {
			container = null;
			deployment = null;
			logger.info("Jaguar stopped");
		}
	}
	
	/**
	 * Returns whether Jaguar is running or not.
	 * 
	 * @return Whether Jaguar is running or not.
	 */
	public static boolean running() {
		return container != null;
	}
	
	/**
	 * Installs the specified components into Jaguar.
	 * This method is intended to use with {@link Scanner} or something like 
	 * component bundle. If you would install components runtime classpath, you 
	 * should invoke this method with {@link Classpath#scan()}.
	 * 
	 * @param components The component classes to be installed.
	 */
	public static synchronized void install(Collection<Class<?>> components) {
		for (Class<?> component : components) {
			install(component);
		}
	}
	
	/**
	 * Installs the specified components into Jaguar.
	 * This method is intended to use with {@link Scanner} or something like 
	 * component bundle. If you would install components runtime classpath, you 
	 * should invoke this method with {@link Classpath#scan()}.
	 * 
	 * @param components The component classes to be installed.
	 */
	public static synchronized void install(Class<?>... components) {
		for (Class<?> component : components) {
			install(component);
		}
	}
	
	/**
	 * Installs the specified component into Jaguar.
	 * The component must be installed after Jaguar started.
	 * 
	 * @param component The component class to be installed.
	 */
	public static synchronized void install(Class<?> component) {
		Preconditions.checkState(container != null, 
				"Cannot install component until Jaguar starts");
		if (!installed(component)) {
			container.install(component);
		}
	}
	
	/**
	 * Indicates the specified component installed or not.
	 * 
	 * @param component The component class to be examined.
	 * @return <code>true</code> if the specified component installed.
	 */
	public static boolean installed(Class<?> component) {
		Preconditions.checkState(container != null, 
				"Cannot indicate if the specified component has been installed " +
				"until Jaguar starts");
		return container.installed(component);
	}
	
	/**
	 * Returns assembled component instance of the specified component type.
	 * This method must be invoked after Jaguar started.
	 * 
	 * @param <T> The component type.
	 * @param component The component type.
	 * @return The component instance of the specified component type.
	 */
	public static <T> T component(Class<T> component) {
		Preconditions.checkState(container != null, 
				"Cannot assemble component until Jaguar starts");
		return container.component(component);
	}
	
	/**
	 * Assembles the specified component instance by injecting the dependent 
	 * components into it.
	 * This method must be invoked after Jaguar started.
	 * 
	 * @param <T> The component type.
	 * @param component The component instance that the dependent components to 
	 * be injected.
	 * @return The assembled component instance.
	 */
	public static <T> T assemble(T component) {
		Preconditions.checkState(container != null, 
				"Cannot assemble dependent component until Jaguar starts");
		return container.assemble(component);
	}
	
	/** Dumps the current Jaguar state into the log. */
	public static void dump() {
		logger.info("Jaguar dump: \n" + new Dump(new Jaguar()));
	}
	
}
