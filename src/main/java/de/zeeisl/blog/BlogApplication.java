package de.zeeisl.blog;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import de.zeeisl.blog.entities.Article;
import de.zeeisl.blog.entities.User;
import de.zeeisl.blog.repositories.ArticleRepository;
import de.zeeisl.blog.repositories.UserRepository;
import de.zeeisl.blog.services.StorageService;

@SpringBootApplication
public class BlogApplication {

	@Autowired
	UserRepository userRepository;

	@Autowired
	ArticleRepository articleRepository;

	@Autowired
	PasswordEncoder passwordEncoder;

	@Autowired
	StorageService storageService;

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

			Article article = new Article();
			article.setTitle("Lorem ipsum dolor sit amet, consetetur sadipscing elitr");
			article.setBanner("/uploads/hd-wallpaper-gf2f4525d7_1920.jpg");
			article.setTeaser("teasert 123 123 123");
			article.setText(
					"Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam. Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam. Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam.");
			article.setCreateAt(new Date());
			article.setPublishDate(article.getCreateAt());
			articleRepository.save(article);

			storageService.init();
		};
	}

}
