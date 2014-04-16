package org.jboss.arquillian.showcase.universe.graphene;

import javax.ejb.EJB;

import org.jboss.arquillian.showcase.universe.model.Conference;
import org.jboss.arquillian.showcase.universe.repository.ConferenceRepository;
import org.jboss.arquillian.warp.Inspection;
import org.jboss.arquillian.warp.servlet.AfterServlet;
import org.junit.Assert;

public class VerifyConference extends Inspection {
    
    private static final long serialVersionUID = 1L;

    private Conference conference;
    
    @EJB
    private ConferenceRepository manager;
    
    public VerifyConference(Conference conference) {
        this.conference = conference;
    }
    
    @AfterServlet
    public void wasInserted() {
        Conference stored = manager.get(conference.getId());
        
        Assert.assertEquals(conference.getName(), stored.getName());
        Assert.assertEquals(conference.getLocation(), stored.getLocation());
        Assert.assertEquals(conference.getDescription(), stored.getDescription());
    }
}
