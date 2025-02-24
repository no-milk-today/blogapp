package ru.practicum.spring.mvc.cars.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.practicum.spring.mvc.cars.dto.UserDto;
import ru.practicum.spring.mvc.cars.service.UserService;

import java.util.List;

@Controller
@RequestMapping("/users")
public class UserController {

    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @GetMapping
    public String users(Model model) {
        List<UserDto> users = service.findAll();
        model.addAttribute("users", users);

        return "users";
    }

    @PostMapping
    public String save(@ModelAttribute UserDto user) {
        service.addUser(user);

        return "redirect:/users";
    }

    @PostMapping(value = "/{id}", params = "_method=delete")
    public String delete(@PathVariable(name = "id") Long id) {
        service.deleteUser(id);

        return "redirect:/users";
    }
}
