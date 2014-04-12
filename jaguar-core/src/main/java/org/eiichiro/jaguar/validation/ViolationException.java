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

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

/**
 * Thrown when a {@link Validator}'s {@code Validator#validate(Object)} method 
 * returns <code>false</code>.
 * 
 * @author <a href="mailto:mail@eiichiro.org">Eiichiro Uchiumi</a>
 */
public class ViolationException extends RuntimeException {

	private static final long serialVersionUID = 2312262750868168703L;

	private final Field field;
	
	private final Class<?> type;
	
	private final Annotation constraint;
	
	private final Object value;
	
	/**
	 * Constructs a new {@code ViolationException} with the specified 
	 * {@code Field}, {@link Constraint} and the value.
	 * 
	 * @param field The field which is invalid.
	 * @param constraint The constraint which is validated.
	 * @param value The field value.
	 */
	public ViolationException(Field field, Annotation constraint, Object value) {
		super("Constraint violation: Field [" + field + "]'s value [" + value 
				+ "] is invalid for [" + constraint + "] constraint");
		this.field = field;
		type = null;
		this.constraint = constraint;
		this.value = value;
	}
	
	/**
	 * Constructs a new {@code ViolationException} with the specified component 
	 * {@code Class} and {@link Constraint} and the instance.
	 * 
	 * @param type The class which is invalid.
	 * @param constraint The constraint which is validated.
	 * @param value The component instance.
	 */
	public ViolationException(Class<?> type, Annotation constraint, Object value) {
		super("Constraint violation: Class [" + type + "]'s value [" + value 
				+ "] is invalid for [" + constraint + "] constraint");
		field = null;
		this.type = type;
		this.constraint = constraint;
		this.value = value;
	}
	
	/**
	 * Returns the field which is invalid.
	 * 
	 * @return The field which is invalid.
	 */
	public Field field() {
		return field;
	}
	
	/**
	 * Returns the class which is invalid.
	 * 
	 * @return The class which is invalid.
	 */
	public Class<?> type() {
		return type;
	}
	
	/**
	 * Returns the constraint which is validated.
	 * 
	 * @return The constraint which is validated.
	 */
	public Annotation constraint() {
		return constraint;
	}
	
	/**
	 * Returns the value which is validated.
	 * 
	 * @return The value which is validated.
	 */
	public Object value() {
		return value;
	}
	
}
