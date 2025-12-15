package ru.practicum.server.user;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.dto.user.UserDto;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserMapper {

    public static UserDto toUserDto(User user) {
        UserDto userDto = new UserDto();

        userDto.setId(user.getId());
        userDto.setName(user.getName());
        userDto.setEmail(user.getEmail());
        return userDto;
    }

    public static User toUser(UserDto userDto) {
        User user = new User();
        user.setId(userDto.getId());
        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());
        user.setState(UserState.ACTIVE);
        user.setRegistrationDate(Instant.now());
        return user;
    }

    public static List<UserDto> mapToUserDto(Iterable<User> users) {
        List<UserDto> result = new ArrayList<>();

        for (User user : users) {
            result.add(toUserDto(user));
        }
        return result;
    }

}
