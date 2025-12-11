package org.example.mediaclient.mappers;

import org.example.lab.Creator;
import org.example.mediaclient.dto.CreatorDto;
import org.springframework.stereotype.Component;

@Component
public class CreatorMapper {

    public CreatorDto fromProtoToDto(Creator proto) {
        CreatorDto dto = new CreatorDto();
        dto.setId(proto.getId());
        dto.setName(proto.getName());
        dto.setEmail(proto.getEmail());
        return dto;
    }
}

