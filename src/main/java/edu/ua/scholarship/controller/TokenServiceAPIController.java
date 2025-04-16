package edu.ua.scholarship.controller;

import edu.ua.scholarship.dto.AuthRequest;
import edu.ua.scholarship.dto.AuthResponse;
import edu.ua.scholarship.exception.UserAlreadyExistException;
import edu.ua.scholarship.service.JwtUtil;
import edu.ua.scholarship.service.TokenService;
import edu.ua.scholarship.service.UserService;
import io.micrometer.observation.annotation.Observed;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class TokenServiceAPIController {

   private final TokenService tokenService;

@PostMapping(value = "/scm/user/token",produces = MediaType.APPLICATION_JSON_VALUE)
@Observed(name = "user.token", contextualName = "create-user-token")
public ResponseEntity<AuthResponse> authorize(@RequestBody AuthRequest authRequest) throws UserAlreadyExistException {

    return new ResponseEntity<>(tokenService.generateToken(authRequest), HttpStatus.CREATED);

}
}
