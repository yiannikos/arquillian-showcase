package org.jboss.arquillian.junit.rules.arquillian.example.container;

import org.jboss.arquillian.junit.rules.arquillian.example.AbstractTestCase;
import org.jboss.arquillian.junit.rules.arquillian.example.SimpleBean;
import org.jboss.arquillian.junit.rules.arquillian.example.rules.RuleWithArqResAndCDIInStatement;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;

import javax.inject.Inject;
import java.net.URL;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class ContainerRuleStatementTestCase extends AbstractTestCase {

    @Inject
    private SimpleBean bean;

    @ArquillianResource
    private URL url;

    @Rule
    public TestRule rule = new RuleWithArqResAndCDIInStatement();

    @Test
    public void should_inject_bean_and_url(){
        assertThat(bean).isNotNull();
        assertThat(url).hasPath("/" + DEPLOYMENT_NAME + "/");
    }

}
