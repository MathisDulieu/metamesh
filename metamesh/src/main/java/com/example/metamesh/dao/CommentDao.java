package com.example.metamesh.dao;

import com.example.metamesh.model.Comment;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CommentDao {

    private final MongoTemplate mongoTemplate;

    public void save(Comment comment) {
        mongoTemplate.save(comment, "comments");
    }

    public List<Comment> getCommentsByPostId(String postId) {
        return mongoTemplate.find(new Query(Criteria.where("postId").is(postId)), Comment.class, "comments");
    }

    public Comment findById(String commentId) {
        return mongoTemplate.findById(commentId, Comment.class, "comments");
    }

    public void remove(Comment comment) {
        mongoTemplate.remove(comment, "comments");
    }

    public void removeCommentsByPostId(String postId) {
        Query query = new Query(Criteria.where("postId").is(postId));
        mongoTemplate.remove(query, Comment.class, "comments");
    }

}
