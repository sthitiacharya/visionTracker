package com.visiontracker.challengeTrackerApplication.repositories;

import com.visiontracker.challengeTrackerApplication.models.db.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRepository extends CrudRepository<User, Integer> {

    @Query("SELECT u FROM User u WHERE u.username = :username")
    public User findUserByUsername(@Param("username") String username);

    @Query("select u from User u where u.userId = :userId")
    public User findUserById(@Param("userId") Integer userId);

    @Query("select u from User u")
    public List<User> findAll();
}
