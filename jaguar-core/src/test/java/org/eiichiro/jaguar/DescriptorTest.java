package org.eiichiro.jaguar;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Set;

import org.eiichiro.jaguar.ConfigurationException;
import org.eiichiro.jaguar.Descriptor;
import org.eiichiro.jaguar.deployment.Production;
import org.eiichiro.jaguar.scope.Singleton;
import org.junit.Test;

import com.google.common.collect.ListMultimap;
import com.google.common.collect.Multimap;

public class DescriptorTest {

	@Test
	public void testDescriptor() {
		try {
			new Descriptor<Object>(null);
			fail();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}
		
		new Descriptor<DescriptorObject>(DescriptorObject.class);
		new Descriptor<DescriptorComponent>(DescriptorComponent.class);
	}

	@Test
	public void testType() {
		assertThat(new Descriptor<DescriptorObject>(DescriptorObject.class).type(), is((Object) DescriptorObject.class));
		assertThat(new Descriptor<DescriptorComponent>(DescriptorComponent.class).type(), is((Object) DescriptorComponent.class));
	}

	@Test
	public void testInterceptor() {
		assertFalse(new Descriptor<DescriptorObject>(DescriptorObject.class).interceptor());
		assertFalse(new Descriptor<DescriptorComponent>(DescriptorComponent.class).interceptor());
		assertTrue(new Descriptor<DescriptorInterceptor>(DescriptorInterceptor.class).interceptor());
		assertTrue(new Descriptor<DescriptorInterceptorComponent>(DescriptorInterceptorComponent.class).interceptor());
	}

	@Test
	public void testDeployments() {
		Set<Class<? extends Annotation>> deployments = new Descriptor<DescriptorObject>(DescriptorObject.class).deployments();
		assertThat(deployments.size(), is(1));
		assertThat(deployments.toArray()[0], is((Object) Production.class));
		deployments = new Descriptor<DescriptorObject2>(DescriptorObject2.class).deployments();
		assertThat(deployments.size(), is(1));
		assertThat(deployments.toArray()[0], is((Object) Production.class));
		deployments = new Descriptor<DescriptorObject3>(DescriptorObject3.class).deployments();
		assertTrue(deployments.isEmpty());
		deployments = new Descriptor<DescriptorComponent>(DescriptorComponent.class).deployments();
		assertThat(deployments.toArray()[0], is((Object) Production.class));
	}

	@Test
	public void testScope() {
		Annotation scope = new Descriptor<DescriptorObject>(DescriptorObject.class).scope();
		assertThat(scope.annotationType(), is((Object) Singleton.class));
		scope = new Descriptor<DescriptorObject2>(DescriptorObject2.class).scope();
		assertThat(scope.annotationType(), is((Object) Singleton.class));
		scope = new Descriptor<DescriptorObject3>(DescriptorObject3.class).scope();
		assertNull(scope);
		
		try {
			new Descriptor<DescriptorObject4>(DescriptorObject4.class);
			fail();
		} catch (ConfigurationException e) {
			e.printStackTrace();
		}
		
		scope = new Descriptor<DescriptorComponent>(DescriptorComponent.class).scope();
		assertThat(scope.annotationType(), is((Object) Singleton.class));
	}

	@Test
	public void testBindings() {
		Set<Annotation> bindings = new Descriptor<DescriptorObject>(DescriptorObject.class).bindings();
		assertThat(bindings.size(), is(1));
		assertThat(bindings.toArray()[0], is(DescriptorBinding.class));
		bindings = new Descriptor<DescriptorObject2>(DescriptorObject2.class).bindings();
		assertThat(bindings.size(), is(1));
		assertThat(bindings.toArray()[0], is(DescriptorBinding.class));
		bindings = new Descriptor<DescriptorObject3>(DescriptorObject3.class).bindings();
		assertTrue(bindings.isEmpty());
		bindings = new Descriptor<DescriptorComponent>(DescriptorComponent.class).bindings();
		assertThat(bindings.size(), is(1));
		assertThat(bindings.toArray()[0], is(DescriptorBinding.class));
	}

