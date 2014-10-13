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
package org.eiichiro.jaguar.aspect;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * {@code @Before} indicates that the annotated aspect method is "Before 
 * advice".
 * Before advice aspect method is invoked <b>before</b> the targeting 
 * component method has been executed.
 * 
 * There are several conventions on before advice method declaration: 
 * <ul>
 * <li>The parameter must be none or the same type as the targeting component 
 * method receiving. If the parameter type is mismatched, {@link Interceptor} 
 * throws {@code IllegalArgumentException}
 * </li>
 * <li>Returning type is not constrained, but always ignored</li>
 * </ul>
 * 
 * @author <a href="mailto:mail@eiichiro.org">Eiichiro Uchiumi</a>
 */
@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Advice
public @interface Before {}
