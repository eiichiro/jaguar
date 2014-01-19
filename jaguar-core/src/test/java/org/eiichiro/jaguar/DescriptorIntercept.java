package org.eiichiro.jaguar;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import org.eiichiro.jaguar.interceptor.Intercept;

@Retention(RetentionPolicy.RUNTIME)
@Intercept
public @interface DescriptorIntercept {

}
