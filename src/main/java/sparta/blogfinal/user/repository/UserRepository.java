package sparta.blogfinal.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sparta.blogfinal.user.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
