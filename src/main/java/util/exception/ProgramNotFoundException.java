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
@ResponseStatus(HttpStatus.NOT_FOUND)
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
