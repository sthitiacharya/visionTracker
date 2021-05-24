/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.User;
import java.util.List;
import java.util.Set;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import util.exception.InputDataValidationException;
import util.exception.InvalidLoginCredentialException;
import util.exception.UnknownPersistenceException;
import util.exception.UserNotFoundException;
import util.exception.UserUsernameExistException;

/**
 *
 * @author sthit
 */
@Stateless
public class UserEntitySessionBean implements UserEntitySessionBeanLocal {
    
    @PersistenceContext(unitName = "ChallengeTrackerApplication-ejbPU")
    private EntityManager entityManager;
    
    private final ValidatorFactory validatorFactory;
    private final Validator validator;

    public UserEntitySessionBean() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }


    @Override
    public Long createNewUser(User newUserEntity) throws UserUsernameExistException, UnknownPersistenceException, InputDataValidationException {
        try {
            Set<ConstraintViolation<User>> constraintViolations = validator.validate(newUserEntity);
            if (!constraintViolations.isEmpty())
            {
                throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
            }
            entityManager.persist(newUserEntity);
            entityManager.flush();
            return newUserEntity.getUserId();
            
        } catch (PersistenceException ex) {
            //within the database, catch the constraint violation exception
            if (ex.getCause().getCause() != null && ex.getCause().getCause().getClass().getName().equals("java.sql.SQLIntegrityConstraintViolationException")) {
                throw new UserUsernameExistException();
            } else {
                throw new UnknownPersistenceException(ex.getMessage());
            }
        }
    }
    
    @Override
    public List<User> retrieveAllUsers()
    {
        Query query = entityManager.createNamedQuery("User.findAll");
        return query.getResultList();
    }
    
    @Override
    public User retrieveUserByUserId(Long userId) throws UserNotFoundException
    {
        User userEntity = entityManager.find(User.class, userId);
        
        if(userEntity != null)
        {
            return userEntity;
        }
        else
        {
            throw new UserNotFoundException("User ID " + userId + " does not exist!");
        }
    }
    
    @Override
    public User retrieveUserByUsername(String username) throws UserNotFoundException {       
        try {
            Query query = entityManager.createNamedQuery("User.findByUsername");
            query.setParameter("username", username);
            return (User) query.getSingleResult();
        } catch (NoResultException | NonUniqueResultException ex) {
            throw new UserNotFoundException("User Username " + username + " does not exist!");
        }
    }

    @Override
    public User userLogin(String username, String password) throws InvalidLoginCredentialException {
        try {
            User userEntity = retrieveUserByUsername(username);
            if (userEntity.getPassword().equals(password)) {
                userEntity.getEnrolledPrograms().size();
                userEntity.getMilestoneList().size();
                userEntity.getMilestonesCreated().size();
                userEntity.getProgramsManaging().size();
                return userEntity;
            } else {
                throw new InvalidLoginCredentialException("Username does not exist or invalid password!");
            }
        } catch (UserNotFoundException ex) {
            throw new InvalidLoginCredentialException("Username does not exist or invalid password!");
        }
    }
    
    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<User>> constraintViolations) {
        String msg = "Input data validation error:";

        for (ConstraintViolation constraintViolation : constraintViolations) {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }
        return msg;
    }
}
