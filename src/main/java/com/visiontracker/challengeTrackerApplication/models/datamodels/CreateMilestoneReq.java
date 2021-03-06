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
    private Long programId;
    private String targetCompletionDate;
    private Long assignedUserId;

    public CreateMilestoneReq()
    {
        milestone = new Milestone();
    }

    public CreateMilestoneReq(Milestone milestone, Long programId, String targetCompletionDate, Long assignedUserId) {
        this.milestone = milestone;
        this.programId = programId;
        this.targetCompletionDate = targetCompletionDate;
        this.setAssignedUserId(assignedUserId);
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
    public Long getProgramId() {
        return programId;
    }

    /**
     * @param programId the programId to set
     */
    public void setProgramId(Long programId) {
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


    public Long getAssignedUserId() {
        return assignedUserId;
    }

    public void setAssignedUserId(Long assignedUserId) {
        this.assignedUserId = assignedUserId;
    }
}
