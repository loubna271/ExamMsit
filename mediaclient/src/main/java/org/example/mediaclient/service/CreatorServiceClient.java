package org.example.mediaclient.service;


import net.devh.boot.grpc.client.inject.GrpcClient;
import org.example.lab.CreatorIdRequest;
import org.example.lab.CreatorServiceGrpc;
import org.example.lab.VideoStream;
import org.example.mediaclient.dto.CreatorDto;
import org.example.mediaclient.dto.VideoDto;
import org.example.mediaclient.mappers.CreatorMapper;
import org.example.mediaclient.mappers.VideoMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CreatorServiceClient {

    @GrpcClient("mediaserver")
    private CreatorServiceGrpc.CreatorServiceBlockingStub stub;

    private final CreatorMapper creatorMapper;
    private final VideoMapper videoMapper;

    public CreatorServiceClient(CreatorMapper creatorMapper, VideoMapper videoMapper) {
        this.creatorMapper = creatorMapper;
        this.videoMapper = videoMapper;
    }

    public CreatorDto getCreator(String id) {
        CreatorIdRequest req = CreatorIdRequest.newBuilder()
                .setId(id)
                .build();

        org.example.lab.Creator proto = stub.getCreator(req);
        return creatorMapper.fromProtoToDto(proto);
    }

    public List<VideoDto> getCreatorVideos(String id) {
        CreatorIdRequest req = CreatorIdRequest.newBuilder()
                .setId(id)
                .build();

        VideoStream videoStream = stub.getCreatorVideos(req);

        return videoStream.getVideosList().stream()
                .map(videoMapper::fromProtoToDto)
                .collect(Collectors.toList());
    }
}