package com.tai.paysafe.repository;

import com.tai.paysafe.entities.User;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.Set;

public interface TestRepository {
    @Query(value = "select u  from User u")
    Optional<Set<User>> getAllUser();
}
