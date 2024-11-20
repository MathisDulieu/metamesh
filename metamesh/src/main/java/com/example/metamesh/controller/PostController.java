package com.example.metamesh.controller;

import com.example.metamesh.model.Post;
import com.example.metamesh.request.CreatePostAnswer;
import com.example.metamesh.request.UpdatePostRequest;
import com.example.metamesh.service.PostService;
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
public class PostController {

    private final PostService postService;

    @PostMapping("/posts")
    @Operation(
            summary = "Create a new post",
            description = "Creates a new post with a title, content, and optional media attachment.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    public CreatePostAnswer createPost(
            @RequestBody(
                    description = "Post object containing the details of the new post.",
                    content = @Content(
                            examples = {
                                    @ExampleObject(
                                            name = "Personal Blog Post",
                                            value = """
                                        {
                                            "title": "A Day in the Life of a Software Engineer",
                                            "content": "Today, I worked on optimizing a database query and learned some new tricks in Spring Boot!",
                                            "mediaId": "98765"
                                        }
                                        """
                                    ),
                                    @ExampleObject(
                                            name = "Travel Experience",
                                            value = """
                                        {
                                            "title": "Exploring the Alps",
                                            "content": "The mountains were breathtaking, and the hike was an unforgettable experience.",
                                            "mediaId": "54321"
                                        }
                                        """
                                    ),
                                    @ExampleObject(
                                            name = "Tech Announcement",
                                            value = """
                                        {
                                            "title": "Introducing MetaMesh v2.0",
                                            "content": "We are thrilled to announce the release of MetaMesh v2.0 with exciting new features.",
                                            "mediaId": null
                                        }
                                        """
                                    ),
                                    @ExampleObject(
                                            name = "Recipe Post",
                                            value = """
                                        {
                                            "title": "Homemade Pizza Recipe",
                                            "content": "Learn how to make delicious homemade pizza with a crispy crust and fresh toppings.",
                                            "mediaId": "12345"
                                        }
                                        """
                                    )
                            }
                    )
            )
            @org.springframework.web.bind.annotation.RequestBody Post postCreatePost) {
        return postService.createPost(postCreatePost);
    }


    @GetMapping("/posts/{postId}")
    @Operation(
            summary = "Get a post by ID",
            description = "Retrieves the details of a specific post by its unique ID.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    public Post getPost(
            @Parameter(description = "The unique ID of the post to retrieve.") @PathVariable String postId) {
        return postService.getPost(postId);
    }

    @PutMapping("/posts/{postId}")
    @Operation(
            summary = "Update a post",
            description = "Updates the details of an existing post, including its title, content, or media.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseEntity<Void> updatePost(
            @Parameter(description = "The unique ID of the post to update.") @PathVariable String postId,
            @RequestBody(
                    description = "Post object containing the updated details of the post.",
                    content = @Content(
                            examples = @ExampleObject(
                                    name = "Update Post Example",
                                    value = """
                                    {
                                        "title": "Updated title",
                                        "content": "Updated content.",
                                        "mediaId": "67890"
                                    }
                                    """
                            )
                    )
            )
            @org.springframework.web.bind.annotation.RequestBody UpdatePostRequest postUpdatePost) {
        return postService.updatePost(postId, postUpdatePost);
    }

    @DeleteMapping("/posts/{postId}")
    @Operation(
            summary = "Delete a post",
            description = "Deletes a specific post by its unique ID.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseEntity<Void> deletePost(
            @Parameter(description = "The unique ID of the post to delete.") @PathVariable String postId) {
        return postService.deletePost(postId);
    }

    @GetMapping("/users/{userId}/posts")
    @Operation(
            summary = "Get posts by user",
            description = "Retrieves all posts created by a specific user, identified by their unique ID.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    public List<Post> getUserPosts(
            @Parameter(description = "The unique ID of the user whose posts are to be retrieved.") @PathVariable String userId) {
        return postService.getUserPosts(userId);
    }

    @GetMapping("/search/posts")
    @Operation(
            summary = "Search posts",
            description = "Searches for posts that match the specified keyword in their title or content.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    public List<Post> searchPosts(
            @Parameter(description = "The keyword to search for in post titles or content.") @RequestParam String keyword) {
        return postService.searchPosts(keyword);
    }
}
