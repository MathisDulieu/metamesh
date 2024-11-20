package com.example.metamesh.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Builder
public class AddCommentAnswer {
    String commentId;
    Date createdAt;
}