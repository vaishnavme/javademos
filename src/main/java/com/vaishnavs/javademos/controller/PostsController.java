package com.vaishnavs.javademos.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vaishnavs.javademos.dto.CreatePostRequestDto;
import com.vaishnavs.javademos.model.PostsEntity;
import com.vaishnavs.javademos.service.PostsService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/posts")
public class PostsController {

  @Autowired
  private PostsService postsService;

  // Create Post
  @PostMapping("")
  public ResponseEntity<PostsEntity> createPost(@Valid @RequestBody CreatePostRequestDto createPostRequestDto) {
    PostsEntity post = postsService.createPost(createPostRequestDto.getTitle(), createPostRequestDto.getContent());
    return ResponseEntity.ok(post);
  }

  // Get All Posts
  @GetMapping("")
  public ResponseEntity<List<PostsEntity>> getAllPosts() {
    List<PostsEntity> posts = postsService.getAllPosts();

    return ResponseEntity.ok(posts);
  }

  // Get Post By Id
  @GetMapping("/{postId}")
  public ResponseEntity<PostsEntity> getPostById(@PathVariable int postId) {
    PostsEntity post = postsService.getPostById(postId);
    return ResponseEntity.ok(post);
  }

  // Update Post by Id
  @PutMapping("/{postId}")
  public ResponseEntity<PostsEntity> updatePost(
      @PathVariable int postId,
      @Valid @RequestBody CreatePostRequestDto updatePostRequestDto) {

    PostsEntity updatedPost = postsService.updatePost(postId, updatePostRequestDto.getTitle(),
        updatePostRequestDto.getContent());

    return ResponseEntity.ok(updatedPost);
  }

  // Delete Post by Id
  @DeleteMapping("/{postId}")
  public ResponseEntity<Void> deletePost(@PathVariable int postId) {
    postsService.deletePost(postId);
    return ResponseEntity.noContent().build();
  }

  /**
   * Handling concurrency.
   * We are trying to update a post's like count concurrently by calling same
   * endpoint multiple times (10,000 times).
   * 
   * If we don't handle concurrency then, we get different like counts on each run
   * (less than 10,000).
   * 
   * 2 ways to handle concurrency:
   * 
   * 1. Optimistic Locking: Adding version field to the entity and using @Version
   * annotation
   * When multiple concurrent requests try to update the same post, they read the
   * same version number, but only the first one succeeds. The others fail because
   * the version has changed.
   * To handle this, we can implement retry logic, but it might lead to
   * performance issues and still throw exceptions if retries exceed. or too many
   * concurrent requests.
   * 
   * 2. Pessimistic Locking: Using synchronized block or method.
   * locks the row until transaction completes
   */

  @PutMapping("/{postId}/like")
  public ResponseEntity<PostsEntity> likePost(@PathVariable int postId) {
    PostsEntity likedPost = postsService.likePost(postId);
    return ResponseEntity.ok(likedPost);
  }
}
