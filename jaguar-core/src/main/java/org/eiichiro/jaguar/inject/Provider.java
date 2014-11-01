/*
 * Copyright (C) 2014 Eiichiro Uchiumi. All Rights Reserved.
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

/**
 * {@code Provider} provides a component instance.
 * You need to declare a subclass of {@code Provider} and install it to the 
 * Jaguar container when you need to manage any third party component the 
 * source code is unmodifiable.
 * 
 * @author <a href="mailto:mail@eiichiro.org">Eiichiro Uchiumi</a>
 */
public interface Provider<T> {

	/**
	 * Returns the component instance. The deployment qualifier, component scope, 
	 * lifecycle and dependencies on the instance are managed by Jaguar.
	 * <b>
	 * NOTE: Jaguar manages not the instance {@code Provider#provide()} 
	 * method returns, but the {@code Provider} instance itself. So 
	 * {@code Provider#instance()} method on a {@code Provider} instance 
	 * should return the same instance anytime for the component scope consistency.
	 * </b>
	 * Example implementation: 
	 * <pre>
	 * public class YourComponentProvider extends Provider&lt;YourComponent&gt; {
	 * 
	 *     private final YourComponent component;
	 *     
	 *     public YourComponentProvider() {
	 *         // Instantiating third party component and caching it.
	 *         component = new YourComponent();
	 *     }
	 *     
	 *     {@code @Override}
	 *     public YourComponent provide(Target injectee) {
	 *         // Returning the instance. 
	 *         return component;
	 *     }
	 * 
	 * }
	 * </pre>
	 * 
	 * @return The component instance.
	 */
	public T provide(Target target);
	
}
