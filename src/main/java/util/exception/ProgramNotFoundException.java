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
public class ProgramNotFoundException extends Exception {

    /**
     * Creates a new instance of <code>ProgramNotFoundException</code> without
     * detail message.
     */
    public ProgramNotFoundException() {
    }

    /**
     * Constructs an instance of <code>ProgramNotFoundException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public ProgramNotFoundException(String msg) {
        super(msg);
    }
}
