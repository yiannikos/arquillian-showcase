package org.jboss.arquillian.junit.rules.arquillian.example.container;

import org.jboss.arquillian.junit.rules.arquillian.example.AbstractTestCase;
import org.jboss.arquillian.junit.rules.arquillian.example.SimpleBean;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import javax.inject.Inject;
import java.net.URL;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Depends on the fix: https://github.com/arquillian/arquillian-core/pull/110
 *
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class ContainerInnerRuleFixedTestCase extends AbstractTestCase {

    @Inject
    private SimpleBean bean;

    @ArquillianResource
    private URL url;

    @Rule
    public TestRule rule = new TestRule() {
        public Statement apply(final Statement statement, Description description) {
            return new Statement() {
                @Override
                public void evaluate() throws Throwable {
                    assertThat(bean).isNotNull();

                    bean.increment();
                    assertThat(bean.getCounter()).isEqualTo(1);

                    assertThat(url).hasPath("/" + DEPLOYMENT_NAME + "/");

                    statement.evaluate();
                }
            };
        }
    };

    @Test
    public void should_inject_bean_and_url(){
        assertThat(bean).isNotNull();
        // now if you called bean.getCounter(), you could expect 1 as a returned value.
        // But it returns 0 because the bean has been "reinjected" by a new instance
        // This problem can be solved by specifying an appropriate scope for the injected bean.
        assertThat(url).hasPath("/" + DEPLOYMENT_NAME + "/");
    }

}
