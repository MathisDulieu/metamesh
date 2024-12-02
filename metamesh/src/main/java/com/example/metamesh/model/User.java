package com.example.metamesh.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@Builder
public class User {
    String id;
    String username;
    String email;
    String password;
    boolean isPrivate;
    boolean isAdmin;
    List<Map<String,String>> subscriptions;
    List<Map<String,String>> subscribers;
}
