package com.example.metamesh.service;

import com.example.metamesh.dao.CommentDao;
import com.example.metamesh.dao.PostDao;
import com.example.metamesh.dao.UserDao;
import com.example.metamesh.model.Comment;
import com.example.metamesh.model.Post;
import com.example.metamesh.model.User;
import com.example.metamesh.request.AddCommentAnswer;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.example.metamesh.config.DateConfig.newDate;
import static java.util.Objects.isNull;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentDao commentDao;
    private final PostDao postDao;
    private final UserDao userDao;
    private final JwtTokenService jwtToken;

    public AddCommentAnswer addComment(String postId, String content) {
        User tokenPeople = jwtToken.resolveTokenFromRequest();
        if (tokenPeople == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }

        Post existingPost = postDao.findById(postId);
        if (isNull(existingPost)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        User author = userDao.findByUsername(existingPost.getAuthor());
        if(author.isPrivate()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }

        String commentId = UUID.randomUUID().toString();
        Date createdAt = newDate();

        Comment comment = Comment.builder()
                .id(commentId)
                .postId(existingPost.getId())
                .username(tokenPeople.getUsername())
                .content(content.replace("\"", ""))
                .createdAt(createdAt)
                .build();

        commentDao.save(comment);

        return AddCommentAnswer.builder()
                .commentId(commentId)
                .createdAt(createdAt)
                .build();
    }

    public List<Comment> getComments(String postId) {
        User tokenPeople = jwtToken.resolveTokenFromRequest();
        if (tokenPeople == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }

        Post existingPost = postDao.findById(postId);
        if (isNull(existingPost)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        User author = userDao.findByUsername(existingPost.getAuthor());
        if (author.isPrivate()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }

        List<Comment> comments = commentDao.getCommentsByPostId(postId);

        return comments.stream()
                .sorted((c1, c2) -> c2.getCreatedAt().compareTo(c1.getCreatedAt()))
                .collect(Collectors.toList());
    }


    public ResponseEntity<Void> deleteComment(String commentId) {
        User tokenPeople = jwtToken.resolveTokenFromRequest();
        if (tokenPeople == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Comment existingComment = commentDao.findById(commentId);
        if (isNull(existingComment)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        if(!tokenPeople.isAdmin()) {
            if(!Objects.equals(existingComment.getUsername(), tokenPeople.getUsername())) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
        }

        commentDao.remove(existingComment);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
