package org.eiichiro.jaguar.inject;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import org.eiichiro.jaguar.Qualifier;

@Retention(RetentionPolicy.RUNTIME)
@Qualifier
public @interface ProviderTestQualifier {

	String value();
	
}
