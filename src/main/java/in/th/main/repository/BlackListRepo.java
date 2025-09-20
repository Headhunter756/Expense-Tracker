package in.th.main.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import in.th.main.entities.BlackList;

@Repository
public interface BlackListRepo extends JpaRepository<BlackList, Long>{
	BlackList findByToken(String token);
	boolean existsByToken(String token);
}
