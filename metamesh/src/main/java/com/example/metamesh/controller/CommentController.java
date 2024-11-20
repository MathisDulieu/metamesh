package com.example.metamesh.controller;

import com.example.metamesh.model.Comment;
import com.example.metamesh.request.AddCommentAnswer;
import com.example.metamesh.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/posts/{postId}/comments")
    @Operation(
            summary = "Add a comment to a post",
            description = "Adds a new comment to a specific post, identified by its unique ID.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    public AddCommentAnswer addComment(
            @Parameter(description = "The unique ID of the post to which the comment will be added.") @PathVariable String postId,
            @RequestBody(
                    description = "The content of the comment.",
                    content = @Content(
                            examples = {
                                    @ExampleObject(
                                            name = "General Feedback",
                                            value = "Great post! I really enjoyed reading this."
                                    ),
                                    @ExampleObject(
                                            name = "Suggestion",
                                            value = "Have you considered adding more details about this topic?"
                                    ),
                                    @ExampleObject(
                                            name = "Personal Experience",
                                            value = "I had a similar experience last year, and I completely agree with your point of view."
                                    ),
                                    @ExampleObject(
                                            name = "Question",
                                            value = "Could you clarify what you meant by the third paragraph?"
                                    ),
                                    @ExampleObject(
                                            name = "Appreciation",
                                            value = "Thanks for sharing this! It was very insightful."
                                    ),
                                    @ExampleObject(
                                            name = "Critique",
                                            value = "I appreciate the effort, but I think this post could use more factual references."
                                    ),
                                    @ExampleObject(
                                            name = "Encouragement",
                                            value = "Keep up the great work! Looking forward to more posts like this."
                                    ),
                                    @ExampleObject(
                                            name = "Support",
                                            value = "I completely agree with your perspective and support your ideas."
                                    ),
                                    @ExampleObject(
                                            name = "Follow-Up",
                                            value = "Thanks for the information. Could you share more about how this applies to recent events?"
                                    ),
                                    @ExampleObject(
                                            name = "Friendly Comment",
                                            value = "Nice work! Your posts always brighten my day."
                                    )
                            }
                    )
            )
            @org.springframework.web.bind.annotation.RequestBody String content) {
        return commentService.addComment(postId, content);
    }

    @GetMapping("/posts/{postId}/comments")
    @Operation(
            summary = "Retrieve comments for a post",
            description = "Retrieves all comments for a specific post, identified by its unique ID.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    public List<Comment> getComments(
            @Parameter(description = "The unique ID of the post whose comments are to be retrieved.") @PathVariable String postId) {
        return commentService.getComments(postId);
    }

    @DeleteMapping("/comments/{commentId}")
    @Operation(
            summary = "Delete a comment",
            description = "Deletes a specific comment, identified by its unique ID.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseEntity<Void> deleteComment(
            @Parameter(description = "The unique ID of the comment to delete.") @PathVariable String commentId) {
        return commentService.deleteComment(commentId);
    }
}
