package com.example.advencedblog;

import com.example.advencedblog.model.User;
import com.example.advencedblog.model.UserType;
import com.example.advencedblog.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import java.util.Locale;

@SpringBootApplication
public class AdvencedblogApplication extends WebMvcConfigurerAdapter implements CommandLineRunner{
	public static void main(String[] args) {
		SpringApplication.run(AdvencedblogApplication.class, args);
	}

	@Autowired
	private UserRepository userRepository;

	@Bean
	public LocaleResolver localeResolver() {
		SessionLocaleResolver sessionLocaleResolver = new SessionLocaleResolver();
		sessionLocaleResolver.setDefaultLocale(Locale.US);
		return sessionLocaleResolver;
	}

	@Bean
	public LocaleChangeInterceptor localeChangeInterceptor() {
		LocaleChangeInterceptor localeChangeInterceptor = new LocaleChangeInterceptor();
		localeChangeInterceptor.setParamName("lang");
		return localeChangeInterceptor;
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(localeChangeInterceptor());
	}

	@Override
	public void run(String... strings) throws Exception {
		User user = userRepository.findUserByEmail("admin@mail.com");
		if (user == null) {
			User admin = User.builder()
					.name("admin")
					.surname("admin")
					.email("admin@mail.com")
					.password(new BCryptPasswordEncoder().encode("12"))
					.type(UserType.ADMIN).build();
			userRepository.save(admin);

		}
	}
}
