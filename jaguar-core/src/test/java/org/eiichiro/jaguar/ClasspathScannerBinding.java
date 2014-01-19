package org.eiichiro.jaguar;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import org.eiichiro.jaguar.inject.Binding;

@Retention(RetentionPolicy.RUNTIME)
@Binding
public @interface ClasspathScannerBinding {

}
