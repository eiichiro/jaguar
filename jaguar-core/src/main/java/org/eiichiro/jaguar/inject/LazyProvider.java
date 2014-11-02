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

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import org.eiichiro.jaguar.Builtin;
import org.eiichiro.jaguar.Container;

/**
 * {@code LazyProvider}
 * 
 * @author <a href="mailto:mail@eiichiro.org">Eiichiro Uchiumi</a>
 */
@Builtin
@Name("org.eiichiro.jaguar.inject.LazyProvider")
public class LazyProvider implements Provider<Lazy<?>> {

	@Inject
	private Container container;
	
	@Override
	public Lazy<?> provide(final Target target) {
		final Type type = ((ParameterizedType) target.type()).getActualTypeArguments()[0];
		return new Lazy<Object>() {

			private Object component;
			
			@Override
			public synchronized Object get() {
				if (component == null) {
					component = container.component(new Target(target.kind(), type, target.qualifiers()));
				}
				
				return component;
			}
			
		};
	}

}
