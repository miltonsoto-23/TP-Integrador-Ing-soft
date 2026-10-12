package com.iot.repositories;

import com.iot.models.entities.SystemConfig; // Tu clase @Entity
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SystemConfigRepository extends JpaRepository<SystemConfig, Long> {
  Optional<SystemConfig> findByKey(String key); // Search by key
}
