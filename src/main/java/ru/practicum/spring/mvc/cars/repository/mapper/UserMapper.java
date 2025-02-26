package ru.practicum.spring.mvc.cars.repository.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.spring.mvc.cars.domain.User;
import ru.practicum.spring.mvc.cars.dto.UserDto;

@Component
public class UserMapper {
    public UserDto toUserDto(User user) {
        return new UserDto(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getAge(),
                user.isActive()
        );
    }

    public User toUser(UserDto userDto) {
        User user = new User();
        user.setId(userDto.id());
        user.setFirstName(userDto.firstName());
        user.setLastName(userDto.lastName());
        user.setAge(userDto.age());
        user.setActive(userDto.active());
        return user;
    }
}
