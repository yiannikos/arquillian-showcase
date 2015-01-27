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
package org.arquillian.extension.decoder;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.*;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.arquillian.extension.decoder.api.Decoder;
import org.arquillian.extension.decoder.api.DecoderRegistry;
import org.arquillian.extension.decoder.configuration.DecoderConfiguration;
import org.arquillian.extension.decoder.configuration.DecoderConfigurator;
import org.arquillian.extension.decoder.impl.DecoderExecutionDecider;
import org.arquillian.extension.decoder.impl.DecoderTestClassScanner;
import org.arquillian.extension.decoder.spi.DecoderProvider;
import org.arquillian.extension.decoder.spi.event.DecideMethodExecutions;
import org.arquillian.extension.decoder.spi.event.ExecutionDecisionEvent;
import org.jboss.arquillian.core.api.InstanceProducer;
import org.jboss.arquillian.core.api.annotation.ApplicationScoped;
import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.core.spi.Manager;
import org.jboss.arquillian.core.spi.ServiceLoader;
import org.jboss.arquillian.core.spi.context.ApplicationContext;
import org.jboss.arquillian.test.spi.context.ClassContext;
import org.jboss.arquillian.test.spi.event.suite.BeforeClass;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * @author <a href="mailto:smikloso@redhat.com">Stefan Miklosovic</a>
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class DecoderTestCase extends AbstractDecoderTestCase
{
    @Inject
    @ApplicationScoped
    private InstanceProducer<ServiceLoader> serviceProducer;

    @Mock
    private ServiceLoader serviceLoader;

    private DecoderConfiguration decoderConfiguration;

    private Manager manager;

    private DecoderProvider decoderProvider = new DecoderProvider()
    {

        @Override
        public Class<? extends Annotation> provides()
        {
            return FakeDecoder.class;
        }
    };

    @Override
    public void addExtensions(List<Class<?>> extensions)
    {
        extensions.add(DecoderTestClassScanner.class);
        extensions.add(DecoderExecutionDecider.class);
        extensions.add(DecoderConfigurator.class);
    }

    @Before
    public void setup()
    {
        serviceProducer.set(serviceLoader);

        List<DecoderProvider> decoderProviders = new ArrayList<DecoderProvider>();
        decoderProviders.add(decoderProvider);

        Mockito.when(serviceLoader.all(DecoderProvider.class)).thenReturn(decoderProviders);

        manager = Mockito.spy(getManager());
        Mockito.when(manager.resolve(ServiceLoader.class)).thenReturn(serviceLoader);

        decoderConfiguration = new DecoderConfiguration();
        bind(ApplicationScoped.class, DecoderConfiguration.class, decoderConfiguration);
    }

    @Test
    public void decoderRegistryTestCase()
    {
        fire(new BeforeClass(FakeTestClass.class));

        assertEventFired(BeforeClass.class, 1);
        assertEventFired(DecideMethodExecutions.class, 1);

        DecoderRegistry decoderRegistry = manager.getContext(ClassContext.class).getObjectStore().get(DecoderRegistry.class);

        DecoderConfiguration configuration = manager.getContext(ApplicationContext.class).getObjectStore().get(DecoderConfiguration.class);

        assertThat(configuration, is(not(nullValue())));

        assertThat(decoderRegistry, is(not(nullValue())));

        List<Method> fakeDecoderMethods = decoderRegistry.getMethodsForAnnotation(FakeDecoder.class);
        assertEquals(2, fakeDecoderMethods.size());

        List<Method> dumymDecoderMethods = decoderRegistry.getMethodsForAnnotation(DummyDecoder.class);
        assertEquals(1, dumymDecoderMethods.size());

        // for every method and for every Decoder annotation of that method
        assertEventFired(ExecutionDecisionEvent.class, 3);
    }

    // utils

    private static final class FakeTestClass
    {
        @Test
        @FakeDecoder
        public void fakeTest()
        {
        }

        @Test
        @FakeDecoder
        @DummyDecoder
        public void dummyTest()
        {
        }

        @Test
        public void someTestMethod()
        {
        }
    }

    @Decoder
    @Target({ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    private @interface FakeDecoder
    {
        String value() default "";
    }

    @Decoder
    @Target({ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    private @interface DummyDecoder
    {
        String value() default "";
    }
}
