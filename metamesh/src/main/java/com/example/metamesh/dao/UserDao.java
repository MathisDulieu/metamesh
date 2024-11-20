package com.example.metamesh.dao;

import com.example.metamesh.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserDao {

    private final MongoTemplate mongoTemplate;

    public boolean isUserInDatabase(String username) {
        return mongoTemplate.exists(new Query(Criteria.where("username").is(username)),"users");
    }

    public void save(User user) {
        mongoTemplate.save(user,"users");
    }

    public User findByEmail(String email) {
        Query query = new Query(Criteria.where("email").is(email));
        return mongoTemplate.findOne(query, User.class, "users");
    }

    public User findByUsername(String username) {
        Query query = new Query(Criteria.where("username").is(username));
        return mongoTemplate.findOne(query, User.class, "users");
    }

    public User findById(String userId) {
        return mongoTemplate.findById(userId, User.class, "users");
    }
}
