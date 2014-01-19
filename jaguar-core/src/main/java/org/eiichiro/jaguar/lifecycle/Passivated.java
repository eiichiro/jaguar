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
package org.eiichiro.jaguar.lifecycle;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.eiichiro.jaguar.scope.Context;
import org.eiichiro.jaguar.scope.Prototype;
import org.eiichiro.jaguar.scope.Thread;

/**
 * Built-in lifecycle that represents a component is passivated.
 * {@code @Passivated} event is raised by the {@link Context} when the context 
 * is passivated (The contexts that cannot detect their passivation such as 
 * {@link Prototype} and {@link Thread} will never raise this lifecycle event).
 * 
 * There are several conventions on {@code @Passivated} event listener method 
 * declaration: 
 * <ul>
 * <li>The parameter must be none</li>
 * <li>Returning type is not constrained, but always ignored</li>
 * </ul>
 * 
 * @author <a href="mailto:eiichiro@eiichiro.org">Eiichiro Uchiumi</a>
 */
@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Lifecycle
public @interface Passivated {}
