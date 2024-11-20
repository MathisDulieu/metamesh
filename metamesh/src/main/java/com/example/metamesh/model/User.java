package com.example.metamesh.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

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
    List<String> subscriptions;
    List<String> subscribers;
}
