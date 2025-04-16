package edu.ua.scholarship.service;

import edu.ua.scholarship.dto.AuthRequest;
import edu.ua.scholarship.dto.AuthResponse;
import edu.ua.scholarship.exception.InvalidPasswordException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class TokenService {

    private  final UserDetailsService userDetailsService;

    public AuthResponse generateToken(AuthRequest authRequest) {
        final UserDetails userDetails = userDetailsService.loadUserByUsername(authRequest.getEmail());
        if(!BCrypt.checkpw(authRequest.getPassword(),userDetails.getPassword())){
            throw new InvalidPasswordException("Incorrect password");
        }

        return new AuthResponse(JwtUtil.generateToken(userDetails));


    }
}
