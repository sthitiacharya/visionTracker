/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 *
 * @author sthit
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class CreateNewMilestoneException extends RuntimeException {

    /**
     * Creates a new instance of <code>CreateNewMilestoneException</code>
     * without detail message.
     */
    public CreateNewMilestoneException() {
    }

    /**
     * Constructs an instance of <code>CreateNewMilestoneException</code> with
     * the specified detail message.
     *
     * @param msg the detail message.
     */
    public CreateNewMilestoneException(String msg) {
        super(msg);
    }
}
