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
public class CreateNewProgramException extends Exception {

    /**
     * Creates a new instance of <code>CreateNewProgramException</code> without
     * detail message.
     */
    public CreateNewProgramException() {
    }

    /**
     * Constructs an instance of <code>CreateNewProgramException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public CreateNewProgramException(String msg) {
        super(msg);
    }
}
