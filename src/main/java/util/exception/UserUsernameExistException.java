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
public class UserUsernameExistException extends Exception {

    /**
     * Creates a new instance of <code>UserUsernameExistException</code> without
     * detail message.
     */
    public UserUsernameExistException() {
    }

    /**
     * Constructs an instance of <code>UserUsernameExistException</code> with
     * the specified detail message.
     *
     * @param msg the detail message.
     */
    public UserUsernameExistException(String msg) {
        super(msg);
    }
}
