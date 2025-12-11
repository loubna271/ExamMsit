package org.example.mediaclient.controller;

import org.example.lab.Creator;
import org.example.lab.UploadVideoRequest;
import org.example.mediaclient.dto.VideoDto;
import org.example.mediaclient.dto.VideoRequestDto;
import org.example.mediaclient.service.VideoServiceClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/videos")
public class VideoController {

    @Autowired
    private VideoServiceClient videoService;

    @PostMapping
    public ResponseEntity<VideoDto> uploadVideo(@RequestBody VideoRequestDto requestDto) {
        // Convertir DTO -> gRPC request
        Creator creator = Creator.newBuilder()
                .setId(requestDto.getCreatorId())
                .setName(requestDto.getCreatorName())
                .setEmail(requestDto.getCreatorEmail())
                .build();

        UploadVideoRequest request = UploadVideoRequest.newBuilder()
                .setTitle(requestDto.getTitle())
                .setDescription(requestDto.getDescription())
                .setUrl(requestDto.getUrl())
                .setDurationSeconds(requestDto.getDurationSeconds())
                .setCreator(creator)
                .build();

        VideoDto videoDto = videoService.uploadVideo(request);
        return ResponseEntity.ok(videoDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<VideoDto> getVideo(@PathVariable String id) {
        VideoDto videoDto = videoService.getVideoById(id);
        return ResponseEntity.ok(videoDto);
    }

    // Version simplifiée pour tester (à supprimer après)
    @PostMapping("/test")
    public ResponseEntity<VideoDto> uploadTestVideo() {
        Creator creator = Creator.newBuilder()
                .setId("test-creator-001")
                .setName("Xproce")
                .setEmail("hirchoua.badr@gmail.com")
                .build();

        UploadVideoRequest request = UploadVideoRequest.newBuilder()
                .setTitle("gRPC 101")
                .setDescription("The gRPC 101 is an introductory course to master Grpc")
                .setUrl("https://github.com/badrhr/gRPC101")
                .setDurationSeconds(380)
                .setCreator(creator)
                .build();

        VideoDto videoDto = videoService.uploadVideo(request);
        return ResponseEntity.ok(videoDto);
    }
}