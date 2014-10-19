package org.eiichiro.jaguar;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import org.eiichiro.jaguar.Stereotype;
import org.eiichiro.jaguar.deployment.Production;
import org.eiichiro.jaguar.scope.Singleton;

@Retention(RetentionPolicy.RUNTIME)
@Production
@Singleton
@DescriptorBinding
@DescriptorConstraint
@DescriptorPointcut
@Stereotype
public @interface DescriptorStereotype {

}
