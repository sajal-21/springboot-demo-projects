package com.example.demo1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public UserResponse getUser(long id) {
        UserEntity userEntity = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User not found with id " + id));
        return toUserResponse(userEntity);
    }

    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream().map(this::toUserResponse).toList();
    }

    public UserResponse saveUser(UserRequest userRequest) {
        UserEntity userEntity = new UserEntity();
        userEntity.setName(userRequest.getName());
        userEntity.setEmail(userRequest.getEmail());
        userRepository.save(userEntity);
        return toUserResponse(userEntity);
    }

    public UserResponse updateUser(long id, UserRequest userRequest) {
        UserEntity userEntity = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User not found with id " + id));
        userEntity.setName(userRequest.getName());
        userEntity.setEmail(userRequest.getEmail());
        userRepository.save(userEntity);
        return toUserResponse(userEntity);
    }

    public void deleteUser(long id) {
        UserEntity userEntity = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User not found with id " + id));
        userRepository.delete(userEntity);
    }

    private UserResponse toUserResponse(UserEntity userEntity) {
        UserResponse userResponse = new UserResponse();
        userResponse.setId(userEntity.getId());
        userResponse.setName(userEntity.getName());
        userResponse.setEmail(userEntity.getEmail());
        return userResponse;
    }

}
