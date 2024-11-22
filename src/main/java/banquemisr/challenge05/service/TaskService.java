package banquemisr.challenge05.service;

import banquemisr.challenge05.dto.TaskDTO;
import banquemisr.challenge05.entity.LoTaskStatus;
import banquemisr.challenge05.entity.Task;
import banquemisr.challenge05.repo.LoTaskStatusRepo;
import banquemisr.challenge05.repo.TaskRepo;
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
        Optional<Task> task = taskRepo.findById(taskId);
        if(task.isEmpty())
            return null;

        return modelMapper.map(task.get(), TaskDTO.class);
    }

    public void deleteTask(Long taskId){
        taskRepo.deleteById(taskId);
    }

    public TaskDTO updateTask(TaskDTO taskDTO){
        if(taskDTO.getTaskId() == null)
            return new TaskDTO();
        Task task = taskRepo.findById(taskDTO.getTaskId()).orElse(null);
        if(task == null)
            throw new RuntimeException("this is not a valid task id");

        Optional<LoTaskStatus> taskStatus = loTaskStatusRepo.findById(taskDTO.getStatusId());
        if(taskStatus.isEmpty())
            throw new RuntimeException("this is not a valid status");

        task = modelMapper.map(taskDTO, Task.class);
        task.setStatus(taskStatus.get());

        task = taskRepo.save(task);
        return modelMapper.map(task, TaskDTO.class);
    }
}
