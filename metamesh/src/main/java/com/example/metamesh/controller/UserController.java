package com.example.metamesh.controller;

import com.example.metamesh.model.User;
import com.example.metamesh.request.LoginRequest;
import com.example.metamesh.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/auth/register")
    @Operation(
            summary = "User registration",
            description = "Registers a new user in the system with their username, email, and password. " +
                    "The password must meet security requirements."
    )
    public ResponseEntity<Void> register(
            @RequestBody(
                    description = "User object containing the registration details.",
                    content = @Content(
                            examples = {
                                    @ExampleObject(
                                            name = "John Doe Registration Example",
                                            value = """
                                        {
                                            "username": "johndoe",
                                            "email": "johndoe@example.com",
                                            "password": "StrongP@ssw0rd!"
                                        }
                                        """
                                    ),
                                    @ExampleObject(
                                            name = "Jane Smith Registration Example",
                                            value = """
                                        {
                                            "username": "janesmith",
                                            "email": "janesmith@example.com",
                                            "password": "AnotherStrongP@ssw0rd!"
                                        }
                                        """
                                    ),
                                    @ExampleObject(
                                            name = "Chris P. Bacon Registration Example",
                                            value = """
                                        {
                                            "username": "chrispbacon",
                                            "email": "chris.p.bacon@example.com",
                                            "password": "ThirdStrongP@ssw0rd!"
                                        }
                                        """
                                    ),
                                    @ExampleObject(
                                            name = "Alice Wonderland Registration Example",
                                            value = """
                                        {
                                            "username": "alicewonderland",
                                            "email": "alice.wonderland@example.com",
                                            "password": "WonderP@ssw0rd42!"
                                        }
                                        """
                                    ),
                                    @ExampleObject(
                                            name = "Bob Builder Registration Example",
                                            value = """
                                        {
                                            "username": "bobbuilder",
                                            "email": "bob.builder@example.com",
                                            "password": "CanWeFixIt123!"
                                        }
                                        """
                                    ),
                                    @ExampleObject(
                                            name = "Dora Explorer Registration Example",
                                            value = """
                                        {
                                            "username": "doraexplorer",
                                            "email": "dora.explorer@example.com",
                                            "password": "Backpack@2023"
                                        }
                                        """
                                    )
                            }
                    )
            )
            @org.springframework.web.bind.annotation.RequestBody User user) {
        return userService.register(user);
    }

    @PostMapping("/auth/login")
    @Operation(
            summary = "User login",
            description = "Authenticates a user using their email and password. Returns an access token upon successful login.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successful authentication, access token returned",
                            content = @Content(
                                    mediaType = "text/plain",
                                    examples = @ExampleObject(
                                            name = "Access Token Example",
                                            value = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid credentials or bad request"
                    )
            }
    )
    public String login(
            @RequestBody(
                    description = "User credentials for login.",
                    content = @Content(
                            examples = {
                                    @ExampleObject(
                                            name = "John Doe Login Example",
                                            value = """
                                        {
                                            "email": "johndoe@example.com",
                                            "password": "StrongP@ssw0rd!"
                                        }
                                        """
                                    ),
                                    @ExampleObject(
                                            name = "Jane Smith Login Example",
                                            value = """
                                        {
                                            "email": "janesmith@example.com",
                                            "password": "AnotherStrongP@ssw0rd!"
                                        }
                                        """
                                    ),
                                    @ExampleObject(
                                            name = "Chris P. Bacon Login Example",
                                            value = """
                                        {
                                            "email": "chris.p.bacon@example.com",
                                            "password": "ThirdStrongP@ssw0rd!"
                                        }
                                        """
                                    ),
                                    @ExampleObject(
                                            name = "Alice Wonderland Login Example",
                                            value = """
                                        {
                                            "email": "alice.wonderland@example.com",
                                            "password": "WonderP@ssw0rd42!"
                                        }
                                        """
                                    ),
                                    @ExampleObject(
                                            name = "Bob Builder Login Example",
                                            value = """
                                        {
                                            "email": "bob.builder@example.com",
                                            "password": "CanWeFixIt123!"
                                        }
                                        """
                                    ),
                                    @ExampleObject(
                                            name = "Dora Explorer Login Example",
                                            value = """
                                        {
                                            "email": "dora.explorer@example.com",
                                            "password": "Backpack@2023"
                                        }
                                        """
                                    )
                            }
                    )
            )
            @org.springframework.web.bind.annotation.RequestBody LoginRequest login) {
        return userService.login(login);
    }

    @PatchMapping("/users/{userId}/privacy")
    @Operation(
            summary = "Set account privacy",
            description = "Allows a user to toggle the privacy of their account (public or private).",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseEntity<Void> setPrivateAccount(
            @Parameter(description = "The unique ID of the user.")
            @PathVariable String userId,
            @RequestBody(
                    description = "True to make the account private, False to make it public.",
                    content = @Content(
                            examples = @ExampleObject(
                                    name = "Privacy Toggle Example",
                                    value = "true"
                            )
                    )
            )
            @org.springframework.web.bind.annotation.RequestBody boolean isPrivate) {
        return userService.setPrivateAccount(userId, isPrivate);
    }

    @PostMapping("/users/{userId}/subscribe")
    @Operation(
            summary = "Subscribe to a user",
            description = "Allows the authenticated user to subscribe to another user by their user ID.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseEntity<Void> subscribeToUser(
            @Parameter(description = "The unique ID of the user to subscribe to.")
            @PathVariable String userId) {
        return userService.subscribeToUser(userId);
    }

    @PostMapping("/users/{userId}/unsubscribe")
    @Operation(
            summary = "Unsubscribe from a user",
            description = "Allows the authenticated user to unsubscribe from another user by their user ID.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseEntity<Void> unsubscribeFromUser(
            @Parameter(description = "The unique ID of the user to unsubscribe from.")
            @PathVariable String userId) {
        return userService.unsubscribeFromUser(userId);
    }


}
