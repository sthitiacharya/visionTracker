/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.exception;

/**
 *
 * @author sthit
 */
public class CreateNewMilestoneException extends Exception {

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
