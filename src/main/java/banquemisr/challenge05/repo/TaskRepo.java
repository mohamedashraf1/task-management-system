package banquemisr.challenge05.repo;

import banquemisr.challenge05.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepo extends JpaRepository<Task, Long> {

    List<Task> findByUserId(Long userId);
}
