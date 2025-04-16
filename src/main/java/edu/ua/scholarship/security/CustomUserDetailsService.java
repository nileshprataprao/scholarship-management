package edu.ua.scholarship.security;

import edu.ua.scholarship.entity.Role;
import edu.ua.scholarship.entity.User;
import edu.ua.scholarship.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    @Autowired
    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email);

        if (user != null) {
            return new org.springframework.security.core.userdetails.User(user.getEmail(),
                    user.getPassword(),
                    List.of(mapRolesToAuthority(user.getRole())));
        }else{
            throw new UsernameNotFoundException("Invalid username or password.");
        }
    }

    private GrantedAuthority mapRolesToAuthority(Role role) {
         return new SimpleGrantedAuthority(role.getName());
    }
}

