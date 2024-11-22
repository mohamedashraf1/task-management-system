package banquemisr.challenge05.controller;

import banquemisr.challenge05.dto.UserDTO;
import banquemisr.challenge05.dto.UpdateUserDTO;
import banquemisr.challenge05.errorhandling.GeneralResponse;
import banquemisr.challenge05.service.AuthService;
import banquemisr.challenge05.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    UserService userService;
    @Autowired
    AuthService authService;

    @GetMapping(params = "userId")
    public ResponseEntity<UserDTO> getUser(@RequestParam Long userId){
        return new ResponseEntity<>(userService.getUser(userId), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUser(){
        return new ResponseEntity<>(userService.getAllUsers(), HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<GeneralResponse> deleteUser(@RequestParam Long userId){
        userService.deleteUser(userId);
        return new ResponseEntity<>(new GeneralResponse("User deleted Successfully"), HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<UserDTO> updateUser(@RequestBody @Valid UpdateUserDTO userDTO){
        return new ResponseEntity<>(userService.updateUser(userDTO), HttpStatus.OK);
    }

    @PostMapping("/admin")
    public ResponseEntity<UserDTO> addAdminUser(@RequestParam Long userId) {
        UserDTO userDto = authService.addAdminUser(userId);
        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }
}
