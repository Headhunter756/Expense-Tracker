package in.th.main.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import in.th.main.entities.User;

@Repository
public interface UserRepo extends JpaRepository<User, Long>
{
	User findByName(String name);
	User findByEmail(String email);
	boolean existsByEmail(String email);
	boolean existsByName(String username);
}
