package com.example.metamesh.controller;

import com.example.metamesh.model.Media;
import com.example.metamesh.service.MediaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class MediaController {

    private final MediaService mediaService;

    @PostMapping(value = "/media/upload", consumes = "multipart/form-data")
    @Operation(
            tags = {"Media"},
            summary = "Upload media",
            description = "Uploads a media file (image, video, etc.) and returns metadata about the uploaded media.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    public Media uploadMedia(
            @Parameter(
                    description = "The media file to upload. Supported formats: JPEG, PNG, MP4, etc.",
                    required = true
            )
            @RequestParam("file") MultipartFile file) {
        return mediaService.uploadMedia(file);
    }

    @GetMapping("/media/{mediaId}")
    @Operation(
            tags = {"Media"},
            summary = "Retrieve media URL",
            description = "Retrieves the URL of a media file by its unique ID.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    public String getMedia(
            @Parameter(description = "The unique ID of the media file.") @PathVariable String mediaId) {
        return mediaService.getMedia(mediaId);
    }
}
