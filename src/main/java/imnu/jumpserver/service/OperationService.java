package imnu.jumpserver.service;

import imnu.jumpserver.model.Operation;
import imnu.jumpserver.repository.OperationRepository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class OperationService {

    @Resource
    OperationRepository operationRepository;

    public void record(String kind, String comment) {
        this.operationRepository.save(new Operation(kind, comment));
    }

}
