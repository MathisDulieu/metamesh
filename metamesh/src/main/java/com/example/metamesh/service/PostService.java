package com.example.metamesh.service;

import com.example.metamesh.dao.CommentDao;
import com.example.metamesh.dao.PostDao;
import com.example.metamesh.dao.UserDao;
import com.example.metamesh.model.Post;
import com.example.metamesh.model.User;
import com.example.metamesh.request.CreatePostAnswer;
import com.example.metamesh.request.SendNotificationRequest;
import com.example.metamesh.request.UpdatePostRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static com.example.metamesh.config.DateConfig.newDate;
import static java.util.Objects.isNull;
import static org.apache.logging.log4j.util.Strings.isEmpty;

@Service
@RequiredArgsConstructor
public class PostService {

    private final JwtTokenService jwtToken;
    private final PostDao postDao;
    private final UserDao userDao;
    private final CommentDao commentDao;
    private final NotificationService notificationService;

    public CreatePostAnswer createPost(Post post) {
        User tokenPeople = jwtToken.resolveTokenFromRequest();
        if (tokenPeople == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }

        if (isEmpty(post.getContent()) || isEmpty(post.getTitle())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        String postId = UUID.randomUUID().toString();

        post.setCreatedAt(newDate());
        post.setId(postId);
        post.setAuthor(tokenPeople.getUsername());
        post.setAuthorId(tokenPeople.getId());

        if (!isNull(post.getMediaId())) {
            System.out.println("MediaID inserted");
            //TODO gérer cette partie
        }

        postDao.save(post);

        if (tokenPeople.getSubscribers() != null && !tokenPeople.getSubscribers().isEmpty()) {
            for (String subscriberId : tokenPeople.getSubscribers().keySet()) {
                User subscriber = userDao.findById(subscriberId);
                if (subscriber != null) {
                    notificationService.sendNotification(SendNotificationRequest.builder()
                            .message("New post from " + tokenPeople.getUsername() + ": " + post.getTitle())
                            .userId(subscriber.getId())
                            .build());
                }
            }
        }

        return CreatePostAnswer.builder()
                .datetime(newDate())
                .postId(postId)
                .build();
    }

    public Post getPost(String postId) {
        User tokenPeople = jwtToken.resolveTokenFromRequest();
        if (tokenPeople == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }

        Post post = postDao.findById(postId);
        if (isNull(post)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        User user = userDao.findByUsername(post.getAuthor());
        if (isNull(user)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        if (user.isPrivate() && !Objects.equals(user.getId(), tokenPeople.getId())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }

        return post;
    }

    public ResponseEntity<Void> updatePost(String postId, UpdatePostRequest postUpdateRequest) {
        User tokenPeople = jwtToken.resolveTokenFromRequest();
        if (tokenPeople == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Post existingPost = postDao.findById(postId);
        if (isNull(existingPost)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        if (!Objects.equals(existingPost.getAuthor(), tokenPeople.getUsername())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        boolean isUpdated = false;

        if (!isEmpty(postUpdateRequest.getTitle()) && !Objects.equals(existingPost.getTitle(), postUpdateRequest.getTitle())) {
            existingPost.setTitle(postUpdateRequest.getTitle());
            isUpdated = true;
        }

        if (!isEmpty(postUpdateRequest.getContent()) && !Objects.equals(existingPost.getContent(), postUpdateRequest.getContent())) {
            existingPost.setContent(postUpdateRequest.getContent());
            isUpdated = true;
        }

        if (!isNull(postUpdateRequest.getMediaId()) && !Objects.equals(existingPost.getMediaId(), postUpdateRequest.getMediaId())) {
            existingPost.setMediaId(postUpdateRequest.getMediaId());
            isUpdated = true;
        }

        if (!isUpdated) {
            return ResponseEntity.status(HttpStatus.NOT_MODIFIED).build();
        }

        postDao.save(existingPost);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    public ResponseEntity<Void> deletePost(String postId) {
        User tokenPeople = jwtToken.resolveTokenFromRequest();
        if (tokenPeople == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Post post = postDao.findById(postId);
        if (isNull(post)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        if(!tokenPeople.isAdmin()) {
            User user = userDao.findByUsername(post.getAuthor());
            if (isNull(user)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }

            if (!Objects.equals(user.getId(), tokenPeople.getId())) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
        }

        notificationService.deleteNotifications("New post from " + post.getAuthor() + ": " + post.getTitle());
        commentDao.removeCommentsByPostId(postId);
        postDao.remove(post);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    public List<Post> getUserPosts(String userId) {
        User tokenPeople = jwtToken.resolveTokenFromRequest();
        if (tokenPeople == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }

        User user = userDao.findById(userId);
        if (isNull(user)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        if(!tokenPeople.isAdmin()) {
            if (user.isPrivate() && !Objects.equals(user.getId(), tokenPeople.getId())) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
            }
        }

        return postDao.getUserPosts(user.getUsername());
    }

    public List<Post> searchPosts(String keyword) {
        User tokenPeople = jwtToken.resolveTokenFromRequest();
        if (tokenPeople == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }

        List<Post> allPosts;
        if (isEmpty(keyword)) {
            allPosts = postDao.findAllPosts();
        } else {
            allPosts = postDao.searchPostsByKeyword(keyword);
        }

        return allPosts.stream()
                .filter(post -> {
                    User author = userDao.findByUsername(post.getAuthor());
                    if (author == null) {
                        return false;
                    }
                    return !author.isPrivate() || Objects.equals(author.getId(), tokenPeople.getId());
                })
                .sorted((p1, p2) -> p2.getCreatedAt().compareTo(p1.getCreatedAt()))
                .toList();
    }

}
