package org.example.mediaclient.controller;

import org.example.mediaclient.dto.CreatorDto;
import org.example.mediaclient.dto.VideoDto;
import org.example.mediaclient.service.CreatorServiceClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/creators")
public class CreatorController {

    private final CreatorServiceClient client;

    public CreatorController(CreatorServiceClient client) {
        this.client = client;
    }

    @GetMapping("/{id}")
    public ResponseEntity<CreatorDto> getCreator(@PathVariable String id) {
        CreatorDto creatorDto = client.getCreator(id);
        return ResponseEntity.ok(creatorDto);
    }

    @GetMapping("/{id}/videos")
    public ResponseEntity<List<VideoDto>> getCreatorVideos(@PathVariable String id) {
        List<VideoDto> videos = client.getCreatorVideos(id);
        return ResponseEntity.ok(videos);
    }
}