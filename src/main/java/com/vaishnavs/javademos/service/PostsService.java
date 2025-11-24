package com.vaishnavs.javademos.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;

import com.vaishnavs.javademos.model.PostsEntity;
import com.vaishnavs.javademos.repository.PostsRepository;

import jakarta.transaction.Transactional;

@Service
public class PostsService {
  @Autowired
  private PostsRepository postsRepository;

  public PostsEntity getPostById(int postId) {
    return postsRepository.findById(postId).orElseThrow(() -> new RuntimeException("Post not found"));
  }

  public PostsEntity createPost(String title, String content) {
    PostsEntity post = new PostsEntity();

    post.setTitle(title);
    post.setContent(content);

    return postsRepository.save(post);
  }

  public PostsEntity updatePost(int postId, String title, String content) {
    PostsEntity post = postsRepository.findById(postId).orElseThrow(() -> new RuntimeException("Post not found"));

    if (title != null && !title.isEmpty()) {
      post.setTitle(title);
    }
    if (content != null && !content.isEmpty()) {
      post.setContent(content);
    }

    return postsRepository.save(post);
  }

  public List<PostsEntity> getAllPosts() {
    return postsRepository.findAll();
  }

  public void deletePost(int postId) {
    postsRepository.deleteById(postId);
  }

  // Optimistic locking using version field
  // @Transactional
  // public PostsEntity likePost(int postId) {
  // int maxRetries = 3;
  // int retryCount = 0;

  // while (retryCount < maxRetries) {
  // try {
  // PostsEntity post = this.getPostById(postId);
  // post.setLikesCount(post.getLikesCount() + 1);
  // return postsRepository.save(post);
  // } catch (ObjectOptimisticLockingFailureException e) {
  // retryCount++;
  // if (retryCount >= maxRetries) {
  // throw new RuntimeException("Failed to update post after " + maxRetries + "
  // retries", e);
  // }
  // // Small delay before retry
  // try {
  // Thread.sleep(50);
  // } catch (InterruptedException ie) {
  // Thread.currentThread().interrupt();
  // throw new RuntimeException("Thread interrupted during retry", ie);
  // }
  // }
  // }

  // throw new RuntimeException("Failed to like post");
  // }

  // Pessimistic Locking
  @Transactional
  public PostsEntity likePost(int postId) {
    PostsEntity post = postsRepository.findByIdWithLock(postId)
        .orElseThrow(() -> new RuntimeException("Post not found"));
    post.setLikesCount(post.getLikesCount() + 1);
    return postsRepository.save(post);
  }
}
