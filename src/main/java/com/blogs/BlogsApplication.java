package com.blogs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BlogsApplication {

	public static void main(String[] args) {
		SpringApplication.run(BlogsApplication.class, args);
		System.out.println("hello world");
	}
}
