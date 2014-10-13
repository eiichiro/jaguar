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

import java.lang.reflect.Method;

import org.eiichiro.reverb.reflection.MethodInvocation;

/**
 * {@code Invocation}
 * 
 * @author <a href="mailto:eiichiro@eiichiro.org">Eiichiro Uchiumi</a>
 */
public class Invocation<R> extends MethodInvocation<R> {

	public Invocation(Method method, Object[] args) {
		super(method, args);
		// TODO Auto-generated constructor stub
	}

	public Invocation(Method method, Object object, Object[] args) {
		super(method, object, args);
		// TODO Auto-generated constructor stub
	}

	public Invocation(Method method, Object object) {
		super(method, object);
		// TODO Auto-generated constructor stub
	}

	public Invocation(Method method) {
		super(method);
		// TODO Auto-generated constructor stub
	}

	public Invocation(Class<?> clazz, String method, Object[] args)
			throws NoSuchMethodException {
		super(clazz, method, args);
		// TODO Auto-generated constructor stub
	}

	public Invocation(Class<?> clazz, String method, Object object,
			Object[] args) throws NoSuchMethodException {
		super(clazz, method, object, args);
		// TODO Auto-generated constructor stub
	}

	public Invocation(Class<?> clazz, String method, Object object,
			Class<?>[] parameterTypes, Object[] args)
			throws NoSuchMethodException {
		super(clazz, method, object, parameterTypes, args);
		// TODO Auto-generated constructor stub
	}

	public Invocation(Class<?> clazz, String method, Class<?>[] parameterTypes,
			Object[] args) throws NoSuchMethodException {
		super(clazz, method, parameterTypes, args);
		// TODO Auto-generated constructor stub
	}

}
