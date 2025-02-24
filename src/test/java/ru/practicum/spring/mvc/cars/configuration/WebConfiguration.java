package ru.practicum.spring.mvc.cars.configuration;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = {"ru.practicum.spring.mvc.cars"})
public class WebConfiguration {
}
