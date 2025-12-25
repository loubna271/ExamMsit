package org.example.mediaserver.mappers;

import org.example.lab.Creator;
import org.example.lab.Video;
import org.springframework.stereotype.Component;

@Component
public class VideoMapper {

    private final CreatorMapper creatorMapper;

    public VideoMapper(CreatorMapper creatorMapper) {
        this.creatorMapper = creatorMapper;
    }

    public Video fromEntityToProto(org.example.mediaserver.dao.entities.Video entity) {
        try {
            if (entity == null) {
                System.out.println("VideoMapper: Entity is null");
                return Video.getDefaultInstance();
            }

            System.out.println("VideoMapper: Converting entity with id=" + entity.getId());
            System.out.println("VideoMapper: Creator is " + (entity.getCreator() != null ? "present" : "null"));

            Video.Builder builder = Video.newBuilder()
                    .setId(entity.getId() != null ? entity.getId() : "")
                    .setTitle(entity.getTitle() != null ? entity.getTitle() : "")
                    .setDescription(entity.getDescription() != null ? entity.getDescription() : "")
                    .setUrl(entity.getUrl() != null ? entity.getUrl() : "")
                    .setDurationSeconds(entity.getDurationSeconds() != null ? entity.getDurationSeconds() : 0);

            // Gestion du Creator
            if (entity.getCreator() != null) {
                System.out.println("VideoMapper: Converting creator with id=" + entity.getCreator().getId());
                Creator protoCreator = creatorMapper.fromEntityToProto(entity.getCreator());
                builder.setCreator(protoCreator);
            } else {
                System.out.println("VideoMapper: No creator, using default instance");
                builder.setCreator(Creator.getDefaultInstance());
            }

            Video result = builder.build();
            System.out.println("VideoMapper: Successfully built proto video");
            return result;

        } catch (Exception e) {
            System.err.println("VideoMapper ERROR: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

}