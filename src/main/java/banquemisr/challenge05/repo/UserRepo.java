package banquemisr.challenge05.repo;

import banquemisr.challenge05.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<User, Long> {
}
