package org.eiichiro.jaguar.lifecycle;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.eiichiro.jaguar.lifecycle.Lifecycle;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Lifecycle
public @interface Startup {}
