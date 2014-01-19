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
package org.eiichiro.jaguar.lifecycle;

import java.lang.annotation.Annotation;

/**
 * {@code LifecycleException}
 * 
 * @author <a href="mailto:eiichiro@eiichiro.org">Eiichiro Uchiumi</a>
 */
public class LifecycleException extends RuntimeException {

	private static final long serialVersionUID = 2172302847059316722L;

	private final Class<?> lifecycle;
	
	private final Object component;
	
	public <L extends Annotation> LifecycleException(Throwable cause,
			Class<L> lifecycle, Object component) {
		super(cause);
		this.lifecycle = lifecycle;
		this.component = component;
	}
	
	public Class<?> lifecycle() {
		return lifecycle;
	}
	
	public Object component() {
		return component;
	}
	
}
