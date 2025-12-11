package org.example.mediaserver.mappers;

import org.example.mediaserver.dao.entities.Creator;
import org.springframework.stereotype.Component;

@Component
public class CreatorMapper {

    public org.example.lab.Creator fromEntityToProto(Creator entity) {
        if (entity == null) {
            return org.example.lab.Creator.getDefaultInstance();
        }

        return org.example.lab.Creator.newBuilder()
                .setId(entity.getId() != null ? entity.getId() : "")
                .setName(entity.getName() != null ? entity.getName() : "")
                .setEmail(entity.getEmail() != null ? entity.getEmail() : "")
                .build();
    }

    public Creator fromProtoToEntity(org.example.lab.Creator proto) {
        if (proto == null) {
            return null;
        }

        Creator entity = new Creator();
        entity.setId(proto.getId());
        entity.setName(proto.getName());
        entity.setEmail(proto.getEmail());
        return entity;
    }
}