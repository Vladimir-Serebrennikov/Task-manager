package hexlet.code.controller;

import hexlet.code.dto.UserDTO.UserCreateDTO;
import hexlet.code.dto.UserDTO.UserDTO;
import hexlet.code.dto.UserDTO.UserUpdateDTO;
import hexlet.code.exception.EntityAssociationException;
import hexlet.code.exception.ResourceNotFoundException;
import hexlet.code.repository.UserRepository;
import hexlet.code.repository.TaskRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import hexlet.code.mapper.UserMapper;
import hexlet.code.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UsersController {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final UserService userService;
    private final TaskRepository taskRepository;

    @GetMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public UserDTO show(@PathVariable Long id) {
        var user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User with id " + id + " not found"));
        return userMapper.map(user);
    }

    @GetMapping(path = "")
    public ResponseEntity<List<UserDTO>>  index() {
        var users = userService.getAll();
        return ResponseEntity.ok()
                .header("X-Total-Count", String.valueOf(users.size()))
                .body(users);
    }

    @PostMapping(path = "")
    @ResponseStatus(HttpStatus.CREATED)
    public UserDTO create(@Valid @RequestBody UserCreateDTO userData) {
        return userService.create(userData);
    }

    @PutMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public UserDTO update(@Valid @RequestBody UserUpdateDTO userData, @PathVariable Long id) {
        var user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User with id " + id + " not found"));
        userMapper.update(userData, user);
        userRepository.save(user);
        return userMapper.map(user);
    }

    @DeleteMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize(value = "@userService.findById(#id).getEmail() == authentication.name")
    public void delete(@PathVariable Long id) {
        var user = userRepository.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("User with id " + id + " not found"));
        if (user != null && taskRepository.findByAssigneeEmail(user.getEmail()).isPresent()) {
            throw new EntityAssociationException("You cannot delete a user with an assigned task");
        }
        if (user != null && !user.getEmail().equals(SecurityContextHolder.getContext().getAuthentication().getName())) {
            throw new EntityAssociationException("You can only delete your own user account");
        }
        userRepository.deleteById(id);
    }

}
