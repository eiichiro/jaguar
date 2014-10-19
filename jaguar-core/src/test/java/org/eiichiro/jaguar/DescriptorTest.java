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
		
		new Descriptor<DescriptorComponent>(DescriptorComponent.class);
		new Descriptor<DescriptorProvider>(DescriptorProvider.class);
	}

	@Test
	public void testType() {
		assertThat(new Descriptor<DescriptorComponent>(DescriptorComponent.class).type(), is((Object) DescriptorComponent.class));
		assertThat(new Descriptor<DescriptorProvider>(DescriptorProvider.class).type(), is((Object) DescriptorProvider.class));
	}

	@Test
	public void testAaspect() {
		assertFalse(new Descriptor<DescriptorComponent>(DescriptorComponent.class).aspect());
		assertFalse(new Descriptor<DescriptorProvider>(DescriptorProvider.class).aspect());
		assertTrue(new Descriptor<DescriptorInterceptor>(DescriptorInterceptor.class).aspect());
		assertTrue(new Descriptor<DescriptorInterceptorProvider>(DescriptorInterceptorProvider.class).aspect());
	}

	@Test
	public void testDeployments() {
		Set<Class<? extends Annotation>> deployments = new Descriptor<DescriptorComponent>(DescriptorComponent.class).deployments();
		assertThat(deployments.size(), is(1));
		assertThat(deployments.toArray()[0], is((Object) Production.class));
		deployments = new Descriptor<DescriptorComponent2>(DescriptorComponent2.class).deployments();
		assertThat(deployments.size(), is(1));
		assertThat(deployments.toArray()[0], is((Object) Production.class));
		deployments = new Descriptor<DescriptorComponent3>(DescriptorComponent3.class).deployments();
		assertTrue(deployments.isEmpty());
		deployments = new Descriptor<DescriptorProvider>(DescriptorProvider.class).deployments();
		assertThat(deployments.toArray()[0], is((Object) Production.class));
	}

	@Test
	public void testScope() {
		Annotation scope = new Descriptor<DescriptorComponent>(DescriptorComponent.class).scope();
		assertThat(scope.annotationType(), is((Object) Singleton.class));
		scope = new Descriptor<DescriptorComponent2>(DescriptorComponent2.class).scope();
		assertThat(scope.annotationType(), is((Object) Singleton.class));
		scope = new Descriptor<DescriptorComponent3>(DescriptorComponent3.class).scope();
		assertNull(scope);
		
		try {
			new Descriptor<DescriptorComponent4>(DescriptorComponent4.class);
			fail();
		} catch (ConfigurationException e) {
			e.printStackTrace();
		}
		
		scope = new Descriptor<DescriptorProvider>(DescriptorProvider.class).scope();
		assertThat(scope.annotationType(), is((Object) Singleton.class));
	}

	@Test
	public void testBindings() {
		Set<Annotation> bindings = new Descriptor<DescriptorComponent>(DescriptorComponent.class).bindings();
		assertThat(bindings.size(), is(1));
		assertThat(bindings.toArray()[0], is(DescriptorBinding.class));
		bindings = new Descriptor<DescriptorComponent2>(DescriptorComponent2.class).bindings();
		assertThat(bindings.size(), is(1));
		assertThat(bindings.toArray()[0], is(DescriptorBinding.class));
		bindings = new Descriptor<DescriptorComponent3>(DescriptorComponent3.class).bindings();
		assertTrue(bindings.isEmpty());
		bindings = new Descriptor<DescriptorProvider>(DescriptorProvider.class).bindings();
		assertThat(bindings.size(), is(1));
		assertThat(bindings.toArray()[0], is(DescriptorBinding.class));
	}

	@Test
	public void testInjects() throws SecurityException, NoSuchFieldException {
		Set<Field> injects = new Descriptor<DescriptorComponent>(DescriptorComponent.class).injects();
		assertThat(injects.size(), is(1));
		assertThat(injects.toArray(new Field[1])[0], is(DescriptorComponent.class.getDeclaredField("object")));
		injects = new Descriptor<DescriptorComponent2>(DescriptorComponent2.class).injects();
		assertTrue(injects.isEmpty());
		injects = new Descriptor<DescriptorProvider>(DescriptorProvider.class).injects();
		assertThat(injects.size(), is(1));
		assertThat(injects.toArray(new Field[1])[0], is(DescriptorProvider.class.getDeclaredField("object")));
	}

	@Test
	public void testConstructor() throws NoSuchMethodException {
		assertNull(new Descriptor<DescriptorComponent>(DescriptorComponent.class).constructor());
		Constructor<DescriptorComponent5> constructor = new Descriptor<DescriptorComponent5>(DescriptorComponent5.class).constructor();
		assertThat(constructor, is(DescriptorComponent5.class.getConstructor(Object.class)));
		
		try {
			new Descriptor<DescriptorComponent6>(DescriptorComponent6.class);
			fail();
		} catch (ConfigurationException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testJoinpoints() throws SecurityException, NoSuchMethodException {
		Set<Method> joinpoints = new Descriptor<DescriptorComponent>(DescriptorComponent.class).joinpoints();
		assertThat(joinpoints.size(), is(1));
		assertThat(joinpoints.toArray(new Method[1])[0], is(DescriptorComponent.class.getDeclaredMethod("method")));
		joinpoints = new Descriptor<DescriptorComponent2>(DescriptorComponent2.class).joinpoints();
		assertTrue(joinpoints.isEmpty());
		joinpoints = new Descriptor<DescriptorProvider>(DescriptorProvider.class).joinpoints();
		assertTrue(joinpoints.isEmpty());
	}

	@Test
	public void testLifecycles() throws SecurityException, NoSuchMethodException {
		Multimap<Class<? extends Annotation>, Method> lifecycles = new Descriptor<DescriptorComponent>(DescriptorComponent.class).lifecycles();
		assertThat(lifecycles.values().size(), is(1));
		assertThat(lifecycles.values().toArray(new Method[1])[0], is(DescriptorComponent.class.getDeclaredMethod("lifecycle")));
		lifecycles = new Descriptor<DescriptorComponent2>(DescriptorComponent2.class).lifecycles();
		assertTrue(lifecycles.isEmpty());
		lifecycles = new Descriptor<DescriptorProvider>(DescriptorProvider.class).lifecycles();
		assertThat(lifecycles.values().size(), is(1));
		assertThat(lifecycles.values().toArray(new Method[1])[0], is(DescriptorProvider.class.getDeclaredMethod("lifecycle")));
	}

	@Test
	public void testValidates() throws SecurityException, NoSuchFieldException {
		Set<Field> validates = new Descriptor<DescriptorComponent>(DescriptorComponent.class).validates();
		assertThat(validates.size(), is(1));
		assertThat(validates.toArray(new Field[1])[0], is(DescriptorComponent.class.getDeclaredField("object2")));
		validates = new Descriptor<DescriptorComponent2>(DescriptorComponent2.class).validates();
		assertTrue(validates.isEmpty());
		validates = new Descriptor<DescriptorProvider>(DescriptorProvider.class).validates();
		assertThat(validates.size(), is(1));
		assertThat(validates.toArray(new Field[1])[0], is(DescriptorProvider.class.getDeclaredField("object2")));
	}

	@Test
	public void testConstraints() throws SecurityException, NoSuchFieldException {
		Set<Annotation> constraints = new Descriptor<DescriptorComponent>(DescriptorComponent.class).constraints();
		assertThat(constraints.size(), is(1));
		assertThat(constraints.toArray(new Annotation[1])[0], is((Annotation) DescriptorComponent.class.getAnnotation(DescriptorConstraint.class)));
		constraints = new Descriptor<DescriptorComponent2>(DescriptorComponent2.class).constraints();
		assertThat(constraints.size(), is(1));
		assertThat(constraints.toArray(new Annotation[1])[0], is((Annotation) DescriptorStereotype.class.getAnnotation(DescriptorConstraint.class)));
		constraints = new Descriptor<DescriptorComponent3>(DescriptorComponent3.class).constraints();
		assertTrue(constraints.isEmpty());
		constraints = new Descriptor<DescriptorProvider>(DescriptorProvider.class).constraints();
		assertThat(constraints.size(), is(1));
		assertThat(constraints.toArray(new Annotation[1])[0], is((Annotation) DescriptorComponent.class.getAnnotation(DescriptorConstraint.class)));
	}

	@Test
	public void testIntercepts() {
		Set<Annotation> intercepts = new Descriptor<DescriptorComponent>(DescriptorComponent.class).pointcuts();
		assertTrue(intercepts.isEmpty());
		intercepts = new Descriptor<DescriptorComponent2>(DescriptorComponent2.class).pointcuts();
		assertTrue(intercepts.isEmpty());
		intercepts = new Descriptor<DescriptorComponent3>(DescriptorComponent3.class).pointcuts();
		assertTrue(intercepts.isEmpty());
		intercepts = new Descriptor<DescriptorInterceptor>(DescriptorInterceptor.class).pointcuts();
		assertThat(intercepts.size(), is(1));
		assertThat(intercepts.toArray(new Annotation[1])[0].annotationType(), is((Object) DescriptorIntercept.class));
		intercepts = new Descriptor<DescriptorProvider>(DescriptorProvider.class).pointcuts();
		assertTrue(intercepts.isEmpty());
		intercepts = new Descriptor<DescriptorInterceptorProvider>(DescriptorInterceptorProvider.class).pointcuts();
		assertThat(intercepts.size(), is(1));
		assertThat(intercepts.toArray(new Annotation[1])[0].annotationType(), is((Object) DescriptorIntercept.class));
	}

	@Test
	public void testAdvices() throws SecurityException, NoSuchMethodException {
		ListMultimap<Class<? extends Annotation>, Method> advices = new Descriptor<DescriptorComponent>(DescriptorComponent.class).advices();
		assertTrue(advices.values().isEmpty());
		advices = new Descriptor<DescriptorComponent2>(DescriptorComponent2.class).advices();
		assertTrue(advices.values().isEmpty());
		advices = new Descriptor<DescriptorInterceptor>(DescriptorInterceptor.class).advices();
		assertThat(advices.values().size(), is(1));
		assertThat(advices.values().toArray(new Method[1])[0], is(DescriptorInterceptor.class.getDeclaredMethod("advice")));
		advices = new Descriptor<DescriptorProvider>(DescriptorProvider.class).advices();
		assertTrue(advices.values().isEmpty());
		advices = new Descriptor<DescriptorInterceptorProvider>(DescriptorInterceptorProvider.class).advices();
		assertThat(advices.values().size(), is(1));
		assertThat(advices.values().toArray(new Method[1])[0], is(DescriptorInterceptorProvider.class.getDeclaredMethod("advice")));
	}

	@Test
	public void testToString() {
		assertThat(new Descriptor<DescriptorComponent>(DescriptorComponent.class).toString(), is(
				"type: class org.eiichiro.jaguar.DescriptorComponent, " +
				"deployments: [interface org.eiichiro.jaguar.deployment.Production], " +
				"scope: @org.eiichiro.jaguar.scope.Singleton(eager=false), " +
				"bindings: [@org.eiichiro.jaguar.DescriptorBinding()], " +
				"constraints: [@org.eiichiro.jaguar.DescriptorConstraint()], constructor: null, " + 
				"injects: [java.lang.Object org.eiichiro.jaguar.DescriptorComponent.object], " +
				"joinpoints: [public void org.eiichiro.jaguar.DescriptorComponent.method()], " +
				"lifecycles: {interface org.eiichiro.jaguar.lifecycle.Constructed=[public void org.eiichiro.jaguar.DescriptorComponent.lifecycle()]}, " +
				"validates: [java.lang.Object org.eiichiro.jaguar.DescriptorComponent.object2]"));
		assertThat(new Descriptor<DescriptorComponent2>(DescriptorComponent2.class).toString(), is(
				"type: class org.eiichiro.jaguar.DescriptorComponent2, " +
				"deployments: [interface org.eiichiro.jaguar.deployment.Production], " +
				"scope: @org.eiichiro.jaguar.scope.Singleton(eager=false), " +
				"bindings: [@org.eiichiro.jaguar.DescriptorBinding()], " +
				"constraints: [@org.eiichiro.jaguar.DescriptorConstraint()], constructor: null, " + 
				"injects: [], joinpoints: [], lifecycles: {}, validates: []"));
		assertThat(new Descriptor<DescriptorComponent3>(DescriptorComponent3.class).toString(), is(
				"type: class org.eiichiro.jaguar.DescriptorComponent3, " +
				"deployments: [], scope: null, bindings: [], constraints: [], constructor: null, injects: [], " +
				"joinpoints: [], lifecycles: {}, validates: []"));
		assertThat(new Descriptor<DescriptorInterceptor>(DescriptorInterceptor.class).toString(), is(
				"type: class org.eiichiro.jaguar.DescriptorInterceptor, " +
				"deployments: [], scope: null, bindings: [], constraints: [], constructor: null, injects: [], " +
				"joinpoints: [], lifecycles: {}, validates: [], " +
				"pointcuts: [@org.eiichiro.jaguar.DescriptorIntercept()], " +
				"advices: {interface org.eiichiro.jaguar.aspect.Before=[public void org.eiichiro.jaguar.DescriptorInterceptor.advice()]}"));
		assertThat(new Descriptor<DescriptorProvider>(DescriptorProvider.class).toString(), is(
				"type: class org.eiichiro.jaguar.DescriptorProvider, " +
				"deployments: [interface org.eiichiro.jaguar.deployment.Production], " +
				"scope: @org.eiichiro.jaguar.scope.Singleton(eager=false), " +
				"bindings: [@org.eiichiro.jaguar.DescriptorBinding()], " +
				"constraints: [@org.eiichiro.jaguar.DescriptorConstraint()], constructor: null, " + 
				"injects: [java.lang.Object org.eiichiro.jaguar.DescriptorProvider.object], " +
				"lifecycles: {interface org.eiichiro.jaguar.lifecycle.Constructed=[public void org.eiichiro.jaguar.DescriptorProvider.lifecycle()]}, " +
				"validates: [java.lang.Object org.eiichiro.jaguar.DescriptorProvider.object2]"));
		assertThat(new Descriptor<DescriptorInterceptorProvider>(DescriptorInterceptorProvider.class).toString(), is(
				"type: class org.eiichiro.jaguar.DescriptorInterceptorProvider, " +
				"deployments: [interface org.eiichiro.jaguar.deployment.Production], " +
				"scope: @org.eiichiro.jaguar.scope.Singleton(eager=false), " +
				"bindings: [@org.eiichiro.jaguar.DescriptorBinding()], " +
				"constraints: [@org.eiichiro.jaguar.DescriptorConstraint()], constructor: null, " +
				"injects: [java.lang.Object org.eiichiro.jaguar.DescriptorInterceptorProvider.object], " +
				"lifecycles: {interface org.eiichiro.jaguar.lifecycle.Constructed=[public void org.eiichiro.jaguar.DescriptorInterceptorProvider.lifecycle()]}, " +
				"validates: [java.lang.Object org.eiichiro.jaguar.DescriptorInterceptorProvider.object2], " +
				"pointcuts: [@org.eiichiro.jaguar.DescriptorIntercept()], " +
				"advices: {interface org.eiichiro.jaguar.aspect.Before=[public void org.eiichiro.jaguar.DescriptorInterceptorProvider.advice()]}"));
		assertThat(new Descriptor<DescriptorComponent5>(DescriptorComponent5.class).toString(), is(
				"type: class org.eiichiro.jaguar.DescriptorComponent5, " +
				"deployments: [], " +
				"scope: null, " +
				"bindings: [], " +
				"constraints: [], constructor: public org.eiichiro.jaguar.DescriptorComponent5(java.lang.Object), " +
				"injects: [], " +
				"joinpoints: [], " +
				"lifecycles: {}, " +
				"validates: []"));
	}

}
