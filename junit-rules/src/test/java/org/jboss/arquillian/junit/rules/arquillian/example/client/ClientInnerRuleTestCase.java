package org.jboss.arquillian.junit.rules.arquillian.example.client;

import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.rules.arquillian.example.AbstractTestCase;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import java.net.URL;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
@RunAsClient
public class ClientInnerRuleTestCase extends AbstractTestCase {

    @ArquillianResource
    private URL url;

    @Rule
    public TestRule rule = new TestRule() {
        public Statement apply(Statement statement, Description description) {
            assertThat(url).hasPath("/" + DEPLOYMENT_NAME + "/");
            return statement;
        }
    };

    @Test
    public void should_inject_url_to_rule_statement() {
        assertThat(url).hasPath("/" + DEPLOYMENT_NAME + "/");
    }

}
