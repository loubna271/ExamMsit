package org.example.mediaserver.service;

import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;
import org.example.lab.CreatorIdRequest;
import org.example.lab.CreatorServiceGrpc;
import org.example.lab.Creator;
import org.example.lab.VideoStream;
import org.example.mediaserver.dao.repositories.CreatorRepository;
import org.example.mediaserver.mappers.CreatorMapper;
import org.example.mediaserver.mappers.VideoMapper;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@GrpcService

@Transactional(readOnly = true)  // Toutes les méthodes sont en lecture seule
public class CreatorService extends CreatorServiceGrpc.CreatorServiceImplBase {

    private final CreatorRepository creatorRepository;
    private final CreatorMapper creatorMapper;
    private final VideoMapper videoMapper;

    public CreatorService(CreatorRepository creatorRepository, CreatorMapper creatorMapper, VideoMapper videoMapper) {
        this.creatorRepository = creatorRepository;
        this.creatorMapper = creatorMapper;
        this.videoMapper = videoMapper;
    }

    @Override
    public void getCreator(CreatorIdRequest request, StreamObserver<Creator> responseObserver) {
        try {
            String creatorId = request.getId();

            if (creatorId == null || creatorId.isEmpty()) {
                responseObserver.onError(new IllegalArgumentException("Creator ID is required"));
                return;
            }

            Optional<org.example.mediaserver.dao.entities.Creator> creatorOpt =
                    creatorRepository.findById(creatorId);

            if (creatorOpt.isPresent()) {
                org.example.mediaserver.dao.entities.Creator creatorEntity = creatorOpt.get();
                Creator creatorProto = creatorMapper.fromEntityToProto(creatorEntity);
                responseObserver.onNext(creatorProto);
                responseObserver.onCompleted();
            } else {
                responseObserver.onError(new RuntimeException("Creator not found with id: " + creatorId));
            }

        } catch (Exception ex) {
            responseObserver.onError(ex);
        }
    }

    @Override
    public void getCreatorVideos(CreatorIdRequest request, StreamObserver<VideoStream> responseObserver) {
        try {
            String creatorId = request.getId();

            if (creatorId == null || creatorId.isEmpty()) {
                responseObserver.onError(new IllegalArgumentException("Creator ID is required"));
                return;
            }

            Optional<org.example.mediaserver.dao.entities.Creator> creatorOpt =
                    creatorRepository.findById(creatorId);

            if (creatorOpt.isPresent()) {
                org.example.mediaserver.dao.entities.Creator creatorEntity = creatorOpt.get();

                // Construire la réponse
                VideoStream.Builder streamBuilder = VideoStream.newBuilder();

                // Vérifier si les vidéos sont chargées (Lazy Loading)
                if (creatorEntity.getVideos() != null) {
                    creatorEntity.getVideos().forEach(video -> {
                        org.example.lab.Video videoProto = videoMapper.fromEntityToProto(video);
                        streamBuilder.addVideos(videoProto);
                    });
                }

                responseObserver.onNext(streamBuilder.build());
                responseObserver.onCompleted();

            } else {
                responseObserver.onError(new RuntimeException("Creator not found with id: " + creatorId));
            }

        } catch (Exception ex) {
            responseObserver.onError(ex);
        }
    }
}