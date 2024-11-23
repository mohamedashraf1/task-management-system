package banquemisr.challenge05.service;

import banquemisr.challenge05.dao.TaskDAO;
import banquemisr.challenge05.dto.TaskDTO;
import banquemisr.challenge05.dto.TaskSearchDTO;
import banquemisr.challenge05.entity.LoTaskStatus;
import banquemisr.challenge05.entity.Task;
import banquemisr.challenge05.errorhandling.BusinessException;
import banquemisr.challenge05.repo.LoTaskStatusRepo;
import banquemisr.challenge05.repo.TaskRepo;
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
    TaskDAO taskDAO;

    public Long createTask(TaskDTO taskDTO){
        Optional<LoTaskStatus> taskStatus = loTaskStatusRepo.findById(taskDTO.getStatusId());
        if(taskStatus.isEmpty())
            throw new BusinessException("this is not a valid status");

        Task task = modelMapper.map(taskDTO, Task.class);
        task.setTaskId(null);
        task.setStatus(taskStatus.get());
        task.setUserId(getLoggedUserId());

        return taskRepo.save(task).getTaskId();
    }

    public TaskDTO getTask(Long taskId){
        TaskDTO taskDTO;
        Task task = taskRepo.findById(taskId).orElse(null);
        if(task == null || !Objects.equals(task.getUserId(), getLoggedUserId()))
            throw new BusinessException("Task not found");

        taskDTO = modelMapper.map(task, TaskDTO.class);

        return taskDTO;
    }

    public List<TaskDTO> getAllTasks(){
        List<Task> tasks = taskRepo.findByUserId(getLoggedUserId());

        return mapList(tasks, TaskDTO.class);
    }

    public void deleteTask(Long taskId){
        Optional<Task> task = taskRepo.findById(taskId);
        if(task.isEmpty() || !Objects.equals(task.get().getUserId(), getLoggedUserId()))
            throw new BusinessException("Task not found");

        taskRepo.deleteById(taskId);
    }

    public TaskDTO updateTask(TaskDTO taskDTO){
        if(taskDTO.getTaskId() == null)
            throw new BusinessException("You must enter task id");

        Task task = taskRepo.findById(taskDTO.getTaskId()).orElse(null);
        if(task == null)
            throw new BusinessException("this is not a valid task id");

        Optional<LoTaskStatus> taskStatus = loTaskStatusRepo.findById(taskDTO.getStatusId());
        if(taskStatus.isEmpty())
            throw new BusinessException("this is not a valid status");

        if(Objects.equals(task.getUserId(), getLoggedUserId())){
            task = modelMapper.map(taskDTO, Task.class);

            task.setStatus(taskStatus.get());
            task.setUserId(getLoggedUserId());

            task = taskRepo.save(task);

            return modelMapper.map(task, TaskDTO.class);
        }else {
            throw new BusinessException("this is not a valid task id");
        }
    }

    public List<TaskDTO> search(TaskSearchDTO criteria){
        return taskDAO.search(criteria);
    }
}
