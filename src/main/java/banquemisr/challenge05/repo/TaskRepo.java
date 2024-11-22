package banquemisr.challenge05.repo;

import banquemisr.challenge05.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepo extends JpaRepository<Task, Long> {
}
