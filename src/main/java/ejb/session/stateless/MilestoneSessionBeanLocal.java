/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Milestone;
import javax.ejb.Local;
import util.exception.CreateNewMilestoneException;
import util.exception.InputDataValidationException;
import util.exception.MilestoneTitleExistException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author sthit
 */
@Local
public interface MilestoneSessionBeanLocal {

    public Long createMilestone(Milestone newMilestone, Long programId) throws MilestoneTitleExistException, UnknownPersistenceException, CreateNewMilestoneException, InputDataValidationException;
    
}
