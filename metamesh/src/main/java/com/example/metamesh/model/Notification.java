package com.example.metamesh.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Builder
public class Notification {
    String id;
    Date createdAt;
    String userId;
    String message;
}
