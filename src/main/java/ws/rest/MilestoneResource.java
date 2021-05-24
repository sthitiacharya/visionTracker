/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.rest;

import ejb.session.stateless.MilestoneSessionBeanLocal;
import ejb.session.stateless.UserEntitySessionBeanLocal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import util.exception.CreateNewMilestoneException;
import util.exception.MilestoneTitleExistException;
import ws.datamodel.CreateMilestoneReq;

/**
 * REST Web Service
 *
 * @author sthit
 */
@Path("Milestone")
public class MilestoneResource {

    UserEntitySessionBeanLocal userEntitySessionBean = lookupUserEntitySessionBeanLocal();

    MilestoneSessionBeanLocal milestoneSessionBean = lookupMilestoneSessionBeanLocal();
    

    @Context
    private UriInfo context;

    
    /**
     * Creates a new instance of MilestoneResource
     */
    public MilestoneResource() {
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createProgram(CreateMilestoneReq createMilestoneReq)
    {
        if(createMilestoneReq != null)
        {
            try
            {
                System.out.println("In createMilestone RESTful Web Service");
                Date creationDate = new Date();
                createMilestoneReq.getMilestone().setCreationDate(creationDate);
                Date date = new SimpleDateFormat("dd-MM-yyyy").parse(createMilestoneReq.getTargetCompletionDate());
                createMilestoneReq.getMilestone().setTargetCompletionDate(date);
                Long milestoneId = milestoneSessionBean.createMilestone(createMilestoneReq.getMilestone(), createMilestoneReq.getProgramId());
                System.out.println("********** MilestoneResource.createMilestone(): Milestone " + milestoneId + " details passed in via web service");
                
                return Response.status(Response.Status.OK).entity(milestoneId).build();
            }
            catch(CreateNewMilestoneException | MilestoneTitleExistException ex)
            {
                return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
            }
            catch(Exception ex)
            {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
            }
        }
        else
        {
            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid create new product request").build();
        }
    }
    
    private MilestoneSessionBeanLocal lookupMilestoneSessionBeanLocal() {
        try {
            javax.naming.Context c = new InitialContext();
            return (MilestoneSessionBeanLocal) c.lookup("java:global/ChallengeTrackerApplication/ChallengeTrackerApplication-ejb/MilestoneSessionBean!ejb.session.stateless.MilestoneSessionBeanLocal");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }

    private UserEntitySessionBeanLocal lookupUserEntitySessionBeanLocal() {
        try {
            javax.naming.Context c = new InitialContext();
            return (UserEntitySessionBeanLocal) c.lookup("java:global/ChallengeTrackerApplication/ChallengeTrackerApplication-ejb/UserEntitySessionBean!ejb.session.stateless.UserEntitySessionBeanLocal");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }

}
