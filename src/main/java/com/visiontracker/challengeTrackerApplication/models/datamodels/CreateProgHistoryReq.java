package com.visiontracker.challengeTrackerApplication.models.datamodels;

import com.visiontracker.challengeTrackerApplication.models.db.ProgressHistory;

public class CreateProgHistoryReq {

    private ProgressHistory progressHistory;
    private Long milestoneId;

    public CreateProgHistoryReq(ProgressHistory progressHistory, Long milestoneId)
    {
        this.setProgressHistory(progressHistory);
        this.setMilestoneId(milestoneId);
    }

    public ProgressHistory getProgressHistory() {
        return progressHistory;
    }

    public void setProgressHistory(ProgressHistory progressHistory) {
        this.progressHistory = progressHistory;
    }

    public Long getMilestoneId() {
        return milestoneId;
    }

    public void setMilestoneId(Long milestoneId) {
        this.milestoneId = milestoneId;
    }
}
