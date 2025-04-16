package edu.ua.scholarship.controller;

import edu.ua.scholarship.dto.UserDto;
import edu.ua.scholarship.entity.User;
import edu.ua.scholarship.exception.UnknownServiceException;
import edu.ua.scholarship.exception.UserAlreadyExistException;
import edu.ua.scholarship.service.UserService;
import io.micrometer.observation.annotation.Observed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserRegistrationAPIController {

  private final UserService userService;
    @Autowired
    public UserRegistrationAPIController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/scm/user")
    @Observed(name = "user.registration", contextualName = "create-user-registration")
    public ResponseEntity<User> registerUser(@RequestBody UserDto user) throws UserAlreadyExistException, UnknownServiceException {

        if(!userService.findByEmailOrUsername(user.getEmail(),user.getUsername()).isEmpty()) {
            throw new UserAlreadyExistException("User already exists");
        }

        return new ResponseEntity<>(this.userService.createUser(user), HttpStatus.CREATED);
    }

    @PostMapping("scm/user/{id}")
    @Observed(name = "user.registration", contextualName = "update-user-registration")
    public ResponseEntity<User> updateUser(@PathVariable("id")Long id, @RequestBody UserDto user) throws UserAlreadyExistException {

        return new ResponseEntity<>(this.userService.updateUser(id, user), HttpStatus.OK);
    }

    @GetMapping(value = "/scm/user" ,produces = MediaType.APPLICATION_JSON_VALUE)
    @Observed(name = "user.registration", contextualName = "get-all-registered-users")
    public ResponseEntity<List<UserDto>> getAllUsers() throws UserAlreadyExistException {

        return new ResponseEntity<>(this.userService.findAllUsers(), HttpStatus.OK);

    }


}
