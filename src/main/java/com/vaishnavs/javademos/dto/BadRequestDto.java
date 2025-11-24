package com.vaishnavs.javademos.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BadRequestDto {
  @Size(min = 5, max = 20, message = "Message must be between 5 and 20 characters")
  private String message;

  @NotBlank(message = "Title must not be blank")
  private String title;
}
