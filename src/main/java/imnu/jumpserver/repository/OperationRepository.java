package imnu.jumpserver.repository;

import imnu.jumpserver.model.Host;
import imnu.jumpserver.model.Operation;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OperationRepository extends JpaRepository<Operation, Long> {

    List<Operation> findAllByOrderByTimeDesc(Pageable pageable);

}
