package com.example.bussinessSystem.Mappers;

import com.example.bussinessSystem.Dto.UserResponse;
import com.example.bussinessSystem.entities.User;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserMapper {

    public UserResponse userToUserResponse(User user){
        UserResponse userResponse = new UserResponse();
        userResponse.setFirstName(user.getFirstName());
        userResponse.setLastName(user.getLastName());
        userResponse.setEmail(user.getEmail());
        userResponse.setPhoneNumber(user.getPhoneNumber());
        return userResponse;
    }

    public List<UserResponse> listUserToUserResponse(List<User> users) {
        List<UserResponse> usersRes = new ArrayList<>();
        for (User u : users){
            UserResponse userResponse = new UserResponse();
            userResponse.setFirstName(u.getFirstName());
            userResponse.setLastName(u.getLastName());
            userResponse.setEmail(u.getEmail());
            userResponse.setPhoneNumber(u.getPhoneNumber());
            usersRes.add(userResponse);
        }
        return usersRes;
    }
}
