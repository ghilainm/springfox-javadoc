/*
 *
 *  Copyright 2018-2019 the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 *
 */
package springfox.javadoc.plugin;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import springfox.javadoc.doclet.SwaggerPropertiesDoclet;

import static springfox.javadoc.doclet.ClassDirectoryOption.SPRINGFOX_JAVADOC_PROPERTIES;

/**
 * Spring configuration that adds the properties file generated by the {@link SwaggerPropertiesDoclet} as property
 * source and also adds the {@link JavadocParameterBuilderPlugin} to the Spring context.
 *
 * @author MartinNeumannBeTSE
 */
@Configuration
@PropertySource(value = "classpath:/"
  + SPRINGFOX_JAVADOC_PROPERTIES, ignoreResourceNotFound = true)
public class JavadocPluginConfiguration {

    @Bean
    public JavadocApiListingBuilderPlugin javadocApiListingBuilderPlugin(Environment environment) {
        return new JavadocApiListingBuilderPlugin(environment);
    }

    @Bean
    public JavadocOperationBuilderPlugin javadocOperationBuilderPlugin(Environment environment) {
        return new JavadocOperationBuilderPlugin(environment);
    }

    @Bean
    public JavadocParameterBuilderPlugin javadocParameterBuilderPlugin(Environment environment) {
        return new JavadocParameterBuilderPlugin(environment);
    }

    @Bean
    public JavadocModelBuilderPlugin javadocModelBuilderPlugin(Environment environment) {
        return new JavadocModelBuilderPlugin(environment);
    }

    @Bean
    public JavadocModelPropertyBuilderPlugin javadocModelPropertyBuilderPlugin(Environment environment) {
        return new JavadocModelPropertyBuilderPlugin(environment);
    }
}
