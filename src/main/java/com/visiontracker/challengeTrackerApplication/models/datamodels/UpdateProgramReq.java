package com.visiontracker.challengeTrackerApplication.models.datamodels;

import com.visiontracker.challengeTrackerApplication.models.db.Program;

import java.util.ArrayList;
import java.util.List;

public class UpdateProgramReq {
    private Program program;
    private Long userId;
    private List<Long> userIds;

    private Long userLoggedIn;

    private String startDate;
    private String targetCompletionDate;

    public UpdateProgramReq() {
        program = new Program();
        userIds = new ArrayList<>();
    }

    public UpdateProgramReq(Program program, Long userId, List<Long> userIds,
                            String startDate, String targetCompletionDate, Long userLoggedIn) {
        this();
        this.program = program;
        this.userId = userId;
        this.userIds = userIds;
        this.startDate = startDate;
        this.targetCompletionDate = targetCompletionDate;
        this.setUserLoggedIn(userLoggedIn);
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
    public Long getUserId() {
        return userId;
    }

    /**
     * @param userId the userId to set
     */
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    /**
     * @return the userIds
     */
    public List<Long> getUserIds() {
        return userIds;
    }

    /**
     * @param userIds the userIds to set
     */
    public void setUserIds(List<Long> userIds) {
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

    public Long getUserLoggedIn() {
        return userLoggedIn;
    }

    public void setUserLoggedIn(Long userLoggedIn) {
        this.userLoggedIn = userLoggedIn;
    }
}
