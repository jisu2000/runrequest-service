package com.runapp.runrequest_service.repo;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.runapp.runrequest_service.model.RunEO;

public interface RunRepo extends JpaRepository<RunEO,String>{
    List<RunEO> findByUserId(Integer userId);
}
