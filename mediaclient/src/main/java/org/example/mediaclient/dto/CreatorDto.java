package org.example.mediaclient.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
public class CreatorDto {
    private String id;
    private String name;

    public String getId() {
        return id;
    }

    public CreatorDto() {
    }

    @Override
    public String toString() {
        return "CreatorDto{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                '}';
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private String email;
}