package org.jboss.arquillian.junit.rules.arquillian.example.rules;

import org.jboss.arquillian.test.api.ArquillianResource;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import java.net.URL;

import static org.assertj.core.api.Assertions.assertThat;
import static org.jboss.arquillian.junit.rules.arquillian.example.AbstractTestCase.DEPLOYMENT_NAME;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class RuleWithArqResInStatement implements TestRule {

    @ArquillianResource
    private URL url;

    public Statement apply(final Statement base, final Description description) {

        return new Statement() {
            @Override public void evaluate() throws Throwable {
                assertThat(url).hasPath("/" + DEPLOYMENT_NAME + "/");
                base.evaluate();
            }
        };
    }
}
