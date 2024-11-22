package banquemisr.challenge05.controller;

import banquemisr.challenge05.dto.UserDTO;
import banquemisr.challenge05.dto.UserDataDTO;
import banquemisr.challenge05.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    UserService userService;

    @PostMapping
    public ResponseEntity<Long> createUser(@RequestBody @Valid UserDTO userDTO){
        return new ResponseEntity<>(userService.createUser(userDTO), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<UserDTO> getUser(@RequestParam Long userId){
        return new ResponseEntity<>(userService.getUser(userId), HttpStatus.OK);
    }

    @DeleteMapping
    public void deleteUser(@RequestParam Long userId){
        userService.deleteUser(userId);
    }

    @PutMapping
    public ResponseEntity<UserDTO> updateUser(@RequestBody @Valid UserDataDTO userDTO){
        return new ResponseEntity<>(userService.updateUser(userDTO), HttpStatus.OK);
    }
}
