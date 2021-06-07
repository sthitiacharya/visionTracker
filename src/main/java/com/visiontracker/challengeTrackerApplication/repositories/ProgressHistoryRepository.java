package com.visiontracker.challengeTrackerApplication.repositories;

import com.visiontracker.challengeTrackerApplication.models.db.ProgressHistory;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.List;

@CrossOrigin(origins = "http://localhost:4200")
public interface ProgressHistoryRepository extends CrudRepository<ProgressHistory, Long> {
    @Query("select p from ProgressHistory p where p.milestoneId = :milestoneId")
    List<ProgressHistory> findProgressHistoriesByMilestoneId(@Param("milestoneId") Long milestoneId);

    @Query("select p from ProgressHistory p where p.progressHistoryId = :progressHistoryId")
    ProgressHistory findProgressHistoryByProgressHistoryId(@Param("progressHistoryId") Long progressHistoryId);
}
