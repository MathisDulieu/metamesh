package com.example.metamesh.controller;

import com.example.metamesh.model.Notification;
import com.example.metamesh.service.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping("/users/{userId}/notifications")
    @Operation(
            tags = {"Notifications"},
            summary = "Get notifications for a user",
            description = "Retrieves all notifications for a specific user, identified by their unique ID.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    public List<Notification> getNotifications(
            @Parameter(description = "The unique ID of the user whose notifications are to be retrieved.") @PathVariable String userId) {
        return notificationService.getNotifications(userId);
    }
}
