package de.zeeisl.blog.controllers;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class RegisterForm {
    @NotNull
    @Size(min = 3, max = 60, message = "Name muss zwischen 3 und 60 zeichen lang sein.")
    private String username;

    @NotNull
    @Email(message = "Die E-Mail-Addresse ist ungültig.")
    private String email;

    @NotNull
    @Size(min = 8, max = 64, message = "Das Passwort muss zwischen 8 und 60 zeichen lang sein.")
    private String password;

    public RegisterForm() {

    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}