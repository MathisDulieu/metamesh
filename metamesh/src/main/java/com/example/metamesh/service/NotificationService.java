package com.example.metamesh.service;

import com.example.metamesh.dao.NotificationDao;
import com.example.metamesh.dao.UserDao;
import com.example.metamesh.model.Notification;
import com.example.metamesh.model.User;
import com.example.metamesh.request.SendNotificationRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static com.example.metamesh.config.DateConfig.newDate;
import static java.util.Objects.isNull;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final JwtTokenService jwtToken;
    private final UserDao userDao;
    private final NotificationDao notificationDao;

    public List<Notification> getNotifications(String userId) {
        User tokenPeople = jwtToken.resolveTokenFromRequest();
        if (tokenPeople == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }

        User user = userDao.findById(userId);
        if(isNull(user)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        if(!Objects.equals(tokenPeople.getId(), userId)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }

        return notificationDao.getNotificationsByUserId(userId);
    }

    public void sendNotification(SendNotificationRequest notificationSendNotification) {
        //TODO Send real notification

        Notification notification = Notification.builder()
                .id(UUID.randomUUID().toString())
                .userId(notificationSendNotification.getUserId())
                .message(notificationSendNotification.getMessage())
                .createdAt(newDate())
                .build();

        notificationDao.save(notification);
    }

    public void deleteNotifications(String message) {
        notificationDao.removeNotifications(message);
    }
}
