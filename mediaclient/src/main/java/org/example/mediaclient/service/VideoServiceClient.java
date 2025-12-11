package org.example.mediaclient.service;

import net.devh.boot.grpc.client.inject.GrpcClient;
import org.example.lab.UploadVideoRequest;
import org.example.lab.Video;
import org.example.lab.VideoIdRequest;
import org.example.lab.VideoServiceGrpc;
import org.example.mediaclient.dto.VideoDto;
import org.example.mediaclient.mappers.VideoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VideoServiceClient {
    @GrpcClient("mediaserver")
    VideoServiceGrpc.VideoServiceBlockingStub stub;
    @Autowired
    private VideoMapper mapper;

    public VideoDto uploadVideo(UploadVideoRequest videoRequest) {
        Video video = stub.uploadVideo(videoRequest);
        VideoDto videoDto = mapper.fromVideoProtoToVideoDto(video);
        return videoDto;
    }
    // Nouvelle méthode pour récupérer une vidéo par ID
    public VideoDto getVideoById(String id) {
        VideoIdRequest request = VideoIdRequest.newBuilder().setId(id).build();
        Video video = stub.getVideo(request);
        return mapper.fromVideoProtoToVideoDto(video);
    }
}