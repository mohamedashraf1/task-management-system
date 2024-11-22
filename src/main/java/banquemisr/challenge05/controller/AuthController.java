package banquemisr.challenge05.controller;

import banquemisr.challenge05.constants.Constant;
import banquemisr.challenge05.dto.*;
import banquemisr.challenge05.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    @Autowired
    AuthService authService;

    @PostMapping("/sign-up")
    ResponseEntity<UserDTO> signUp(@RequestBody CreateUserDTO createUserDto) {
        UserDTO userDto = authService.signUp(createUserDto);
        return new ResponseEntity<>(userDto, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    ResponseEntity<JwtTokenDto> login(@RequestBody LoginDto loginDTO) {
        JwtTokenDto token = authService.login(loginDTO);
        return new ResponseEntity<>(token, HttpStatus.OK);
    }

    @PostMapping("/refresh-token")
    ResponseEntity<JwtTokenDto> refreshToken(@RequestHeader String authorization, @RequestBody RefreshDto refreshDto) {
        JwtTokenDto token = null;

        if (authorization != null && authorization.startsWith(Constant.AUTH_HEADER_PREFIX) && refreshDto.getRefreshToken() != null) {
            token = authService.refreshToken(authorization.replace(Constant.AUTH_HEADER_PREFIX, ""), refreshDto.getRefreshToken());
        }
        return new ResponseEntity<>(token, HttpStatus.OK);
    }

}
