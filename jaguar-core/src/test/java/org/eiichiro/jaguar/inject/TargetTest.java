package org.eiichiro.jaguar.inject;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

import org.eiichiro.jaguar.inject.Target;
import org.junit.Test;

public class TargetTest {

	@Test
	public void testTarget() throws SecurityException, NoSuchFieldException {
		try {
			new Target(null, null, null);
			fail();
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
		
		try {
			new Target(Target.Kind.FIELD, null, null);
			fail();
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
		
		new Target(Target.Kind.FIELD, String.class, null);
		new Target(Target.Kind.FIELD, String.class, new HashSet<Annotation>());
	}

	@Test
	public void testKind() throws SecurityException, NoSuchFieldException {
		Target key = new Target(Target.Kind.FIELD, String.class, new HashSet<Annotation>());
		assertThat(key.kind(), is(Target.Kind.FIELD));
	}

	@Test
	public void testType() throws SecurityException, NoSuchFieldException {
		Target key = new Target(Target.Kind.FIELD, String.class, new HashSet<Annotation>());
		assertThat(key.type(), is((Object) String.class));
	}

	@Test
	public void testQualifiers() throws SecurityException, NoSuchFieldException {
		Field field = TargetComponent.class.getDeclaredField("object2");
		Set<Annotation> qualifiers = new HashSet<Annotation>();
		
		for (Annotation annotation : field.getAnnotations()) {
			qualifiers.add(annotation);
		}
		
		Target key = new Target(Target.Kind.FIELD, String.class, qualifiers);
		assertThat(key.qualifiers().size(), is(2));
		assertThat(((Annotation) key.qualifiers().toArray()[0]).annotationType(), is((Object) TargetQualifier.class));
		assertThat(((Annotation) key.qualifiers().toArray()[1]).annotationType(), is((Object) TargetBinding.class));
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testToString() throws SecurityException, NoSuchFieldException {
		Field field = TargetComponent.class.getDeclaredField("object2");
		Set<Annotation> qualifiers = new HashSet<Annotation>();
		
		for (Annotation annotation : field.getAnnotations()) {
			qualifiers.add(annotation);
		}
		
		Target key = new Target(Target.Kind.FIELD, (Class<Object>) field.getType(), qualifiers);
		assertThat(key.toString(), 
				is("kind: FIELD, type: class java.lang.Object, qualifiers: [@org.eiichiro.jaguar.inject.TargetQualifier(), @org.eiichiro.jaguar.inject.TargetBinding()]"));
	}

}
