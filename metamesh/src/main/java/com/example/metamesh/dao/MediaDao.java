package com.example.metamesh.dao;

import com.example.metamesh.model.Media;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MediaDao {

    private final MongoTemplate mongoTemplate;

    public void save(Media media) {
        mongoTemplate.save(media,"media");
    }

    public Media findById(String mediaId) {
        return mongoTemplate.findById(mediaId, Media.class, "media");
    }
}
