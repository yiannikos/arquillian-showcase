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
package org.arquillian.extension.static_.container.controller.impl;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.security.AccessController;
import java.security.PrivilegedAction;

import org.arquillian.extension.static_.container.controller.api.StaticContainerController;
import org.jboss.arquillian.container.test.api.ContainerController;
import org.jboss.arquillian.core.api.Instance;
import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.core.api.annotation.Observes;
import org.jboss.arquillian.test.spi.TestClass;
import org.jboss.arquillian.test.spi.event.suite.BeforeClass;

/**
 * Statically injects {@link ContainerController} into a test class.
 * 
 * @author <a href="mailto:smikloso@redhat.com">Stefan Miklosovic</a>
 *
 */
public class StaticContainerControllerBuilder
{
    @Inject
    private Instance<ContainerController> containerController;

    public void onBeforeClass(@Observes BeforeClass event)
    {
        final TestClass testClass = event.getTestClass();

        final Field[] fields = testClass.getJavaClass().getDeclaredFields();

        for (final Field field : fields)
        {
            if (!Modifier.isStatic(field.getModifiers()) || field.getType() != ContainerController.class)
            {
                continue;
            }

            if (field.getAnnotation(StaticContainerController.class) == null)
            {
                continue;
            }

            AccessController.doPrivileged(new PrivilegedAction<Void> ()
            {

                @Override
                public Void run()
                {
                    field.setAccessible(true);        
                    return null;
                }
            });

            try {
                field.set(null, containerController.get());    
            } catch (Exception ex)
            {
                throw new RuntimeException(
                    "Could not set static ContainerController field for test class" + testClass.getName(), ex);
            }

        }
    }
}
