package org.example.mediaclient.mappers;

import org.example.lab.Creator;
import org.example.lab.Video;
import org.example.mediaclient.dto.CreatorDto;
import org.example.mediaclient.dto.VideoDto;
import org.springframework.stereotype.Component;

@Component
public class VideoMapper {

    private final CreatorMapper creatorMapper;

    public VideoMapper(CreatorMapper creatorMapper) {
        this.creatorMapper = creatorMapper;
    }

    public VideoDto fromProtoToDto(Video video) {
        if (video == null) {
            return null;
        }

        VideoDto dto = new VideoDto();
        dto.setId(video.getId());
        dto.setTitle(video.getTitle());  // Map title -> name
        dto.setDescription(video.getDescription());
        dto.setUrl(video.getUrl());
        dto.setDurationSeconds(video.getDurationSeconds());

        // Inclure le creator si présent
        if (video.hasCreator()) {
            CreatorDto creatorDto = creatorMapper.fromProtoToDto(video.getCreator());
            dto.setCreator(creatorDto);
        }

        return dto;
    }
    // Ajoute cette méthode pour compatibilité avec VideoServiceClient
    public VideoDto fromVideoProtoToVideoDto(Video video) {
        // Appelle simplement la méthode principale
        return fromProtoToDto(video);
    }
}