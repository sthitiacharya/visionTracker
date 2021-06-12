package com.visiontracker.challengeTrackerApplication.helper;

import com.visiontracker.challengeTrackerApplication.models.db.Milestone;
import com.visiontracker.challengeTrackerApplication.models.db.Program;
import com.visiontracker.challengeTrackerApplication.models.db.User;

public class UtilClass {
    public UtilClass() {}

    public void clearUserLists(User user) {
        if (!user.getProgramsManaging().isEmpty())
        {
            user.getProgramsManaging().clear();
        }
        if (!user.getMilestoneList().isEmpty())
        {
            user.getMilestoneList().clear();
        }
        if (!user.getEnrolledPrograms().isEmpty())
        {
            user.getEnrolledPrograms().clear();
        }
        if (!user.getMilestonesCreated().isEmpty())
        {
            user.getMilestonesCreated().clear();
        }
    }

    public void clearProgramLists(Program program) {
        if (!program.getMilestoneList().isEmpty())
        {
            program.getMilestoneList().clear();
        }
        if (!program.getUserList().isEmpty())
        {
            program.getUserList().clear();
        }
    }

    public void clearMilestoneLists(Milestone milestone) {
        if (!milestone.getProgressHistories().isEmpty())
        {
            milestone.getProgressHistories().clear();
        }
    }
}
