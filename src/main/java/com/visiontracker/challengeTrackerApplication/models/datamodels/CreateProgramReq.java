/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visiontracker.challengeTrackerApplication.models.datamodels;


import com.visiontracker.challengeTrackerApplication.models.db.Program;

import java.util.List;

/**
 *
 * @author sthit
 */
public class CreateProgramReq {
    private Program program;
    private Integer userId;
    private List<Integer> userIds;
    
    private String startDate;
    private String targetCompletionDate;
    
    public CreateProgramReq() {
    }

    public CreateProgramReq(Program program, Integer userId, List<Integer> userIds, String startDate, String targetCompletionDate) {
        this.program = program;
        this.userId = userId;
        this.userIds = userIds;
        this.startDate = startDate;
        this.targetCompletionDate = targetCompletionDate;
    }

    /**
     * @return the program
     */
    public Program getProgram() {
        return program;
    }

    /**
     * @param program the program to set
     */
    public void setProgram(Program program) {
        this.program = program;
    }

    /**
     * @return the userId
     */
    public Integer getUserId() {
        return userId;
    }

    /**
     * @param userId the userId to set
     */
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    /**
     * @return the userIds
     */
    public List<Integer> getUserIds() {
        return userIds;
    }

    /**
     * @param userIds the userIds to set
     */
    public void setUserIds(List<Integer> userIds) {
        this.userIds = userIds;
    }

    /**
     * @return the startDate
     */
    public String getStartDate() {
        return startDate;
    }

    /**
     * @param startDate the startDate to set
     */
    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    /**
     * @return the targetCompletionDate
     */
    public String getTargetCompletionDate() {
        return targetCompletionDate;
    }

    /**
     * @param targetCompletionDate the targetCompletionDate to set
     */
    public void setTargetCompletionDate(String targetCompletionDate) {
        this.targetCompletionDate = targetCompletionDate;
    }

}
