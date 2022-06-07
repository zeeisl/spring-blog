package de.zeeisl.blog;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import de.zeeisl.blog.entities.User;
import de.zeeisl.blog.repositories.UserRepository;

@SpringBootApplication
public class BlogApplication {
	
	@Autowired
    UserRepository userRepository;
	
    @Autowired
    PasswordEncoder passwordEncoder;

	public static void main(String[] args) {
		SpringApplication.run(BlogApplication.class, args);
	}

	@Bean
	public CommandLineRunner commandLineRunner() {
		return args -> {
			User u = new User();
			u.setUsername("zeeisl");
			u.setEmail("zi@asdf.de");
			u.setPassword(passwordEncoder.encode("password"));
			u.setCreatedAt(new Date());
			userRepository.save(u);
		};
	}

}
