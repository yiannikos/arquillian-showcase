package org.jboss.arquillian.showcase.universe.warp;

import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.suite.Suite;
import org.jboss.arquillian.junit.suite.resolve.ClassResolverStrategy;
import org.jboss.arquillian.persistence.PersistenceTest;
import org.jboss.arquillian.warp.WarpTest;
import org.junit.runner.RunWith;

@PersistenceTest @WarpTest
@Suite(value = "org.jboss.arquillian.showcase.universe.warp.ConferenceWebClientTestCase", strategy = ClassResolverStrategy.class)
@RunWith(Arquillian.class)
public class WarpSuite {

//   @Deployment
//   public static WebArchive deploy() {
//      return Deployments.Client.web()
//            .addClass(Deployments.class)
//            .addAsResource("datasets/conference_with_speaker.yml", "datasets/conference_with_speaker.yml");
//   }
}
