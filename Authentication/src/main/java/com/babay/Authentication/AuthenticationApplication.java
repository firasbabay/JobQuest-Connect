package com.babay.Authentication;

import com.babay.Authentication.Model.Role;
import com.babay.Authentication.Model.User;
import com.babay.Authentication.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients(basePackages = {"com.babay.Authentication.proxy"})
public class AuthenticationApplication implements CommandLineRunner {

	@Autowired
	private UserRepository userRepository ;
	public static void main(String[] args) {
		SpringApplication.run(AuthenticationApplication.class, args);
	}



	public void run(String... args) throws Exception {
		User adminAccount = userRepository.findByRole(Role.ADMIN);
		if (null == adminAccount){
			User user = new User();
			user.setEmail("admin@gamil.com");
			user.setUsername("Firas007");
			user.setRole(Role.ADMIN);
			user.setPassword(new BCryptPasswordEncoder().encode("admin"));
			userRepository.save(user);
		}

	}

}
