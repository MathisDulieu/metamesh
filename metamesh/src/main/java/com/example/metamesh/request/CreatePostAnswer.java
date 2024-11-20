package com.example.metamesh.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Builder
public class CreatePostAnswer {
    String postId;
    Date datetime;
}
