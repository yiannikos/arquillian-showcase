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
package org.arquillian.extension.decoder.jira.impl;

import java.net.URI;

import org.arquillian.extension.decoder.api.DecoderClientFactory;
import org.arquillian.extension.decoder.jira.configuration.JiraDecoderConfiguration;
import org.jboss.arquillian.core.spi.Validate;

import com.atlassian.jira.rest.client.api.JiraRestClient;
import com.atlassian.jira.rest.client.internal.async.AsynchronousJiraRestClientFactory;

/**
 * @author <a href="mailto:smikloso@redhat.com">Stefan Miklosovic</a>
 *
 */
public class JiraDecoderClientFactory implements DecoderClientFactory<JiraDecoderConfiguration, JiraDecoderClient>
{

    private JiraDecoderConfiguration decoderConfiguration = null;

    @Override
    public JiraDecoderClient build(JiraDecoderConfiguration decoderConfiguration) throws Exception
    {
        Validate.notNull(decoderConfiguration, "Jira decoder configuration has to be set.");
        this.decoderConfiguration = decoderConfiguration;

        final URI jiraServerUri = this.decoderConfiguration.getServerURI();
        final String username = this.decoderConfiguration.getUsername();
        final String password = this.decoderConfiguration.getPassword();

        final AsynchronousJiraRestClientFactory factory = new AsynchronousJiraRestClientFactory();
        final JiraRestClient restClient = factory.createWithBasicHttpAuthentication(jiraServerUri, username, password);

        final JiraDecoderClient client = new JiraDecoderClient();
        client.setConfiguration(this.decoderConfiguration);
        client.initializeRestClient(restClient);
        client.setDecoderStrategy(new JiraDecoderStrategy(decoderConfiguration));

        return client;
    }

}
