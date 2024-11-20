package com.example.metamesh.service;

import com.example.metamesh.dao.MediaDao;
import com.example.metamesh.model.Media;
import com.example.metamesh.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MediaService {

    private final MediaDao mediaDao;
    private final JwtTokenService jwtToken;

    public Media uploadMedia(MultipartFile file) {
        User tokenPeople = jwtToken.resolveTokenFromRequest();
        if (tokenPeople == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }

        //TODO Upload le fichier sur azure

        String url = "";
        Media media = Media.builder()
                .id(UUID.randomUUID().toString())
                .url(url)
                .build();

        mediaDao.save(media);
        return media;
    }

    public String getMedia(String mediaId) {
        User tokenPeople = jwtToken.resolveTokenFromRequest();
        if (tokenPeople == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }

        Media media = mediaDao.findById(mediaId);

        return media.getUrl();
    }

}
