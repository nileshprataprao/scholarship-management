package edu.ua.scholarship.service;

import edu.ua.scholarship.dto.UserDto;
import edu.ua.scholarship.entity.Role;
import edu.ua.scholarship.entity.User;
import edu.ua.scholarship.repository.RoleRepository;
import edu.ua.scholarship.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleService roleService;

    public UserService(UserRepository userRepository,
                       RoleRepository roleRepository,
                       PasswordEncoder passwordEncoder,RoleService roleService) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleService = roleService;
    }

    public User createUser(UserDto userDto) {
        User user = convertFromDtoForCreate(userDto,new User());
        return userRepository.save(user);
    }

    public User updateUser(Long id, UserDto userDto) {

        User user =  findById(id);
        convertFromDtoForUpdate(userDto,user);

       return userRepository.save(user);
    }

    public User findById(Long id) {
        return userRepository.findById(id).stream().findFirst().orElse(null);
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public List<UserDto> findByEmailOrUsername(String email,String username) {

        return userRepository.findByEmailOrUsername(email,username).stream().map(this::convertEntityToDto).toList();
    }

    public List<UserDto> findAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream().map(this::convertEntityToDto)
                .toList();
    }

    private UserDto convertEntityToDto(User user){
        UserDto userDto = new UserDto();
        userDto.setFirstName(user.getFirstName());
        userDto.setUsername(user.getUsername());
        userDto.setLastName(user.getLastName());
        userDto.setEmail(user.getEmail());
        userDto.setId(user.getId());
        String role = user.getRole() != null ? user.getRole().getName() : "";
        userDto.setRole(role);
        userDto.setNetId(user.getNetId());
        userDto.setPhoneNumber(user.getPhoneNumber());
        return userDto;
    }

    private User convertFromDtoForCreate(UserDto userDto, User user){
        user.setUsername(userDto.getUsername());
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setEmail(userDto.getEmail());
        user.setNetId(userDto.getNetId());
        user.setSecurityQuestion1(userDto.getSecurityQuestion1());
        user.setSecurityQuestion2(userDto.getSecurityQuestion2());
        user.setAnswer1(userDto.getAnswer1());
        user.setAnswer2(userDto.getAnswer2());
        user.setPhoneNumber(userDto.getPhoneNumber());

        //encrypt the password once we integrate spring security
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        setUserRole(userDto, user);
        return user;
    }

    private void setUserRole(UserDto userDto, User user) {
        final String roleName = userDto.getRole();
        Role role = roleRepository.findByName(roleName);
        if(role == null){
            role = roleService.createRole(roleName);
        }
        user.setRole(role);
    }


    private void convertFromDtoForUpdate(UserDto userDto,User user){
       if(userDto.getFirstName() != null){
           user.setFirstName(userDto.getFirstName());
       }
       if(userDto.getLastName() != null){
           user.setLastName(userDto.getLastName());
       }
       if(userDto.getEmail() != null){
           user.setEmail(userDto.getEmail());
       }
       if(userDto.getPhoneNumber() != null){
           user.setPhoneNumber(userDto.getPhoneNumber());
       }
       if(userDto.getSecurityQuestion1() != null){
           user.setSecurityQuestion1(userDto.getSecurityQuestion1());
       }
       if(userDto.getSecurityQuestion2() != null){
           user.setSecurityQuestion2(userDto.getSecurityQuestion2());
       }
       if(userDto.getAnswer1() != null){
           user.setAnswer1(userDto.getAnswer1());
       }
       if(userDto.getAnswer2() != null){
           user.setAnswer2(userDto.getAnswer2());
       }
       if (userDto.getRole() != null){
           setUserRole(userDto, user);
       }
    }

}
