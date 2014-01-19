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
package org.eiichiro.jaguar.validation;

import org.eiichiro.jaguar.Builtin;
import org.eiichiro.jaguar.inject.Name;
import org.eiichiro.jaguar.scope.Singleton;

/**
 * Built-in {@link Validator} implementation corresponding to the 
 * {@code Required} constraint.
 * 
 * @author <a href="mailto:eiichiro@eiichiro.org">Eiichiro Uchiumi</a>
 */
@Builtin
@Singleton
@Name("org.eiichiro.jaguar.validation.RequiredValidator")
public class RequiredValidator implements Validator<Required> {

	/**
	 * Returns <code>true</code> if the specified component field is not 
	 * <code>null</code>. If it is not so, returns <code>null</code>.
	 * 
	 * @param object The component field to be validated.
	 * @return <code>true</code> if the specified component field is not 
	 * <code>null</code>.
	 */
	public boolean validate(Required required, Object object) {
		return object != null;
	}

}
