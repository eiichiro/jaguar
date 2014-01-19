package org.eiichiro.jaguar.deployment;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.eiichiro.jaguar.deployment.Deployment;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Deployment
public @interface Deployment1 {}
