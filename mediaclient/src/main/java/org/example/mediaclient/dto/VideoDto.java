package org.example.mediclient.dto;



import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VideoDto {
    private String id;
    private String name;
    private String url;
    private String description;
    private CreatorDto creator;
}
