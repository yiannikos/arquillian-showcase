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
package org.arquillian.extension.static_.controller.test;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;

import org.arquillian.extension.static_.container.controller.api.StaticContainerController;
import org.jboss.arquillian.container.test.api.ContainerController;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author <a href="mailto:smikloso@redhat.com">Stefan Miklosovic</a>
 *
 */
@RunWith(Arquillian.class)
@RunAsClient
public class StaticContainerControllerTestCase
{
    @StaticContainerController
    private static ContainerController containerController;

    @BeforeClass
    public static void assertContainerControllerExistInBeforeClass()
    {
        Assert.assertThat(containerController, is(not(nullValue())));
    }

    @Before
    public void assertContainerControllerExistInBefore()
    {
        Assert.assertThat(containerController, is(not(nullValue())));
    }

    @Test
    public void assertThatContainerControllerExistInTest()
    {
        Assert.assertThat(containerController, is(not(nullValue())));
    }

    @After
    public void assertContainerControllerExistInAfter()
    {
        Assert.assertThat(containerController, is(not(nullValue())));
    }

    @AfterClass
    public static void assertContainerControllerExistInAfterClass()
    {
        Assert.assertThat(containerController, is(not(nullValue())));
    }
}
