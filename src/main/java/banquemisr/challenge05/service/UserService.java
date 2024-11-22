package banquemisr.challenge05.service;

import banquemisr.challenge05.dto.UserDTO;
import banquemisr.challenge05.dto.UpdateUserDTO;
import banquemisr.challenge05.entity.User;
import banquemisr.challenge05.repo.UserRepo;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static banquemisr.challenge05.util.Util.getLoggedUserId;
import static banquemisr.challenge05.util.Util.mapList;

@Service
public class UserService {
    @Autowired
    UserRepo userRepo;
    @Autowired
    ModelMapper modelMapper;


    public UserDTO getUser(Long userId){
        Optional<User> user = userRepo.findById(userId);
        if(user.isEmpty())
            return null;

        return modelMapper.map(user.get(), UserDTO.class);
    }

    public List<UserDTO> getAllUsers(){
        List<User> users = userRepo.findAll();

        return mapList(users, UserDTO.class);
    }

    public void deleteUser(Long userId){
        userRepo.deleteById(userId);
    }

    public UserDTO updateUser(UpdateUserDTO updateUserDTO){

        User user = userRepo.findById(getLoggedUserId()).orElse(null);
        if(user == null)
            throw new RuntimeException("this is not a valid user id");

        user.setName(updateUserDTO.getName());
        user.setMobileNumber(updateUserDTO.getMobileNumber());

        user = userRepo.save(user);
        return modelMapper.map(user, UserDTO.class);
    }
}
