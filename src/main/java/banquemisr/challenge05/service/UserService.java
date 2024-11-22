package banquemisr.challenge05.service;

import banquemisr.challenge05.dto.UserDTO;
import banquemisr.challenge05.dto.UserDataDTO;
import banquemisr.challenge05.entity.User;
import banquemisr.challenge05.repo.UserRepo;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    @Autowired
    UserRepo userRepo;
    @Autowired
    ModelMapper modelMapper;

    public Long createUser(UserDTO userDTO){

        User user = modelMapper.map(userDTO, User.class);
        user.setUserId(null);

        return userRepo.save(user).getUserId();
    }

    public UserDTO getUser(Long userId){
        Optional<User> user = userRepo.findById(userId);
        if(user.isEmpty())
            return null;

        return modelMapper.map(user.get(), UserDTO.class);
    }

    public void deleteUser(Long userId){
        userRepo.deleteById(userId);
    }

    public UserDTO updateUser(UserDataDTO userDataDTO){
        if(userDataDTO.getUserId() == null)
            return null;
        User user = userRepo.findById(userDataDTO.getUserId()).orElse(null);
        if(user == null)
            throw new RuntimeException("this is not a valid user id");

        user.setName(userDataDTO.getName());
        user.setMobileNumber(userDataDTO.getMobileNumber());

        user = userRepo.save(user);
        return modelMapper.map(user, UserDTO.class);
    }
}
