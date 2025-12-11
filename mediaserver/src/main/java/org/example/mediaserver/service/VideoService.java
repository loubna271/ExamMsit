package org.example.mediaserver.service;

import io.grpc.stub.StreamObserver;
import jakarta.transaction.Transactional;
import net.devh.boot.grpc.server.service.GrpcService;
import org.example.lab.UploadVideoRequest;
import org.example.lab.VideoIdRequest;
import org.example.lab.VideoServiceGrpc;
import org.example.lab.Video;
import org.example.mediaserver.dao.entities.Creator;
import org.example.mediaserver.dao.repositories.CreatorRepository;
import org.example.mediaserver.dao.repositories.VideoRepository;
import org.example.mediaserver.mappers.CreatorMapper;
import org.example.mediaserver.mappers.VideoMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;
import java.util.UUID;

@GrpcService
@Transactional
public class VideoService extends VideoServiceGrpc.VideoServiceImplBase {

    private final VideoRepository videoRepository;
    private final CreatorRepository creatorRepository;
    private final VideoMapper videoMapper;
    private final CreatorMapper creatorMapper;

    // Constructeur avec @Autowired
    @Autowired
    public VideoService(VideoRepository videoRepository,
                        CreatorRepository creatorRepository,
                        VideoMapper videoMapper,
                        CreatorMapper creatorMapper) {
        this.videoRepository = videoRepository;
        this.creatorRepository = creatorRepository;
        this.videoMapper = videoMapper;
        this.creatorMapper = creatorMapper;
    }

    @Override
    public void uploadVideo(UploadVideoRequest request, StreamObserver<Video> responseObserver) {
        try {
            // 1. Gérer le Creator (créer ou récupérer)
            Creator creatorEntity;
            org.example.lab.Creator protoCreator = request.getCreator();

            if (protoCreator != null && !protoCreator.getId().isEmpty()) {
                // Chercher d'abord si le creator existe
                Optional<Creator> existingCreator = creatorRepository.findById(protoCreator.getId());
                if (existingCreator.isPresent()) {
                    creatorEntity = existingCreator.get();
                } else {
                    // Créer un nouveau creator
                    creatorEntity = new Creator();
                    creatorEntity.setId(protoCreator.getId());
                    creatorEntity.setName(protoCreator.getName());
                    creatorEntity.setEmail(protoCreator.getEmail());
                    creatorEntity = creatorRepository.save(creatorEntity);
                }
            } else {
                // Si pas de creator dans la requête, créer un ID par défaut
                creatorEntity = new Creator();
                creatorEntity.setId(UUID.randomUUID().toString());
                creatorEntity.setName("Unknown Creator");
                creatorEntity.setEmail("unknown@example.com");
                creatorEntity = creatorRepository.save(creatorEntity);
            }

            // 2. Créer et sauvegarder la Video
            org.example.mediaserver.dao.entities.Video videoEntity = new org.example.mediaserver.dao.entities.Video();
            videoEntity.setId(UUID.randomUUID().toString());
            videoEntity.setTitle(request.getTitle());
            videoEntity.setDescription(request.getDescription());
            videoEntity.setUrl(request.getUrl());
            videoEntity.setDurationSeconds(request.getDurationSeconds());
            videoEntity.setCreator(creatorEntity);  // ASSOCIER LE CREATOR

            videoEntity = videoRepository.save(videoEntity);

            // 3. Convertir et renvoyer la réponse
            Video response = videoMapper.fromEntityToProto(videoEntity);
            responseObserver.onNext(response);
            responseObserver.onCompleted();

        } catch (Exception ex) {
            responseObserver.onError(ex);
        }
    }

    @Override
    public void getVideo(VideoIdRequest request, StreamObserver<Video> responseObserver) {
        try {
            Optional<org.example.mediaserver.dao.entities.Video> videoOpt =
                    videoRepository.findById(request.getId());

            if (videoOpt.isPresent()) {
                Video protoVideo = videoMapper.fromEntityToProto(videoOpt.get());
                responseObserver.onNext(protoVideo);
                responseObserver.onCompleted();
            } else {
                responseObserver.onError(new Exception("Video not found with id: " + request.getId()));
            }
        } catch (Exception ex) {
            responseObserver.onError(ex);
        }
    }
}