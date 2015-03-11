package org.jboss.arquillian.showcase.universe.persistence;

import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.suite.Suite;
import org.jboss.arquillian.junit.suite.resolve.ClassResolverStrategy;
import org.jboss.arquillian.persistence.PersistenceTest;
import org.junit.runner.RunWith;

@PersistenceTest
@Suite(value = "org.jboss.arquillian.showcase.universe.persistence.ConferenceRepositoryTestCase", strategy = ClassResolverStrategy.class)
@RunWith(Arquillian.class)
public class PersistenceSuite {

//   @Deployment
//   public static JavaArchive deploy() {
//       return Deployments.Backend.conference();
//   }
}
