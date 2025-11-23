package com.vaishnavs.javademos.dto;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreatePostRequestDto {
  @Size(max = 120, message = "Title can have maximum 120 characters")
  private String title;

  @Size(max = 512, message = "Content can have maximum 512 characters")
  private String content;
}
