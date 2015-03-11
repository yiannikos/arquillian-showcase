package org.jboss.arquillian.showcase.universe;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.suite.Suite;
import org.jboss.arquillian.junit.suite.resolve.PackageResolverStrategy;
import org.jboss.arquillian.persistence.PersistenceTest;
import org.jboss.arquillian.warp.WarpTest;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.runner.RunWith;

@PersistenceTest @WarpTest
@Suite(value = "org.jboss.arquillian.showcase.universe*|.*(core|warp|drone|graphene|persistence).*Suite", strategy = PackageResolverStrategy.class)
@RunWith(Arquillian.class)
public class ApplicationSuite {

// @Deployment
// public static JavaArchive deploy() {
//     return Deployments.Backend.conference()
//           .addAsResource("datasets/conference_with_speaker.yml", "datasets/conference_with_speaker.yml");
// }

   @Deployment
   public static WebArchive deploy() {
       return Deployments.Client.full()
             .addAsResource("datasets/conference_with_speaker.yml", "datasets/conference_with_speaker.yml");
   }
}
   