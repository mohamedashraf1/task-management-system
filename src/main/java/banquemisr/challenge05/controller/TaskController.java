package banquemisr.challenge05.controller;

import banquemisr.challenge05.dto.TaskDTO;
import banquemisr.challenge05.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/task")
public class TaskController {
    @Autowired
    TaskService taskService;

    @PostMapping
    public ResponseEntity<Long> createTask(@RequestBody @Valid TaskDTO taskDTO){
        return new ResponseEntity<>(taskService.createTask(taskDTO), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<TaskDTO> getTask(@RequestParam Long taskId){
        return new ResponseEntity<>(taskService.getTask(taskId), HttpStatus.OK);
    }

    @DeleteMapping
    public void deleteTask(@RequestParam Long taskId){
        taskService.deleteTask(taskId);
    }

    @PutMapping
    public ResponseEntity<TaskDTO> updateTask(@RequestBody @Valid TaskDTO taskDTO){
        return new ResponseEntity<>(taskService.updateTask(taskDTO), HttpStatus.OK);
    }
}
