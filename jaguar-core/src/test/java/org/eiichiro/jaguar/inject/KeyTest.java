package org.eiichiro.jaguar.inject;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

import org.eiichiro.jaguar.inject.Key;
import org.junit.Test;

public class KeyTest {

	@Test
	public void testKey() throws SecurityException, NoSuchFieldException {
		try {
			new Key<Object>(null, null);
			fail();
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
		
		new Key<String>(String.class, null);
		new Key<String>(String.class, new HashSet<Annotation>());
	}

	@Test
	public void testType() throws SecurityException, NoSuchFieldException {
		Key<String> key = new Key<String>(String.class, new HashSet<Annotation>());
		assertThat(key.type(), is((Object) String.class));
	}

	@Test
	public void testBindings() throws SecurityException, NoSuchFieldException {
		Field field = KeyObject.class.getDeclaredField("object2");
		Set<Annotation> bindings = new HashSet<Annotation>();
		
		for (Annotation annotation : field.getAnnotations()) {
			if (annotation.annotationType().isAnnotationPresent(Binding.class)) {
				bindings.add(annotation);
			}
		}
		
		Key<String> key = new Key<String>(String.class, bindings);
		assertThat(key.bindings().size(), is(1));
		assertThat(((Annotation) key.bindings().toArray()[0]).annotationType(), is((Object) KeyBinding.class));
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testToString() throws SecurityException, NoSuchFieldException {
		Field field = KeyObject.class.getDeclaredField("object2");
		Set<Annotation> bindings = new HashSet<Annotation>();
		
		for (Annotation annotation : field.getAnnotations()) {
			if (annotation.annotationType().isAnnotationPresent(Binding.class)) {
				bindings.add(annotation);
			}
		}
		
		Key<Object> key = new Key<Object>((Class<Object>) field.getType(), bindings);
		assertThat(key.toString(), 
				is("type: class java.lang.Object, bindings: [@org.eiichiro.jaguar.inject.KeyBinding()]"));
	}

}
