package com.promotion.ssg_assignment1.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {
    @Query(value = "select u from User u where u.userId = :userId and u.deleted = false")
    Optional<User> getByUserId(Long userId);
}
