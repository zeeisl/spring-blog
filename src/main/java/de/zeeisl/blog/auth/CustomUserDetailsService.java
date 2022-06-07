package de.zeeisl.blog.auth;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import de.zeeisl.blog.entities.User;
import de.zeeisl.blog.repositories.UserRepository;

@Service("userDetailsService")
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(final String username)
            throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if(user == null)
                throw new UsernameNotFoundException(username);
        return user;
    }

}
