package org.eiichiro.jaguar.inject;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;

import org.eiichiro.jaguar.scope.Singleton;

@Singleton
public class ProviderTestComponentProvider implements Provider<ProviderTestComponent> {

	private Map<String, ProviderTestComponent> components = new HashMap<>();
	
	public ProviderTestComponentProvider() {
		components.put("", new ProviderTestComponent(""));
	}
	
	@Override
	public ProviderTestComponent provide(Target target) {
		synchronized (components) {
			for (Annotation qualifier : target.qualifiers()) {
				if (qualifier.annotationType().equals(ProviderTestQualifier.class)) {
					String value = ((ProviderTestQualifier) qualifier).value();
					
					if (!components.containsKey(value)) {
						components.put(value, new ProviderTestComponent(value));
					}
					
					return components.get(value);
				}
			}
			
			return components.get("");
		}
	}

}
