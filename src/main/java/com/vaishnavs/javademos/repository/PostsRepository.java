package com.vaishnavs.javademos.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.vaishnavs.javademos.model.PostsEntity;

import jakarta.persistence.LockModeType;

public interface PostsRepository extends JpaRepository<PostsEntity, Integer> {

  @Lock(LockModeType.PESSIMISTIC_WRITE)
  @Query("SELECT p FROM PostsEntity p WHERE p.id = :id")
  Optional<PostsEntity> findByIdWithLock(@Param("id") int id);
}
