/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Program;
import java.util.List;
import javax.ejb.Local;
import util.exception.CreateNewProgramException;
import util.exception.InputDataValidationException;
import util.exception.ProgramNotFoundException;
import util.exception.ProgramTitleExistException;
import util.exception.UnknownPersistenceException;
import util.exception.UserNotFoundException;

/**
 *
 * @author sthit
 */
@Local
public interface ProgramSessionBeanLocal {
    public Long createProgram(Program program, Long programManagerId, List<Long> userIds) throws ProgramTitleExistException, UnknownPersistenceException, InputDataValidationException, CreateNewProgramException;
    public List<Program> retrieveAllPrograms();

    public Program retrieveProgramByProgramId(Long programId) throws ProgramNotFoundException;
}
