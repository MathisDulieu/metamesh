package com.example.metamesh.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Builder
public class Comment {
    String commentId;
    String postId;
    String content;
    String username;
    Date createdAt;
}
