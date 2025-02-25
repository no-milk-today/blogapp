package ru.practicum.spring.mvc.cars.configuration;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = {"ru.practicum.spring.mvc.cars"})
@PropertySource("classpath:application.properties")
public class WebConfiguration implements WebMvcConfigurer {
}
