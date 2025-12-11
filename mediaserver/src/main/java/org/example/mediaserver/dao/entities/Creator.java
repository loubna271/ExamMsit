package org.example.mediaserver.dao.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


@Entity
@Data
@Builder
@AllArgsConstructor


@Table(name = "creators")
public class Creator {
    @Id
    private String id;
    private String name;

    public Creator() {
    }

    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Creator{" +
                "id='" + id + '\'' +
                '}';
    }

    @OneToMany(mappedBy = "creator", fetch = FetchType.LAZY)
    private List<Video> videos = new ArrayList<>();

    public String getName() { return this.name; }
    public void setName(String name) {
        this.name = name;
    }

    public List<Video> getVideos() {
        return videos;
    }

    public void setVideos(List<Video> videos) {
        this.videos = videos;
    }

}