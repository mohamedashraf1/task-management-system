package banquemisr.challenge05.service;


import banquemisr.challenge05.constants.Constant;
import banquemisr.challenge05.dto.CreateUserDTO;
import banquemisr.challenge05.dto.JwtTokenDto;
import banquemisr.challenge05.dto.LoginDto;
import banquemisr.challenge05.dto.UserDTO;
import banquemisr.challenge05.entity.Role;
import banquemisr.challenge05.entity.User;
import banquemisr.challenge05.repo.RoleRepo;
import banquemisr.challenge05.repo.UserRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;


@Log4j2
@Service
@RequiredArgsConstructor
public class AuthService {
    @Autowired
    UserRepo userRepo;
    @Autowired
    RoleRepo roleRepo;
    @Autowired
    JwtService jwtService;
    @Autowired
    ModelMapper modelMapper;
    @Autowired
    PasswordEncoder passwordEncoder;

    public UserDTO signUp(CreateUserDTO userDTO) {
        User user = modelMapper.map(userDTO, User.class);

        String hashedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(hashedPassword);

        // default role is user
        Role r = roleRepo.findById(Constant.USER_ROLE).orElseThrow();
        List<Role> roles = new ArrayList<>();
        roles.add(r);
        user.setRoles(roles);

        user = userRepo.save(user);

        return modelMapper.map(user, UserDTO.class);
    }

    public UserDTO addAdminUser(Long userId) {
        User user = userRepo.findById(userId).orElse(null);
        if(user == null)
            throw new RuntimeException("User not found");

        Role r = roleRepo.findById(2L).orElseThrow();
        List<Role> roles = new ArrayList<>();
        roles.add(r);
        user.setRoles(roles);

        user = userRepo.save(user);

        return modelMapper.map(user, UserDTO.class);
    }

    public JwtTokenDto login(LoginDto loginDTO) {
        Optional<User> user = userRepo.findByEmail(loginDTO.getEmail());
        if (user.isEmpty())
            throw new RuntimeException("Invalid input credentials");

        boolean isCorrectPassword = verifyPassword(loginDTO.getPassword(), user.get().getPassword());
        if (!isCorrectPassword)
            throw new RuntimeException("Invalid input credentials");

        return jwtService.generateTokensByUser(user.get());
    }

    public JwtTokenDto refreshToken(String accessToken, String refreshToken) {

        try {
            if (jwtService.isTokenExpired(accessToken) || jwtService.isTokenExpired(refreshToken)) {
                throw new RuntimeException("Tokens are invalid");
            }
        } catch (Exception e) {
            throw new RuntimeException("Tokens are invalid");
        }

        return jwtService.generateTokens(jwtService.extractAllClaimsExpired(accessToken));
    }

    private Boolean verifyPassword(String password, String hashedPassword) {
        if (hashedPassword != null && password != null) {
            return passwordEncoder.matches(password, hashedPassword);
        }
        return false;
    }

}
