package com.modyo.test.statemachine;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
@ComponentScan({"com.modyo.ms.commons", "com.modyo.test.statemachine"})
@EnableWebMvc
public class Application {

  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }

}
