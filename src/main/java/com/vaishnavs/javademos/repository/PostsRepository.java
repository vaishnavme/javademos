package com.vaishnavs.javademos.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vaishnavs.javademos.model.PostsEntity;

public interface PostsRepository extends JpaRepository<PostsEntity, Integer> {
}
