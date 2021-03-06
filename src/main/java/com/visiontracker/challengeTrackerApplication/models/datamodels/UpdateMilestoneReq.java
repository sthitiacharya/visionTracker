package com.visiontracker.challengeTrackerApplication.models.datamodels;

import com.visiontracker.challengeTrackerApplication.models.db.Milestone;

public class UpdateMilestoneReq {
    private Milestone milestone;
    private Long programId;
    private String targetCompletionDate;
    private Long assignedUserId;

    public UpdateMilestoneReq()
    {
        milestone = new Milestone();
    }

    public UpdateMilestoneReq(Milestone milestone, Long programId, String targetCompletionDate, Long assignedUserId) {
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
