/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.rest;

import ejb.session.stateless.UserEntitySessionBeanLocal;
import entity.User;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import util.exception.InputDataValidationException;
import util.exception.InvalidLoginCredentialException;
import util.exception.UnknownPersistenceException;
import util.exception.UserUsernameExistException;
import ws.datamodel.LoginReq;

/**
 * REST Web Service
 *
 * @author sthit
 */
@Path("User")
public class UserResource {

    UserEntitySessionBeanLocal userEntitySessionBeanLocal = lookupUserEntitySessionBeanLocal();

    @Context
    private UriInfo context;
    
    

    /**
     * Creates a new instance of UserResource
     */
    public UserResource() {
        //userSessionBeanLocal = lookupUserEntitySessionBeanLocal();
    }

    @Path("register")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createUser(User newUser) {
        if (newUser == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid create new user request").build();
        }
        try {
            Long userEntityId = userEntitySessionBeanLocal.createNewUser(newUser);
            return Response.status(Response.Status.OK).entity(userEntityId).build();
        } catch (UserUsernameExistException | UnknownPersistenceException ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
        } catch (InputDataValidationException ex) {
            return Response.status(Status.UNAUTHORIZED).entity(ex.getMessage()).build();
        }        
    }
    
//    @Path("userLogin")
//    @GET
//    @Consumes(MediaType.TEXT_PLAIN)
//    @Produces(MediaType.APPLICATION_JSON)
//    public Response userLogin(@QueryParam("username") String username, @QueryParam("password") String password) {
//        try {
//            User userEntity = userEntitySessionBeanLocal.userLogin(username, password);
//            userEntity.getEnrolledPrograms().clear();
//            userEntity.getMilestoneList().clear();
//            userEntity.getMilestonesCreated().clear();
//            userEntity.getProgramsManaging().clear();
//            System.out.println("********** UserResource.userLogin(): User " + userEntity.getUsername() + " login remotely via web service");           
//            return Response.status(Status.OK).entity(userEntity).build();
//        } catch (InvalidLoginCredentialException ex) {
//            return Response.status(Status.UNAUTHORIZED).entity(ex.getMessage()).build();
//        } catch (Exception ex) {
//            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
//        }
//    }

    @Path("login")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response userLogin(LoginReq loginReq) {
        try {
            User userEntity = userEntitySessionBeanLocal.userLogin(loginReq.getUsername(), loginReq.getPassword());
            userEntity.getEnrolledPrograms().clear();
            userEntity.getMilestoneList().clear();
            userEntity.getMilestonesCreated().clear();
            userEntity.getProgramsManaging().clear();
            System.out.println("********** UserResource.userLogin(): User " + userEntity.getUsername() + " login remotely via web service");  
            
            return Response.status(Status.OK).entity(userEntity).build();
        } catch (InvalidLoginCredentialException ex) {
            return Response.status(Status.UNAUTHORIZED).entity(ex.getMessage()).build();
        } catch (Exception ex) {
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }
    
    @Path("retrieveAllUsers")
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveAllUsers()
    {
        try
        {
            List<User> users = userEntitySessionBeanLocal.retrieveAllUsers();
            
            for(User user : users)
            {
                /*for (Program program : user.getEnrolledPrograms())
                {
                    program.getMilestoneList().clear();
                    program.getUserList().clear();
                }*/
                user.getEnrolledPrograms().clear();
                /*for (Program program : user.getProgramsManaging())
                {
                    program.getMilestoneList().clear();
                    program.getUserList().clear();
                }*/
                user.getProgramsManaging().clear();
                user.getMilestoneList().clear();
                user.getMilestonesCreated().clear();
            }
            
            GenericEntity<List<User>> genericEntity = new GenericEntity<List<User>>(users) {
            };
            
            return Response.status(Status.OK).entity(genericEntity).build();
        }    
        catch(Exception ex)
        {
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
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
