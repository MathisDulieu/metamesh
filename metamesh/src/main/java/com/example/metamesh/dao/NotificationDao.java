package com.example.metamesh.dao;

import com.example.metamesh.model.Notification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class NotificationDao {

    private final MongoTemplate mongoTemplate;

    public List<Notification> getNotificationsByUserId(String userId) {
        return mongoTemplate.find(new Query(Criteria.where("userId").is(userId)), Notification.class, "notifications");
    }

    public void removeNotifications(String message) {
        mongoTemplate.remove(new Query(Criteria.where("message").is(message)),"notifications");
    }

    public void save(Notification notification) {
        mongoTemplate.save(notification,"notifications");
    }
}
