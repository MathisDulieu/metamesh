package com.example.metamesh.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UpdatePostRequest {
    String title;
    String content;
    String mediaId;
}
