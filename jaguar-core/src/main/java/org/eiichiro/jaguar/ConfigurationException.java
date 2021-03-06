/*
 * Copyright (C) 2014 Eiichiro Uchiumi. All Rights Reserved.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 *     
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.eiichiro.jaguar;

/**
 * Thrown when any component misconfiguration is detected by the container.
 * 
 * @author <a href="mailto:mail@eiichiro.org">Eiichiro Uchiumi</a>
 */
public class ConfigurationException extends RuntimeException {

	private static final long serialVersionUID = 76315245856062540L;

	/**
	 * Constructs a new {@code ConfigurationException} with the specified 
	 * detail message.
	 * 
	 * @param message The detail message.
	 */
	public ConfigurationException(String message) {
		super(message);
	}

	/**
	 * Constructs a new {@code ConfigurationException} with the specified detail 
	 * message and the cause of this exception.
	 * 
	 * @param message The detail message.
	 * @param cause The cause of this exception.
	 */
	public ConfigurationException(String message, Throwable cause) {
		super(message, cause);
	}

}
