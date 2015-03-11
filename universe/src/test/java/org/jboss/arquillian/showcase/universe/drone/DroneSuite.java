package org.jboss.arquillian.showcase.universe.drone;

import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.suite.Suite;
import org.jboss.arquillian.junit.suite.resolve.PackageResolverStrategy;
import org.junit.runner.RunWith;

@Suite(value = "org.jboss.arquillian.showcase.universe.drone|.*ConferenceWebClientTestCase", strategy = PackageResolverStrategy.class)
@RunWith(Arquillian.class)
public class DroneSuite {

//   @Deployment(testable = false)
//   public static WebArchive deploy() {
//      return Deployments.Client.web();
//   }

}
