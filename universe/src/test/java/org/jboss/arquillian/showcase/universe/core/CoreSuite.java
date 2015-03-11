package org.jboss.arquillian.showcase.universe.core;

import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.suite.Suite;
import org.jboss.arquillian.junit.suite.resolve.PackageResolverStrategy;
import org.junit.runner.RunWith;

@Suite(value = "org.jboss.arquillian.showcase.universe.core|.*Conference.*TestCase", strategy = PackageResolverStrategy.class)
@RunWith(Arquillian.class)
public class CoreSuite {

//   @Deployment
//   public static WebArchive deploy() {
//       return Deployments.Client.rest();
//   }
}
