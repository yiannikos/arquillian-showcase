package org.jboss.arquillian.junit.rules.arquillian.example;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.junit.runner.RunWith;

import java.io.File;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
@RunWith(Arquillian.class)
public abstract class AbstractTestCase {

    public static final String DEPLOYMENT_NAME = "cdi-test-rule";

    @Deployment
    public static WebArchive deploy() {
        final File[] assertJ = Maven.resolver().loadPomFromFile("pom.xml").resolve("org.assertj:assertj-core").withTransitivity().asFile();
        return ShrinkWrap.create(WebArchive.class, DEPLOYMENT_NAME + ".war")
            .addClasses(SimpleBean.class)
            .addAsLibraries(assertJ)
            .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
    }
}
