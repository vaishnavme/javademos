package com.vaishnavs.javademos.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vaishnavs.javademos.model.PostsEntity;
import com.vaishnavs.javademos.repository.PostsRepository;

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
}