	@Test
	public void testInjects() throws SecurityException, NoSuchFieldException {
		Set<Field> injects = new Descriptor<DescriptorObject>(DescriptorObject.class).injects();
		assertThat(injects.size(), is(1));
		assertThat(injects.toArray(new Field[1])[0], is(DescriptorObject.class.getDeclaredField("object")));
		injects = new Descriptor<DescriptorObject2>(DescriptorObject2.class).injects();
		assertTrue(injects.isEmpty());
		injects = new Descriptor<DescriptorComponent>(DescriptorComponent.class).injects();
		assertThat(injects.size(), is(1));
		assertThat(injects.toArray(new Field[1])[0], is(DescriptorComponent.class.getDeclaredField("object")));
	}

	@Test
	public void testConstructor() throws NoSuchMethodException {
		assertNull(new Descriptor<DescriptorObject>(DescriptorObject.class).constructor());
		Constructor<DescriptorObject5> constructor = new Descriptor<DescriptorObject5>(DescriptorObject5.class).constructor();
		assertThat(constructor, is(DescriptorObject5.class.getConstructor(Object.class)));
		
		try {
			new Descriptor<DescriptorObject6>(DescriptorObject6.class);
			fail();
		} catch (ConfigurationException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testJoinpoints() throws SecurityException, NoSuchMethodException {
		Set<Method> joinpoints = new Descriptor<DescriptorObject>(DescriptorObject.class).joinpoints();
		assertThat(joinpoints.size(), is(1));
		assertThat(joinpoints.toArray(new Method[1])[0], is(DescriptorObject.class.getDeclaredMethod("method")));
		joinpoints = new Descriptor<DescriptorObject2>(DescriptorObject2.class).joinpoints();
		assertTrue(joinpoints.isEmpty());
		joinpoints = new Descriptor<DescriptorComponent>(DescriptorComponent.class).joinpoints();
		assertTrue(joinpoints.isEmpty());
	}

	@Test
	public void testLifecycles() throws SecurityException, NoSuchMethodException {
		Multimap<Class<? extends Annotation>, Method> lifecycles = new Descriptor<DescriptorObject>(DescriptorObject.class).lifecycles();
		assertThat(lifecycles.values().size(), is(1));
		assertThat(lifecycles.values().toArray(new Method[1])[0], is(DescriptorObject.class.getDeclaredMethod("lifecycle")));
		lifecycles = new Descriptor<DescriptorObject2>(DescriptorObject2.class).lifecycles();
		assertTrue(lifecycles.isEmpty());
		lifecycles = new Descriptor<DescriptorComponent>(DescriptorComponent.class).lifecycles();
		assertThat(lifecycles.values().size(), is(1));
		assertThat(lifecycles.values().toArray(new Method[1])[0], is(DescriptorComponent.class.getDeclaredMethod("lifecycle")));
	}

	@Test
	public void testValidates() throws SecurityException, NoSuchFieldException {
		Set<Field> validates = new Descriptor<DescriptorObject>(DescriptorObject.class).validates();
		assertThat(validates.size(), is(1));
		assertThat(validates.toArray(new Field[1])[0], is(DescriptorObject.class.getDeclaredField("object2")));
		validates = new Descriptor<DescriptorObject2>(DescriptorObject2.class).validates();
		assertTrue(validates.isEmpty());
		validates = new Descriptor<DescriptorComponent>(DescriptorComponent.class).validates();
		assertThat(validates.size(), is(1));
		assertThat(validates.toArray(new Field[1])[0], is(DescriptorComponent.class.getDeclaredField("object2")));
	}

	@Test
	public void testConstraints() throws SecurityException, NoSuchFieldException {
		Set<Annotation> constraints = new Descriptor<DescriptorObject>(DescriptorObject.class).constraints();
		assertThat(constraints.size(), is(1));
		assertThat(constraints.toArray(new Annotation[1])[0], is((Annotation) DescriptorObject.class.getAnnotation(DescriptorConstraint.class)));
		constraints = new Descriptor<DescriptorObject2>(DescriptorObject2.class).constraints();
		assertThat(constraints.size(), is(1));
		assertThat(constraints.toArray(new Annotation[1])[0], is((Annotation) DescriptorStereotype.class.getAnnotation(DescriptorConstraint.class)));
		constraints = new Descriptor<DescriptorObject3>(DescriptorObject3.class).constraints();
		assertTrue(constraints.isEmpty());
		constraints = new Descriptor<DescriptorComponent>(DescriptorComponent.class).constraints();
		assertThat(constraints.size(), is(1));
		assertThat(constraints.toArray(new Annotation[1])[0], is((Annotation) DescriptorObject.class.getAnnotation(DescriptorConstraint.class)));
	}

	@Test
	public void testIntercepts() {
		Set<Annotation> intercepts = new Descriptor<DescriptorObject>(DescriptorObject.class).intercepts();
		assertTrue(intercepts.isEmpty());
		intercepts = new Descriptor<DescriptorObject2>(DescriptorObject2.class).intercepts();
		assertTrue(intercepts.isEmpty());
		intercepts = new Descriptor<DescriptorObject3>(DescriptorObject3.class).intercepts();
		assertTrue(intercepts.isEmpty());
		intercepts = new Descriptor<DescriptorInterceptor>(DescriptorInterceptor.class).intercepts();
		assertThat(intercepts.size(), is(1));
		assertThat(intercepts.toArray(new Annotation[1])[0].annotationType(), is((Object) DescriptorIntercept.class));
		intercepts = new Descriptor<DescriptorComponent>(DescriptorComponent.class).intercepts();
		assertTrue(intercepts.isEmpty());
		intercepts = new Descriptor<DescriptorInterceptorComponent>(DescriptorInterceptorComponent.class).intercepts();
		assertThat(intercepts.size(), is(1));
		assertThat(intercepts.toArray(new Annotation[1])[0].annotationType(), is((Object) DescriptorIntercept.class));
	}

	@Test
	public void testAdvices() throws SecurityException, NoSuchMethodException {
		ListMultimap<Class<? extends Annotation>, Method> advices = new Descriptor<DescriptorObject>(DescriptorObject.class).advices();
		assertTrue(advices.values().isEmpty());
		advices = new Descriptor<DescriptorObject2>(DescriptorObject2.class).advices();
		assertTrue(advices.values().isEmpty());
		advices = new Descriptor<DescriptorInterceptor>(DescriptorInterceptor.class).advices();
		assertThat(advices.values().size(), is(1));
		assertThat(advices.values().toArray(new Method[1])[0], is(DescriptorInterceptor.class.getDeclaredMethod("advice")));
		advices = new Descriptor<DescriptorComponent>(DescriptorComponent.class).advices();
		assertTrue(advices.values().isEmpty());
		advices = new Descriptor<DescriptorInterceptorComponent>(DescriptorInterceptorComponent.class).advices();
		assertThat(advices.values().size(), is(1));
		assertThat(advices.values().toArray(new Method[1])[0], is(DescriptorInterceptorComponent.class.getDeclaredMethod("advice")));
	}

	@Test
	public void testToString() {
		assertThat(new Descriptor<DescriptorObject>(DescriptorObject.class).toString(), is(
				"type: class org.eiichiro.jaguar.DescriptorObject, " +
				"deployments: [interface org.eiichiro.jaguar.deployment.Production], " +
				"scope: @org.eiichiro.jaguar.scope.Singleton(eager=false), " +
				"bindings: [@org.eiichiro.jaguar.DescriptorBinding()], " +
				"constraints: [@org.eiichiro.jaguar.DescriptorConstraint()], constructor: null, " + 
				"injects: [java.lang.Object org.eiichiro.jaguar.DescriptorObject.object], " +
				"joinpoints: [public void org.eiichiro.jaguar.DescriptorObject.method()], " +
				"lifecycles: {interface org.eiichiro.jaguar.lifecycle.Constructed=[public void org.eiichiro.jaguar.DescriptorObject.lifecycle()]}, " +
				"validates: [java.lang.Object org.eiichiro.jaguar.DescriptorObject.object2]"));
		assertThat(new Descriptor<DescriptorObject2>(DescriptorObject2.class).toString(), is(
				"type: class org.eiichiro.jaguar.DescriptorObject2, " +
				"deployments: [interface org.eiichiro.jaguar.deployment.Production], " +
				"scope: @org.eiichiro.jaguar.scope.Singleton(eager=false), " +
				"bindings: [@org.eiichiro.jaguar.DescriptorBinding()], " +
				"constraints: [@org.eiichiro.jaguar.DescriptorConstraint()], constructor: null, " + 
				"injects: [], joinpoints: [], lifecycles: {}, validates: []"));
		assertThat(new Descriptor<DescriptorObject3>(DescriptorObject3.class).toString(), is(
				"type: class org.eiichiro.jaguar.DescriptorObject3, " +
				"deployments: [], scope: null, bindings: [], constraints: [], constructor: null, injects: [], " +
				"joinpoints: [], lifecycles: {}, validates: []"));
		assertThat(new Descriptor<DescriptorInterceptor>(DescriptorInterceptor.class).toString(), is(
				"type: class org.eiichiro.jaguar.DescriptorInterceptor, " +
				"deployments: [], scope: null, bindings: [], constraints: [], constructor: null, injects: [], " +
				"joinpoints: [], lifecycles: {}, validates: [], " +
				"intercepts: [@org.eiichiro.jaguar.DescriptorIntercept()], " +
				"advices: {interface org.eiichiro.jaguar.interceptor.Before=[public void org.eiichiro.jaguar.DescriptorInterceptor.advice()]}"));
		assertThat(new Descriptor<DescriptorComponent>(DescriptorComponent.class).toString(), is(
				"type: class org.eiichiro.jaguar.DescriptorComponent, " +
				"deployments: [interface org.eiichiro.jaguar.deployment.Production], " +
				"scope: @org.eiichiro.jaguar.scope.Singleton(eager=false), " +
				"bindings: [@org.eiichiro.jaguar.DescriptorBinding()], " +
				"constraints: [@org.eiichiro.jaguar.DescriptorConstraint()], constructor: null, " + 
				"injects: [java.lang.Object org.eiichiro.jaguar.DescriptorComponent.object], " +
				"lifecycles: {interface org.eiichiro.jaguar.lifecycle.Constructed=[public void org.eiichiro.jaguar.DescriptorComponent.lifecycle()]}, " +
				"validates: [java.lang.Object org.eiichiro.jaguar.DescriptorComponent.object2]"));
		assertThat(new Descriptor<DescriptorInterceptorComponent>(DescriptorInterceptorComponent.class).toString(), is(
				"type: class org.eiichiro.jaguar.DescriptorInterceptorComponent, " +
				"deployments: [interface org.eiichiro.jaguar.deployment.Production], " +
				"scope: @org.eiichiro.jaguar.scope.Singleton(eager=false), " +
				"bindings: [@org.eiichiro.jaguar.DescriptorBinding()], " +
				"constraints: [@org.eiichiro.jaguar.DescriptorConstraint()], constructor: null, " +
				"injects: [java.lang.Object org.eiichiro.jaguar.DescriptorInterceptorComponent.object], " +
				"lifecycles: {interface org.eiichiro.jaguar.lifecycle.Constructed=[public void org.eiichiro.jaguar.DescriptorInterceptorComponent.lifecycle()]}, " +
				"validates: [java.lang.Object org.eiichiro.jaguar.DescriptorInterceptorComponent.object2], " +
				"intercepts: [@org.eiichiro.jaguar.DescriptorIntercept()], " +
				"advices: {interface org.eiichiro.jaguar.interceptor.Before=[public void org.eiichiro.jaguar.DescriptorInterceptorComponent.advice()]}"));
		assertThat(new Descriptor<DescriptorObject5>(DescriptorObject5.class).toString(), is(
				"type: class org.eiichiro.jaguar.DescriptorObject5, " +
				"deployments: [], " +
				"scope: null, " +
				"bindings: [], " +
				"constraints: [], constructor: public org.eiichiro.jaguar.DescriptorObject5(java.lang.Object), " +
				"injects: [], " +
				"joinpoints: [], " +
				"lifecycles: {}, " +
				"validates: []"));
	}

}
