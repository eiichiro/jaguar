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
package org.eiichiro.jaguar.aspectj;

import static org.eiichiro.jaguar.Jaguar.*;

import org.aspectj.lang.annotation.SuppressAjWarnings;
import org.eiichiro.jaguar.aspectj.AjAssembled;

/**
 * {@code Assemble} is an <a href="http://www.eclipse.org/aspectj/">AspectJ 5</a> 
 * aspect to assemble {@code @AjAssembled}-annotated component with Jaguar.
 * If you're using Jaguar core only, you need to implement the following 
 * code to get an assembled component: 
 * <pre>
 * public class AjAssembledTestObject1 {
 * 
 *     @Inject AjAssembledTestObject2 ajAssembledTestObject2;
 * 
 * }
 * </pre>
 * <pre>
 * AjAssembledTestObject1 ajAssembledTestObject1 = new AjAssembledTestObject1();
 * // The dependencies are injected.
 * Jaguar.assemble(ajAssembledTestObject1);
 * </pre>
 * However, if you use Jaguar AspectJ, you can get the same just by the 
 * following code: 
 * <pre>
 * @AjAssembled
 * public class AjAssembledTestObject1 {
 * 
 *     @Inject AjAssembledTestObject2 ajAssembledTestObject2;
 * 
 * }
 * </pre>
 * <pre>
 * // The dependencies are injected at the instantiation by Jaguar AspectJ.
 * AjAssembledTestObject1 ajAssembledTestObject1 = new AjAssembledTestObject1();
 * </pre>
 * {@code Assemble}'s advice calls {@code Jaguar#assemble(Object)} method 
 * within it and the advice is weaven into the initialization (constructor 
 * execution) of {@code @AjAssembled}-annotated class by AspectJ 5. So you can 
 * get the assembled instance just by instantiating it directly.
 * This does not only make your code simple, but also make easy to integrate 
 * Jaguar component with third-party libraries. For example, in the case of 
 * using Apache Struts {@code Action} as Jaguar component. If you're using 
 * Jaguar core only, you need to modify all constructors of all Apache 
 * Struts {@code Action} classes to assemble them. If you use this, you don't 
 * need it. Because the dependency resolution is executed in the constructor 
 * automatically by AspectJ.
 * 
 * @see AjAssembled
 * @author <a href="mailto:eiichiro@eiichiro.org">Eiichiro Uchiumi</a>
 */
public aspect Assemble {

	/**
	 * Assembles a component by invoking {@code Jaguar#assemble(Object)} 
	 * method.
	 */
	@SuppressAjWarnings("adviceDidNotMatch")
	before(): initialization((@AjAssembled *+).new(..)) {
		assemble(thisJoinPoint.getThis());
	}
	
}
