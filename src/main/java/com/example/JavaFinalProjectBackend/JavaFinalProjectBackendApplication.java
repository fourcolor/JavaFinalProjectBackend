package com.example.JavaFinalProjectBackend;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;

@SpringBootApplication
@RestController
public class JavaFinalProjectBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(JavaFinalProjectBackendApplication.class, args);
	}

	@GetMapping("/")
    public String sayHello() {
        return "Hello World";
    }
}
