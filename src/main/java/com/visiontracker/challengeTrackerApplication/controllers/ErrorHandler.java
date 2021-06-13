package com.visiontracker.challengeTrackerApplication.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import util.exception.*;

import static org.springframework.http.HttpStatus.*;

@RestControllerAdvice
public class ErrorHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({RuntimeException.class})
    @ResponseStatus(INTERNAL_SERVER_ERROR)
    public void handleRunTimeException() { }

    @ExceptionHandler({UserNotFoundException.class, ProgramNotFoundException.class, MilestoneNotFoundException.class
            , ProgressHistoryNotFoundException.class, RewardNotFoundException.class})
    @ResponseStatus(NOT_FOUND)
    public ResponseEntity<Object> handleNotFoundException(Exception ex)
    {
        return new ResponseEntity<>(ex.getMessage(), NOT_FOUND);
    }

    @ExceptionHandler({CreateNewMilestoneException.class, CreateNewProgramException.class,
            MilestoneTitleExistException.class, ProgramTitleExistException.class, RedeemRewardException.class, UpdateMilestoneException.class,
            UpdateProgramException.class, UserUsernameExistException.class})
    @ResponseStatus(BAD_REQUEST)
    public ResponseEntity<Object> handleBadRequestException(Exception ex)
    {
        return new ResponseEntity<>(ex.getMessage(), BAD_REQUEST);
    }

    @ExceptionHandler({InvalidLoginCredentialException.class})
    @ResponseStatus(UNAUTHORIZED)
    public ResponseEntity<Object> handleUnauthorizedException(InvalidLoginCredentialException ex)
    {
        return new ResponseEntity<>(ex.getMessage(), UNAUTHORIZED);
    }

}
