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
package org.arquillian.extension.decoder.jira.configuration;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.arquillian.extension.decoder.jira.impl.JiraDecoderClient;
import org.arquillian.extension.decoder.jira.impl.JiraDecoderClientFactory;
import org.arquillian.extension.decoder.spi.event.DecoderExtensionConfigured;
import org.jboss.arquillian.config.descriptor.api.ArquillianDescriptor;
import org.jboss.arquillian.config.descriptor.api.ExtensionDef;
import org.jboss.arquillian.core.api.Instance;
import org.jboss.arquillian.core.api.InstanceProducer;
import org.jboss.arquillian.core.api.annotation.ApplicationScoped;
import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.core.api.annotation.Observes;
import org.jboss.arquillian.core.spi.ServiceLoader;

/**
 * @author <a href="mailto:smikloso@redhat.com">Stefan Miklosovic</a>
 *
 */
public class JiraDecoderConfigurator
{
    private static final Logger logger = Logger.getLogger(JiraDecoderConfigurator.class.getName());

    private static final String EXTENSION_NAME = "decoder-jira";

    @Inject
    private Instance<ServiceLoader> serviceLoader;

    @Inject
    @ApplicationScoped
    private InstanceProducer<JiraDecoderConfiguration> jiraDecoderConfiguration;

    @Inject
    @ApplicationScoped
    private InstanceProducer<JiraDecoderClient> jiraDecoderClient;

    public void onDecoderExtensionConfigured(@Observes DecoderExtensionConfigured event, ArquillianDescriptor arquillianDescriptor) throws Exception
    {
        JiraDecoderConfiguration jiraDecoderConfiguration = new JiraDecoderConfiguration();

        for (final ExtensionDef extension : arquillianDescriptor.getExtensions())
        {
            if (extension.getExtensionName().equals(EXTENSION_NAME))
            {
                jiraDecoderConfiguration.setConfiguration(extension.getExtensionProperties());
                jiraDecoderConfiguration.validate();
                break;
            }
        }

        this.jiraDecoderConfiguration.set(jiraDecoderConfiguration);

        final JiraDecoderClient jiraDecoderClient = new JiraDecoderClientFactory().build(this.jiraDecoderConfiguration.get());

        this.jiraDecoderClient.set(jiraDecoderClient);

        if (logger.isLoggable(Level.INFO))
        {
            System.out.println("Configuration of Arquillian JIRA extension: ");
            System.out.println(jiraDecoderConfiguration.toString());
        }
    }
}
