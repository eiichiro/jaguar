/*
 * Copyright (C) 2014 Eiichiro Uchiumi. All Rights Reserved.
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
