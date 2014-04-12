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

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * {@code @Singleton}-annotated component is sustained in singleton 
 * (per-{@code ClassLoader}) scope.
 * 
 * @author <a href="mailto:mail@eiichiro.org">Eiichiro Uchiumi</a>
 */
@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Scope(SingletonContext.class)
public @interface Singleton {

	/**
	 * Returns whether the component is "eager" singleton or not.
	 * If <code>true</code> returned, the container assembles the instance when 
	 * the component class is installed. If <code>false</code> returned 
	 * (default), the container assembles the instance when the component 
	 * instance is requested.
	 * 
	 * @return Whether the component is "eager" singleton or not.
	 */
	boolean eager() default false;
	
}
