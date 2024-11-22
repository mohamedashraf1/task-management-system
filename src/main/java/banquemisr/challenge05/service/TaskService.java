package banquemisr.challenge05.service;

import banquemisr.challenge05.dto.TaskDTO;
import banquemisr.challenge05.entity.LoTaskStatus;
import banquemisr.challenge05.entity.Task;
import banquemisr.challenge05.repo.LoTaskStatusRepo;
import banquemisr.challenge05.repo.TaskRepo;
import banquemisr.challenge05.repo.UserRepo;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static banquemisr.challenge05.util.Util.getLoggedUserId;
import static banquemisr.challenge05.util.Util.mapList;

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
        task.setUserId(getLoggedUserId());

        return taskRepo.save(task).getTaskId();
    }

    public TaskDTO getTask(Long taskId){
        TaskDTO taskDTO;
        Task task = taskRepo.findById(taskId).orElse(null);
        if(task == null)
            return null;

        taskDTO = modelMapper.map(task, TaskDTO.class);

        if(Objects.equals(task.getUserId(), getLoggedUserId())){
            return taskDTO;
        }else{
            return null;
        }
    }

    public List<TaskDTO> getAllTasks(){
        List<Task> tasks = taskRepo.findByUserId(getLoggedUserId());

        return mapList(tasks, TaskDTO.class);
    }

    public void deleteTask(Long taskId){
        taskRepo.deleteById(taskId);
    }

    public TaskDTO updateTask(TaskDTO taskDTO){
        if(taskDTO.getTaskId() == null)
            return null;

        Task task = taskRepo.findById(taskDTO.getTaskId()).orElse(null);
        if(task == null)
            throw new RuntimeException("this is not a valid task id");

        Optional<LoTaskStatus> taskStatus = loTaskStatusRepo.findById(taskDTO.getStatusId());
        if(taskStatus.isEmpty())
            throw new RuntimeException("this is not a valid status");

        if(Objects.equals(task.getUserId(), getLoggedUserId())){
            task = modelMapper.map(taskDTO, Task.class);

            task.setStatus(taskStatus.get());
            task.setUserId(getLoggedUserId())
            ;
            task = taskRepo.save(task);

            return modelMapper.map(task, TaskDTO.class);
        }else {
            return null;
        }
    }
}
