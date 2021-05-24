/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.rest;

import ejb.session.stateless.ProgramSessionBeanLocal;
import entity.Program;
import entity.User;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import util.exception.CreateNewProgramException;
import util.exception.ProgramTitleExistException;
import ws.datamodel.CreateProgramReq;

/**
 * REST Web Service
 *
 * @author sthit
 */
@Path("Program")
public class ProgramResource {

    ProgramSessionBeanLocal programSessionBeanLocal = lookupProgramSessionBeanLocal();

    @Context
    private UriInfo context;

    
    /**
     * Creates a new instance of ProgramResource
     */
    public ProgramResource() {
        //programSessionBeanLocal = lookupProgramSessionBeanLocal();
    }

    @Path("retrieveAllPrograms")
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveAllPrograms()
    {
        try
        {
            List<Program> programs = programSessionBeanLocal.retrieveAllPrograms();
            
            for(Program program : programs)
            {
                program.getUserList().clear();
                program.getMilestoneList().clear();
                program.getProgramManager().getEnrolledPrograms().clear();
                program.getProgramManager().getMilestoneList().clear();
                program.getProgramManager().getMilestonesCreated().clear();
                program.getProgramManager().getProgramsManaging().clear();
            }
            
            GenericEntity<List<Program>> genericEntity = new GenericEntity<List<Program>>(programs) {
            };
            
            return Response.status(Status.OK).entity(genericEntity).build();
        }    
        catch(Exception ex)
        {
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }
    
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createProgram(CreateProgramReq createProgramReq)
    {
        if(createProgramReq != null)
        {
            try
            {
                System.out.println("In createProgram RESTful Web Service");
                //System.out.println("Program ID: " + createProgramReq.getProgram().getProgramId());
                //String startDate = (createProgramReq.getStartDate()).toString();
                Date date = new SimpleDateFormat("dd-MM-yyyy").parse(createProgramReq.getStartDate());
                createProgramReq.getProgram().setStartDate(date);
                //String targetCompletionDate = (createProgramReq.getTargetCompletionDate()).toString();
                Date date2 = new SimpleDateFormat("dd-MM-yyyy").parse(createProgramReq.getTargetCompletionDate());
                createProgramReq.getProgram().setTargetCompletionDate(date2);
                //Date date2 = new SimpleDateFormat("dd-MM-yyyy").parse(stringDate2);
                Long programId = programSessionBeanLocal.createProgram(createProgramReq.getProgram(), createProgramReq.getUserId(), createProgramReq.getUserIds());
                System.out.println("********** ProgramResource.createProgram(): Program " + programId + " details passed in via web service");
                return Response.status(Response.Status.OK).entity(programId).build();
            }
            catch(CreateNewProgramException | ProgramTitleExistException ex)
            {
                return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
            }
            catch (ParseException ex)
            {
                System.out.println("Date parsed incorrectly");
                return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
            }
            catch(Exception ex)
            {
                return Response.status(Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
            }
        }
        else
        {
            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid create new product request").build();
        }
    }
    
    private ProgramSessionBeanLocal lookupProgramSessionBeanLocal() {
        try {
            javax.naming.Context c = new InitialContext();
            return (ProgramSessionBeanLocal) c.lookup("java:global/ChallengeTrackerApplication/ChallengeTrackerApplication-ejb/ProgramSessionBean!ejb.session.stateless.ProgramSessionBeanLocal");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }
}
