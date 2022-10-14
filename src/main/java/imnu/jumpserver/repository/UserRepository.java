package imnu.jumpserver.repository;

import imnu.jumpserver.model.Host;
import imnu.jumpserver.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findByName(String name);

}
