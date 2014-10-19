package org.eiichiro.jaguar.inject;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

import org.eiichiro.jaguar.inject.Injectee;
import org.junit.Test;

public class InjecteeTest {

	@Test
	public void testInjectee() throws SecurityException, NoSuchFieldException {
		try {
			new Injectee(null, null, null);
			fail();
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
		
		try {
			new Injectee(Injectee.Kind.FIELD, null, null);
			fail();
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
		
		new Injectee(Injectee.Kind.FIELD, String.class, null);
		new Injectee(Injectee.Kind.FIELD, String.class, new HashSet<Annotation>());
	}

	@Test
	public void testKind() throws SecurityException, NoSuchFieldException {
		Injectee key = new Injectee(Injectee.Kind.FIELD, String.class, new HashSet<Annotation>());
		assertThat(key.kind(), is(Injectee.Kind.FIELD));
	}

	@Test
	public void testType() throws SecurityException, NoSuchFieldException {
		Injectee key = new Injectee(Injectee.Kind.FIELD, String.class, new HashSet<Annotation>());
		assertThat(key.type(), is((Object) String.class));
	}

	@Test
	public void testQualifiers() throws SecurityException, NoSuchFieldException {
		Field field = InjecteeComponent.class.getDeclaredField("object2");
		Set<Annotation> qualifiers = new HashSet<Annotation>();
		
		for (Annotation annotation : field.getAnnotations()) {
			qualifiers.add(annotation);
		}
		
		Injectee key = new Injectee(Injectee.Kind.FIELD, String.class, qualifiers);
		assertThat(key.qualifiers().size(), is(2));
		assertThat(((Annotation) key.qualifiers().toArray()[0]).annotationType(), is((Object) InjecteeQualifier.class));
		assertThat(((Annotation) key.qualifiers().toArray()[1]).annotationType(), is((Object) InjecteeBinding.class));
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testToString() throws SecurityException, NoSuchFieldException {
		Field field = InjecteeComponent.class.getDeclaredField("object2");
		Set<Annotation> qualifiers = new HashSet<Annotation>();
		
		for (Annotation annotation : field.getAnnotations()) {
			qualifiers.add(annotation);
		}
		
		Injectee key = new Injectee(Injectee.Kind.FIELD, (Class<Object>) field.getType(), qualifiers);
		assertThat(key.toString(), 
				is("kind: FIELD, type: class java.lang.Object, qualifiers: [@org.eiichiro.jaguar.inject.InjecteeQualifier(), @org.eiichiro.jaguar.inject.InjecteeBinding()]"));
	}

}
