package com.example.metamesh.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Builder
public class Post {
    String id;
    String mediaId;
    String title;
    String content;
    String author;
    String authorId;
    Date createdAt;
}
