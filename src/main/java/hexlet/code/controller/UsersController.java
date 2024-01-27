package hexlet.code.controller;

import hexlet.code.dto.UserCreateDTO;
import hexlet.code.dto.UserDTO;
import hexlet.code.dto.UserUpdateDTO;
import hexlet.code.exception.ResourceNotFoundException;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import hexlet.code.repository.UserRepository;
import hexlet.code.mapper.UserMapper;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UsersController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserMapper userMapper;

    @GetMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public UserDTO show(@PathVariable Long id) {
        var user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User with id " + id + " not found"));
        return userMapper.map(user);
    }

    @GetMapping(path = "")
    @ResponseStatus(HttpStatus.OK)
    public List<UserDTO> index() {
        var users = userRepository.findAll();
        var result = users.stream()
                .map(userMapper::map)
                .toList();
        return result;
    }

    @PostMapping(path = "")
    @ResponseStatus(HttpStatus.CREATED)
    public UserDTO create(@Valid @RequestBody UserCreateDTO userData) {
        var user = userMapper.map(userData);
        userRepository.save(user);
        return userMapper.map(user);
    }

    @PutMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public UserDTO update(@Valid @RequestBody UserUpdateDTO userData, @PathVariable Long id) {
        var user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User with id " + id + " not found"));
        userRepository.save(user);
        return userMapper.map(user);
    }

}
