package com.example.ems.repository;

import com.example.ems.entity.UserGroupEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserGroupRepository extends JpaRepository<UserGroupEntity, Integer> {

    Optional<UserGroupEntity> findByName(String name);
}
