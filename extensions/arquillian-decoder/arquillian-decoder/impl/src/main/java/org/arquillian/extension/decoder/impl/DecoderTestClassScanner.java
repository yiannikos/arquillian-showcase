/*
 * JBoss, Home of Professional Open Source
 * Copyright 2015, Red Hat, Inc. and/or its affiliates, and individual
 * contributors by the @authors tag. See the copyright.txt in the
 * distribution for a full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.arquillian.extension.decoder.impl;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.arquillian.extension.decoder.api.Decoder;
import org.arquillian.extension.decoder.api.DecoderRegistry;
import org.arquillian.extension.decoder.configuration.DecoderConfiguration;
import org.arquillian.extension.decoder.spi.DecoderProvider;
import org.arquillian.extension.decoder.spi.event.DecideMethodExecutions;
import org.jboss.arquillian.core.api.Event;
import org.jboss.arquillian.core.api.Instance;
import org.jboss.arquillian.core.api.InstanceProducer;
import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.core.api.annotation.Observes;
import org.jboss.arquillian.core.spi.ServiceLoader;
import org.jboss.arquillian.core.spi.Validate;
import org.jboss.arquillian.test.spi.TestClass;
import org.jboss.arquillian.test.spi.annotation.ClassScoped;
import org.jboss.arquillian.test.spi.event.suite.BeforeClass;

/**
 * @author <a href="mailto:smikloso@redhat.com">Stefan Miklosovic</a>
 *
 */
public class DecoderTestClassScanner
{
    @Inject
    @ClassScoped
    private InstanceProducer<DecoderRegistry> decoderRegistry;

    @Inject
    private Instance<ServiceLoader> serviceLoader;

    @Inject
    private Event<DecideMethodExecutions> decideMethodExecution;

    @Inject
    private Instance<DecoderConfiguration> decoderConfiguration;

    public void onBeforeClass(@Observes BeforeClass event)
    {
        TestMethodExecutionRegister.setConfigration(decoderConfiguration.get());
        TestMethodExecutionRegister.clear();

        if (decoderConfiguration.get().getIgnore())
        {
            return;
        }

        final Collection<DecoderProvider> decoderProviders = serviceLoader.get().all(DecoderProvider.class);

        checkDecoderProviderUniqueness(decoderProviders);

        final Map<Method, List<Annotation>> scannedTestMethods = scanTestMethods(event.getTestClass(), Decoder.class);

        DecoderRegistryImpl decoderRegistry = new DecoderRegistryImpl();
        decoderRegistry.put(scannedTestMethods);
        this.decoderRegistry.set(decoderRegistry);

        decideMethodExecution.fire(new DecideMethodExecutions());
    }

    private void checkDecoderProviderUniqueness(final Collection<DecoderProvider> decoderProviders)
    {
        final Set<Class<? extends Annotation>> uniqueProviders = new HashSet<Class<? extends Annotation>>();

        for (final DecoderProvider decoderProvider : decoderProviders)
        {
            Class<? extends Annotation> decoderClass = decoderProvider.provides();

            if (decoderClass == null) {
                throw new IllegalStateException(
                    String.format("Decoder provider's provides() method (%s) returns null object.",
                        decoderProvider.getClass().getName()));
            }

            Decoder decoderAnnotation = decoderClass.getAnnotation(Decoder.class);

            if (decoderAnnotation == null)
            {
                throw new IllegalStateException(
                    String.format("Decoder provider (%s) does not provide annotation annotated by Decoder class.",
                        decoderProvider.getClass().getName()));
            }

            if (!uniqueProviders.add(decoderClass))
            {
                throw new IllegalStateException(
                    String.format("You have put on class path providers which provide the same decoder annotation (%s).",
                        decoderAnnotation.annotationType()));
            }
        }
    }

    private Map<Method, List<Annotation>> scanTestMethods(TestClass testClass, Class<? extends Annotation> decoderAnnotation)
    {
        Validate.notNull(testClass, "Test class to scan must be specified.");

        final Map<Method, List<Annotation>> methodAnnotationsMap = new HashMap<Method, List<Annotation>>();

        final Method[] methods = testClass.getJavaClass().getMethods();

        for (final Method method : methods)
        {
            List<Annotation> methodAnnotations = new ArrayList<Annotation>();

            for (final Annotation annotation : method.getAnnotations())
            {
                if (annotation.annotationType().isAnnotationPresent(decoderAnnotation))
                {
                    methodAnnotations.add(annotation);
                }
            }

            if (methodAnnotations.size() > 0)
            {
                methodAnnotationsMap.put(method, methodAnnotations);
            }

        }

        return methodAnnotationsMap;
    }
}
