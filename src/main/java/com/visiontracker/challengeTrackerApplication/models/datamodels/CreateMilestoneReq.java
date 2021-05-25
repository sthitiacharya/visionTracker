/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visiontracker.challengeTrackerApplication.models.datamodels;


import com.visiontracker.challengeTrackerApplication.models.db.Milestone;

/**
 *
 * @author sthit
 */
public class CreateMilestoneReq {
    
    private Milestone milestone;
    private Integer programId;
    private String targetCompletionDate;

    public CreateMilestoneReq()
    {
        
    }
    public CreateMilestoneReq(Milestone milestone, Integer programId, String targetCompletionDate) {
        this.milestone = milestone;
        this.programId = programId;
        this.targetCompletionDate = targetCompletionDate;
    }

    /**
     * @return the milestone
     */
    public Milestone getMilestone() {
        return milestone;
    }

    /**
     * @param milestone the milestone to set
     */
    public void setMilestone(Milestone milestone) {
        this.milestone = milestone;
    }

    /**
     * @return the programId
     */
    public Integer getProgramId() {
        return programId;
    }

    /**
     * @param programId the programId to set
     */
    public void setProgramId(Integer programId) {
        this.programId = programId;
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
