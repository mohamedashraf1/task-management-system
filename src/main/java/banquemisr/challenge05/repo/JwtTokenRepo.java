package banquemisr.challenge05.repo;

import banquemisr.challenge05.entity.JwtToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface JwtTokenRepo extends JpaRepository<JwtToken, Long> {
    List<JwtToken> findByUserId(Long userId);
}
