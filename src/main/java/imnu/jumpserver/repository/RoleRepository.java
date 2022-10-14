package imnu.jumpserver.repository;

import imnu.jumpserver.model.Role;
import imnu.jumpserver.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

}
