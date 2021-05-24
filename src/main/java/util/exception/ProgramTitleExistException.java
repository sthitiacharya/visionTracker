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
public class ProgramTitleExistException extends Exception {

    /**
     * Creates a new instance of <code>ProgramTitleExistException</code> without
     * detail message.
     */
    public ProgramTitleExistException() {
    }

    /**
     * Constructs an instance of <code>ProgramTitleExistException</code> with
     * the specified detail message.
     *
     * @param msg the detail message.
     */
    public ProgramTitleExistException(String msg) {
        super(msg);
    }
}
