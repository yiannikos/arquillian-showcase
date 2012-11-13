/*
 * JBoss, Home of Professional Open Source
 * Copyright 2012, Red Hat Middleware LLC, and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
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
package com.acme.daemon;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Ensures that this test was lanuched in a VM from the Arquillian Main Daemon
 * 
 * @author <a href="mailto:alr@jboss.org">Andrew Lee Rubinger</a>
 */
@RunWith(Arquillian.class)
public class DaemonTest {

    private static final String NAME_SW_CLASSLOADER = "org.jboss.shrinkwrap.api.classloader.ShrinkWrapClassLoader";

    @Deployment
    public static JavaArchive createDeployment() {
        final JavaArchive archive = ShrinkWrap.create(JavaArchive.class).addClass(ClassLoaderDiscoveryBean.class);
        return archive;
    }

    /**
     * Ensures we're tested in the context of the Arquillian Server Daemon by checking the loading CL of our test bean
     * is a ShrinkWrap CL (relies on implementation internals, really, but proves that we were not launched by this
     * current VM on the client side).
     */
    @Test
    public void isExecutedInArquillianServerDaemon() {
        final String classLoaderName = new ClassLoaderDiscoveryBean().getClassLoader();
        System.out.println("Loading CL Name: " + classLoaderName);
        Assert.assertEquals("Test was not executed via Arquillian Server Daemon Main Launcher", NAME_SW_CLASSLOADER,
            classLoaderName);
    }

}
