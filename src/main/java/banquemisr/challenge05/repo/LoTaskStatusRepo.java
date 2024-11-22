package banquemisr.challenge05.repo;

import banquemisr.challenge05.entity.LoTaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoTaskStatusRepo extends JpaRepository<LoTaskStatus, Integer> {
}
