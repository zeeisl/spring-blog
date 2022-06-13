package de.zeeisl.blog.transitonObjects.user;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.web.multipart.MultipartFile;

import de.zeeisl.blog.entities.User;

public class EditUserdataForm {

    @NotNull
    @Size(min = 3, max = 60, message = "Name muss zwischen 3 und 60 zeichen lang sein.")
    private String name;

    private String profilePictureLink;
    
    private MultipartFile profilePicture;

    public EditUserdataForm() {

    }
    public void fromEntity(User user) {
        this.name = user.getUsername();
        this.profilePictureLink = user.getProfilePicture();
    }

    public void updateEntity(User user) {
        user.setUsername(this.getName());
        user.setProfilePicture(this.getProfilePictureLink());
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfilePictureLink() {
        return this.profilePictureLink;
    }

    public void setProfilePictureLink(String profilePictureLink) {
        this.profilePictureLink = profilePictureLink;
    }

    public MultipartFile getProfilePicture() {
        return this.profilePicture;
    }

    public void setProfilePicture(MultipartFile profilePicture) {
        this.profilePicture = profilePicture;
    }
}
