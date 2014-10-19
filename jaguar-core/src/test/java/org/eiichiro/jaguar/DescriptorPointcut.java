package org.eiichiro.jaguar;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import org.eiichiro.jaguar.aspect.Pointcut;

@Retention(RetentionPolicy.RUNTIME)
@Pointcut
public @interface DescriptorPointcut {

}
