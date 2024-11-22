package banquemisr.challenge05.service;

import banquemisr.challenge05.dto.TaskDTO;
import banquemisr.challenge05.entity.LoTaskStatus;
import banquemisr.challenge05.entity.Task;
import banquemisr.challenge05.entity.User;
import banquemisr.challenge05.repo.LoTaskStatusRepo;
import banquemisr.challenge05.repo.TaskRepo;
import banquemisr.challenge05.repo.UserRepo;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TaskService {
    @Autowired
    TaskRepo taskRepo;
    @Autowired
    ModelMapper modelMapper;
    @Autowired
    LoTaskStatusRepo loTaskStatusRepo;
    @Autowired
    UserRepo userRepo;

    public Long createTask(TaskDTO taskDTO){
        Optional<LoTaskStatus> taskStatus = loTaskStatusRepo.findById(taskDTO.getStatusId());
        if(taskStatus.isEmpty())
            throw new RuntimeException("this is not a valid status");

        Task task = modelMapper.map(taskDTO, Task.class);
        task.setTaskId(null);
        task.setStatus(taskStatus.get());

        return taskRepo.save(task).getTaskId();
    }

    public TaskDTO getTask(Long taskId){
        TaskDTO taskDTO;
        Task task = taskRepo.findById(taskId).orElse(null);
        if(task == null)
            return null;

        taskDTO = modelMapper.map(task, TaskDTO.class);

        Optional<User> user = userRepo.findById(task.getUserId());
        user.ifPresent(value -> taskDTO.setUserName(value.getName()));

        return taskDTO;
    }

    public void deleteTask(Long taskId){
        taskRepo.deleteById(taskId);
    }

    public TaskDTO updateTask(TaskDTO taskDTO){
        TaskDTO newTaskDto;
        if(taskDTO.getTaskId() == null)
            return null;

        Task task = taskRepo.findById(taskDTO.getTaskId()).orElse(null);
        if(task == null)
            throw new RuntimeException("this is not a valid task id");

        Optional<LoTaskStatus> taskStatus = loTaskStatusRepo.findById(taskDTO.getStatusId());
        if(taskStatus.isEmpty())
            throw new RuntimeException("this is not a valid status");

        Optional<User> user = userRepo.findById(task.getUserId());

        task = modelMapper.map(taskDTO, Task.class);
        task.setStatus(taskStatus.get());

        task.setUserId(user.get().getUserId());
        task = taskRepo.save(task);

        newTaskDto = modelMapper.map(task, TaskDTO.class);
        user.ifPresent(value -> newTaskDto.setUserName(value.getName()));

        return newTaskDto;
    }
}
