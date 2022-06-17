package de.zeeisl.blog.auth;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.server.ResponseStatusException;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;

import de.zeeisl.blog.entities.User;

public class AuthContainer {

    public AuthContainer() {
    }

    private Authentication getAuth(){
        return SecurityContextHolder.getContext().getAuthentication();
    }

    public boolean isUserLoggedIn() {
        return this.getAuth().isAuthenticated() && !(this.getAuth().getPrincipal() instanceof String);
    }

    public User getLoggedInUser() {
        if (!this.getAuth().isAuthenticated() || (this.getAuth().getPrincipal() instanceof String)) {
            return null;
        }

        return (User) this.getAuth().getPrincipal();
    }

    public User getLoggedInUserOrFail() {
        if (!this.getAuth().isAuthenticated() || (this.getAuth().getPrincipal() instanceof String)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        return (User) this.getAuth().getPrincipal();
    }
}
