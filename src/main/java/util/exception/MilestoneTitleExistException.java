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
public class MilestoneTitleExistException extends RuntimeException {

    /**
     * Creates a new instance of <code>MilestoneTitleExistException</code>
     * without detail message.
     */
    public MilestoneTitleExistException() {
    }

    /**
     * Constructs an instance of <code>MilestoneTitleExistException</code> with
     * the specified detail message.
     *
     * @param msg the detail message.
     */
    public MilestoneTitleExistException(String msg) {
        super(msg);
    }
}
