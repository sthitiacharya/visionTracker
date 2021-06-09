package com.visiontracker.challengeTrackerApplication.repositories;

import com.visiontracker.challengeTrackerApplication.models.db.Program;
import com.visiontracker.challengeTrackerApplication.models.db.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.List;

@CrossOrigin(origins = "http://localhost:4200")
public interface UserRepository extends CrudRepository<User, Long> {

    @Query("SELECT u FROM User u WHERE u.username = :username")
    public User findUserByUsername(@Param("username") String username);

    @Query("select u from User u where u.userId = :userId")
    public User findUserById(@Param("userId") Long userId);

    @Query("select u from User u")
    List<User> findAll();

    @Query("select distinct pm.userId from ProgramMember pm where pm.programId = :programId")
    List<User> findUsersByProgramId(@Param("programId") Program programId);
}
