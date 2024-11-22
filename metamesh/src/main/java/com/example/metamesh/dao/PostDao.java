package com.example.metamesh.dao;

import com.example.metamesh.model.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class PostDao {

    private final MongoTemplate mongoTemplate;

    public Post findById(String postId) {
        return mongoTemplate.findById(postId, Post.class, "posts");
    }

    public void save(Post post) {
        mongoTemplate.save(post,"posts");
    }

    public void remove(Post post) {
        mongoTemplate.remove(post, "posts");
    }

    public List<Post> getUserPosts(String username) {
        return mongoTemplate.find(new Query(Criteria.where("author").is(username)), Post.class, "posts");
    }

    public List<Post> searchPostsByKeyword(String keyword) {
        Query query = new Query(Criteria.where("title").regex(keyword, "i"));
        return mongoTemplate.find(query, Post.class, "posts");
    }

    public List<Post> findAllPosts() {
        return mongoTemplate.findAll(Post.class, "posts");
    }
}
