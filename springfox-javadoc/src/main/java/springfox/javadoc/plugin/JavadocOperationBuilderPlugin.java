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

import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import springfox.documentation.builders.ResponseMessageBuilder;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.schema.ModelReference;
import springfox.documentation.service.ResponseMessage;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.OperationBuilderPlugin;
import springfox.documentation.spi.service.contexts.OperationContext;

import java.util.HashSet;
import java.util.Set;

/**
 * Plugin to generate the @@ApiOperation values from the properties
 * file generated by the {@link springfox.javadoc.doclet.SwaggerPropertiesDoclet}
 */
@Component
public class JavadocOperationBuilderPlugin implements OperationBuilderPlugin {

    private static final String PERIOD = ".";
    private final Environment environment;

    public JavadocOperationBuilderPlugin(Environment environment) {
        this.environment = environment;
    }


    @Override
    public void apply(OperationContext context) {

        String notes = context.requestMappingPattern() + PERIOD + context.httpMethod().toString() + ".notes";
        if (StringUtils.hasText(notes) && StringUtils.hasText(environment.getProperty(notes))) {
            context.operationBuilder().notes("<b>" + context.getName() + "</b><br/>" + environment.getProperty(notes));
        }
        String returnDescription = context.requestMappingPattern() + PERIOD + context.httpMethod().toString()
          + ".return";
        if (StringUtils.hasText(returnDescription) && StringUtils.hasText(environment.getProperty(returnDescription))) {
            context.operationBuilder().summary("returns " + environment.getProperty(returnDescription));
        }
        String throwsDescription = context.requestMappingPattern() + PERIOD + context.httpMethod().toString()
          + ".throws.";
        int i = 0;
        Set<ResponseMessage> responseMessages = new HashSet<>();
        while (StringUtils.hasText(throwsDescription + i)
          && StringUtils.hasText(environment.getProperty(throwsDescription + i))) {
            String[] throwsValues = StringUtils.split(environment.getProperty(throwsDescription + i), "-");
            if (throwsValues != null && throwsValues.length == 2) {
                // TODO[MN]: proper mapping once
                // https://github.com/springfox/springfox/issues/521 is solved
                String thrownExceptionName = throwsValues[0];
                String throwComment = throwsValues[1];
                ModelReference model = new ModelRef(thrownExceptionName);
                ResponseMessage message = new ResponseMessageBuilder().code(500).message(throwComment)
                  .responseModel(model).build();
                responseMessages.add(message);
            }
            i++;
        }
        context.operationBuilder().responseMessages(responseMessages);
    }

    @Override
    public boolean supports(DocumentationType documentationType) {
        return true;
    }
}